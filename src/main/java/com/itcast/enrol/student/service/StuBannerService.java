package com.itcast.enrol.student.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.banner.BannerMapper;
import com.itcast.enrol.common.entity.Banner;


/**
 * banner表 服务类
 **/
@Service
public class StuBannerService extends BaseService<Banner> {

    @Autowired
    private BannerMapper bannerDao;
    
    public List<Banner> queryBannerList() {
        return bannerDao.selectBannerList();
    }
}
