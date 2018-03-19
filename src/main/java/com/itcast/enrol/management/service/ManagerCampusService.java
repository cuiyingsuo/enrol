package com.itcast.enrol.management.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.organize.CampusMapper;
import com.itcast.enrol.common.entity.Campus;

/**
 * 分校表 服务类
 **/
@Service
public class ManagerCampusService extends BaseService<Campus> {


    @Autowired
    private CampusMapper campusDao;

    /**
     * 查询校区列表
     * @param goodsId	商品id
     * @return
     */
    public List<Map<String, Object>> queryCampusListByGoodsId(Long goodsId) {
        return campusDao.selectCampusListBygoodsId(goodsId);
    }

    /**
     * 通过 状态查询 ，只返回 id，name列
     * @param status
     * @return
     */
    public List<Map<String, Object>> queryCampusListByStatus(Byte status) {
        return campusDao.queryCampusListByStatus(status);
    }

}
