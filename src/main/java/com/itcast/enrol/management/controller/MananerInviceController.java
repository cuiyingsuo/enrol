package com.itcast.enrol.management.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.ManageBaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.ems.SynchBaseDataForEms;
import com.itcast.enrol.common.entity.OrderFlow;
import com.itcast.enrol.common.utils.BusLogUtil;
import com.itcast.enrol.common.utils.XlsUtil;
import com.itcast.enrol.management.service.ManagerInvoiceService;
import com.itcast.enrol.management.service.ManagerOrderFlowService;


/**
 * Created by liuzhongshuai on 2017/11/11.
 */
@RestController
@RequestMapping("/managenment/invoice")
public class MananerInviceController {


    BusLogUtil busLogUtil = new BusLogUtil(MananerInviceController.class);

    @Autowired
    private ManagerInvoiceService invoiceService;
    
    @Autowired
    private ManagerOrderFlowService orderFlowService;
    
    @Autowired
    private SynchBaseDataForEms sysnchBaseDataForEms;
    

    /**
     * 发票条件分页查询
     *
     * @param conditions
     * @return
     */
    @GetMapping("/queryInvoice")
    public ManageBaseBody queryInvoice(Map<String, String> conditions) {

        ManageBaseBody baseBody = new ManageBaseBody();

        if (!conditions.containsKey("page")) {
            conditions.put("page", "1");
        }
        if (!conditions.containsKey("limit")) {
            conditions.put("limit", "10");
        }
        BasePage basePage = invoiceService.queryInvoiceToPage(conditions);

        baseBody.setCode(0);
        baseBody.setMsg("");
        baseBody.setCount(basePage.getCount());
        baseBody.setData(basePage.getPageData());

        return baseBody;
    }


    /**
     * 查询发票信息通过订单编号
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/getInvoiceDetail")
    public BaseBody getInvoice(String orderNo) {
        BaseBody baseBody = new BaseBody();

        if (StringUtils.isEmpty(orderNo)) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("参数值为null!");
        }
        Map<String, String> invoiceQuery = new HashMap<>();
        invoiceQuery.put("orderNo", orderNo);
        baseBody.setContent(invoiceService.queryInvoice(invoiceQuery));

        return baseBody;
    }


    /**
     * 开发票 多选的情况下 id 以 ',' 拼接
     *
     * @param ids
     * @return
     */
    @PutMapping("/invoiced")
    public BaseBody invoiced(String ids) {

        BaseBody baseBody = new BaseBody();

        if (StringUtils.isEmpty(ids)) {
            baseBody.setMessage("参数不正确!");
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }
        invoiceService.invoiceClose(ids);
        baseBody.setMessage("成功!");
        return baseBody;
    }
    /**
     * 录入发票号
     * @param invoiceId
     * @param invoiceNo
     * @return
     */
    @RequestMapping(value = "/recordInvoiceNo", method = RequestMethod.POST)
    public BaseBody recordInvoiceNo(@RequestParam Long invoiceId,@RequestParam String invoiceNo){
    	 BaseBody baseBody = new BaseBody();
    	 
    	 if (StringUtils.isEmpty(invoiceNo)) {
             baseBody.setMessage("参数不正确!");
             baseBody.setSuccess(ReturnStatus.FAILD);
             return baseBody;
         }
    	 
         String subOrderNo = invoiceService.recordInvoiceNo(invoiceId,invoiceNo);
         try{
	         //同步ems
	         OrderFlow orderFlow = new OrderFlow();
	         orderFlow.setOrderNo(subOrderNo);
	         orderFlow.setStatus(2);
	         orderFlow=orderFlowService.selectOne(orderFlow);
	         Map<String, String> map = new LinkedHashMap<>();
	     	 map.put("invoice", invoiceNo);
	     	 map.put("paymentId", orderFlow.getFlowId());
	     	 sysnchBaseDataForEms.pushInvoiceToEms(map);
         }catch(Exception e){
        	 busLogUtil.error("发票信息同步ems失败", subOrderNo);
         }
     	 
         baseBody.setMessage("成功!");
         return baseBody;
    }

    /**
     * 导出发票
     *
     * @param conditions
     */
    @GetMapping("/importInvoice")
    public void importInvoice(HttpServletResponse response, Map<String, String> conditions) throws UnsupportedEncodingException {

        List<Map<String, Object>> list = invoiceService.queryInvoice(conditions);

        list.stream().forEach(mapChild -> {
            if (mapChild.containsKey("paid")) {
                Integer paid = Integer.parseInt(mapChild.get("paid").toString()) / 100;
                mapChild.put("paid", paid);
            }
            if (mapChild.containsKey("orderPrice")) {
                Integer orderPrice = Integer.parseInt(mapChild.get("orderPrice").toString()) / 100;
                mapChild.put("orderPrice", orderPrice);
            }
            if (mapChild.containsKey("price")) {
                Integer price = Integer.parseInt(mapChild.get("price").toString()) / 100;
                mapChild.put("price", price);
            }
            if (mapChild.containsKey("invoiceType")) {
                mapChild.put("invoiceType", mapChild.get("invoiceType").toString().equals("0") ? "纸质发票" : "电子发票");
            }
            if (mapChild.containsKey("titleType")) {
                mapChild.put("titleType", mapChild.get("titleType").toString().equals("0") ? "个人" : "单位");
            }
            if (mapChild.containsKey("isApply")) {
                mapChild.remove("isApply");
            }
            if (mapChild.containsKey("id")) {
                mapChild.remove("id");
            }
        });
        //生成待下载文件
        XSSFWorkbook xls = XlsUtil.buildExcelDocument(list, "学员编号ID", "校区/教学点", "学员姓名", "班级名称", "应交学费", "已交学费", "班主任姓名", "发票类型", "抬头类型", "发票抬头", "纳税人识别号", "发票内容", "开票金额", "地址、电话", "开户行及账号", "电子邮箱", "学员联系电话", "备注");
        //设置下载时客户端Excel的名称
        String filename = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(("invoice(" + filename + ").xls").getBytes(), "utf-8"));

        try (OutputStream ouputStream = response.getOutputStream()) {
            xls.write(ouputStream);
            ouputStream.flush();
        } catch (IOException e) {
            busLogUtil.error("文件导出异常:{}", e);
        }


    }
}
