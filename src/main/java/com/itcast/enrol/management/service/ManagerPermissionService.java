package com.itcast.enrol.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.dao.permission.PermissionMapper;

/**
 * 资源表 服务类
 **/
@Service
public class ManagerPermissionService {

	@Autowired
	private PermissionMapper permissionDao;

}
