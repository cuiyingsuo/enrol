package com.itcast.enrol.student.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.allinpay.ets.client.RequestOrder;
import com.allinpay.util.HttpUtils;
import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.entity.OrderSub;
import com.itcast.enrol.common.utils.payment.RSA2ForMchtUtils;
import com.itcast.enrol.student.service.StuOrderMainService;
import com.itcast.enrol.student.service.StuPayService;
import com.itcast.enrol.student.vo.StuInfoOfRedis;

/**
 * 支付访问接口
 *
 * @author My
 */
@RestController
@RequestMapping("/payController")
public class StuPayController extends StuBaseController {

    // 统一记录日志类
    private Logger logger = LoggerFactory.getLogger("enrol");
    @Autowired
    private StuOrderMainService orderMainService;

    @Autowired
    private StuPayService payService;

    @Value("${server.token-key-mobile}")
    private String loginToken;

    /**
     * 支付平台支付地址
     */
    @Value("${itcast-pay.pay_url}")
    private String payUrl;
    /**
     * 传智支付平台appid
     */
    @Value("${itcast-pay.app_id}")
    private String appId;
    /**
     * 传智支付平台版本号
     */
    @Value("${itcast-pay.version}")
    private String version;
    /**
     * 传给支付平台的同步返回地址
     */
    @Value("${itcast-pay.return_url}")
    private String returnUrl;
    /**
     * 传给支付平台的异步返回地址
     */
    @Value("${itcast-pay.notify_url}")
    private String notifyUrl;
    /**
     * 秘钥
     */
    @Value("${itcast-pay.private-key}")
    private String privateKey;

    /**
     * 支付平台公钥
     */
    @Value("${itcast-pay.public-key}")
    private String publicKey;

    /**
     * 全款支付
     *
     * @param orderMainNo 主订单编码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/payFull", method = RequestMethod.POST)
    public BaseBody<T> payFull(@RequestParam String orderMainNo, @RequestParam String clientIp, HttpServletRequest request,
                               HttpServletResponse response) {
        logger.info("全款支付start：{}", orderMainNo);

        String userToken = request.getHeader(loginToken);
        StuInfoOfRedis userInfo = getUserInfoByRedis(userToken);

        String mobile = userInfo.getMobile();

        Map<String, Object> requestOrderMap = new HashMap<String, Object>();

        Map<String, Object> orderMainMap = orderMainService.queryOrderMainInfo(orderMainNo, mobile);

        Map<String, Object> orderMainInfo = (Map<String, Object>) orderMainMap.get("orderMainInfo");
        if (null != orderMainMap && orderMainMap.size() > 0) {

            int status = Integer.parseInt(orderMainInfo.get("orderStatus").toString());

            if (status == 2) {
                return fail(ReturnStatus.STATUS_ERROR, "订单已支付完成，不可支付");
            }
            if (status == 3) {
                return fail(ReturnStatus.STATUS_ERROR, "订单退费中，不可支付");
            }
            if (status == 4) {
                return fail(ReturnStatus.STATUS_ERROR, "订单已退费，不可支付");
            }
        }

        OrderSub orderSub = null;
        try {
            orderSub = payService.createOrderSub(String.valueOf(userInfo.getId()), mobile, Integer.parseInt(orderMainInfo.get("orderPrice").toString()), orderMainNo);
        } catch (Exception e) {
            logger.error("支付订单生成异常:{}",e);
            return fail(ReturnStatus.ERROR, "支付订单生成异常");
        }

        SortedMap<String, String> paramsMap = new TreeMap<>();
        paramsMap.put("app_id", appId);
        paramsMap.put("version", version);
        paramsMap.put("charset", "utf-8");
        paramsMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        paramsMap.put("return_url", returnUrl);
        paramsMap.put("notify_url", notifyUrl);

        //业务参数集合
        SortedMap<String, Object> bizParams = new TreeMap<>();
        bizParams.put("merchant_order_no", orderSub.getOrderNo());
        BigDecimal bigDecimal = new BigDecimal(orderSub.getOrderPrice());//new BigDecimal("1");//
        bizParams.put("total_amount", bigDecimal.movePointLeft(2).floatValue());
        bizParams.put("original_price", bigDecimal.movePointLeft(2).floatValue());
        bizParams.put("currency", "CNY");
        bizParams.put("product_name", orderMainInfo.get("goodsName").toString());
        bizParams.put("order_desc", orderMainInfo.get("className").toString());
        bizParams.put("client_type", "H5");
        bizParams.put("conn_type", "INDIRECT");
        bizParams.put("client_ip", clientIp);
        bizParams.put("extend1", "enrol" + userInfo.getId() + mobile);

        paramsMap.put("biz_params", JSON.toJSONString(bizParams));

        String sign = RSA2ForMchtUtils.RSA2Sign(paramsMap, privateKey);
        paramsMap.put("sign", sign);

        try {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                paramsMap.put(entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            logger.warn("####################################################################");
            logger.warn("测试预下单数据准备接口 urlencode 编码异常.....");
            logger.warn("####################################################################");
        }
        String paramsJson = JSON.toJSONString(paramsMap);
        logger.info("请求支付平台，支付报文：{}", paramsJson);

        try {
            String payResult = HttpUtils.doPost(payUrl, paramsJson);
            logger.info("支付平台返回报文：{}", payResult);
            JSONObject payRelJsonObj = JSON.parseObject(payResult);

            SortedMap<String, String> payRelParamsMap = new TreeMap<>();
            String payRelCode = payRelJsonObj.getString("code");
            String payRelMsg = payRelJsonObj.getString("msg");
            String payRelTimestamp = payRelJsonObj.getString("timestamp");
            String payRelSign = payRelJsonObj.getString("sign");
            payRelParamsMap.put("code", payRelCode);
            payRelParamsMap.put("msg", payRelMsg);
            payRelParamsMap.put("timestamp", payRelTimestamp);
            payRelParamsMap.put("sign", payRelSign);

            String payRelBiz_params = payRelJsonObj.getString("biz_params");
            JSONObject payRelBizParams = JSON.parseObject(payRelBiz_params);
            SortedMap<String, Object> payRelBizParamsMap = new TreeMap<>();
            payRelBizParamsMap.put("merchant_order_no", payRelBizParams.getString("merchant_order_no"));
            payRelBizParamsMap.put("redirect_url", payRelBizParams.getString("redirect_url"));
            payRelParamsMap.put("biz_params", JSON.toJSONString(payRelBizParamsMap));

            boolean signStatus = RSA2ForMchtUtils.rsa2Check(payRelParamsMap, publicKey);

            if (!signStatus) {
                logger.info("请求支付平台返回签名验证失败：{},{}", payRelSign, signStatus);
                return fail(ReturnStatus.ERROR, "请求支付异常");
            }
            String payRelRedirectUrl = payRelBizParams.getString("redirect_url");

            if (null != payRelCode && !"".equals(payRelCode) && null != payRelRedirectUrl && !"".equals(payRelRedirectUrl)) {
                requestOrderMap.put("payUrl", payRelRedirectUrl);
                logger.info("返回移动端支付信息：{}", orderMainNo);
                return success(requestOrderMap);
            } else {
                return fail(ReturnStatus.STATUS_ERROR, "订单信息生成失败");
            }
        } catch (Exception e) {
            logger.info("支付平台异常{}", e);
            e.printStackTrace();
        }
        return fail(ReturnStatus.ERROR, "服务器内部异常");
    }

    /**
     * 分次支付
     *
     * @param orderMainNo 主订单号
     * @param payPrice    支付金额
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/payPart", method = RequestMethod.POST)
    public BaseBody<T> payPart(@RequestParam String orderMainNo, @RequestParam String clientIp, @RequestParam int payPrice,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("请求分次支付：支付金额={}", payPrice);

        String userToken = request.getHeader(loginToken);
        StuInfoOfRedis userInfo = getUserInfoByRedis(userToken);

        String mobile = userInfo.getMobile();

        BigDecimal myformat = new BigDecimal(payPrice);
        int price = myformat.movePointRight(2).intValue();

        RequestOrder requestOrder = null;

        Map<String, Object> orderMainMap = orderMainService.queryOrderMainInfo(orderMainNo, mobile);

        if (null != orderMainMap && orderMainMap.size() > 0) {
            Map<String, Object> orderMainInfo = (Map<String, Object>) orderMainMap.get("orderMainInfo");

            int status = Integer.parseInt(orderMainInfo.get("orderStatus")
                    .toString());
            int orderMainPrice = Integer.parseInt(orderMainInfo.get(
                    "orderPrice").toString());
            int orderMainPaid = Integer.parseInt(orderMainInfo.get("paid")
                    .toString());
            int payType = Integer.parseInt(orderMainInfo.get("payType")
                    .toString());

            if (status == 2) {
                return fail(ReturnStatus.STATUS_ERROR, "订单已支付完成，不可支付");
            }
            if (status == 3) {
                return fail(ReturnStatus.STATUS_ERROR, "订单退费中，不可支付");
            }
            if (status == 4) {
                return fail(ReturnStatus.STATUS_ERROR, "订单已退费，不可支付");
            }
            logger.info("分次支付：{}{}{}", orderMainPrice, orderMainPaid, price);

            if (((orderMainPrice - orderMainPaid) - price) < 0) {
                return fail(ReturnStatus.STATUS_ERROR, "支付金额不得超过订单未支付金额");
            }

            String classTypeCode = orderMainInfo.get("classTypeCode").toString();
            String classTypeName = orderMainInfo.get("classTypeName").toString();

            if (classTypeName.contains("基础")) {
                classTypeCode = "class_detail_base";
            } else {
                classTypeCode = "class_detail_employment";
            }

            if (payType == 1 || payType == 2) {
                List<Map<String, Object>> payOrderList = (List<Map<String, Object>>) orderMainMap.get("payOrderList");
                logger.info("分次支付，支付次数：{}", payOrderList.size() + 1);
                if (null == payOrderList || payOrderList.size() == 0) {
                    // 首次支付
                    if (classTypeCode.equals("class_detail_base")) {
                        if (price < 20000) {
                            return fail(ReturnStatus.PARAM_ERROR, "支付金额不低于200元");
                        }
                    }
                    if (classTypeCode.equals("class_detail_employment")) {
                        if (price < 50000) {
                            return fail(ReturnStatus.PARAM_ERROR, "支付金额不低于500元");
                        }
                    }

                } else if (payOrderList.size() == 1) {
                    if (payType == 2) {
                        return fail(ReturnStatus.PARAM_ERROR, classTypeName + "贷款订单不允许二次支付");
                    }
                    // 二次支付
                    if (classTypeCode.equals("class_detail_base")) {
                        if ((orderMainPrice - orderMainPaid - price) > 0) {
                            return fail(ReturnStatus.PARAM_ERROR, classTypeName
                                    + ":二次支付需支付订单剩余全部未支付金额："
                                    + (orderMainPrice - orderMainPaid));
                        }
                    }
                    if (classTypeCode.equals("class_detail_employment")) {
                        if ((orderMainPrice - orderMainPaid) > 300000 && price < 300000) {
                            return fail(ReturnStatus.PARAM_ERROR, "支付金额不低于3000元");
                        }
                    }
                } else {
                    // 三次支付
                    if ((orderMainPrice - orderMainPaid - price) > 0) {
                        return fail(ReturnStatus.PARAM_ERROR, classTypeName
                                + ":三次支付需支付订单剩余全部未支付金额："
                                + (orderMainPrice - orderMainPaid));
                    }
                }

                OrderSub orderSub = null;
                try {
                    orderSub = payService.createOrderSub(String.valueOf(userInfo.getId()), mobile, price, orderMainNo);
                } catch (Exception e) {
                	logger.error("支付订单生成异常:{}",e);
                    return fail(ReturnStatus.ERROR, "支付订单生成异常");
                }
                if(null==orderSub){
                	return fail(ReturnStatus.ERROR, "支付订单生成失败");
                }
                SortedMap<String, String> paramsMap = new TreeMap<>();
                paramsMap.put("app_id", appId);
                paramsMap.put("version", version);
                paramsMap.put("charset", "utf-8");
                paramsMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
                paramsMap.put("return_url", returnUrl);
                paramsMap.put("notify_url", notifyUrl);

                //业务参数集合
                SortedMap<String, Object> bizParams = new TreeMap<>();
                bizParams.put("merchant_order_no", orderSub.getOrderNo());
                BigDecimal bigDecimal = new BigDecimal(orderSub.getOrderPrice());//new BigDecimal("1");//
                bizParams.put("total_amount", bigDecimal.movePointLeft(2).floatValue());
                bizParams.put("original_price", bigDecimal.movePointLeft(2).floatValue());
                bizParams.put("currency", "CNY");
                bizParams.put("product_name", orderMainInfo.get("goodsName").toString());
                bizParams.put("order_desc", orderMainInfo.get("className").toString());
                bizParams.put("client_type", "H5");
                bizParams.put("conn_type", "INDIRECT");
                bizParams.put("client_ip", clientIp);
                bizParams.put("extend1", "enrol" + userInfo.getId() + mobile);

                paramsMap.put("biz_params", JSON.toJSONString(bizParams));

                String sign = RSA2ForMchtUtils.RSA2Sign(paramsMap, privateKey);
                paramsMap.put("sign", sign);

                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    try {
                        paramsMap.put(entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        logger.warn("####################################################################");
                        logger.warn("测试预下单数据准备接口 urlencode 编码异常.....");
                        logger.warn("####################################################################");
                    }
                }
                String paramsJson = JSON.toJSONString(paramsMap);
                logger.info("请求支付平台，支付报文：{}", paramsJson);

                try {
                    String payResult = HttpUtils.doPost(payUrl, paramsJson);
                    logger.info("支付平台返回报文：{}", payResult);
                    JSONObject payRelJsonObj = JSON.parseObject(payResult);

                    SortedMap<String, String> payRelParamsMap = new TreeMap<>();
                    String payRelCode = payRelJsonObj.getString("code");
                    String payRelMsg = payRelJsonObj.getString("msg");
                    String payRelTimestamp = payRelJsonObj.getString("timestamp");
                    String payRelSign = payRelJsonObj.getString("sign");
                    payRelParamsMap.put("code", payRelCode);
                    payRelParamsMap.put("msg", payRelMsg);
                    payRelParamsMap.put("timestamp", payRelTimestamp);
                    payRelParamsMap.put("sign", payRelSign);

                    String payRelBiz_params = payRelJsonObj.getString("biz_params");
                    JSONObject payRelBizParams = JSON.parseObject(payRelBiz_params);
                    SortedMap<String, Object> payRelBizParamsMap = new TreeMap<>();
                    payRelBizParamsMap.put("merchant_order_no", payRelBizParams.getString("merchant_order_no"));
                    payRelBizParamsMap.put("redirect_url", payRelBizParams.getString("redirect_url"));
                    payRelParamsMap.put("biz_params", JSON.toJSONString(payRelBizParamsMap));

                    boolean signStatus = RSA2ForMchtUtils.rsa2Check(payRelParamsMap, publicKey);

                    if (!signStatus) {
                        logger.info("请求支付平台返回签名验证失败：{},{}", payRelSign, signStatus);
                        return fail(ReturnStatus.ERROR, "请求支付异常");
                    }
                    String payRelRedirectUrl = payRelBizParams.getString("redirect_url");

                    Map<String, Object> requestOrderMap = new HashMap<String, Object>();
                    if (null != payRelCode && !"".equals(payRelCode) && null != payRelRedirectUrl && !"".equals(payRelRedirectUrl)) {
                        requestOrderMap.put("payUrl", payRelRedirectUrl);
                        logger.info("返回移动端支付信息：{}", orderMainNo);
                        return success(requestOrderMap);
                    } else {
                        return fail(ReturnStatus.STATUS_ERROR, "订单信息生成失败");
                    }
                } catch (Exception e) {
                    logger.info("支付平台异常");
                    e.printStackTrace();
                }
                return fail(ReturnStatus.ERROR, "服务器内部异常");


            } else {
                return fail(ReturnStatus.PARAM_ERROR, "订单为全额支付订单，不支持分次支付");
            }
        } else {
            return fail(ReturnStatus.DATA_NULL, "获取订单信息失败");
        }

    }

    /**
     * 获取数据参数
     *
     * @param request
     * @return
     */
    private SortedMap<String, Object> resolveRequestParamsMap(HttpServletRequest request) {
        SortedMap<String, Object> resultMap = null;
        try {
            InputStream in = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();

            String returnParam = new String(out.toByteArray(), "UTF-8");
            logger.info("############################################################################");
            logger.info("回调信息：{}", returnParam);
            logger.info("############################################################################");

            Map<String, Object> requestParams = JSON.parseObject(returnParam, Map.class, Feature.IgnoreNotMatch);
            resultMap = new TreeMap<String, Object>();
            if (null != requestParams && !requestParams.isEmpty()) {
                for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                        resultMap.put(key, URLDecoder.decode(value, "UTF-8"));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            logger.error("订单确认获取参数错误");
        }
        return resultMap;
    }

    public static void main(String[] args) throws Exception {
        SortedMap<String, String> paramsMap = new TreeMap<>();
        paramsMap.put("app_id", "pp201801167OmvcrgD");
        paramsMap.put("version", "1.0");
        paramsMap.put("charset", "utf-8");
        paramsMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        paramsMap.put("return_url", "http://dev-enrol.itcast.cn/my-order.html");
        paramsMap.put("notify_url", "http://dev-enrol.itcast.cn/enrol/payResultController/payResult");

        //业务参数集合
        SortedMap<String, Object> bizParams = new TreeMap<>();
        bizParams.put("merchant_order_no", "jsadlkfoasdiufosaj123312");
        bizParams.put("total_amount", 123);
        bizParams.put("original_price", 123);
        bizParams.put("currency", "CNY");
        bizParams.put("product_name", "test");
        bizParams.put("order_desc", "test");
        bizParams.put("client_type", "H5");
        bizParams.put("conn_type", "INDIRECT");
        bizParams.put("client_ip", "172.168.1.9");
        bizParams.put("extend1", "enrol" + "15210173019");

        paramsMap.put("biz_params", new JSONObject(bizParams).toString());
        System.out.println(JSON.toJSONString(paramsMap));
        String sign = RSA2ForMchtUtils.RSA2Sign(paramsMap, "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC5mEO2e7nrmR+qbTvAx/mSJVk3zAkjsV5W2csS/3UBf6ExqZSUrKtLOL6KsPs5xG1vlkgh86qnQ8MeaMis7mqCRN1oQnQeFoW05cxBYVwhFTMF2MREnQQSzG0+HUec3ZGztwcPNT6gK+GpvjGLs6yCuTWWxbwhxKPvTvEWqcKY/LgbYGdfblbcsiNbpvLAuIaZcHno2syHzQiSheleICQ6qlybOh4y31id7cHQNcIUDDFf7lCPLRHOYMlHrI6D6SAdXDhvEjs/5kj+N/TN4T3/iXoy1rSHwtiHhdCAqmi2pjlrmdj4xz5zS8pWrbOzWOwQ7y2zdTHU1LHvFrjMpRR3AgMBAAECggEAGsANknHG3GRXohLYd/Laz+u+LkKkVpasCEyf7n8PeEXTD/tnRP6WOeNV41ua+jqLo3e/sdUTFcS4jNuBgRtkEp6mYu6hGe0ZHHe6tvm3c7VAg2k48MLI4YTBgiBqP/vXklWhGNoNyrxIbFTTg1VEWhsj1DVv5lYXYti3+xJA0Bft81FWY37lucr3jeEvf9raVeXedWraqC9AzUl7Be3mjCpm+xzIsWj1Cz8v2D2ASoDvMdSKgDYD/74x/WIwaxWTHecsJpCCFE8hpQDW/3Ds9n+1mI8ipS400wU8ww/KRiLZg3Cu2TC4LkpLXpJyQ+eH/RCNWaqsikp9bB/tvZhKgQKBgQDkAKfpbwaBtiJ2H/zF8lAU84t5dsxWyVqjYUJ3bA7/JbXrrWGrgfi/x/KIpzlKRCJ5MUvkrsxtwl7bsNqv00T9/RcXCfjUFZm1ERFW2LTjD/0T72TDIrGdHeDVDy1dn33YogL1Jid4auuX5FcyUH2G2xFBCGMej5CJq218+xVcoQKBgQDQYoEop8sLf1OjPJ7PK3b3EzdTgO07SP3i+4pxQcY5PlcPKQqXRc4xhQzdI6NKOjnJBL4qDckVf/8QBPSCyBx1SZCUT+NYZmCUdvm8rt/PUyewInhOZfc2706WufFTj8hk9AZgmeKtSfLFUBYyunRN8PXe8vFips2temxr9rCCFwKBgQCOdg50n+er4pbTwhbZxBbfkHEkeuMgkv1sOlgicEVKjVCHKrVtSW+YvSFA22PDkKENooyuHb4kEp/cmzt5QwPHw3zK63MraTe6WCjCX/NeMN5Lt3f5KVNbPZD+71XEOuSSGGDKtoVRC1WbeZQ2Hu7f9T9pqAjuzntcWd22itgFIQKBgH3SbHAy2Eup5sNScAVhFiTbSnW2DAfW681o2k/GSjz4IjL6MfXi5TxLPNgtk4PXIlr47SWRS1AbB9QRWd25nE1JTO3wSrJupnQeXm6KHIMfj4AvX/reYGWgGxCxV0CAOZyTJ2KeqBq/4sE2uSCKKn2BN8QVZqCNXPUZTWvm+O6jAoGBANyKPhW1CImhAQKJrq1fdFUUn0Kfxc5s/LWNp0FP7XCYAFLKNS19jbgZZzW2CHZsUMvIDrn6pPvA3gNPsPS21p09IicLg68KVls+aKEBHl83ENty7Na9MbhTOJG6ovFET1D7Aw5NWfACyYn/1pfdEfiEAN8V/JfLgexREg99qpd7");
        paramsMap.put("sign", sign);
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            try {
                paramsMap.put(entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }
        String paramsJson = JSON.toJSONString(paramsMap);
        System.out.println("请求支付上传报文：" + paramsJson);
        String payResult = HttpUtils.doPost("http://proxy.boxuegu.com/pp-gateway-d/pp-gateway-d/gateway/unifyOrder", paramsJson);
        JSONObject payRelJsonObj = JSON.parseObject(payResult);
        System.out.println("返回报文：" + payResult);
        SortedMap<String, String> payRelParamsMap = new TreeMap<>();
        String payRelCode = payRelJsonObj.getString("code");
        String payRelMsg = payRelJsonObj.getString("msg");
        String payRelTimestamp = payRelJsonObj.getString("timestamp");
        String payRelSign = payRelJsonObj.getString("sign");

		/*{	"biz_params":{		
			"merchant_order_no":"jsadlkfoasdiufosaj123312",		
			"redirect_url":"http://proxy.boxuegu.com/pp-gateway-d/pp-gateway-d/gateway/mobileCheckout/getPayIndex?a=255&b=172.168.1.9&c=H5&d=0&e="	},	
			"code":"0000",	
			"msg":"下单成功",	
			"sign":"heT+InUfiww7xlRHHMElLYPSEZP7B7aJU7LApARSm8+vXL0N3SdikHi8oWNzOBAxlMMcYG5AD2Tnh6chyTkGBPxQClBhhXNLWcb+STZXHo5BrFPZWBndNjAWmGo1XBa06Z0GstSi0imAigylGBaIxDajZjqUr8e8Q4Y+iJ5d3VILTFC9HO4Y37L/bDJSUA+XVZQTEwSYVycaUHlEaBKUVqFNB9MvI8/Jz3MDjwAG9EigdfpRj36qVonzWlWjnmzYwrvhLehI/HqnP2jvEODZCdR2RJ17dZKrj0geBBWkEmbxAsGwHKaTxLVcJ07GbsCHFCM8O8n0LbywTYUMBF6v5g==",	
			"timestamp":"2018-01-19 11:33:50"}*/

        payRelParamsMap.put("code", payRelCode);
        payRelParamsMap.put("msg", payRelMsg);
        payRelParamsMap.put("timestamp", payRelTimestamp);
        payRelParamsMap.put("sign", payRelSign);

        String payRelBiz_params = payRelJsonObj.getString("biz_params");
        JSONObject payRelBizParams = JSON.parseObject(payRelBiz_params);
        SortedMap<String, Object> payRelBizParamsMap = new TreeMap<>();
        payRelBizParamsMap.put("merchant_order_no", payRelBizParams.getString("merchant_order_no"));
        payRelBizParamsMap.put("redirect_url", payRelBizParams.getString("redirect_url"));
        payRelParamsMap.put("biz_params", JSON.toJSONString(payRelBizParamsMap));

        System.out.println(JSON.toJSONString(payRelParamsMap));
        System.out.println("验证签名之前：" + payRelParamsMap.get("code") + payRelParamsMap.get("msg") + payRelParamsMap.get("timestamp") + payRelParamsMap.get("charset") + payRelParamsMap.get("biz_params"));

        boolean signmy = RSA2ForMchtUtils.rsa2Check(payRelParamsMap, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAirSttzhIu1rJeQReJlnfDx6wcFJEshAoYm0cA0u1ZYMWo1DQF0DmBKDsIGepmoabARM6a6Dh2KRK8h7j5LuzqopiW7NZC19L1hCAyKkTb+Il8ADJKCpjivkaPcEzWi8ahXtQKJGL9OO+qSjqOrTrKTX/a//Px6FNRFpU8/W5bGj+dJT8kptr7WnhRLT+7NCX0+fEiQjZWFZ1IBwZOKr/hEEp0hk8ADrjuEUdNoo/d3EVbQlwPjyLXxdiyo96oau2RGgmqRpvFsRxE6/6zXhAmVSqPwWCRCA3pMaWWtPdLvNUOdDwOJmbgzu0aruU48ZwwQNbs2HhglxERaipt6AbSwIDAQAB");
        System.out.println(signmy);
    }

}