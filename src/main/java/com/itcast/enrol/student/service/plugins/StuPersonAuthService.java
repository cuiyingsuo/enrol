package com.itcast.enrol.student.service.plugins;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class StuPersonAuthService {
	
	// 统一记录日志类
	private Logger logger = LoggerFactory.getLogger("enrol");
		
	private static String encoding = "UTF-8";
	private static String algorithm = "HmacSHA256";
	private static String projectId = "1111563517";
	private String projectSecret = "95439b0863c241c63a861b87d1e647b7";
	
	@Value("${person.urlPath}")
	private String urlPath;
	
	public static void main(String[] args) throws Exception{
		System.out.println(new StuPersonAuthService().requestPerson("任森涛", "142703199405200619"));
	}
	/***
	 * 个人实名认证-请求
	 * @throws Exception
	 */
	public boolean requestPerson(String name,String idno) throws Exception {
		String signature = null;
		String resultJSON = null;
		// 设置个人银行四要素请求参数
		String personApply = setPersonJSONStr(name,idno);
		signature = EncryptionHMAC.getHMACHexString(personApply, projectSecret, algorithm, encoding);
		logger.info("个人实名认证-请求开始：{},{},{}",urlPath,signature,personApply);
		// 银行四要素认证请求地址（测试环境）
		
		//模拟请求
		resultJSON = sendPost(urlPath, signature, personApply);
		logger.info("个人实名认证-请求完成：{}",resultJSON);
		/* 至此，已完成个人银行四要素实名认证请求 */
		JSONObject personJSON = JSONObject.parseObject(resultJSON);
		if(Integer.parseInt(String.valueOf(personJSON.get("errCode")))==0) {
			return true;
		}
		return false;
	}
	
	/***
	 * 设置个人银行四要素请求参数
	 * 
	 * @return
	 */
	private static String setPersonJSONStr(String name,String idno) {
		JSONObject obj = new JSONObject();
		//obj.put("mobile", "1520581****");
		obj.put("name", name);
		//obj.put("cardno", "621483******7383");
		obj.put("idno", idno);
		return obj.toString();
	}
	
	/***
	 * 设置Headers报头信息
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	private static Map<String, String> getHeaders(String signature, String jsonStr) throws Exception {
		Map<String, String> headersMap = new LinkedHashMap<String, String>();
		headersMap.put("X-timevale-project-id", projectId);
		headersMap.put("X-timevale-signature", signature); // 请求参数和projectSecret参数通过HmacSHA256加密的16进制字符串
		headersMap.put("signature-algorithm", algorithm);
		headersMap.put("Content-Type", "application/json");
		headersMap.put("Charset", encoding);
		return headersMap;
	}
	
	/***
	 * 模拟发送POST请求
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	private static String sendPost(String urlPath, String signature, String jsonStr) throws Exception {
		String result = null;
		// 建立连接
		URL url = new URL(urlPath);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		// 设置Headers参数
		httpConn.setDoOutput(true); // 需要输出
		httpConn.setDoInput(true); // 需要输入
		httpConn.setUseCaches(false); // 不允许缓存
		httpConn.setRequestMethod("POST"); // 设置POST方式连接
		// 设置Headers属性
		for (Entry<String, String> entry : getHeaders(signature, jsonStr).entrySet()) {
			httpConn.setRequestProperty(entry.getKey(), entry.getValue());
		}

		// 连接会话
		httpConn.connect();

		// 建立输入流，向指向的URL传入参数
		DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
		// 设置请求参数
		dos.write(jsonStr.toString().getBytes("UTF-8"));
		System.out.println(jsonStr);
		dos.flush();
		dos.close();
		// 获得响应状态
		int resultCode = httpConn.getResponseCode();
		if (HttpURLConnection.HTTP_OK == resultCode) {
			StringBuffer sb = new StringBuffer();
			String readLine = new String();
			BufferedReader responseReader = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			while ((readLine = responseReader.readLine()) != null) {
				sb.append(readLine);
			}
			// System.out.println(httpConn.get.getRequestURI());
			responseReader.close();
			result = sb.toString();
		}
		return result;
	}
}
