package com.itcast.enrol.common.dao.goods;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.GoodsHot;

/**
 * 
 * GoodsHotMapper数据库操作接口类
 * 
 **/

public interface GoodsHotMapper extends BaseDao<GoodsHot> {
	
	List<Map<String,Object>> countGoodsHot();
	
	List<Map<String,Object>> selectGHList();
	
	int insertData(GoodsHot gh);
	
}