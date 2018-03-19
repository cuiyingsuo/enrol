package com.itcast.enrol.student.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.order.OrderSubMapper;
import com.itcast.enrol.common.entity.Bill;
import com.itcast.enrol.common.entity.OrderSub;

/**
 * 订单信息表（子表）； 服务类
 **/
@Service
public class StuOrderSubService extends BaseService<OrderSub> {


    @Autowired
    private StuPayService payService;

    @Autowired
    private OrderSubMapper orderSubDao;

    @Autowired
    private BillHandlerService billService;

    /**
     * 查询分订单信息
     *
     * @param id 分订单id
     * @return
     */
    public OrderSub queryByPrimaryKey(Long id) {
        return orderSubDao.selectByPrimaryKey(id);
    }

    public Map<String, Object> queryReceiptInfo(String orderSubNo) {
        List<Map<String, Object>> receiptInfos = orderSubDao.selectReceiptInfo(orderSubNo);
        if (CollectionUtils.isEmpty(receiptInfos)) {
            return null;
        }
        Map<String, Object> receiptInfo = receiptInfos.get(0);
        Object receiptNoObj = receiptInfo.get("receiptNo");
        int isAft = Integer.parseInt(String.valueOf(receiptInfo.get("isAft")));
        if(isAft==0){
	        Bill bill = new Bill();
	        bill.setSubOrderNo(orderSubNo);
	        receiptInfo.put("billStatus", false);
	        
	        
	        List<Bill> billList = billService.select(bill);
	        
	        if (billList.size() > 0) {
	        	receiptInfo.put("billStatus", true);
	            if (null == receiptNoObj) {
	                String receiptNo = payService.getReceiptNo();
	                OrderSub orderSub = new OrderSub();
	                orderSub.setReceiptNo(receiptNo);
	                orderSubDao.updateByPrimaryKeySelective(orderSub);
	                receiptInfo.put("receiptNo", receiptNo);
	            }
	        }
        }else{
        	receiptInfo.put("billStatus", true);
        }
        return receiptInfo;
    }

}
