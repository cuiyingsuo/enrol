package com.itcast.enrol.student.service.plugins;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.allinpay.ets.client.RequestOrder;
import com.allinpay.model.AllinPayStageDto;
import com.allinpay.service.AllinPayStageConsume;
import com.allinpay.util.HttpUtils;
import com.allinpay.util.RsaSign;
import com.allinpay.util.RsaUtil;
import com.itcast.enrol.common.dao.order.OrderMainMapper;
import com.itcast.enrol.common.dao.order.OrderSubMapper;
import com.itcast.enrol.common.dao.student.StudentMapper;
import com.itcast.enrol.common.entity.Campus;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.entity.OrderSub;
import com.itcast.enrol.common.entity.Student;
import com.itcast.enrol.common.utils.MD5Util;
import com.itcast.enrol.management.service.ManagerCampusService;

@Service
public class AllinpayService {
	// 统一记录日志类
	private Logger logger = LoggerFactory.getLogger("enrol");
	
	@Value("${allin.h5-pay.pay-test}")
	private String payTest;
	/**---------------------------------支付相关参数开始--------------------------------------------*/
	@Value("${allin.h5-pay.reg-url}")
	private String regUrl;
	@Value("${allin.h5-pay.key}")
	private String key;
	@Value("${allin.h5-pay.version}")
	private String version;
	@Value("${allin.h5-pay.merchant-id}")
	private String merchantId;// 商户号
	@Value("${allin.h5-pay.receiveUrl}")
	private String receiveUrl;// 商户系统通知地址
	
	
	private String language = "1";
	private String inputCharset = "1";
	private String pickupUrl = "";// 取货地址
	private String payType = "33";
	private String signType = "0";
	private String orderNo = "";
	private String orderAmount = "";
	private String orderDatetime = "";// 商户的订单提交时间
	private String orderCurrency = "0";// 订单金额币种类型
	private String orderExpireDatetime = "";
	private String payerTelephone = "";
	private String payerEmail = "";
	private String payerName = "";
	private String payerIDCard = "";
	private String pid = "";
	private String productName = "";// 商品名称
	private String productId = "enrol";
	private String productNum = "1";
	private String productPrice = "";
	private String productDesc = "";
	private String ext1 = "";
	private String ext2 = "";
	private String extTL = "";
	private String issuerId = "";// 发卡方代码
	private String pan = "";
	private String tradeNature = "SERVICES";// GOODS表示实物类型，SERVICES表示服务类型
	private String sign = "";
	/**------------------------------------------支付参数结束-------------------------------------------------------*/

	@Autowired
	private OrderMainMapper orderMainDao;

	@Autowired
	private OrderSubMapper orderSubDao;

	@Autowired
	private StudentMapper studentDao;
	
	@Autowired
	private ManagerCampusService campusService;
	
	
	/**---------------------------------------信用卡分期参数开始---------------------------------------*/
	@Value("${allin.card-install.app_key}")
	private String app_key;
	@Value("${allin.card-install.method}")
	private String method;
	@Value("${allin.card-install.v}")
	private String v;
	@Value("${allin.card-install.sign_v}")
	private String sign_v;
	@Value("${allin.card-install.mer_id}")
	private String mer_id;
	
	@Value("${allin.card-install.return_url}")
	private String return_url;
	@Value("${allin.card-install.reqUrl}")
	private String reqUrl;
	
	@Value("${allin.card-install.keyPath}")
	private String keyPath;
	@Value("${allin.card-install.publicKeyPath}")
	private String publicKeyPath;
	
	private String privatePwd = "123456";
	private String channel="1";
	private String pdno="0200";
	private String format="json";
	private String nper="6";
	private String notify_url="http://172.17.0.91:8080/enrol/payController/installmentNotify";
	
	private String timestamp;
	private String order_id;
	private String amount;
	private String trade_date;
	private String trade_time;
	private String description;
	private String comment;
	private String creditName;
	private String cetitype;
	private String idno;
	private String phoneNo;
	private String creditNo;
	private String validty_period;
	private String cvv;
	private String relatePhone;
	private String relate_addr;
	private int goods_type;
	private String insur_code;
	private String cosign_name;
	private String cosign_phone;
	private String cosign_address;
	private String express;
	private String express_code;
	private String ext_info;
	/**-------------------------------------------信用卡分期参数结束----------------------------------------
	 * @throws Exception */

	

	// 拼接支付订单信息
	public RequestOrder getRequestOrder(String userId, OrderSub orderSub,
			OrderMain orderMain) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		Student student = studentDao.selectByPrimaryKey(Long.parseLong(userId));
		String allinpayUserid = student.getAllinpayUserid();
		
		Campus campus = campusService.selectByPrimaryKey(orderMain.getCampusId());
		String campusMerchantId = campus.getMerchantId().trim();
		if(null==campusMerchantId||"".equals(campusMerchantId)){
			campusMerchantId=merchantId;
		}
		if(null==campusMerchantId||"".equals(campusMerchantId)){
			throw new RuntimeException("未找到对应商户号");
		}
		
		if (null == allinpayUserid || "".equals(allinpayUserid)) {
			logger.info("去通联支付注册：campusMerchantId={}", campusMerchantId);
			
			String partnerUserId = student.getMobile()+System.currentTimeMillis();
			String md5PartnerUserId = MD5Util.encryption(partnerUserId);
			// 注册通联会员
			allinpayUserid = allinpayRegister(signType, campusMerchantId, partnerUserId, key);
			if (null != allinpayUserid && !"".equals(allinpayUserid)) {
				logger.info("注册通联支付会员成功：campusMerchantId={}，allinpayUserid={}",
						campusMerchantId, allinpayUserid);
				student.setAllinpayUserid(allinpayUserid);
				studentDao.updateByPrimaryKeySelective(student);
			}else{
				throw new RuntimeException("注册通联会员失败");
			}
		}
		if (null == allinpayUserid || "".equals(allinpayUserid)) {
			throw new RuntimeException("获取通联会员信息失败");
		}
		logger.info("组装支付订单:userId={},orderSub={},orderMain={}", userId,
				orderSub, orderMain);
		// 构造订单请求对象，生成signMsg。
		RequestOrder requestOrder = new RequestOrder();
		if (null != inputCharset && !"".equals(inputCharset)) {
			requestOrder.setInputCharset(Integer.parseInt(inputCharset));
		}
		requestOrder.setPickupUrl(receiveUrl.trim());
		requestOrder.setReceiveUrl(receiveUrl.trim());
		requestOrder.setVersion(version.trim());
		if (null != language && !"".equals(language)) {
			requestOrder.setLanguage(Integer.parseInt(language));
		}
		requestOrder.setSignType(Integer.parseInt(signType));
		requestOrder.setPayType(Integer.parseInt(payType));
		requestOrder.setIssuerId(issuerId);
		requestOrder.setMerchantId(campusMerchantId);
		requestOrder.setPayerName(payerName);
		requestOrder.setPayerEmail(payerEmail);
		requestOrder.setPayerTelephone(payerTelephone);
		requestOrder.setPayerIDCard(payerIDCard);
		requestOrder.setPid(pid);
		requestOrder.setOrderNo(orderSub.getOrderNo());
		//TODO(去支付写死了是1分，上线要改正)
		if(null!=payTest && payTest.equals("test")){
		  requestOrder.setOrderAmount(1L);
		}else{
		  requestOrder.setOrderAmount(orderSub.getOrderPrice().longValue());
		}
		requestOrder.setOrderCurrency(orderCurrency);
		
		requestOrder.setOrderDatetime(sdf.format(orderSub.getCreateTime()));

		requestOrder.setOrderExpireDatetime("60");
		requestOrder.setProductName(orderMain.getGoodsName().trim());

		requestOrder.setProductPrice(orderSub.getOrderPrice().longValue());

		if (null != productNum && !"".equals(productNum)) {
			requestOrder.setProductNum(Integer.parseInt(productNum));
		}
		requestOrder.setProductId(productId);
		requestOrder.setProductDesc(orderMain.getSubjectName());
		requestOrder.setExt1("<USER>" + allinpayUserid + "</USER>");
		requestOrder.setExt2(ext2);
		requestOrder.setExtTL(extTL);// 通联商户拓展业务字段，在v2.2.0版本之后才使用到的，用于开通分账等业务
		requestOrder.setPan(pan);
		requestOrder.setTradeNature(tradeNature);
		requestOrder.setKey(key); // key为MD5密钥，密钥是在通联支付网关会员服务网站上设置。

		String strSrcMsg = requestOrder.getSrc(); // 此方法用于debug，测试通过后可注释。
		String strSignMsg = requestOrder.doSign(); // 签名，设为signMsg字段值。
		logger.info(strSrcMsg);
		requestOrder.setSignMsg(strSignMsg);

		return requestOrder;
	}

	/**
	 * 通联h5支付注册会员
	 * 
	 * @param signType
	 * @param merchantId
	 * @param partnerUserId
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	private String allinpayRegister(String signType, String merchantId,
			String partnerUserId, String key) throws Exception {
		String result = "";
		String src = getSignSrc(signType, merchantId, partnerUserId, key);
		String signMsg = MD5Util.encryption(src).toUpperCase().trim();
		result = getSignSrcAndSignMsg(signType, merchantId, partnerUserId,
				signMsg);
		logger.info("注册报文：{}，提交地址：{}",result,regUrl);
		String callBack = HttpUtils.doPost(regUrl,result);//HttpUtil.sendGet(regUrl, result);
		
		logger.info("返回报文：{}",callBack + "\n");
		result = getUserIdByJson(callBack);
		return result;
	}

	// 原串 的首尾要加上 & & 这个很重要
	private String getSignSrc(String signType, String merchantId,
			String partnerUserId, String key) {
		String result = "";
		result = "&signType=" + signType + "&merchantId=" + merchantId
				+ "&partnerUserId=" + partnerUserId + "&key=" + key + "&";
		return result;
	}

	// 通过Md5获得源串
	private String getSignSrcAndSignMsg(String signType, String merchantId,
			String partnerUserId, String signMsg) {
		String result = "";
		result = "signType=" + signType + "&merchantId=" + merchantId
				+ "&partnerUserId=" + partnerUserId + "&signMsg=" + signMsg
				+ "";
		return result;
	}

	private String getUserIdByJson(String return_msg) {
		String result = "";
		JSONObject object;
		try {
			object = new JSONObject(return_msg);
			result = object.optString("userId");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;

	}
	public static void main(String[] args){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		System.out.println(sdf.format(new Date()));
	}
	public String getCardInstallmentInfo(OrderSub orderSub) {
		logger.info("分期支付，提交支付方参数组装开始");
		AllinPayStageDto consumeDto = new AllinPayStageDto();
		
		BigDecimal myformat = new BigDecimal("100000");//BigDecimal myformat=new BigDecimal(orderSub.getOrderPrice());
		float price = myformat.movePointLeft(2).floatValue();
		
		consumeDto.setApp_key(app_key);
		consumeDto.setMethod(method);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = sdf.format(new Date());
		logger.info("时间戳：{}",timestamp);
		consumeDto.setTimestamp(timestamp);
		consumeDto.setV(v);
		consumeDto.setSign_v(sign_v);
		consumeDto.setFormat(format);
		consumeDto.setMer_id(mer_id);
		consumeDto.setChannel(channel);
		consumeDto.setOrder_id(orderSub.getOrderNo());
		consumeDto.setAmount(String.valueOf(price));
		
		Long createTime = orderSub.getCreateTime();
		sdf.applyPattern("yyyyMMdd");
		String trade_date = sdf.format(createTime);
		sdf.applyPattern("HHmmss");
		String trade_time = sdf.format(createTime);
		
		consumeDto.setTrade_date(trade_date);
		consumeDto.setTrade_time(trade_time);
		
		consumeDto.setNper(nper);
		consumeDto.setPdno(pdno);
		
		consumeDto.setNotify_url(notify_url);
		consumeDto.setReturn_url(return_url);
		
		AllinPayStageConsume consume = new  AllinPayStageConsume(consumeDto, keyPath, privatePwd, reqUrl);
		String rel=null;
		try {
			rel = consume.getRequestHtml();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rel;
	}
	
	public boolean installmentNotify(HashMap<String,String> requestMap){
		StringBuilder msg = RsaSign.getQueryParamsString(requestMap);
		logger.info("分期支付返回msg={}",msg);
		
		boolean verifyRel = RsaUtil.rsaVerify(sign, msg.toString(), publicKeyPath);
		logger.info("分期支付验证结果：{}",verifyRel);
		return verifyRel;
	}
}
