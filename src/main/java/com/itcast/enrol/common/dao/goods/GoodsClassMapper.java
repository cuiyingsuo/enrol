package com.itcast.enrol.common.dao.goods;


import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.GoodsClass;

/**
 * EnrolGoodsClassMapper数据库操作接口类
 **/

public interface GoodsClassMapper extends BaseDao<GoodsClass> {


    /**
     * 商品管理条件查询接口
     *
     * @param conditions
     * @return
     */
    List<Map<String, Object>> queryGoodsForClass(Map<String, String> conditions);

    /**
     * 商品详情（商品信息 ，班级信息，学科信息，校区信息）
     *
     * @param goodsId
     * @return
     */
    Map<String, String> getGoodsDetail(Long goodsId);

}