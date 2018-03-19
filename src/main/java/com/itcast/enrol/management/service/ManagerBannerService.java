package com.itcast.enrol.management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.banner.BannerMapper;
import com.itcast.enrol.common.entity.Banner;
import com.itcast.enrol.common.exception.AmountPromiseException;
import com.itcast.enrol.common.exception.FileValueNoMatchException;
import com.itcast.enrol.common.utils.BeanUtils;


/**
 * banner表 服务类
 **/
@Service
public class ManagerBannerService extends BaseService<Banner> {

    @Autowired
    private BannerMapper bannerDao;
    
    public List<Banner> queryBannerList() {
        return bannerDao.selectBannerList();
    }

    /**
     * 新增banner
     *
     * @param banner
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Banner mergeBanner(Banner banner) {

        int maxBannerCount = 5;
        //查询现有可用banner数量，最多5个
        Integer banCount = bannerDao.getCountForEnable();

        Long currTime = System.currentTimeMillis();

        if (banner.getId() != null && banner.getId() > 0) {
            if (null == banner.getStatus()) {
                throw new FileValueNoMatchException("字段status只能为 0、1", 1006);
            }
            if (null != banCount && banCount.intValue() >= maxBannerCount && banner.getStatus() == 1) {
                throw new AmountPromiseException("banner 最多为5个");
            }
            banner.setEditTime(currTime);
            Banner bannerNow = bannerDao.selectByPrimaryKey(banner.getId());
            if (null == bannerNow) {
                throw new RuntimeException("banner 为null");
            }
            BeanUtils.copyProperties(banner, bannerNow);
            bannerDao.updateByPrimaryKeySelective(bannerNow);

        } else {
            if (null != banCount && banCount.intValue() >= maxBannerCount) {
                throw new AmountPromiseException("banner 最多为5个");
            }
            banner.setCreateTime(currTime);
            bannerDao.insert(banner);
        }
        return banner;
    }

    /**
     * 发布/取消发布 banner
     *
     * @param id
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    public int upOrDownBanner(int id, int status) {

        Banner banner = new Banner();
        banner.setId((long) id);
        banner.setStatus((byte) status);

        int effRow = bannerDao.updateByPrimaryKeySelective(banner);
        return effRow;
    }


    /**
     * 支持条件查询 banner集合
     *
     * @param banner
     * @return
     */
    public BasePage<Banner> queryBannersToPage(Banner banner, int startNum, int pageSize) {

        BasePage<Banner> bannerBasePage = this.queryListByPage(banner, startNum, pageSize, "create_time desc");

        return bannerBasePage;
    }

}