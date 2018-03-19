package com.itcast.enrol.common.dao.marketing;


import java.util.List;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.MarketingDeduction;

public interface MarketingDeductionMapper extends BaseDao<MarketingDeduction> {

    /**
     * 查询优惠策略 通过 维度产品
     *
     * @param targetId
     * @return
     */
    List<MarketingDeduction> deductionForTarget(Long targetId);
}