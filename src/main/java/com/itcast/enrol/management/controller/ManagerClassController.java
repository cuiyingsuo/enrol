package com.itcast.enrol.management.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.ManageBaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.entity.EnrolClass;
import com.itcast.enrol.management.service.ManagerClassService;

/**
 * @author liuzs Created by liuzhongshuai on 2017/10/24.
 */
@RestController
@RequestMapping("/managenment/class")
public class ManagerClassController {

	@Autowired
	private ManagerClassService managerClassService;

	/**
	 * 条件分页查询班级列表
	 *
	 * @param enrolClass
	 * @return
	 */
	@GetMapping("/queryClassList")
	public ManageBaseBody<Map<String, String>> queryClassList(
			EnrolClass enrolClass,
			@RequestParam(defaultValue = "1", name = "page") int startNum,
			@RequestParam(defaultValue = "10", name = "limit") int pageSize) {
		ManageBaseBody<Map<String, String>> baseBody = new ManageBaseBody();
		if (null == enrolClass) {
			enrolClass = new EnrolClass();
		}
		BasePage<Map<String, String>> bannerBasePage = managerClassService
				.queryClassToPage(enrolClass, startNum, pageSize);
		baseBody.setMsg("查询成功!");
		baseBody.setCode(0);
		baseBody.setCount(bannerBasePage.getCount());
		baseBody.setData(bannerBasePage.getPageData());
		return baseBody;
	}

	/**
	 * 编辑班级信息
	 * @param enrolClass
	 * @return
	 */
	@GetMapping("/editClassInfo")
	public ManageBaseBody<String> editClassInfo(EnrolClass enrolClass) {
		ManageBaseBody<String> baseBody = new ManageBaseBody();
		if (null == enrolClass || enrolClass.getId() == null) {
			baseBody.setMsg("参数：班级id，必传！");
			baseBody.setCode(ReturnStatus.PARAM_ERROR);
			baseBody.setSuccess(false);
			return baseBody;
		}
		EnrolClass ec = managerClassService.selectByPrimaryKey(enrolClass
				.getId());
		ec.setStartDate(enrolClass.getStartDate());
		ec.setClassTime(enrolClass.getClassTime());
		ec.setPlan(enrolClass.getPlan());
		
		ec.setId(enrolClass.getId());
		managerClassService.updateByPk(ec);

		baseBody.setMsg("保存成功");
		baseBody.setCode(0);
		return baseBody;
	}

}
