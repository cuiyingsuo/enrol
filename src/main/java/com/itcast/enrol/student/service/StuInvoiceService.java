package com.itcast.enrol.student.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.invoice.InvoiceMapper;
import com.itcast.enrol.common.entity.Invoice;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 发票表 服务类
 **/
@Service
public class StuInvoiceService extends BaseService<Invoice> {

    @Autowired
    private InvoiceMapper invoiceDao;

    /**
     * 查询发票信息
     *
     * @param orderMainNo
     * @return
     */
    public Map<String, Object> selectByOrderMainNo(String orderMainNo) {
        return invoiceDao.selectOneByOrderMainNo(orderMainNo);
    }

    public int addInvoiceInfo(Invoice invoice) {

        return invoiceDao.insertSelective(invoice);
    }

    public int updateInvoiceInfo(Invoice invoice) {
        return invoiceDao.updateByPrimaryKeySelective(invoice);
    }

    /**
     * 分页查询
     *
     * @param conditions
     * @return
     */
    public BasePage queryInvoiceToPage(Map<String, String> conditions) {
        PageHelper.startPage(Integer.parseInt(conditions.get("page")), Integer.parseInt(conditions.get("limit")));

        List resultList = invoiceDao.queryInvoice(conditions);
        PageInfo<Map> pageInfo = new PageInfo<Map>(resultList);

        BasePage basePage = new BasePage();
        basePage.setPageData(pageInfo.getList());
        basePage.setPageSize(Integer.parseInt(conditions.get("limit")));
        basePage.setCurrentPage(Integer.parseInt(conditions.get("page")));
        basePage.setTotalPage(pageInfo.getPages());
        basePage.setCount(pageInfo.getTotal());
        return basePage;
    }


    /**
     * 查询
     *
     * @param conditions
     * @return
     */
    public List<Map<String, Object>> queryInvoice(Map<String, String> conditions) {

        List<Map<String, Object>> resultList = invoiceDao.queryInvoice(conditions);

        return resultList;
    }

    /**
     * 开发票
     */
    @Transactional(rollbackFor = Exception.class)
    public void invoiceClose(String ids) {

        String[] idArry = ids.split(",");
        for (String id : idArry) {
            Invoice invoice = invoiceDao.selectByPrimaryKey(Long.parseLong(id));
            if (null == invoice) {
                throw new RuntimeException("发票信息不存在!");
            }
            if (invoice.getIsApply() == 0) {
                throw new RuntimeException("发票" + invoice.getTitle() + "是未申请状态!");
            }
            if (invoice.getIsApply() == 2) {
                throw new RuntimeException("发票" + invoice.getTitle() + "已开!");
            }
            if (invoice.getIsApply() != 1) {
                throw new RuntimeException("发票" + invoice.getTitle() + "状态不正确!");
            }
            invoice.setIsApply(2);
            invoiceDao.updateByPrimaryKey(invoice);
        }
    }
    
    /**
     * 记录发票号
     * @param invoiceId
     * @param invoiceNo
     */
    @Transactional(rollbackFor = Exception.class)
    public String recordInvoiceNo(Long invoiceId,String invoiceNo){
    	Invoice invoice = invoiceDao.selectByPrimaryKey(invoiceId);
    	
    	if (null == invoice) {
            throw new RuntimeException("发票信息不存在!");
        }
        if (invoice.getIsApply() == 0) {
            throw new RuntimeException("发票" + invoice.getTitle() + "是未申请状态!");
        }
        if (invoice.getIsApply() == 2) {
            throw new RuntimeException("发票" + invoice.getTitle() + "已开!");
        }
        if (invoice.getIsApply() != 1) {
            throw new RuntimeException("发票" + invoice.getTitle() + "状态不正确!");
        }
        invoice.setIsApply(2);
        invoice.setInvoiceNo(invoiceNo);
        invoiceDao.updateByPrimaryKey(invoice);
        
        return invoice.getOrderNo();
    }

}
