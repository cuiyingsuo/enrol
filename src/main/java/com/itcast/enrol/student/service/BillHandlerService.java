package com.itcast.enrol.student.service;

import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.entity.Bill;
import com.itcast.enrol.common.utils.HttpUtil;
import com.itcast.enrol.common.utils.JsonUtil;
import com.itcast.enrol.common.utils.XlsUtil;
import com.itcast.enrol.common.utils.payment.RSA2ForMchtUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author liuzs
 *         Created by liuzhongshuai on 2018/1/22.
 */
@Service
public class BillHandlerService extends BaseService<Bill> {


    private final Logger logger = LoggerFactory.getLogger(BillHandlerService.class);

    /**
     * 支付平台appId
     */
    @Value("${itcast-pay.app_id}")
    private String appId;

    @Value("${itcast-pay.private-key}")
    private String privateKey;

    @Value("${itcast-pay.bill-dir}")
    private String billLocal;

    @Value("${itcast-pay.bill-url}")
    private String billUrl;

    @Value("${spring.profiles.active}")
    private String active;


    @Transactional(rollbackFor = Exception.class)
    public List<Bill> getBillToLocal(String date) {
        SortedMap<String, String> params = new TreeMap();
        params.put("app_id", appId);
        params.put("format", "JSON");
        params.put("version", "1.0");
        params.put("charset", "UTF-8");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("biz_params", "{\"bill_date\":\"" + date + "\",\"bill_type\":\"ALL\"}");

        String sign = RSA2ForMchtUtils.RSA2Sign(params, privateKey);
        params.put("sign", sign);
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                params.put(entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String resultStr = HttpUtil.httpJsonPost(billUrl, params);

        if (StringUtils.isEmpty(resultStr)) {
            logger.error("获取对账结果异常!,返回值为：{}", resultStr);
            return null;
        }
        Map<String, String> resultMap = JsonUtil.jsonToObject(resultStr, Map.class);
        if (!"0000".equals(resultMap.get("code"))) {
            logger.error("获取对账结果异常!,返回值为：{}", resultStr);
            return null;
        }
        //获取下载对账结果账单
        String billUrl = resultMap.get("bill_download_url");

        if("product".equals(active)){
            billUrl=billUrl.replace("https","http");
        }
        String fileName = "bill_" + System.currentTimeMillis() + ".xls";
        //下载对账单到本地
        HttpUtil.httpGetownload(billUrl, billLocal, fileName);
        //解析
        List<Bill> billList = XlsUtil.xlsResolver(billLocal + "/" + fileName, true);
        this.batchInsert(billList);
        return billList;
    }
}