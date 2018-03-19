package com.itcast.enrol.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.dao.permission.RolePermissionMapper;

/**
 * 角色与资源关系表 服务类
 **/
@Service
public class ManagerRolePermissionService {

	@Autowired
	private RolePermissionMapper rolePermissionDao;

}
