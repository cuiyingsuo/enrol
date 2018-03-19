package com.itcast.enrol.common.dao.goods;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.Goods;

/**
 * 
 * EnrolGoodsMapper数据库操作接口类
 * 
 **/

public interface GoodsMapper extends BaseDao<Goods> {
	
	/**
	 * 
	 * 查询商品（课程）规格
	 */
	List<Map<String,Object>> selectGoodsSpec(Long goodsId);
	
	List<Map<String,Object>> selectGoodsListBySubjectId(Long subjectId);
	
	Map<String,Object> selectGoodsInfo(Long goodsId);
}