package com.itcast.enrol.common.dao.contract;

import java.util.List;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.Contract;

/**
 * ContractMapper数据库操作接口类
 **/

public interface ContractMapper extends BaseDao<Contract> {
	
	List<Contract> selectListByOrderMainNo(String orderMainNo);
	
	Contract selectContractLastTime(String orderMainNo);
}