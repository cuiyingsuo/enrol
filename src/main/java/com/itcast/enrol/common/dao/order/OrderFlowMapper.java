package com.itcast.enrol.common.dao.order;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.OrderFlow;
import com.itcast.enrol.management.vo.PayFlowQueryEx;

/**
 * EnrolOrderFlowMapper数据库操作接口类
 **/

public interface OrderFlowMapper extends BaseDao<OrderFlow> {

    /**
     * 条件分页查询 订单支付流水信息
     * @param payFlowQueryEx
     * @return
     */
    List<Map> queryFlowToPage(PayFlowQueryEx payFlowQueryEx);


}