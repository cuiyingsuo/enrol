package com.itcast.enrol.management.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.goods.GoodsMapper;
import com.itcast.enrol.common.dao.order.OrderFlowMapper;
import com.itcast.enrol.common.dao.order.OrderMainMapper;
import com.itcast.enrol.common.dao.order.OrderSubMapper;
import com.itcast.enrol.common.dao.organize.CampusMapper;
import com.itcast.enrol.common.dao.organize.ClassMapper;
import com.itcast.enrol.common.dao.sequence.SeqMapper;
import com.itcast.enrol.common.dao.student.ClassStudentMapper;
import com.itcast.enrol.common.dao.student.StudentMapper;
import com.itcast.enrol.common.entity.Bill;
import com.itcast.enrol.common.entity.ClassStudent;
import com.itcast.enrol.common.entity.Contract;
import com.itcast.enrol.common.entity.Goods;
import com.itcast.enrol.common.entity.OrderFlow;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.entity.OrderSub;
import com.itcast.enrol.common.entity.Student;
import com.itcast.enrol.common.exception.BusinessException;
import com.itcast.enrol.common.utils.GenerateSequenceUtil;
import com.itcast.enrol.common.utils.MD5Util;
import com.itcast.enrol.common.utils.RedisUtil;
import com.itcast.enrol.management.vo.OrderQueryEx;
import com.itcast.enrol.management.vo.StuGoodsEx;
import com.itcast.enrol.student.service.plugins.StuContractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 订单信息表（主表）； 服务类
 **/
@Service
public class ManagerOrderMainService extends BaseService<OrderMain> {

    private Logger logger = LoggerFactory.getLogger("enrol");

    @Autowired
    private OrderMainMapper orderMainDao;

    @Autowired
    private OrderSubMapper orderSubDao;

    @Autowired
    private GoodsMapper goodsDao;

    @Autowired
    private ClassMapper classDao;

    @Autowired
    private ClassStudentMapper classStudentDao;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private OrderFlowMapper orderFlowMapper;

    @Autowired
    private ManagerMarketingService marketingService;

    @Autowired
    private StuContractService contractService;

    @Autowired
    private ManagerInvoiceService invoiceService;

    @Autowired
    private ManagerPayService payService;

    @Autowired
    private CampusMapper campusDao;

    @Autowired
    private SeqMapper seqDao;
    
    @Value("${enrol.user.login.key}")
    private String key;
    
    @Autowired
    private ManagerBillHandlerService billService;

    /**
     * 查询主订单详情
     *
     * @param orderNo
     * @return
     */
    public Map<String, Object> queryOrderMainInfo(String orderMainNo,
                                                  String mobile) {
        Map<String, Object> orderMainInfo = new HashMap<String, Object>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderMainNo", orderMainNo);
        params.put("mobile", mobile);
        Map<String, Object> orderMain = orderMainDao
                .selectOrderMainInfo(params);
        if (null == orderMain) {
            return null;
        }
        
        if (String.valueOf(orderMain.get("classTypeName")).contains("基础")) {
            orderMain.put("classTypeCode", "class_detail_base");
        } else {
            orderMain.put("classTypeCode", "class_detail_employment");
        }

        addContractButtonStatus(orderMain);

        List<Map<String, Object>> payOrderList = orderSubDao
                .selectByMergeOrderNo(orderMainNo);
        
        //增加是否可查看凭证状态
        for(Map<String,Object> payOrder:payOrderList){
        	String orderNo = String.valueOf(payOrder.get("orderNo"));
        	int isAft = Integer.parseInt(String.valueOf(payOrder.get("isAft")));
        	if(isAft==0){
        		Bill bill = new Bill();
            	bill.setSubOrderNo(orderNo);
            	List<Bill> billList = billService.select(bill);
            		if(billList.size()>0){
            			payOrder.put("receiptStatus", true);
            		}else{
            			payOrder.put("receiptStatus", false);
            		}
        	}else{
        		payOrder.put("receiptStatus", true);
        	}
        	
        }
        
        // 给前端一个是否最后一次支付的状态
        orderMain.put("isLast", false);
        int payTimes = payOrderList.size();
        String classType = orderMain.get("classTypeCode").toString();
        if ("class_detail_base".equals(classType) && payTimes == 1) {
            orderMain.put("isLast", true);
        }
        if ("class_detail_employment".equals(classType) && payTimes == 2) {
            orderMain.put("isLast", true);
        }
        orderMain.put("currentTime", System.currentTimeMillis());
        // 给前端一个订单未支付超时的时间
        if (payTimes == 0) {
            orderMain
                    .put("endTime", Long.valueOf(orderMain.get("createTime")
                            .toString()) + 60 * 60 * 1000);
        }

        Map<String, Object> myInvoice = invoiceService.selectByOrderMainNo(orderMainNo);
        if (null != myInvoice) {
            orderMain.put("invoiceStatus", 2);
            orderMain.put("eInvoice", myInvoice.get("eInvoice"));
        } else {
            addInvoiceStatus(orderMain);
        }

        orderMainInfo.put("orderMainInfo", orderMain);
        orderMainInfo.put("payOrderList", payOrderList);
        return orderMainInfo;
    }

    /**
     * 查询主订单列表
     *
     * @param mobile
     * @return
     * @throws ParseException
     */
    public List<Map<String, Object>> queryOrderMainList(String mobile) {

        List<Map<String, Object>> orderList = orderMainDao
                .selectOrderMainList(mobile);

        List<Map<String, Object>> orderListRel = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < orderList.size(); i++) {
            Map<String, Object> orderMap = orderList.get(i);

            Long createTime = Long.valueOf(String.valueOf(orderMap
                    .get("createTime")));
            Long currentTime = System.currentTimeMillis();

            int orderStatus = Integer.parseInt(String.valueOf(orderList.get(i).get("orderStatus")));

            if (orderStatus == 0 && currentTime - createTime >= 60 * 60 * 1000) {
                orderMap.put("isCancel", true);
            }

            addContractButtonStatus(orderMap);

            orderListRel.add(orderMap);
        }
        return orderListRel;
    }

    /**
     * 添加前台关于合同的按钮显示状态。（签订合同，查看合同，作废合同）
     */
    private void addContractButtonStatus(Map<String, Object> orderMain) {
        String orderMainNo = String.valueOf(orderMain.get("orderMainNo"));

        Contract contract = contractService.getContractLastTime(orderMainNo);
        int contractCounts = contractService
                .getCountsByOrderMainNo(orderMainNo);

            // 已生成过合同
            int contractStatus = contract.getContractStatus();
            
            if(contractStatus==0){
            	addSignContractStatus(orderMain);
            }
            if (contractStatus == 1) {
            	
            	orderMain.put("signContractStatus", true);
                if (contractCounts > 1) {
                    // 存在多个合同，其它合同时可以查看的
                    orderMain.put("seeContractStatus", true);
                } else {
                    // 不存在多个合同，当前最后一份合同还未完成签订，不能查看合同
                    orderMain.put("seeContractStatus", false);
                }
                // 最后生成的合同还未签订，不能作废合同
                orderMain.put("cancelContractStatus", false);
            }
            if (contractStatus == 2) {
                // 最后生成合同已签订，不能再签合同，可查看合同，不能作废合同
                orderMain.put("signContractStatus", false);
                orderMain.put("seeContractStatus", true);
                orderMain.put("cancelContractStatus", false);
            }
            if (contractStatus == 3) {
                // 最后生成合同允许作废，不能再签合同，可查看合同，可作废合同
                orderMain.put("signContractStatus", false);
                orderMain.put("seeContractStatus", true);
                orderMain.put("cancelContractStatus", true);
            }
            if (contractStatus == 4) {
                // 最后生成合同已作废，允许再签合同，可查看合同，不能作废合同
                orderMain.put("signContractStatus", true);
                orderMain.put("seeContractStatus", true);
                orderMain.put("cancelContractStatus", false);
            }
    }

    /**
     * 添加是否可以签订合同
     *
     * @param orderMap
     */
    private void addSignContractStatus(Map<String, Object> orderMap) {
    
        int orderStatus = Integer.parseInt(String.valueOf(orderMap
                .get("orderStatus")));

        Date startDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = String.valueOf(orderMap.get("startDate"));

            startDate = sdf.parse(date);
        } catch (ParseException e) {
            logger.info("班级{}开班时间异常", orderMap.get("classId"));
            e.printStackTrace();
        }

        // 管理端设置此状态允许签订合同
            if (2 == orderStatus && null != startDate
                    && isCanSignContract(startDate)) {
            	orderMap.put("signContractStatus", true);
            	
            	String orderMainNo = String.valueOf(orderMap.get("orderMainNo"));
            	OrderSub orderSub = new OrderSub();
            	orderSub.setMergeOrderNo(orderMainNo);
            	orderSub.setStatus(2);
            	List<OrderSub> osList = orderSubDao.select(orderSub);
            	
            	for(OrderSub os:osList){
            		if(os.getIsAft()==0){
	            		Bill bill = new Bill();
	                	bill.setSubOrderNo(os.getOrderNo());
	                	List<Bill> billList = billService.select(bill);
	                		if(null==billList||billList.size()==0){
	                			orderMap.put("signContractStatus", false);
	                			break;
	                		}
            		}
            	}
            } else {
                orderMap.put("signContractStatus", false);
            }
    }

    /**
     * 添加是否可以开具发票
     */
    private void addInvoiceStatus(Map<String, Object> orderMap) {
        int orderStatus = Integer.parseInt(String.valueOf(orderMap
                .get("orderStatus")));

        Date startDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = String.valueOf(orderMap.get("startDate"));

            startDate = sdf.parse(date);
        } catch (ParseException e) {
            logger.info("班级{}开班时间异常", orderMap.get("classId"));
            e.printStackTrace();
        }

        if (2 == orderStatus && null != startDate
                && isCanMakeInvoice(startDate)) {
            orderMap.put("invoiceStatus", 1);
        } else {
            orderMap.put("invoiceStatus", 0);
        }
        
        String orderMainNo = String.valueOf(orderMap.get("orderMainNo"));
    	List<Map<String, Object>> payOrderList = orderSubDao
                .selectByMergeOrderNo(orderMainNo);
        //根据对账，增加是否可申请发票状态
        for(Map<String,Object> payOrder:payOrderList){
        	String orderNo = String.valueOf(payOrder.get("orderNo"));
        	int isAft = Integer.parseInt(String.valueOf(payOrder.get("isAft")));
        	if(isAft==0){
        		Bill bill = new Bill();
            	bill.setSubOrderNo(orderNo);
            	List<Bill> billList = billService.select(bill);
            		if(billList.size()>0){
            			orderMap.put("invoiceStatus", 1);
            		}else{
            			orderMap.put("invoiceStatus", 0);
            		}
        	}else{
        		orderMap.put("invoiceStatus", 1);
        	}
        }
    }

    /**
     * 开班日期当天9点以后可以签订合同
     *
     * @param startDate
     * @return
     */
    private boolean isCanSignContract(Date startDate) {

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date t9 = c.getTime();

        if (t9.getTime() < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    /**
     * //开班日期7天以后可以开发票
     *
     * @param startDate
     * @return
     */
    private boolean isCanMakeInvoice(Date startDate) {

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DAY_OF_MONTH, 7);
        Date d7 = c.getTime();

        if (d7.getTime() < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    /**
     * 获取确认订单信息
     *
     * @param classId
     * @param goodsId
     * @return
     * @throws ParseException
     */
    @Transactional
    public Map<String, Object> confirmMainOrder(Long classId, Long goodsId) throws ParseException {

        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        // 查询班级信息包含校区信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("classId", classId);
        params.put("goodsId", goodsId);
        //TODO
        Map<String, Object> enrolClass = classDao.selectClassInfo(params);

        Map<String, Object> relMap = new HashMap<String, Object>();
        relMap.put("goodsName", goods.getName());
        //relMap.put("coverImg", goods.getCoverImg());
        relMap.put("coverImg", goods.getCoverImgUrl());

        if (null != enrolClass) {
            relMap.put("campusId", enrolClass.get("campusId"));
            relMap.put("subjectId", enrolClass.get("subjectId"));
            relMap.put("teachModeCode", enrolClass.get("teachModeCode"));
            relMap.put("classTypeCode", enrolClass.get("classTypeCode"));
            relMap.put("startDate", enrolClass.get("startDate"));
            relMap.put("price", enrolClass.get("discountPrice"));

            relMap.put("address", enrolClass.get("address"));
            relMap.put("className", enrolClass.get("className"));
            relMap.put("periods", enrolClass.get("periods"));
            relMap.put("teachModeName", enrolClass.get("teachModeName"));
            relMap.put("classTime", enrolClass.get("classTime"));

            relMap.put("isFree", enrolClass.get("isFree"));
            relMap.put("isOtherExpense", enrolClass.get("isOtherExpense"));
            relMap.put("otherExpense", enrolClass.get("otherExpense"));
            relMap.put("otherExpenseRemark", enrolClass.get("otherExpenseRemark"));

            ArrayList<Map<String, Object>> marketListQuery = new ArrayList<Map<String, Object>>();
            marketListQuery.add(relMap);
            marketingService.goodsMarket(marketListQuery);
            for (Map<String, Object> rMap : marketListQuery) {
                Object lastPrice = rMap.get("lastPrice");
                if (null == lastPrice) {
                    rMap.put("lastPrice", rMap.get("price"));
                }
            }
        }
        return relMap;
    }

    /**
     * 提交主订单信息
     *
     * @param studentId
     * @param studentMobile
     * @param studentName
     * @param goodsId
     * @param classId
     * @param payType
     * @param isReceipt
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderMain submitMainOrder(Long studentId, String studentMobile,
                                     String studentName, Long goodsId, Long classId, int payType,
                                     int isReceipt) {
        logger.info("准备提交订单...");
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        Byte isFull = goods.getIsFull();
        Byte isBatch = goods.getIsBatch();
        Byte isLoan = goods.getIsLoan();
        int isFree = goods.getIsFree().intValue();
        int isOtherExpense = goods.getIsOtherExpense().intValue();
        int otherExpense = goods.getOtherExpense().intValue();
        String otherExpenseRemark = goods.getOtherExpenseRemark();
        if (isFree != 1) {
            if (payType == 0 && isFull == '0') {
                throw new RuntimeException("商品不支持全款支付");
            }
            if (payType == 1 && isBatch == '0') {
                throw new RuntimeException("商品不支持分次支付");
            }
            if (payType == 2 && isLoan == '0') {
                throw new RuntimeException("商品不支持贷款支付");
            }
        }

        // 查询班级信息包含校区信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("classId", classId);
        params.put("goodsId", goodsId);
        //TODO
        Map<String, Object> enrolClass = classDao.selectClassInfo(params);

        if (payType == 2
                && String.valueOf(enrolClass.get("classTypeCode")).equals(
                "class_detail_base")) {
            throw new RuntimeException("基础班不支持贷款");
        }

        OrderMain orderMain = new OrderMain();
        orderMain.setStudentId(studentId);
        orderMain.setClassId(classId);
        orderMain.setGoodsId(goodsId);
        orderMain.setIsCancel(0);

        orderMain.setIsFree(isFree);
        orderMain.setIsOtherExpense(isOtherExpense);
        orderMain.setOtherExpense(otherExpense);
        orderMain.setOtherExpenseRemark(otherExpenseRemark);

        logger.info("查询学生对当前班级报名情况信息");
        List<OrderMain> orderMainList = orderMainDao.select(orderMain);
        for (OrderMain order : orderMainList) {
            int status = order.getStatus();
            if (status == 0 || status == 1 || status == 2) {
                throw new RuntimeException("请勿重复报名");
            }
        }
        //String mergeOrderNo = String.valueOf(GenerateSequenceUtil.generateSequenceNo());

        String[] seqNum = seqDao.nextVal("merge_order_no").split(",");
        if (seqNum == null) {
            throw new RuntimeException("获取订单编号失败");
        }
        logger.info("获取订单编号,根据{},{},{}", String.valueOf(enrolClass.get("campusCode")), seqNum[0], seqNum[1]);
        String mergeOrderNo = String.valueOf(enrolClass.get("campusCode")) + seqNum[0] + String.format("%04d", Integer.parseInt(seqNum[1]));


        logger.info("组装主订单");
        orderMain.setMergeOrderNo(mergeOrderNo);

        orderMain.setCostPrice(goods.getPrice());

        orderMain.setStudentName(studentName);
        orderMain.setStudentMobile(studentMobile);
        orderMain.setSubjectId(Long.parseLong(enrolClass.get("subjectId")
                .toString()));
        orderMain.setSubjectName(enrolClass.get("subjectName").toString());
        orderMain.setCampusId(Long.parseLong(enrolClass.get("campusId")
                .toString()));
        orderMain.setCampusName(enrolClass.get("campusName").toString());
        orderMain.setGoodsName(goods.getName());
        orderMain.setClassName(enrolClass.get("className").toString());
        orderMain.setPayChannelName("");
        orderMain.setPaid(0);
        orderMain.setIsAft((byte) 0);
        orderMain.setStatus(0);
        orderMain.setElecReceipt(isReceipt);
        orderMain.setCreatorId(studentId);
        orderMain.setCreator(studentName);
        orderMain.setCreateTime(System.currentTimeMillis());
        orderMain.setGoodsId(goodsId);
        // 订单减免
        marketingService.orderMarket(orderMain, "start_class_time");

        /*if (isFree == 1) {
            orderMain.setOrderPrice(0);
            orderMain.setPayType(0);
        } else {*/
        orderMain.setOrderPrice(orderMain.getOrderPrice() + otherExpense);
        orderMain.setPayType(payType);
        /*}*/

        int result = orderMainDao.insertSelective(orderMain);
        logger.info("提交主订单:{}", orderMain.getMergeOrderNo());

        // 插入学生班级关联
        insertStuClassByStatus(classId, studentId, studentName, 1);
        //生成合同记录
		Contract contract = new Contract();
		contract.setOrderMainNo(orderMain.getMergeOrderNo());
		List<Contract> contractList = contractService.select(contract);
		if(contractList.size()==0){
			contract.setContractStatus(0);
			contract.setCreater("system");
			contract.setCreateTime(System.currentTimeMillis());
			contractService.insertSelective(contract);
		}
        if (result != 0) {
            logger.info("{}({})提交主订单：{}。成功", studentName, studentMobile,
                    mergeOrderNo);
            return orderMain;
        }
        logger.info("{}({})提交主订单。失败", studentName, studentMobile);
        return null;
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId) {
        OrderMain orderMain = orderMainDao.selectByPrimaryKey(orderId);
        if (orderMain.getStatus() == 0) {
            orderMain.setIsCancel(1);
            orderMainDao.updateByPrimaryKey(orderMain);
            logger.info("{}({})取消订单：{}，成功。课程：{},订单状态改为：{}",
                    orderMain.getStudentName(), orderMain.getStudentMobile(),
                    orderMain.getMergeOrderNo(), orderMain.getGoodsName(),
                    orderMain.getStatus());
            // 取消订单，将学生与班级的关联数据状态置为无效0
            ClassStudent cs = new ClassStudent();
            cs.setClassId(orderMain.getClassId());
            cs.setStudentId(orderMain.getStudentId());

            List<ClassStudent> csList = classStudentDao.select(cs);
            for (ClassStudent c : csList) {
                if (c.getDataStatus().intValue() == 1) {
                    c.setDataStatus(0);
                    classStudentDao.updateByPrimaryKeySelective(c);
                }
            }

            return true;
        }
        logger.info("{}({})取消订单：{}，失败。订单状态：{}", orderMain.getStudentName(),
                orderMain.getStudentMobile(), orderMain.getMergeOrderNo(),
                orderMain.getStatus());
        return false;
    }

    /**
     * 后台下订单
     *
     * @param stuGoodsEx
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderMain generateOrder(StuGoodsEx stuGoodsEx) {
        // 生成订单时 验证学生信息，系统中没有学生信息需要自动注册
        Student studentQuery = new Student();
        studentQuery.setId(stuGoodsEx.getStudentId());
        Student studentResult = studentMapper.selectOne(studentQuery);

        Student student = studentResult;
        // 注册学生
        if (null == studentResult || null == studentResult.getMobile()) {
            student = new Student();
            // 执行 添加学生信息
            student.setId(stuGoodsEx.getStudentId());
            student.setCardNo(stuGoodsEx.getCardNo());
            student.setName(stuGoodsEx.getStuName());
            student.setMobile(stuGoodsEx.getStuMobile());
            // 加密生成密码(手机号+密码+秘钥 md5 处理)
            String md5Str = MD5Util.encryption(
                    student.getMobile() + "itheima" + key).trim();
            student.setPassword(md5Str);
            student.setStatus((byte) 4);
            student.setCreateTime(System.currentTimeMillis());
            student.setCreator(stuGoodsEx.getCreator());
            studentMapper.insertSelective(student);
            logger.info("*****************************学生信息:{}", student);
        }
        if (null != studentResult && !StringUtils.isEmpty(studentResult.getMobile())) {
            if (!studentResult.getName().equals(stuGoodsEx.getStuName())) {
                throw new RuntimeException("查询到的学生姓名与输入的姓名不匹配!");
            }
        }
        // 查询该学生是否下过订单，如果下过订单，查询所下订单的班级是否跟当前所下订单的班级相同
        OrderMain orderQyery = new OrderMain();
        orderQyery.setStudentId(stuGoodsEx.getStudentId());
        orderQyery.setIsCancel(0);
        orderQyery.setCreator(stuGoodsEx.getCreator());
        List<OrderMain> orderMains = orderMainDao.select(orderQyery);
        // 选课唯一性验证
        for (OrderMain orderChild : orderMains) {
            if (orderChild.getGoodsId().longValue() == stuGoodsEx.getGoodsId().longValue() && orderChild.getClassId().longValue() == stuGoodsEx.getClassId().longValue()) {
                throw new RuntimeException("请勿重复报名!");
            }
        }
        Goods goods = goodsDao.selectByPrimaryKey(stuGoodsEx.getGoodsId());

        if (goods == null || goods.getStatus().byteValue() == 0) {
            throw new BusinessException("该商品不存在,或已下架!");
        }
        // 查询班级信息包含校区信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("classId", stuGoodsEx.getClassId());
        params.put("goodsId", stuGoodsEx.getGoodsId());

        Map<String, Object> enrolClass = classDao.selectClassInfo(params);
        //组装订单实体
        OrderMain orderMain = new OrderMain();
        String[] seqNum = seqDao.nextVal("merge_order_no").split(",");
        if (seqNum == null) {
            throw new RuntimeException("获取订单编号失败");
        }
        logger.info("获取订单编号{},{},{}", String.valueOf(enrolClass.get("campusCode")), seqNum[0], seqNum[1]);
        String mergeOrderNo = String.valueOf(enrolClass.get("campusCode")) + seqNum[0] + String.format("%04d", Integer.parseInt(seqNum[1]));
        // 补录订单 要有特殊前缀
        orderMain.setStatus(0);
        if (stuGoodsEx.getIsAft().intValue() == 1) {
            mergeOrderNo = "BL" + mergeOrderNo;
            orderMain.setStatus(1);
            orderMain.setPayTime(System.currentTimeMillis());
        }
        orderMain.setMergeOrderNo(mergeOrderNo);
        //ems同步过来的最终价格，存在缓存里
        
        orderMain.setCostPrice(goods.getPrice());
        orderMain.setIsAft(stuGoodsEx.getIsAft());
        orderMain.setStudentId(stuGoodsEx.getStudentId());
        orderMain.setStudentName(stuGoodsEx.getStuName());
        orderMain.setStudentMobile(stuGoodsEx.getStuMobile());
        orderMain.setSubjectId(Long.parseLong(String.valueOf(enrolClass.get("subjectId"))));
        orderMain.setSubjectName(String.valueOf(enrolClass.get("subjectName")));
        orderMain.setCampusId(Long.parseLong(String.valueOf(enrolClass.get("campusId"))));
        orderMain.setCampusName(String.valueOf(enrolClass.get("campusName")));
        orderMain.setClassId(stuGoodsEx.getClassId());
        orderMain.setGoodsName(goods.getName());
        orderMain.setClassName(String.valueOf(enrolClass.get("className")));
        orderMain.setPayType(Integer.parseInt(stuGoodsEx.getPayType()));
        orderMain.setPayChannelName("");
        orderMain.setPaid(0);
        orderMain.setIsFree(goods.getIsFree());
        orderMain.setOtherExpense(goods.getOtherExpense().intValue());
        orderMain.setOtherExpenseRemark(goods.getOtherExpenseRemark());
        orderMain.setElecReceipt(1);
        orderMain.setCreator(stuGoodsEx.getCreator());
        orderMain.setCreatorId(stuGoodsEx.getCreatorId());
        orderMain.setCreateTime(System.currentTimeMillis());
        orderMain.setGoodsId(stuGoodsEx.getGoodsId());
        //订单减免
        marketingService.orderMarket(orderMain, "start_class_time");
        
        Map<String, Object> childMap = (Map<String,Object>)RedisUtil.get(stuGoodsEx.getStuMobile()+stuGoodsEx.getClassId());
        int orderPrice = (Integer)childMap.get("tuition");
        orderMain.setOrderPrice(orderPrice*100);
        int deposit = (Integer)childMap.get("deposit");;
        if(0!=deposit){
        	BigDecimal bd = new BigDecimal(deposit);
        	
        	orderMain.setOrderPrice(bd.movePointRight(2).intValue());
        	orderMain.setIsFree(1);
        	orderMain.setIsOtherExpense(1);
            
            orderMain.setOtherExpense(bd.movePointRight(2).intValue());
            orderMain.setOtherExpenseRemark("押金");
        }
        int lastPrice = orderMain.getOrderPrice().intValue() + goods.getOtherExpense().intValue();
        //orderMain.setOrderPrice(lastPrice);
        // 如果是补录订单
        Integer status = 1;
        int paid = 0;
        if (stuGoodsEx.getIsAft().intValue() == 1) {
            paid = new BigDecimal(stuGoodsEx.getPrice()).multiply(new BigDecimal("100")).intValue();
            orderMain.setPaid(paid);
            status = 2;
            logger.info("管理端补录订单，支付金额：{}，订单金额：{}",paid,orderMain.getOrderPrice().intValue());
            //基础班免费,并且没有其他费用 例如  押金之类，直接修改订单状态为  支付完成
            if (orderMain.getOrderPrice().intValue() == 0) {
                orderMain.setStatus(2);
                orderMain.setPaid(0);
            }
            if(paid<0){
                throw new RuntimeException("对不起，输入金额不合法!");
            }
            //验证支付金额是否大于订单金额
            if (paid > orderMain.getOrderPrice().intValue()) {
                throw new RuntimeException("当前支付金额大于订单金额!");
            }
            //全款支付
            if ("0".equals(stuGoodsEx.getPayType())) {
                if (paid != orderMain.getOrderPrice().intValue()) {
                    throw new RuntimeException("全款支付，订单总额为:" + (orderMain.getOrderPrice() / 100) + "元");
                }
                orderMain.setStatus(2);
            }
            //分次支付
            if ("1".equals(stuGoodsEx.getPayType())) {
                if(paid<=0){
                    throw new RuntimeException("对不起，输入金额不合法!");
                }
            }
            // 分次支付生成分订单
            OrderSub orderSub = new OrderSub();

            orderSub.setCreateTime(System.currentTimeMillis());
            orderSub.setCreator(stuGoodsEx.getCreator());
            orderSub.setMergeOrderNo(orderMain.getMergeOrderNo());
            orderSub.setOrderNo(String.valueOf(GenerateSequenceUtil.generateSequenceNo()));
            orderSub.setOrderPrice(paid);
            orderSub.setReceiptNo(payService.getReceiptNo());
            orderSub.setPayTime(System.currentTimeMillis());
            orderSub.setStatus(2);
            orderSub.setIsAft(1);
            orderSubDao.insertSelective(orderSub);
            //生成补录流水
            OrderFlow orderFlow = new OrderFlow();
            orderFlow.setOrderNo(orderSub.getOrderNo());
            orderFlow.setFlowId("BL" + System.currentTimeMillis());
            orderFlow.setMargeOrderNo(orderMain.getMergeOrderNo());
            orderFlow.setAmount(paid);
            orderFlow.setCreateDatetime(new Date());
            orderFlow.setPayDescription("补录订单支付!");
            orderFlow.setStatus(1);
            orderFlow.setReceivablesCampusCode(stuGoodsEx.getCampusCode());
            orderFlow.setPaymentType(stuGoodsEx.getPayType());
            orderFlowMapper.insertSelective(orderFlow);

        }
        // 保存主订单
        orderMainDao.insertSelective(orderMain);
        //生成合同记录
		Contract contract = new Contract();
		contract.setOrderMainNo(orderMain.getMergeOrderNo());
		List<Contract> contractList = contractService.select(contract);
		if(contractList.size()==0){
			contract.setContractStatus(0);
			contract.setCreater("system");
			contract.setCreateTime(System.currentTimeMillis());
			contractService.insertSelective(contract);
		}
        //学员报名状态
        insertStuClassByStatus(stuGoodsEx.getClassId(), stuGoodsEx.getStudentId(), stuGoodsEx.getCreator(), status);
        return orderMain;
    }


    /**
     * 订单管理 条件 分页查询
     *
     * @param orderQueryEx
     * @return
     */

    public BasePage queryOrderMainsToPage(OrderQueryEx orderQueryEx) {
        PageHelper.startPage(orderQueryEx.getPage(), orderQueryEx.getLimit());

        List resultList = orderMainDao.queryOrderMainsToPage(orderQueryEx);

        PageInfo<Map> pageInfo = new PageInfo<Map>(resultList);

        BasePage basePage = new BasePage();
        basePage.setPageData(pageInfo.getList());
        basePage.setPageSize(orderQueryEx.getLimit());
        basePage.setCurrentPage(orderQueryEx.getPage());
        basePage.setTotalPage(pageInfo.getPages());
        basePage.setCount(pageInfo.getTotal());
        return basePage;
    }

    /**
     * 取消订单
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateOrderCancle() {
        // 所有超时订单
        List<Map<String, Object>> orderList = orderMainDao
                .selectOrderCancle(System.currentTimeMillis());
        for (Map orderMap : orderList) {
            long id = Long.valueOf(String.valueOf(orderMap.get("id")));
            long classId = Long
                    .valueOf(String.valueOf(orderMap.get("classId")));
            long studentId = Long.valueOf(String.valueOf(orderMap
                    .get("studentId")));
            // 超时订单置为取消状态1
            OrderMain om = new OrderMain();
            om.setId(id);
            om.setIsCancel(1);
            orderMainDao.updateByPrimaryKeySelective(om);
            // 订单取消后将学生与班级关联数据状态置为无效0
            ClassStudent cs = new ClassStudent();
            cs.setClassId(classId);
            cs.setStudentId(studentId);
            List<ClassStudent> csList = classStudentDao.select(cs);
            for (ClassStudent csL : csList) {
                if (csL.getDataStatus().intValue() == 1) {
                    csL.setDataStatus(0);
                    classStudentDao.updateByPrimaryKeySelective(csL);
                }
            }
        }
        return orderList.size();
    }
    
    public OrderMain selectOrderMainByNo(String mergeOrderNo){
    	return orderMainDao.selectOrderMainByNo(mergeOrderNo);
    }

    /**
     * 需要生成合同的订单
     *
     * @return
     */
    public List<Map<String, Object>> selectNeedCreatAgreement() {
        return orderMainDao.selectNeedCreatAgreement();
    }

    /**
     * 保存班级学生关联
     *
     * @param classId
     * @param studentId
     * @param creator
     * @param status
     * @return
     */
    private int insertStuClassByStatus(Long classId, Long studentId,
                                       String creator, Integer status) {
        ClassStudent classStudent = new ClassStudent();
        classStudent.setClassId(classId);
        classStudent.setStudentId(studentId);
        classStudent.setStatus(status);
        classStudent.setCreateTime(System.currentTimeMillis());
        classStudent.setCreator(creator);
        return classStudentDao.insertSelective(classStudent);
    }
}
