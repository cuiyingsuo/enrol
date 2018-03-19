package com.itcast.enrol.management.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.marketing.MarketingMapper;
import com.itcast.enrol.common.dao.organize.ClassMapper;
import com.itcast.enrol.common.entity.EnrolClass;
import com.itcast.enrol.common.entity.Marketing;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.utils.BeanUtils;
import com.itcast.enrol.management.vo.UserEx;


/**
 * 营销策略表 服务类
 **/
@Service
public class ManagerMarketingService extends BaseService<Marketing> {


    @Autowired
    private MarketingMapper marketingMapper;

    @Autowired
    private ClassMapper classMapper;


    /**
     * 查询非删除状态的营销数据
     *
     * @return
     */
    public BasePage queryMarking(Integer startPage, Integer pageSize) {

        Marketing marketingQuery = new Marketing();
        marketingQuery.setIsDel(0);
        BasePage basePage = this.queryListByPage(marketingQuery, startPage, pageSize);
        return basePage;
    }

    /**
     * 添加、修改营销信息
     *
     * @param marketing
     */
    @Transactional(rollbackFor = Exception.class)
    public void mergeMarketing(Marketing marketing, UserEx userEx) {
        //唯一性验证 -> 学科、校区、班级类型、授课模式、期间类型、
        if (marketing.getId() != null) {
            Marketing marketingQueryResult = this.selectByPrimaryKey(marketing.getId());
            if (null == marketingQueryResult) {
                throw new RuntimeException("查询不到原记录");
            }
            BeanUtils.copyProperties(marketing, marketingQueryResult);
            marketingQueryResult.setEditTime(new Date());
            marketingQueryResult.setEditor(userEx.getName());
            marketingQueryResult.setEditorId(userEx.getId());
            this.updateByPk(marketingQueryResult);
            return;
        }
        //新增
        marketing.setCreateTime(new Date());
        marketing.setCreator(userEx.getName());
        marketing.setCreatorId(userEx.getId());

        this.insertSelective(marketing);
    }

    /**
     * 商品列表显示营销活动
     *
     * @param goodsList
     * @return
     */
    public List<Map<String, Object>> goodsMarket(List<Map<String, Object>> goodsList) throws ParseException {

        //查询营销活动
        Marketing marketingQuery = new Marketing();
        marketingQuery.setStatus(1);
        marketingQuery.setIsDel(0);
        List<Marketing> marketings = marketingMapper.select(marketingQuery);

        marketings = marketings.stream().filter(s -> s.getEndTime().getTime() > System.currentTimeMillis()).collect(Collectors.toList());

        marketings.sort((Marketing h1, Marketing h2) -> h2.getReductionPrice().compareTo(h1.getReductionPrice()));

        if (CollectionUtils.isEmpty(marketings)) {
            return goodsList;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Map<String, Object> godosMap : goodsList) {

            String gCampusId = String.valueOf(godosMap.get("campusId"));
            String gSubjectId = String.valueOf(godosMap.get("subjectId"));
            String gClassType = String.valueOf(godosMap.get("classTypeCode"));
            String gTeachMode = String.valueOf(godosMap.get("teachModeCode"));

            String dateStr = simpleDateFormat.format(godosMap.get("startDate"));

            Date gDate = simpleDateFormat.parse(dateStr);
            long startClassTime = gDate.getTime();

            for (Marketing marketing : marketings) {
                List<String> campusIds = Arrays.asList(marketing.getCampusIds().split(","));
                List<String> subjectIds = Arrays.asList(marketing.getSubjectIds().split(","));
                List<String> classTypes = Arrays.asList(marketing.getClassTypeCodes().split(","));
                List<String> teachModes = Arrays.asList(marketing.getTeachModelCodes().split(","));
                long startTime = marketing.getStartTime().getTime();
                long endTime = marketing.getEndTime().getTime();

                if (campusIds.contains(gCampusId) && subjectIds.contains(gSubjectId) && classTypes.contains(gClassType) && teachModes.contains(gTeachMode)) {
                    if (startClassTime >= startTime && startClassTime <= endTime) {
                        long disPrice = marketing.getReductionPrice();
                        long goodsPrice = Long.valueOf(String.valueOf(godosMap.get("price")));
                        godosMap.put("marketName", marketing.getName());
                        godosMap.put("lastPrice", (goodsPrice - disPrice));
                        break;
                    }
                }
            }
        }
        return goodsList;
    }


    /**
     * 订单优惠处理
     *
     * @param orderMain
     * @param durationTypeCode:start_class_time/pay_date_time
     * @return
     */
    public OrderMain orderMarket(OrderMain orderMain, String durationTypeCode) {
        //订单优惠 减免
        Marketing marketingQuery = new Marketing();
        marketingQuery.setCampusIds(orderMain.getCampusId().toString());
        marketingQuery.setSubjectIds(orderMain.getSubjectId().toString());

        EnrolClass enrolClass = classMapper.selectByPrimaryKey(orderMain.getClassId());
        if (null == enrolClass) {
            throw new RuntimeException("班级信息不存在!");
        }
        marketingQuery.setTeachModelCodes(enrolClass.getTeachModeCode());
        marketingQuery.setClassTypeCodes(enrolClass.getClassTypeCode());
        marketingQuery.setStatus(1);
        marketingQuery.setIsDel(0);

        List<Marketing> marketings = marketingMapper.matchMarking(marketingQuery);

        marketings.sort((Marketing h1, Marketing h2) -> h2.getReductionPrice().compareTo(h1.getReductionPrice()));

        if (CollectionUtils.isEmpty(marketings)) {
            orderMain.setOrderPrice(orderMain.getCostPrice());
            return orderMain;
        }

        for (Marketing marketing : marketings) {
            if (null != marketing && marketing.getDurationTypeCode().equals(durationTypeCode)) {
                Long currentTime = System.currentTimeMillis();
                //优惠期间为开班时间
                if ("start_class_time".equals(durationTypeCode)) {
                    currentTime = enrolClass.getStartDate().getTime();
                } else if ("pay_date_time".equals(durationTypeCode)) {
                    currentTime = System.currentTimeMillis();
                }
                //优惠期间为支付时间
                //判断订单 优惠开始时间是否 包含当前时间
                Long startTime = marketing.getStartTime().getTime();
                Long endTime = marketing.getEndTime().getTime();
                if (currentTime >= startTime && currentTime <= endTime) {
                    //减价
                    int lastPrice=orderMain.getCostPrice().intValue() - marketing.getReductionPrice().intValue();
                    orderMain.setOrderPrice(lastPrice);
                    orderMain.setMarketingName(marketing.getName());
                    orderMain.setMarketingId(marketing.getId());
                    orderMain.setPrefAmount(marketing.getReductionPrice().intValue());
                    break;
                }
            }
        }
        return orderMain;
    }

    /**
     * 营销下拉选
     *
     * @return
     */
    public List<Map<String, String>> marketingDownSelect() {
        return marketingMapper.marketingDownSelect();
    }

}
