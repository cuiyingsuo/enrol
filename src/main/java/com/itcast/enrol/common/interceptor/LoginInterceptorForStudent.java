package com.itcast.enrol.common.interceptor;

import com.itcast.enrol.common.exception.UnLoginException;
import com.itcast.enrol.common.utils.RedisUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuzhongshuai
 */
public class LoginInterceptorForStudent extends HandlerInterceptorAdapter {

    @Value("${server.token-key-mobile}")
    private String loginToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String tokenValue = request.getHeader(loginToken);

        if (StringUtils.isEmpty(tokenValue)) {
            tokenValue = request.getParameter(loginToken);
        }
        if (StringUtils.isEmpty(tokenValue)) {
            //用户未登录
            throw new UnLoginException("用户未登录!", 1002);
        }
        if (!RedisUtil.hasKey(tokenValue)) {
            //用户登录超时，请重新登录
            throw new UnLoginException("用户登录会话超时,请重新登录!", 1002);
        }
        //初始化超时 时长
        RedisUtil.expire(tokenValue, 3600);
        //}
        return true;
    }
}