package com.itcast.enrol.common.annotation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.itcast.enrol.common.utils.TokenUtil;
import com.itcast.enrol.management.vo.UserEx;

/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/11/10.
 */
public class SessionHandler implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.hasParameterAnnotation(Session.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        Class<?> targetClass = methodParameter.getParameterType();

        if (targetClass.isAssignableFrom(UserEx.class)) {

            String tokenKey = "enrol-token";

            String userToken = request.getHeader(tokenKey);
            if (org.springframework.util.StringUtils.isEmpty(userToken)) {
                userToken = request.getParameter(tokenKey);
            }
            UserEx userEx = TokenUtil.getUserEx(userToken);
            return userEx;
        }
        return null;
    }
}
