package com.itcast.enrol.student.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.entity.Subject;
import com.itcast.enrol.student.service.StuSubjectService;

/**
 * 
 * 学科表 访问控制器类
 * 
 **/
@RestController
@RequestMapping("/subjectController")
public class StuSubjectController extends StuBaseController{

	// 统一记录日志类
	private Logger Log = LoggerFactory.getLogger("enrol");
	@Autowired
	// 当前业务操作接口bo
	private StuSubjectService subjectService;

	/**
	 * 查询banner信息
	 * 
	 * @param id
	 *            学科id
	 * @return
	 */
	@RequestMapping(value = "/getSubjectInfo", method = RequestMethod.GET)
	public BaseBody getSubjectInfo(@RequestParam Long id) {

		Subject subjectInfo = subjectService.queryByPrimaryKey(id);
		if (null != subjectInfo) {
			return success(subjectInfo);
		}
		return fail();
	}

	/**
	 * 查询学科列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getSubjectlist", method = RequestMethod.GET)
	public BaseBody getSubjectlist() {

		List<Subject> esList = subjectService.querySubjectList();
		if (esList.size() > 0) {
			return success(esList);
		}
		
		return fail();
	}
}
