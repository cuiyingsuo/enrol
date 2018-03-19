package com.itcast.enrol.student.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itcast.enrol.common.ems.SynchBaseDataForEms;
import com.itcast.enrol.common.entity.Campus;
import com.itcast.enrol.common.entity.OrderFlow;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.entity.OrderSub;
import com.itcast.enrol.common.utils.payment.RSA2ForMchtUtils;
import com.itcast.enrol.student.service.StuCampusService;
import com.itcast.enrol.student.service.StuOrderMainService;
import com.itcast.enrol.student.service.StuOrderSubService;
import com.itcast.enrol.student.service.StuPayService;

/**
 * 
 * 支付回调接口
 * 
 * @author My
 *
 */
@RestController
@RequestMapping("/payResultController")
public class StuPayResultController {

	// 统一记录日志类
	private Logger logger = LoggerFactory.getLogger("enrol");

	@Autowired
	private StuPayService payService;
	
	@Autowired
	private StuOrderSubService orderSubService;
	@Autowired
	private StuOrderMainService orderMainService;
	@Autowired
	private SynchBaseDataForEms synchBaseDataForEms;
	@Autowired
	private StuCampusService campusService;

	@Value("${allin.h5-pay.return-url}")
	private String returnUrl;
	/**
	 * 支付平台公钥
	 */
	@Value("${itcast-pay.public-key}")
	private String publicKey;
	
	/**
	 * 支付结果回执
	 * 
	 * @param request
	 * @param response
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/payResult", method = RequestMethod.POST)
	public Map<String,String> payResult(@RequestBody Map<String,String> result){
		logger.info("支付平台异步回调进入...{}",JSON.toJSONString(result));
		Map<String,String> retMap = new HashMap<String,String>();
		
		String appId = result.get("app_id");
		String format = result.get("format");
		String version = result.get("version");
		String charset = result.get("charset");
		String timestamp = result.get("timestamp");
		String sign = result.get("sign");
		logger.info("{},{},{},{},{},{}",appId,format,version,charset,timestamp,sign);
		
		String biz_params = result.get("biz_params");
		
		logger.info("{}",biz_params);
		
		JSONObject bizPObj = JSON.parseObject(biz_params);
		String merchant_order_no = bizPObj.getString("merchant_order_no");
		String main_order_no = bizPObj.getString("main_order_no");
		String order_status = bizPObj.getString("order_status");
		String total_amount = bizPObj.getString("total_amount");
		String discount_amount = bizPObj.getString("discount_amount");
		
		logger.info("{},{},{},{},{}",merchant_order_no,main_order_no,order_status,total_amount,discount_amount);
		
		String sub_order = bizPObj.getString("sub_order");
		JSONObject bizSubObj = JSON.parseObject(sub_order);
		String sub_order_no = bizSubObj.getString("sub_order_no");
		String amount = bizSubObj.getString("amount");
		String currency = bizSubObj.getString("currency");
		String payment_time = bizSubObj.getString("payment_time");
		String canceled_time = bizSubObj.getString("canceled_time");
		String sub_order_status = bizSubObj.getString("sub_order_status");
		
		logger.info("{},{},{},{},{},{}",sub_order_no,amount,currency,payment_time,canceled_time,sub_order_status);
		retMap.put("merchant_order_no", merchant_order_no);
		
		/*SortedMap<String,String> paramsMap = new TreeMap<>();
		paramsMap.put("app_id", appId);
		paramsMap.put("version", format);
		paramsMap.put("version", version);
		paramsMap.put("charset", charset);
		paramsMap.put("timestamp", timestamp);
		paramsMap.put("sign", sign);
		//业务参数集合
		SortedMap<String,Object> bizParams = new TreeMap<>();
		bizParams.put("merchant_order_no", merchant_order_no);
		bizParams.put("main_order_no", main_order_no);
		bizParams.put("order_status", order_status);
		bizParams.put("total_amount", total_amount);
		bizParams.put("discount_amount", discount_amount);
		
		bizParams.put("sub_order", sub_order);
		bizParams.put("sub_order_no", sub_order_no);
		bizParams.put("amount", amount);
		bizParams.put("currency", currency);
		bizParams.put("payment_time", payment_time);
		bizParams.put("canceled_time", canceled_time);
		bizParams.put("sub_order_status", sub_order_status);
		
		paramsMap.put("biz_params", JSON.toJSONString(bizParams));*/
		
		boolean signStatus = RSA2ForMchtUtils.rsa2Check(result, publicKey);
		
		// 记录流水
				OrderSub orderSub = new OrderSub();
				orderSub.setOrderNo(merchant_order_no);
				orderSub = orderSubService.selectOne(orderSub);
				logger.info("支付平台回调，支付订单号：{}",merchant_order_no);
				logger.info("分订单id：{}",orderSub.getId());
				OrderMain om = new OrderMain();
				om.setMergeOrderNo(orderSub.getMergeOrderNo());
				logger.info("支付平台回调，主订单号：{},{}",orderSub.getMergeOrderNo(),om.getMergeOrderNo());
				OrderMain orderMain=orderMainService.selectOne(om);
				logger.info("主订单id：{}",orderMain.getId());
				
				OrderFlow orderFlow = new OrderFlow();
				BigDecimal bd = new BigDecimal(total_amount);
				orderFlow.setAmount(bd.movePointRight(2).intValue());
				orderFlow.setCreateDatetime(new Date());
				orderFlow.setMargeOrderNo(orderSub.getMergeOrderNo());
				orderFlow.setOrderNo(merchant_order_no);
				orderFlow.setPayChannelCode("payment");
				orderFlow.setPayChannelName("传智支付平台");
				orderFlow.setPayId(orderSub.getId());
				orderFlow.setFlowId(main_order_no);
				orderFlow.setPaymentType(String.valueOf(orderMain.getPayType()));
				orderFlow.setPayUserId(orderMain.getStudentId());
				
				if("SUCCESS".equals(order_status)){
					orderFlow.setStatus(1);
					logger.info("支付流水状态：{}", orderFlow.getStatus());
					orderFlow.setPayDescription("支付成功");
				}else{
					orderFlow.setStatus(0);
					logger.info("支付流水状态：{}", orderFlow.getStatus());
					orderFlow.setPayDescription("支付失败");
					retMap.put("return_code", "SUCCESS");
					retMap.put("return_msg", "支付失败");
				}
				payService.recordFlow(orderFlow);
				logger.info("回执验证：{},{}", sign,signStatus);
				
				if(signStatus){
					if(orderFlow.getStatus()==1){
						// 记录订单信息
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							payService.recordOrderInfo(merchant_order_no,sdf.parse(timestamp).getTime());
							
							//将支付结果同步到ems
							Map<String, String> payRel = new HashMap<String,String>();
							payRel.put("clazzId", String.valueOf(orderMain.getClassId()));
							payRel.put("fee", total_amount);
							if(orderMain.getIsFree()==0){
								payRel.put("incomeExpensesType", "1");
							}else{
								payRel.put("incomeExpensesType", "2");
							}
							payRel.put("paymentId", main_order_no);
							Campus campus = campusService.selectByPrimaryKey(orderMain.getCampusId());
							payRel.put("schoolCode", campus.getSerialCode());
							payRel.put("studentId", String.valueOf(orderMain.getStudentId()));
							synchBaseDataForEms.pushPayRelToEms(payRel);
							
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (Exception e) {
		                    logger.error("同步ems异常:{}", e);
		                }
						retMap.put("return_code", "SUCCESS");
						retMap.put("return_msg", "支付成功");
					}else{
						logger.info("支付平台异步回调，返回支付状态非成功状态.{},{}",orderFlow.getStatus(),order_status);
						retMap.put("return_code", "SUCCESS");
						retMap.put("return_msg", "支付失败");
					}
				}else{
					logger.info("支付平台异步回调，验证签名失败");
					retMap.put("return_code", "FAIL");
					retMap.put("return_msg", "签名验证失败");
				}
		return retMap;
	}
	public void payResult1(HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		logger.info("--------------------------------------订单结果回执---------------------------------------------------");
		// 接收Server返回的支付结果
		String merchantId = request.getParameter("merchantId");
		String version = request.getParameter("version");
		String language = request.getParameter("language");
		String signType = request.getParameter("signType");
		String payType = request.getParameter("payType");
		String issuerId = request.getParameter("issuerId");
		String paymentOrderId = request.getParameter("paymentOrderId");
		String orderNo = request.getParameter("orderNo");
		String orderDatetime = request.getParameter("orderDatetime");
		String orderAmount = request.getParameter("orderAmount");
		String payDatetime = request.getParameter("payDatetime");
		String payAmount = request.getParameter("payAmount");
		String ext1 = request.getParameter("ext1");
		String ext2 = request.getParameter("ext2");
		String payResult = request.getParameter("payResult");
		String errorCode = request.getParameter("errorCode");
		String returnDatetime = request.getParameter("returnDatetime");
		String signMsg = request.getParameter("signMsg");

		logger.info(
				"merchantId：{}  version：{}  language：{}  signType：{}  payType：{}  issuerId：{}  "
						+ "paymentOrderId：{}  orderNo：{}  orderDatetime:{}  orderAmount:{}  payDatetime:{}"
						+ "  payAmount:{}  ext1:{}  ext2:{}  payResult:{}  errorCode:{}  returnDatetime:{}  signMsg:{}",
				merchantId, version, language, signType, payType, issuerId,
				paymentOrderId, orderNo, orderDatetime, orderAmount,
				payDatetime, payAmount, ext1, ext2, payResult, errorCode,
				returnDatetime, signMsg);

		// 验签是商户为了验证接收到的报文数据确实是支付网关发送的。
		// 构造订单结果对象，验证签名。
		com.allinpay.ets.client.PaymentResult paymentResult = new com.allinpay.ets.client.PaymentResult();
		paymentResult.setMerchantId(merchantId);
		paymentResult.setVersion(version);
		paymentResult.setLanguage(language);
		paymentResult.setSignType(signType);
		paymentResult.setPayType(payType);
		paymentResult.setIssuerId(issuerId);
		paymentResult.setPaymentOrderId(paymentOrderId);
		paymentResult.setOrderNo(orderNo);
		paymentResult.setOrderDatetime(orderDatetime);
		paymentResult.setOrderAmount(orderAmount);
		paymentResult.setPayDatetime(payDatetime);
		paymentResult.setPayAmount(payAmount);
		paymentResult.setExt1(ext1);
		paymentResult.setExt2(ext2);
		paymentResult.setPayResult(payResult);
		paymentResult.setErrorCode(errorCode);
		paymentResult.setReturnDatetime(returnDatetime);
		paymentResult.setKey("1234567890");
		// signMsg为服务器端返回的签名值。
		paymentResult.setSignMsg(signMsg);
		// signType为"1"时，必须设置证书路径。
		// paymentResult.setCertPath("/opt/conf/TLCert-test.cer");

		// 验证签名：返回true代表验签成功；否则验签失败。
		boolean verifyResult = paymentResult.verify();

		// 验签成功，还需要判断订单状态，为"1"表示支付成功。
		boolean paySuccess = verifyResult && payResult.equals("1");

		// 记录通联回执信息
		payService.recordPaymentResult(paymentResult, verifyResult);
		// 记录流水
		OrderSub orderSub = new OrderSub();
		orderSub.setOrderNo(orderNo);
		orderSub = orderSubService.selectOne(orderSub);
		
		OrderMain orderMain = new OrderMain();
		orderMain.setMergeOrderNo(orderSub.getMergeOrderNo());
		orderMain=orderMainService.selectOne(orderMain);
		
		OrderFlow orderFlow = new OrderFlow();
		orderFlow.setAmount(Integer.parseInt(orderAmount));
		orderFlow.setCreateDatetime(new Date());
		orderFlow.setMargeOrderNo(orderSub.getMergeOrderNo());
		orderFlow.setOrderNo(orderNo);
		orderFlow.setPayChannelCode("pay_TL");
		orderFlow.setPayChannelName("通联银行卡支付");
		orderFlow.setPayId(orderSub.getId());
		orderFlow.setFlowId(paymentOrderId);
		orderFlow.setPaymentType(String.valueOf(orderMain.getPayType()));
		orderFlow.setPayUserId(orderMain.getStudentId());
		orderFlow.setStatus(Integer.parseInt(payResult));
		if(orderFlow.getStatus()==1){
			logger.info("支付流水状态：{}", orderFlow.getStatus());
			orderFlow.setPayDescription("支付成功");
		}else{
			logger.info("支付流水状态：{}", orderFlow.getStatus());
			orderFlow.setPayDescription("支付失败");
		}
		payService.recordFlow(orderFlow);
		
		logger.info("回执验证：{}", paySuccess);
		if (paySuccess) {
			// 记录订单信息
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			payService.recordOrderInfo(paymentResult.getOrderNo(),sdf.parse(paymentResult.getPayDatetime()).getTime());
		}
		try {
			response.sendRedirect(returnUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 信用卡分期回调通知
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/installmentNotify", method = RequestMethod.POST)
	public String installmentNotify(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("------------------------------------------分期支付，异步通知-----------------------------------------");
		Map<String, String[]> requestMap = request.getParameterMap();
		HashMap<String, String> notifyMap = new HashMap<String, String>();
		for (String key : requestMap.keySet()) {
			String value = requestMap.get(key)[0];
			notifyMap.put(key, value);
		}
		boolean verifyRel = payService.installmentNotify(notifyMap);
		if (verifyRel) {
			logger.info("分期支付，异步通知.返回{}", "success");
			return "success";
		}
		logger.info("分期支付，异步通知.返回{}", "error");
		return "error";
	}
}