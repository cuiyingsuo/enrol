package com.itcast.enrol.common.dao.invoice;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.Invoice;


/**
 * EnrolInvoiceMapper数据库操作接口类
 **/

public interface InvoiceMapper extends BaseDao<Invoice> {

    Map<String, Object> selectOneByOrderMainNo(String orderMainNo);

    /**
     * 条件查询开具发票
     *
     * @param conditions
     * @return
     */
    List<Map<String,Object>> queryInvoice(Map<String, String> conditions);
}