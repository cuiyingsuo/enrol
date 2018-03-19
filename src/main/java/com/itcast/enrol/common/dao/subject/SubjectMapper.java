package com.itcast.enrol.common.dao.subject;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.Subject;


/**
 * 
 * EnrolSubjectMapper数据库操作接口类
 * 
 **/

public interface SubjectMapper extends BaseDao<Subject> {


	
	/**
	 * 
	 * 学科列表
	 */
	List<Subject> selectSubjectList();

	/**
	 * 学科下拉选查询 返回字段（id，name）
	 * @param status
	 * @return
	 */
	List<Map<String,Object>> querySubjectListByStatus(Byte status);
}