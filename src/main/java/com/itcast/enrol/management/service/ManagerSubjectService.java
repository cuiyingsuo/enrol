package com.itcast.enrol.management.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.subject.SubjectMapper;
import com.itcast.enrol.common.entity.Subject;

/**
 * 学科表 服务类
 **/
@Service
public class ManagerSubjectService extends BaseService<Subject> {

	@Autowired
	private SubjectMapper subjectDao;

	public Subject queryByPrimaryKey(Long id) {
		return subjectDao.selectByPrimaryKey(id);
	}

	public List<Subject> querySubjectList() {
		return subjectDao.selectSubjectList();
	}

	/**
	 * 学科下拉选查询 通过状态 返回字段（id，name）
	 *
	 * @param status
	 * @return
	 */
	public List<Map<String, Object>> querySubjectListByStatus(Byte status) {
		return subjectDao.querySubjectListByStatus(status);
	}

}
