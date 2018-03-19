package com.itcast.enrol.student.controller;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.entity.Banner;
import com.itcast.enrol.student.service.StuBannerService;

/**
 * 
 * banner表 访问控制器类
 * 
 **/
@RestController
@RequestMapping("/bannerController")
public class StuBannerController extends StuBaseController {

	@Autowired
	private StuBannerService bannerService;

	/**
	 * 查询banner信息
	 * 
	 * @param id
	 *            banner id
	 * @return
	 */
	@RequestMapping(value = "/getBannerInfo", method = RequestMethod.GET)
	public BaseBody<T> getBannerInfo(@RequestParam Long id) {

		Banner banner = bannerService.selectByPrimaryKey(id);
		if (null != banner) {
			return success(banner);
		}

		return fail();
	}

	/**
	 * 查询banner列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getBannerList", method = RequestMethod.GET)
	public BaseBody<T> getBannerList() {

		List<Banner> bannerList = bannerService.queryBannerList();
		if (null != bannerList && bannerList.size() > 0) {
			return success(bannerList);
		}

		return fail();
	}
}
