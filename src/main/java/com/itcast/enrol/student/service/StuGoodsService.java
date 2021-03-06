package com.itcast.enrol.student.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.goods.GoodsClassMapper;
import com.itcast.enrol.common.dao.goods.GoodsMapper;
import com.itcast.enrol.common.dao.subject.SubjectMapper;
import com.itcast.enrol.common.entity.Goods;
import com.itcast.enrol.common.entity.GoodsClass;
import com.itcast.enrol.common.exception.BeanNullException;
import com.itcast.enrol.common.utils.BeanUtils;
import com.itcast.enrol.management.vo.GoodsEx;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 商品表 服务类
 **/
@Service
public class StuGoodsService extends BaseService<Goods> {

    @Autowired
    private GoodsMapper goodsDao;

    @Autowired
    private GoodsClassMapper goodsClassMapper;

    @Autowired
    private StuCampusService campusService;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private MarketingService marketingService;


    public Goods queryByPrimaryKey(Long id) {
        return goodsDao.selectByPrimaryKey(id);
    }

    /**
     * 商品详情
     *
     * @param goodsId
     * @return
     */
    public Map<String, String> getGoodsDetail(Long goodsId) {

        Goods queryResult = goodsDao.selectByPrimaryKey(goodsId);
        if (null == queryResult) {
            throw new BeanNullException("没有查到该商品!");
        }
        Map<String, String> mapResult = goodsClassMapper.getGoodsDetail(goodsId);
        if (CollectionUtils.isEmpty(mapResult)) {
            throw new BeanNullException("不能匹配该商品详情!");
        }
        return mapResult;
    }

    /**
     * 根据学科查询商品列表
     *
     * @param subjectId
     * @return
     */
    public List<Map<String, Object>> queryBySubjectId(Long subjectId) {
        List<Map<String, Object>> goodsList = goodsDao.selectGoodsListBySubjectId(subjectId);
        return goodsList;
    }

    public BasePage<Map<String, Object>> queryGoodsPage(Long subjectId, int startPage, int pageSize) throws ParseException {
        PageHelper.startPage(startPage, pageSize);
        List goodsList = goodsDao.selectGoodsListBySubjectId(subjectId);

        marketingService.goodsMarket(goodsList);

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(goodsList);

        BasePage<Map<String, Object>> basePage = new BasePage<>();
        basePage.setPageData(pageInfo.getList());
        basePage.setPageSize(pageSize);
        basePage.setCurrentPage(startPage);
        basePage.setTotalPage(pageInfo.getPages());
        return basePage;
    }

    public Map<String, Object> queryGoodsSpec(Long goodsId) {
        Map<String, Object> goodsSpec = new HashMap<String, Object>();
        Map<String, Object> goodsMap = goodsDao.selectGoodsInfo(goodsId);
        List<Map<String, Object>> campusList = campusService.queryCampusListByGoodsId(goodsId);
        goodsSpec.put("goodsInfo", goodsMap);
        goodsSpec.put("campusList", campusList);
        return goodsSpec;
    }

    public Map<String, Object> queryGoodsInfo(Long goodsId) {
        Map<String, Object> goodsMap = goodsDao.selectGoodsInfo(goodsId);
        return goodsMap;
    }

    /**
     * 条件查询商品列表
     *
     * @param conditions
     * @return
     */
    public BasePage<Map<String, Object>> queryByConditions(int startPage, int pageSize, Map<String, String> conditions) {

        PageHelper.startPage(startPage, pageSize);
        List<Map<String, Object>> resultList = goodsClassMapper.queryGoodsForClass(conditions);

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(resultList);

        BasePage<Map<String, Object>> basePage = new BasePage<>();
        basePage.setPageData(pageInfo.getList());
        basePage.setPageSize(pageSize);
        basePage.setCurrentPage(startPage);
        basePage.setCount(pageInfo.getTotal());
        basePage.setTotalPage(pageInfo.getPages());

        return basePage;
    }

    /**
     * 保存商品时候批量生成课程
     *
     * @param goodsEx
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchAddGoodsClass(GoodsEx goodsEx) {

        //查询原有商品信息 是否存在
        Goods goodsNow = new Goods();

        goodsEx.setCreator(goodsEx.getCreator());

        goodsNow.setSubjectId(Long.parseLong(goodsEx.getSubjectId()));

        goodsEx.setSubjectId(null);

        BeanUtils.copyProperties(goodsEx, goodsNow);

        goodsNow.setCreateTime(System.currentTimeMillis());

        goodsDao.insertSelective(goodsNow);

        String[] classIds = goodsEx.getClassIdStrs().split(",");

        for (String classId : classIds) {
            GoodsClass goodsClass = new GoodsClass();
            goodsClass.setClassId(Long.parseLong(classId));
            goodsClass.setCreateTime(System.currentTimeMillis());
            goodsClass.setGoodsId(goodsNow.getId());
            goodsClass.setCreator(goodsEx.getCreator());
            goodsClassMapper.insertSelective(goodsClass);
        }
    }

    /**
     * 修改商品
     *
     * @param goodsEx
     */
    @Transactional(rollbackFor = Exception.class)
    public void editGoodsClass(GoodsEx goodsEx) {
        //查询原有商品信息 是否存在
        Goods goodsNow = this.queryByPrimaryKey(goodsEx.getId());

        if (null == goodsNow) {
            throw new RuntimeException("查询不到原有商品信息!");
        }
        String editor = goodsEx.getCreator();
        goodsEx.setCreator(goodsEx.getCreator());

        goodsNow.setSubjectId(Long.parseLong(goodsEx.getSubjectId()));

        goodsEx.setSubjectId(null);

        BeanUtils.copyProperties(goodsEx, goodsNow);

        goodsNow.setEditor(editor);
        goodsNow.setEditTime(System.currentTimeMillis());

        goodsDao.updateByPrimaryKey(goodsNow);

        String[] classIds = goodsEx.getClassIdStrs().split(",");
        //先删除 商品与 class的关联 ，然后 再重新插入
        GoodsClass goodsClassCondition = new GoodsClass();
        goodsClassCondition.setGoodsId(goodsNow.getId());
        goodsClassMapper.delete(goodsClassCondition);

        for (String classId : classIds) {
            GoodsClass goodsClass = new GoodsClass();
            goodsClass.setClassId(Long.parseLong(classId));
            goodsClass.setCreateTime(System.currentTimeMillis());
            goodsClass.setGoodsId(goodsNow.getId());
            goodsClass.setCreator(goodsEx.getCreator());
            goodsClassMapper.insertSelective(goodsClass);
        }
    }

}
