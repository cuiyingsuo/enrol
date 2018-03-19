package com.itcast.enrol.common.dao.banner;

import java.util.List;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.Banner;

/**
 * EnrolBannerMapper数据库操作接口类
 **/

public interface BannerMapper extends BaseDao<Banner> {



    /**
     * 查询可以状态banner的数量
     * @return
     */
    int getCountForEnable();
    
    List<Banner> selectBannerList();
}