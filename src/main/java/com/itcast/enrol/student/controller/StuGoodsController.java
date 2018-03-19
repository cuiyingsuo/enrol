package com.itcast.enrol.student.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.student.service.StuGoodsHotService;
import com.itcast.enrol.student.service.StuGoodsService;

/**
 * 
 * 商品表 访问控制器类
 * 
 **/
@RestController
@RequestMapping("/goodsController")
public class StuGoodsController extends StuBaseController{

	// 统一记录日志类
	private Logger Log = LoggerFactory.getLogger("enrol");
	
	//商品服务
	@Autowired
	private StuGoodsService goodsService;
	
	//热门商品服务（热门课程）
	@Autowired
	private StuGoodsHotService goodsHotService;
	

	/**
	 * 查询课程（商品）信息
	 * @param goodsId	商品id
	 * @return
	 */
	@RequestMapping(value = "/getGoodsInfo", method = RequestMethod.GET)
	public BaseBody<T> getGoodsInfo(@RequestParam Long goodsId) {

		Map<String, Object> goodsInfoMap = goodsService.queryGoodsInfo(goodsId);
		if (null != goodsInfoMap && goodsInfoMap.size() > 0) {
			return success(goodsInfoMap);
		}
		
		return fail();
	}

	/**
	 * 查询课程（商品）列表
	 * @param subjectId	学科id
	 * @param startPage	起始页
	 * @param pageSize	每页数据条数
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/getGoodsList", method = RequestMethod.GET)
	public BaseBody getGoodsList(@RequestParam Long subjectId,@RequestParam(defaultValue = "1") int startPage,@RequestParam(defaultValue = "10") int pageSize) throws ParseException {

		BasePage<Map<String, Object>> goodsList = goodsService.queryGoodsPage(subjectId,startPage,pageSize);
		if (null != goodsList && goodsList.getPageData().size() > 0) {
			return success(goodsList);
		}
		
		return fail();
	}

	/**
	 * 立即报名toast获取商品规格（课程信息）
	 * @param goodsId	商品id
	 * @return
	 */
	@RequestMapping(value = "/getGoodsSpec", method = RequestMethod.GET)
	public BaseBody getGoodsSpec(@RequestParam Long goodsId) {

		Map<String, Object> goodsSpec = goodsService.queryGoodsSpec(goodsId);
		if (null != goodsSpec && goodsSpec.size() > 0) {
			return success(goodsSpec);
		}
		
		return fail();
	}
	/**
	 * 获取热门课程列表
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/getGoodsHot", method = RequestMethod.GET)
	public BaseBody getGoodsHotList() throws ParseException{

		List<Map<String,Object>> goodsHotList = goodsHotService.queryAll();
		
		if (null != goodsHotList && goodsHotList.size() > 0) {
			return success(goodsHotList);
		}
		
		return fail();
	}
}
