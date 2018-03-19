package com.itcast.enrol.student.service.plugins;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.utils.SMSSendUtil;

@Service
public class StuSMSService {
	@Value("${sms.templeteid}")
	private String templeteid;
	@Value("${sms.channel}")
	private String channel;
	@Value("${sms.password}")
	private String password;
	@Value("${sms.requestUrl}")
	private String requestUrl;
	/**
	 * 合同验证码
	 * @param mobile
	 * @return
	 */
	public String sendContractSms(String mobile){
		String msgNum = SMSSendUtil.getMsgNum();
		boolean msgStatus=SMSSendUtil.sendMessage(mobile, templeteid, "{\"vcode\":\""+msgNum+"\"}", SMSSendUtil.USAGE_LOGIN, SMSSendUtil.SMSTYPE_VERIFICATION_CODE, channel, password, requestUrl);
		if(msgStatus){
			return msgNum;
		}
		return "";
	}
}
