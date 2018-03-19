package com.itcast.enrol.common.dao.dict;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.Dict;

/**
 * EnrolDictMapper数据库操作接口类
 **/

public interface DictMapper extends BaseDao<Dict> {

    /**
     * 根据类型查询字典集合
     *
     * @param type
     * @return
     */
    List<Map<String, String>> selectInfoByType(String type);


    /**
     * 通过分类查明细
     * @param typeCode
     * @return
     */
    List<Map<String, String>> getDetailType(String typeCode);

}