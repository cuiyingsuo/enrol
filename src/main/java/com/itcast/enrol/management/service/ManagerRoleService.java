package com.itcast.enrol.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.permission.RoleMapper;
import com.itcast.enrol.common.entity.Role;

/**
 * 角色表 服务类
 **/
@Service
public class ManagerRoleService extends BaseService<Role> {

    @Autowired
    private RoleMapper roleDao;

}
