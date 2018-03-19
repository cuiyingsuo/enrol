package com.itcast.enrol.student.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.organize.ClassMapper;
import com.itcast.enrol.common.entity.EnrolClass;

/**
 * 班级表 服务类
 **/
@Service
public class StuClassService extends BaseService<EnrolClass> {

    @Autowired
    private ClassMapper classDao;
    
    @Autowired
    private MarketingService marketingService;

    /**
     * 查询班级列表（带校区、商品信息，商品详情页立即报名，规格弹框使用）
     * @param goodsId	商品id
     * @param campusId	校区id
     * @return
     * @throws ParseException 
     */
    public List<Map<String, Object>> queryClassListOfGoodsSpec(Long goodsId, Long campusId) throws ParseException {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("goodsId", goodsId);
        params.put("campusId", campusId);
        List<Map<String, Object>> classList = classDao.selectClassListOfGoodsSpec(params);

        marketingService.goodsMarket(classList);
        
        for(Map<String,Object> classMap:classList){
        	Object lastPrice = classMap.get("lastPrice");
	        if(null==lastPrice){
	        	classMap.put("lastPrice", classMap.get("price"));
	        }
        }
        return classList;
    }
}
