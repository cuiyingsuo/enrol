package com.itcast.enrol.common.dao.student;

import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.Student;

/**
 * 
 * EnrolStudentMapper数据库操作接口类
 * 
 **/

public interface StudentMapper extends BaseDao<Student> {

	/**
	 *
	 * @param params
	 * @return
	 */
	int updatePasswordByMobile(Map<String, String> params);
	
}