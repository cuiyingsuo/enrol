package com.itcast.enrol.management.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.order.OrderFlowMapper;
import com.itcast.enrol.common.dao.order.OrderMainMapper;
import com.itcast.enrol.common.dao.order.OrderSubMapper;
import com.itcast.enrol.common.entity.OrderFlow;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.entity.OrderSub;
import com.itcast.enrol.common.exception.BeanNullException;
import com.itcast.enrol.common.exception.BusinessException;
import com.itcast.enrol.common.utils.GenerateSequenceUtil;
import com.itcast.enrol.management.vo.PayFlowQueryEx;
import com.itcast.enrol.management.vo.UserEx;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 订单支付流水表/资金流水表 服务类
 **/
@Service
public class ManagerOrderFlowService extends BaseService<OrderFlow> {

    @Autowired
    private OrderFlowMapper orderFlowDao;

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private OrderSubMapper orderSubMapper;

    @Autowired
    private ManagerPayService payService;


    /**
     * 分页查询支付流水
     *
     * @param payFlowQueryEx
     * @return
     */
    public BasePage<Map> queryFlowToPage(PayFlowQueryEx payFlowQueryEx) {

        PageHelper.startPage(payFlowQueryEx.getPage(), payFlowQueryEx.getLimit());

        List resultList = orderFlowDao.queryFlowToPage(payFlowQueryEx);

        PageInfo<Map> pageInfo = new PageInfo<Map>(resultList);

        BasePage basePage = new BasePage();
        basePage.setPageData(pageInfo.getList());
        basePage.setPageSize(payFlowQueryEx.getLimit());
        basePage.setCurrentPage(payFlowQueryEx.getPage());
        basePage.setCount(pageInfo.getTotal());
        basePage.setTotalPage(pageInfo.getPages());
        return basePage;
    }

    /**
     * 分页查询支付流水
     *
     * @param payFlowQueryEx
     * @return
     */
    public List<Map<String, Object>> queryFlows(PayFlowQueryEx payFlowQueryEx) {
        List resultList = orderFlowDao.queryFlowToPage(payFlowQueryEx);
        return resultList;
    }


    /**
     * 补录订单继续支付
     *
     * @param orderNo
     * @param payPrice
     * @param userEx
     */
    @Transactional(rollbackFor = Exception.class)
    public void appendAftOrder(String orderNo, Integer payPrice, UserEx userEx) {

        int payPriceTurn = payPrice.intValue() * 100;
        //查询订单
        OrderMain orderMainQuery = new OrderMain();
        orderMainQuery.setMergeOrderNo(orderNo);
        orderMainQuery.setIsCancel(0);
        orderMainQuery.setIsAft((byte) 1);

        OrderMain orderMain = orderMainMapper.selectOne(orderMainQuery);

        if (null == orderMain) {
            throw new BeanNullException("查询不到该订单信息");
        }
        if (orderMain.getOrderPrice().intValue() == orderMain.getPaid().intValue()) {
            throw new BusinessException("订单已支付全额，请勿重复支付!");
        }
        int amountPrice = orderMain.getOrderPrice().intValue() - orderMain.getPaid().intValue();
        //修改主订单，支付金额，增加 分次支付单据 增加支付流水
        if (payPriceTurn > amountPrice) {
            throw new BusinessException("支付金额超出订单剩余支付金额!");
        }
        if (payPriceTurn == amountPrice) {
            orderMain.setStatus(2);
        }
        orderMain.setPaid(orderMain.getPaid().intValue() + payPriceTurn);
        orderMain.setEditor(userEx.getName());
        orderMain.setEditTime(System.currentTimeMillis());
        orderMainMapper.updateByPrimaryKey(orderMain);
        //增加分次订单
        OrderSub orderSub = new OrderSub();
        orderSub.setCreateTime(System.currentTimeMillis());
        orderSub.setCreator(userEx.getName());
        orderSub.setMergeOrderNo(orderMain.getMergeOrderNo());
        orderSub.setOrderNo(String.valueOf(GenerateSequenceUtil.generateSequenceNo()));
        orderSub.setOrderPrice(payPriceTurn);
        orderSub.setReceiptNo(payService.getReceiptNo());
        orderSub.setPayTime(System.currentTimeMillis());
        orderSub.setStatus(2);
        orderSubMapper.insertSelective(orderSub);
        //增加补录流水
        OrderFlow orderFlow = new OrderFlow();
        orderFlow.setFlowId("BL" + System.currentTimeMillis());
        orderFlow.setMargeOrderNo(orderMain.getMergeOrderNo());
        orderFlow.setAmount(payPriceTurn);
        orderFlow.setCreateDatetime(new Date());
        orderFlow.setPayDescription("补录订单后台支付!");
        orderFlow.setStatus(1);
        orderFlow.setPaymentType("1");
        orderFlowDao.insertSelective(orderFlow);
    }

}
