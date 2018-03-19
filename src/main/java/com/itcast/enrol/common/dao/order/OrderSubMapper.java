package com.itcast.enrol.common.dao.order;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.OrderSub;

/**
 * 
 * EnrolOrderSubMapper数据库操作接口类
 * 
 **/
public interface OrderSubMapper extends BaseDao<OrderSub>{



	/**
	 * 根据主订单号查询分订单
	 */
	List<Map<String,Object>> selectByMergeOrderNo(String orderNo);
	
	/**
	 * 查询凭证信息
	 * @param orderSubNo
	 * @return
	 */
	List<Map<String,Object>> selectReceiptInfo(String orderSubNo);

}