package com.itcast.enrol.common.dao.organize;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.EnrolClass;
import com.itcast.enrol.management.vo.ClassEx;

/**
 * EnrolClassMapper数据库操作接口类
 **/

public interface ClassMapper extends BaseDao<EnrolClass> {

    /**
     * 根据班级id查询班级信息（包含校区信息）
     */
    Map<String, Object> selectClassInfo(Map<String,Object> params);

    /**
     * 查询商品下，某校区班级列表
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectClassListOfGoodsSpec(Map<String, Long> params);

    /**
     * 条件查询班级 选项 返回字段 （id，name）
     *
     * @param enrolClass
     * @return
     */
    List<Map> queryClassByMulitField(ClassEx enrolClass);
    
    /**
     * 条件查询班级列表返回班级所有相关信息
     * @param enrolClass
     * @return
     */
    List<Map<String,String>> selectClassListToPage(EnrolClass enrolClass);
    
}