package com.itcast.enrol.common.dao.organize;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.Campus;

/**
 * 
 * EnrolCampusMapper数据库操作接口类
 * 
 **/

public interface CampusMapper extends BaseDao<Campus>{


	/**
	 * 查询校区
	 * @param goodsId
	 * @return
	 */
	List<Map<String,Object>> selectCampusListBygoodsId(Long goodsId);

	/**
	 * 通过 状态查询 ，只返回 id，name列
	 * @param status
	 * @return
	 */
	List<Map<String,Object>> queryCampusListByStatus(Byte status);
}