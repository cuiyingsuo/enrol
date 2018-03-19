package com.itcast.enrol.common.utils;

import com.allinpay.util.StringUtils;
import com.itcast.enrol.common.base.UserPermiss;
import com.itcast.enrol.common.entity.User;
import com.itcast.enrol.common.exception.UnLoginException;
import com.itcast.enrol.management.vo.UserEx;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuzs
 */
public class TokenUtil {


    /**
     * 登录token 后缀
     */
    public static String USER_LOGIN_SUFFIX = "ENROL_MANAGER_USER_LOGIN";

    /**
     * 菜单及权限 存入 redis key 后缀
     */
    public static String PERMISS_SUFFIX = "ENROL_MANAGER_PERMISS";


    /**
     * 保存登录会话
     */
    public static final ThreadLocal<UserEx> LOCAL_USER = new ThreadLocal<>();

    /**
     * 保存权限会话
     */
    public static final ThreadLocal<UserPermiss> LOCAL_PERMISS = new ThreadLocal<>();


    public static UserPermiss getLocalPermiss() {

        return LOCAL_PERMISS.get();
    }

    public static void setLocalPermiss(UserPermiss userPermiss) {

        LOCAL_PERMISS.remove();
        LOCAL_PERMISS.set(userPermiss);
    }

    public static void setLocalUser(UserEx userEx) {
        LOCAL_USER.remove();
        LOCAL_USER.set(userEx);
    }

    public static UserEx getLocalUser() {
        return LOCAL_USER.get();
    }

    public static String getToken(String pre) {
        return MD5Util.encryption(pre + System.currentTimeMillis());
    }


    /**
     * redis 获取权限信息
     *
     * @param userEx
     * @return
     */
    public static UserPermiss getUserPermissForRedis(UserEx userEx) {
        String md5MenusStr = MD5Util.encryption(userEx.getMobile() + TokenUtil.PERMISS_SUFFIX);
        byte[] menusObjectStr = (byte[]) RedisUtil.get(md5MenusStr);

        Object obj = BeanUtils.objectDeserialization(menusObjectStr);

        return (UserPermiss) obj;
    }


    /**
     * 用户登录后保存信息到redis
     *
     * @param user
     * @param times
     * @return token
     */
    public static String setUserEx(User user, int times) {
        Map<String, String> userInfoMap = new HashMap<>(3);
        userInfoMap.put("userId", String.valueOf(user.getId()));
        userInfoMap.put("userName", user.getName());
        userInfoMap.put("mobile", user.getMobile());
        HashOperations<String, String, String> hashOperations = RedisUtil.getRedisTemplate().opsForHash();

        String token = MD5Util.encryption(user.getMobile() + TokenUtil.USER_LOGIN_SUFFIX);

        hashOperations.putAll(token, userInfoMap);
        RedisUtil.expire(token, times);

        return token;
    }

    /**
     * redis 获取登录信息
     *
     * @param token
     * @return
     */
    public static UserEx getUserEx(String token) {

        if (StringUtils.isEmpty(token)) {
            throw new UnLoginException("用户未登录!", 1002);
        }
        HashOperations<String, String, String> hashOperations = RedisUtil.getRedisTemplate().opsForHash();

        Map<String, String> values = hashOperations.entries(token);

        if (CollectionUtils.isEmpty(values)) {
            throw new UnLoginException("请重新登录!", 1002);
        }

        String userId = values.get("userId");
        String userName = values.get("userName");
        String mobile = values.get("mobile");
        UserEx userEx = new UserEx();
        userEx.setId(Long.parseLong(userId));
        userEx.setName(userName);
        userEx.setMobile(mobile);

        return userEx;
    }

}
