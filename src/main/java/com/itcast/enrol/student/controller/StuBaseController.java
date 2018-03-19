package com.itcast.enrol.student.controller;

import org.apache.poi.ss.formula.functions.T;

import com.allinpay.util.StringUtils;
import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.exception.UnLoginException;
import com.itcast.enrol.common.utils.RedisUtil;
import com.itcast.enrol.student.vo.StuInfoOfRedis;

public class StuBaseController {
	/**
	 * 用token在redis查用户信息
	 * 
	 * @param token
	 * @return
	 */
	protected StuInfoOfRedis getUserInfoByRedis(String token) {
		StuInfoOfRedis loginInfo = null;
		if (StringUtils.isEmpty(token)) {
            throw new UnLoginException("用户未登录!", ReturnStatus.LOGIN_ERROR);
        }
		if (RedisUtil.hasKey(token)) {
			loginInfo = (StuInfoOfRedis)RedisUtil.get(token);
		} else {
			throw new UnLoginException("用户登录超时!", ReturnStatus.LOGIN_ERROR);
		}
		return loginInfo;
	}
	/**
	 * 成功返回
	 * @param content
	 * @return
	 */
	protected BaseBody<T> success(Object content) {
		BaseBody baseBody = new BaseBody();

		baseBody.setSuccess(true);
		baseBody.setCode(200);
		baseBody.setContent(content);
		baseBody.setMessage("成功");
		return baseBody;
	}
	/**
	 * 失败返回
	 * @param code
	 * @param msg
	 * @return
	 */
	protected BaseBody<T> fail(int code, String msg) {
		BaseBody baseBody = new BaseBody();
		
		baseBody.setSuccess(false);
		baseBody.setCode(code);
		baseBody.setMessage(msg);
		return baseBody;
	}
	
	/**
	 * 失败返回
	 * @param code
	 * @param msg
	 * @return
	 */
	protected BaseBody<T> fail() {
		BaseBody baseBody = new BaseBody();
		
		baseBody.setSuccess(false);
		baseBody.setCode(ReturnStatus.DATA_NULL);
		baseBody.setMessage("查无数据");
		return baseBody;
	}
}
