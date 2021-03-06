package com.itcast.enrol.student.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.enrol.common.dao.goods.GoodsHotMapper;
import com.itcast.enrol.common.dao.goods.GoodsMapper;
import com.itcast.enrol.common.entity.GoodsHot;

/**
 * 商品班级关系表 服务类
 **/
@Service
public class StuGoodsHotService {
	private Logger logger = LoggerFactory.getLogger("enrol");
	
	// 热门课程dao
	@Autowired
	private GoodsHotMapper goodsHotDao;
	
	@Autowired
	private GoodsMapper goodsDao;
	
	@Autowired
    private MarketingService marketingService;

	/**
	 * 查询全部热门课程
	 * 
	 * @return
	 * @throws ParseException 
	 */
	public List<Map<String,Object>> queryAll() throws ParseException {
		List<Map<String,Object>> goodsHotList = goodsHotDao.selectGHList();
		marketingService.goodsMarket(goodsHotList);
		return goodsHotList;
	}
	@Transactional
	public void updateGoodshot(){
		List<Map<String,Object>> ghListMap = goodsHotDao.countGoodsHot();
		
		List<Map<String,Object>> ghList = goodsHotDao.selectGHList();
		for(Map gh:ghList){
			goodsHotDao.deleteByPrimaryKey(gh.get("id"));
		}
		
		for(Map<String,Object> ghMap:ghListMap){
			GoodsHot gh = new GoodsHot();
			gh.setGoodsId(Long.valueOf(String.valueOf(ghMap.get("goodsId"))));
			goodsHotDao.insertData(gh);
		}
		logger.info("热门课程已更新.");
	}
}