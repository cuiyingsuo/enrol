package com.itcast.enrol.management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.goods.GoodsClassMapper;
import com.itcast.enrol.common.entity.GoodsClass;

/**
 * 商品班级关系表 服务类
 **/
@Service
public class ManagerGoodsClassService extends BaseService<GoodsClass> {

	// 当前业务数据操作接口dao
	@Autowired
	private GoodsClassMapper goodsClassMapper;

	/**
	 * 查询商品列表
	 * 
	 * @param goodsId
	 *            商品id
	 * @return
	 */
	public List<GoodsClass> queryByGoodsId(Long goodsId) {
		GoodsClass goodsClass = new GoodsClass();
		goodsClass.setGoodsId(goodsId);
		return goodsClassMapper.select(goodsClass);
	}
}