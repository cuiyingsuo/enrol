package com.itcast.enrol.management.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.allinpay.ets.client.PaymentResult;
import com.allinpay.ets.client.RequestOrder;
import com.itcast.enrol.common.dao.order.OrderFlowMapper;
import com.itcast.enrol.common.dao.order.OrderMainMapper;
import com.itcast.enrol.common.dao.order.OrderSubMapper;
import com.itcast.enrol.common.dao.order.PaymentResultMapper;
import com.itcast.enrol.common.dao.organize.ClassMapper;
import com.itcast.enrol.common.dao.sequence.SeqMapper;
import com.itcast.enrol.common.dao.student.ClassStudentMapper;
import com.itcast.enrol.common.dao.student.StudentMapper;
import com.itcast.enrol.common.entity.ClassStudent;
import com.itcast.enrol.common.entity.EnrolClass;
import com.itcast.enrol.common.entity.EnrolPaymentResult;
import com.itcast.enrol.common.entity.OrderFlow;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.entity.OrderSub;
import com.itcast.enrol.common.utils.GenerateSequenceUtil;
import com.itcast.enrol.student.service.plugins.AllinpayService;
import com.itcast.enrol.student.service.plugins.StuContractService;

@Service
public class ManagerPayService {

    private Logger logger = LoggerFactory.getLogger("enrol");

    @Autowired
    private OrderMainMapper orderMainDao;

    @Autowired
    private OrderSubMapper orderSubDao;

    @Autowired
    private OrderFlowMapper orderFlowDao;

    @Autowired
    private StudentMapper studentDao;

    @Autowired
    private ClassMapper classDao;

    @Autowired
    private ManagerClassStudentService classStudentService;

    @Autowired
    private AllinpayService allinpayService;

    @Autowired
    private PaymentResultMapper paymentResultDao;

    @Autowired
    private ClassStudentMapper classStudentDao;

    @Autowired
    private StuContractService contractService;

    @Autowired
    private SeqMapper seqDao;

    /**
     * 全款支付拼接支付订单信息
     *
     * @param mobile       手机号码
     * @param mergeOrderNo 主订单号
     * @return
     * @throws Exception
     */
    @Transactional
    public RequestOrder payFullRequestOrder(String userId, String mobile,
                                            String mergeOrderNo) throws Exception {
        OrderMain orderMain = new OrderMain();
        orderMain.setMergeOrderNo(mergeOrderNo);
        orderMain = orderMainDao.selectOne(orderMain);

        int isFree = orderMain.getIsFree();
        int otherExpense = orderMain.getOtherExpense();

        OrderSub orderSub = new OrderSub();
        //String orderNo = String.valueOf(GenerateSequenceUtil.generateSequenceNo());
        orderSub.setMergeOrderNo(mergeOrderNo);
        orderSub.setStatus(2);
        int subNum = orderSubDao.selectCount(orderSub);
        String orderNo = mergeOrderNo + String.format("%02d", subNum + 1);
        orderSub.setOrderNo(orderNo);
        if (isFree == 1) {
            orderSub.setOrderPrice(otherExpense);
        } else {
            orderSub.setOrderPrice(orderMain.getOrderPrice());
        }
        orderSub.setStatus(0);
        orderSub.setCreateTime(System.currentTimeMillis());
        orderSub.setCreator(mobile);
        orderSubDao.insertSelective(orderSub);
        logger.info("用户{}创建支付订单:{}", mobile, orderNo);
        RequestOrder requestOrder = allinpayService.getRequestOrder(userId,
                orderSub, orderMain);
        return requestOrder;
    }

    /**
     * 分次支付拼接支付订单
     *
     * @param mobile       手机号
     * @param payPrice     支付金额
     * @param mergeOrderNo 主订单号
     * @return
     * @throws Exception
     */
    @Transactional
    public OrderSub createOrderSub(String userId, String mobile,
                                   int payPrice, String mergeOrderNo) {
        OrderMain orderMain = new OrderMain();
        orderMain.setMergeOrderNo(mergeOrderNo.trim());
        orderMain = orderMainDao.selectOne(orderMain);

        OrderSub orderSub = new OrderSub();
        //String orderNo = String.valueOf(GenerateSequenceUtil.generateSequenceNo());
        orderSub.setMergeOrderNo(mergeOrderNo);
        orderSub.setStatus(2);
        int subNum = orderSubDao.selectCount(orderSub);
        String orderNo = mergeOrderNo + String.format("%02d", subNum + 1);
        orderSub.setOrderNo(orderNo);
        orderSub.setOrderPrice(payPrice);
        orderSub.setStatus(0);
        orderSub.setMergeOrderNo(mergeOrderNo.trim());
        orderSub.setCreateTime(System.currentTimeMillis());
        orderSub.setCreator(mobile);

        try {
            orderSubDao.insertSelective(orderSub);
        } catch (Exception e) {
            logger.info("存在重复分次支付单：{}", orderNo);
            OrderSub os = new OrderSub();
            os.setOrderNo(orderNo);
            orderSub = orderSubDao.select(os).get(0);
        }
        logger.info("用户{}创建支付订单:{}", mobile, orderNo);
        return orderSub;
    }

    /**
     * 修改订单
     * <p>
     * 支付回执验证结果实体类
     *
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    public void recordOrderInfo(String orderNo, Long payTime) throws ParseException {
        logger.info(
                "-----------------------------------修改订单-----------------------------------------orderSubNo={}",
                orderNo);
        OrderSub orderSub = new OrderSub();
        orderSub.setOrderNo(orderNo);
        List<OrderSub> orderSubList = orderSubDao.select(orderSub);
        orderSub = orderSubList.get(0);
        //支付成功后，页面点返回到通联支付成功倒计时页面，通联会重复调用回执
        if (null != orderSub && orderSub.getStatus() == 2) {
            return;
        }

        if (null != orderSub) {

            orderSub.setEditor("SYSTEM");
            orderSub.setEditTime(System.currentTimeMillis());
            orderSub.setPayTime(System.currentTimeMillis());
            orderSub.setStatus(2);

            // 主订单状态修改
            OrderMain orderMain = new OrderMain();
            orderMain.setMergeOrderNo(orderSub.getMergeOrderNo());
            logger.info(
                    "-------------------------------------获取主订单信息成功------------------------------orderMainNo={}",
                    orderSub.getMergeOrderNo());
            List<OrderMain> orderMainList = orderMainDao.select(orderMain);
            orderMain = orderMainList.get(0);
            logger.info("主订单类型：{},价格：{},已支付：{}", orderMain.getPayType(),
                    orderMain.getOrderPrice(), orderMain.getPaid());
            int isFree = orderMain.getIsFree();
            /*if(isFree==1){
				orderMain.setPaid(0);
				orderMain.setStatus(2);
			}else{
				orderMain.setPaid(orderMain.getPaid() + orderSub.getOrderPrice());
			}*/
            orderMain.setPaid(orderMain.getPaid() + orderSub.getOrderPrice());
            int paid = orderMain.getPaid();
            int orderPrice = orderMain.getOrderPrice();
            if (orderMain.getPayType() == 0) {
                // 全款支付订单状态改为支付完成
                if (paid == orderPrice) {
                    logger.info("{}：主订单-{}，支付订单-{}，全款支付，成功.共支付:{}",
                            orderMain.getStudentId(),
                            orderSub.getMergeOrderNo(), orderNo,
                            orderMain.getPaid());
                    orderMain.setStatus(2);
                } else {
                    // 已付款金额不等于订单金额情况（全款支付不应该出现）
                    logger.info("{}：主订单-{}，支付订单-{}，全款支付，出错，未支付：{}",
                            orderMain.getStudentId(),
                            orderSub.getMergeOrderNo(), orderNo,
                            orderMain.getOrderPrice() - orderMain.getPaid());
                    orderMain.setStatus(1);
                }
            }
            {
                if (paid == orderPrice) {
                    logger.info("主订单-{}，支付订单-{}，分次支付，成功.订单金额已全部支付", orderSub.getMergeOrderNo(), orderNo);
                    orderMain.setStatus(2);
                } else {
                    logger.info("主订单-{}，支付订单-{}，分次支付，成功.订单剩余未支付金额：{}", orderSub.getMergeOrderNo(), orderNo,
                            (orderMain.getOrderPrice() - orderMain.getPaid()));
                    orderMain.setStatus(1);
                }
            }
            logger.info("修改主订单={}", orderMain.getMergeOrderNo());
            orderMain.setEditorId(orderMain.getCreatorId());
            orderMain.setEditor(orderMain.getCreator());
            orderMain.setEditTime(System.currentTimeMillis());
            orderMain.setPayTime(System.currentTimeMillis());
            orderMain.setPayChannelName("支付平台");
            //去支付的过程中，订单超时，有可能将此状态改为取消；此时若已支付成功，回调后修改此状态为正常
            orderMain.setIsCancel(0);
            //去支付的过程中，订单超时，学生与班级的关联关系数据状态置为无效0，此时若支付成功，将学生与班级的关联关系数据状态置为有效1
            ClassStudent cs = new ClassStudent();
            cs.setClassId(orderMain.getClassId());
            cs.setStudentId(orderMain.getStudentId());
            //重置最后一次学生与班级的关联数据状态
            List<ClassStudent> csList = classStudentDao.select(cs);
            cs = csList.get(csList.size() - 1);
            cs.setDataStatus(1);
            classStudentDao.updateByPrimaryKeySelective(cs);


            logger.info("----------------------------------修改主订单信息-----------------------------------------------");
            // 修改主订单信息
            orderMainDao.updateByPrimaryKeySelective(orderMain);
            //if(orderMain.getElecReceipt()==1){
            orderSub.setReceiptNo(getReceiptNo());
            //}

            orderSub.setPayTime(payTime);
            logger.info("----------------------------------修改分订单信息-----------------------------------------------");
            // 修改分订单信息
            orderSubDao.updateByPrimaryKeySelective(orderSub);

            OrderSub record = new OrderSub();
            record.setMergeOrderNo(orderMain.getMergeOrderNo());
            record.setStatus(2);
            int payTimes = orderSubDao.selectCount(record);
            //通过订单状态修改学生与班级信息关联状态
            classStudentService.updateByOrderStatus(payTimes, orderMain.getStatus(), orderMain.getClassId(), orderMain.getStudentId());

            EnrolClass enrolClass = classDao.selectByPrimaryKey(orderMain
                    .getClassId());
            enrolClass.setCurrent(enrolClass.getCurrent() + 1);
            classDao.updateByPrimaryKeySelective(enrolClass);

        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void recordFlow(OrderFlow orderFlow) {
        logger.info("支付增加流水：{}", orderFlow.getFlowId());
        orderFlowDao.insertSelective(orderFlow);
        logger.info("支付生成流水：{}，成功", orderFlow.getFlowId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void recordPaymentResult(PaymentResult paymentResult,
                                    boolean verifyResult) {
        logger.info("-------------------------记录支付回执结果-------------------------------");
        EnrolPaymentResult payResult = new EnrolPaymentResult();
        payResult.setMerchantId(paymentResult.getMerchantId());
        payResult.setVersion(paymentResult.getVersion());
        payResult.setLanguage(paymentResult.getLanguage());
        payResult.setSignType(paymentResult.getSignType());
        payResult.setPayType(paymentResult.getPayType());
        payResult.setIssuserId(paymentResult.getIssuerId());
        payResult.setPaymentOrderId(paymentResult.getPaymentOrderId());
        payResult.setOrderNo(paymentResult.getOrderNo());
        payResult.setOrderDateTime(paymentResult.getOrderDatetime());
        payResult.setOrderAmount(paymentResult.getOrderAmount());
        payResult.setPayDateTime(paymentResult.getPayDatetime());
        payResult.setPayAmount(paymentResult.getPayAmount());
        payResult.setExt1(paymentResult.getExt1());
        payResult.setExt2(paymentResult.getExt2());
        payResult.setPayResult(paymentResult.getPayResult());
        payResult.setErrorCode(paymentResult.getErrorCode());
        payResult.setReturnDateTime(paymentResult.getReturnDatetime());
        // signMsg为服务器端返回的签名值。
        payResult.setSignMsg(paymentResult.getSignMsg());
        payResult.setVerifyResult(verifyResult);
        paymentResultDao.insertSelective(payResult);
    }

    @Transactional(rollbackFor = Exception.class)
    public String makeCardInstallmentInfo(String orderMainNo) {
        logger.info("分期支付，查询主订单：{}", orderMainNo);
        OrderMain orderMain = new OrderMain();
        orderMain.setMergeOrderNo(orderMainNo);
        orderMain = orderMainDao.selectOne(orderMain);
        if (orderMain.getPayType() != 2) {
            throw new RuntimeException("订单非贷款订单");
        }
        if (orderMain.getPaid().intValue() <= 0) {
            throw new RuntimeException("贷款支付需先支付定金");
        }
        OrderSub orderSub = new OrderSub();
        String orderSubNo = String.valueOf(GenerateSequenceUtil
                .generateSequenceNo());
        logger.info("分期支付，生成分订单：{}", orderSubNo);
        orderSub.setMergeOrderNo(orderMainNo);
        orderSub.setOrderNo(orderSubNo);
        orderSub.setCreateTime(System.currentTimeMillis());
        orderSub.setCreator("SYSTEM");
        int orderSubPrice = orderMain.getOrderPrice() - orderMain.getPaid();
        orderSub.setOrderPrice(orderSubPrice);
        orderSub.setStatus(0);

        orderSubDao.insertSelective(orderSub);
        return allinpayService.getCardInstallmentInfo(orderSub);

    }

    @Transactional(rollbackFor = Exception.class)
    public boolean installmentNotify(HashMap<String, String> requestMap) {

        boolean verifyRel = allinpayService.installmentNotify(requestMap);
        if (verifyRel) {
            String result = requestMap.get("result");
            String orderNo = requestMap.get("orderId");
            String totalAmt = requestMap.get("totalAmt");

            logger.info("分期支付结果，查询分订单：{}", orderNo);
            OrderSub orderSub = new OrderSub();
            orderSub.setOrderNo(orderNo);
            orderSub = orderSubDao.selectOne(orderSub);
            logger.info("分期支付结果，查询主订单：{}", orderSub.getMergeOrderNo());
            OrderMain orderMain = new OrderMain();
            orderMain.setMergeOrderNo(orderSub.getMergeOrderNo());
            orderMain = orderMainDao.selectOne(orderMain);

            logger.info("分期支付结果，生成流水：主订单={}，分订单={}", orderSub.getMergeOrderNo(), orderNo);
            OrderFlow orderFlow = new OrderFlow();
            orderFlow.setAmount(Integer.valueOf(totalAmt));
            orderFlow.setMargeOrderNo(orderSub.getMergeOrderNo());
            orderFlow.setOrderNo(orderNo);
            orderFlow.setPayDescription("贷款支付");
            orderFlow.setPaymentType("2");
            orderFlow.setPayUserId(orderMain.getStudentId());
            orderFlow.setCreateDatetime(new Date());
            orderFlow.setPayChannelCode("pay_TL");
            orderFlow.setPayChannelName("通联支付");

            if ("2".equals(result)) {
                logger.info("分期支付结果，支付平台返回支付成功：{}", result);
                orderSub.setStatus(2);
                orderMain.setStatus(2);
                orderFlow.setStatus(1);

                orderSubDao.updateByPrimaryKeySelective(orderSub);
                orderMainDao.updateByPrimaryKeySelective(orderMain);
            } else {
                orderFlow.setStatus(0);
            }
            orderFlowDao.updateByPrimaryKeySelective(orderFlow);
        }
        return verifyRel;
    }

    //生成收据编号
    @Transactional(rollbackFor = Exception.class)
    public String getReceiptNo() {
        logger.info("申城凭证号");
        String[] seqVal = seqDao.nextVal("receipt_no").split(",");

        return "CZBK" + seqVal[0] + String.format("%06d", Integer.parseInt(seqVal[1]));
    }

}