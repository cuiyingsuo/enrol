package com.itcast.enrol.common.interceptor;

import com.itcast.enrol.common.base.BaseMenu;
import com.itcast.enrol.common.base.UserPermiss;
import com.itcast.enrol.common.exception.PermissException;
import com.itcast.enrol.common.utils.BeanUtils;
import com.itcast.enrol.common.utils.MD5Util;
import com.itcast.enrol.common.utils.RedisUtil;
import com.itcast.enrol.common.utils.TokenUtil;
import com.itcast.enrol.management.service.ManagerUserService;
import com.itcast.enrol.management.vo.UserEx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/11/17.
 *         管理端 用户鉴权
 */
public class PermissInterceptor extends HandlerInterceptorAdapter {

    @Value("${server.context-path}")
    private String contextPath;

    @Value("${server.token-key}")
    private String tokenKey;

    @Autowired
    private ManagerUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uriStr = request.getRequestURI().replace(contextPath, "");

        UserEx userEx = TokenUtil.getLocalUser();

        UserPermiss userPermiss = TokenUtil.getUserPermissForRedis(userEx);

        if (null == userPermiss) {
            //重新放菜单到redis
            userPermiss = userService.roleAndMenus(userEx.getId());
            //序列化
            byte[] permsObjectStr = BeanUtils.objectSerialiable(userPermiss);
            String md5PermsStr = MD5Util.encryption(userEx.getMobile() + TokenUtil.PERMISS_SUFFIX);
            RedisUtil.set(md5PermsStr, permsObjectStr, (long) 4000);
        }

        List<BaseMenu> baseMenuList = userPermiss.getUserPerms();

        List list = baseMenuList.stream().filter(chile -> uriStr.equals(chile.getUrl())).collect(Collectors.toList());

        if (list.size() > 0) {
            return true;
        }
        throw new PermissException("您无该功能权限!", 50001);
    }

}
