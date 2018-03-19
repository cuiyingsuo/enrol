package com.itcast.enrol.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.dao.user.UserRoleMapper;

/**
 * 用户角色表 服务类
 **/
@Service
public class ManagerUserRoleService {

	@Autowired
	private UserRoleMapper userRoleDao;

}
