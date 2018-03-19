package com.itcast.enrol.student.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.dao.marketing.MarketingClassMapper;


/**
 * 营销策略与班次关系表 服务类
 **/
@Service
public class MarketingClassService {

    @Autowired
    private MarketingClassMapper marketingClassDao;
}
