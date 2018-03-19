package com.itcast.enrol.common.dao.marketing;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.Marketing;

/**
 * @author liuzs
 */
public interface MarketingMapper extends BaseDao<Marketing> {


    /**
     * 营销管理 匹配
     * @param marketing
     * @return
     */
    List<Marketing> matchMarking(Marketing marketing);

    /**
     * 营销下拉选
     *
     * @return
     */
    List<Map<String, String>> marketingDownSelect();
}