package com.itcast.enrol.common.dao.order;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.management.vo.OrderQueryEx;

/**
 * 
 * EnrolOrderMainMapper数据库操作接口类
 * 
 **/

public interface OrderMainMapper extends BaseDao<OrderMain>{


	/**
	 * 查询订单详情
	 * @param orderMainNo
	 * @param mobile
	 * @return
	 */
	Map<String,Object> selectOrderMainInfo(Map<String,Object> parmas);
	/**
	 * 
	 * 查询主订单列表
	 * 
	 */
	List<Map<String,Object>> selectOrderMainList(String mobile);

	/**
	 * 订单管理 分页 查询
	 * @param orderQueryEx
	 * @return
	 */
	List<Map> queryOrderMainsToPage(OrderQueryEx orderQueryEx);
	/**
	 * 查询所有超时订单
	 * @return
	 */
	List<Map<String,Object>> selectOrderCancle(Long currentTime);
	/**
	 * 需要生成合同的订单
	 * @return
	 */
	List<Map<String,Object>> selectNeedCreatAgreement();
	
	OrderMain selectOrderMainByNo(String mergeOrderNo);

}