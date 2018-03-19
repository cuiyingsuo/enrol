package com.itcast.enrol.common.dao.permission;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.RolePermission;

/**
 * 
 * EnrolRolePermissionMapper数据库操作接口类
 * 
 **/

public interface RolePermissionMapper extends BaseDao<RolePermission>{


    /**
     * 通过角色查询权限
     * @param roleIds
     * @return
     */
    List<Map<String,String>> queryPermissionByRole(@Param(value="roleIds") String roleIds);


	

}