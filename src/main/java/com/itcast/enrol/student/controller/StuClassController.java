package com.itcast.enrol.student.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.student.service.StuClassService;

/**
 * 
 * 班级表 访问控制器类
 * 
 **/
@RestController
@RequestMapping("/classController")
public class StuClassController extends StuBaseController{

	@Autowired
	private StuClassService classService;
	/**
	 * 获取班级列表（商品规格弹框）
	 * @param goodsId	商品id
	 * @param campusId	校区id
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/getClassListOfSpec", method = RequestMethod.GET)
	public BaseBody<T> getClassListOfSpec(@RequestParam Long goodsId,@RequestParam Long campusId) throws ParseException{
		
		List<Map<String,Object>> classList = classService.queryClassListOfGoodsSpec(goodsId,campusId);
		
		if (null != classList&&classList.size()>0) {
			return success(classList);
		}
		
		return fail();
	}
}
