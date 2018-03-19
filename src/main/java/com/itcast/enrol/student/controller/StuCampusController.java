package com.itcast.enrol.student.controller;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.entity.Campus;
import com.itcast.enrol.student.service.StuCampusService;

/**
 * 
 * 分校表 访问控制器类
 * 
 **/
@RestController
@RequestMapping("/campusController")
public class StuCampusController extends StuBaseController{

	@Autowired
	private StuCampusService campusService;

	/**
	 * 查询分校信息
	 * @param id	校区id
	 * @return
	 */
	@RequestMapping(value = "/getCampusInfo", method = RequestMethod.GET)
	public BaseBody<T> getCampusInfo(@RequestParam Long id) {
		
		Campus campus = campusService.selectByPrimaryKey(id);
		if (null!=campus) {
			return success(campus);
		}
		
		return fail();
	}
	
}
