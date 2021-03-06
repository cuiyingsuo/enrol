package com.itcast.enrol.common.utils;

import java.util.Map;

import tk.mybatis.mapper.util.StringUtil;

/**
 * Created by liuzhongshuai on 2017/12/25.
 */
public class HttpHelper {


    /**
     * 通过参数 生成 签名(md5)信息 并追加到参数列表返回
     * 参数顺序 以入参的 map key 顺序为主（建议 传递 linkedMap）
     * 现有业务 参数顺序都以 ascii排序
     *
     * @param param
     * @param appKey:秘钥
     * @return map
     */
    public static Map<String, String> handlerSigUrlByMd5(Map<String, String> param, String appKey) {

        StringBuilder stringBuilder = new StringBuilder("");
        for (Map.Entry<String, String> entry : param.entrySet()) {
        	if(entry.getValue()!=null){
	            stringBuilder.append(entry.getKey());
	            stringBuilder.append("=");
	            stringBuilder.append(entry.getValue());
	            stringBuilder.append("&");
        	}
        }
        stringBuilder.append(appKey);
        String md5Str = stringBuilder.toString();
        String sign = MD5Util.encryption(md5Str).toLowerCase();

        param.put("sig", sign);
        return param;
    }
}
