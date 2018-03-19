package com.itcast.enrol.common.interceptor;

import com.itcast.enrol.common.entity.User;
import com.itcast.enrol.common.exception.UnLoginException;
import com.itcast.enrol.common.utils.MD5Util;
import com.itcast.enrol.common.utils.RedisUtil;
import com.itcast.enrol.common.utils.TokenUtil;
import com.itcast.enrol.management.vo.UserEx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liuzhongshuai on 2017/11/17.
 */
public class LoginInterceptorForManager extends HandlerInterceptorAdapter {

    @Value("${server.token-key}")
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
        UserEx userEx = TokenUtil.getUserEx(tokenValue);

        if (null == userEx) {
            throw new UnLoginException("用户未登录!", 1002);
        }
        //保存当前会话
        TokenUtil.setLocalUser(userEx);
        //初始化超时 时长
        RedisUtil.expire(tokenValue, 3600);

        return true;
    }
}
