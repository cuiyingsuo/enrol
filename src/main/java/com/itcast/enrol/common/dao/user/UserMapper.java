package com.itcast.enrol.common.dao.user;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.User;

/**
 * EnrolUserMapper数据库操作接口类
 **/

public interface UserMapper extends BaseDao<User> {


    /**
     * 用户查询出角色
     *
     * @param userId
     * @return
     */
    List<Map<String, String>> roleByUser(Long userId);


}