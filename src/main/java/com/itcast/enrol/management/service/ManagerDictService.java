package com.itcast.enrol.management.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.dict.DictMapper;
import com.itcast.enrol.common.entity.Dict;

/**
 * 字典表 服务类
 **/
@Service
public class ManagerDictService extends BaseService<Dict> {


    @Autowired
    private DictMapper dictDao;

    /**
     * 通过字典类型，获取下级分类明细
     *
     * @param typeCode
     * @return
     */
    public Map<String, List<Map<String, String>>> getDetailType(String typeCode) {

        List<Map<String, String>> mapList = dictDao.getDetailType(typeCode);

        Map<String, List<Map<String, String>>> stringListMap = new HashMap<String, List<Map<String, String>>>(16);
        stringListMap.put(typeCode, mapList);

        return stringListMap;
    }
}
