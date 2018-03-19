package com.itcast.enrol.management.controller;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.ManageBaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.utils.BusLogUtil;
import com.itcast.enrol.common.utils.XlsUtil;
import com.itcast.enrol.management.service.ManagerOrderFlowService;
import com.itcast.enrol.management.vo.PayFlowQueryEx;
import com.mysql.jdbc.log.LogUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author liuzs
 * 支付流水处理器
 * Created by liuzhongshuai on 2017/10/31.
 */
@RestController
@RequestMapping("/managenment/payFlow")
public class ManagerPayFlowController {

    private static BusLogUtil logUtils = new BusLogUtil(ManagerPayFlowController.class);

    @Autowired
    private ManagerOrderFlowService orderFlowService;


    /**
     * 条件及分页查询支付流水
     *
     * @param payFlowQueryEx
     * @return
     */
    @GetMapping("/queryPayFlow")
    public ManageBaseBody<BasePage> queryPayFlow(@Valid PayFlowQueryEx payFlowQueryEx, BindingResult bindingResult) {

        ManageBaseBody<BasePage> baseBody = new ManageBaseBody<BasePage>();
        if (bindingResult.hasErrors()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(10);
            baseBody.setMsg(bindingResult.getFieldError().getDefaultMessage());
            return baseBody;
        }
        BasePage basePage = orderFlowService.queryFlowToPage(payFlowQueryEx);
        baseBody.setData(basePage.getPageData());
        baseBody.setCount(basePage.getCount());
        baseBody.setCode(0);

        return baseBody;
    }

    /**
     * 导出查询结果
     *
     * @param request
     * @param response
     */
    @GetMapping("/importResult")
    public void importResult(@Valid HttpServletRequest request, HttpServletResponse response, PayFlowQueryEx payFlowQueryEx, BindingResult bindingResult) throws UnsupportedEncodingException {

        if (bindingResult.hasErrors()) {
            return;
        }
        List<Map<String, Object>> mapList = orderFlowService.queryFlows(payFlowQueryEx);
        //转换
        for (Map<String, Object> map : mapList) {
            if (map.containsKey("status")) {
                if ("0".equals(String.valueOf(map.get("status"))))
                    map.put("status", "失败");
                else
                    map.put("status", "成功");
            }
            if (map.containsKey("orderPrice")) {
                String orderPrice = String.valueOf(map.get("orderPrice"));
                BigDecimal bigDecimal = new BigDecimal(orderPrice);
                BigDecimal csDecimal = new BigDecimal("100");
                double value = bigDecimal.divide(csDecimal, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                map.put("orderPrice", String.valueOf(value));
            }
            if (map.containsKey("amount")) {
                String orderPrice = String.valueOf(map.get("amount"));
                BigDecimal bigDecimal = new BigDecimal(orderPrice);
                BigDecimal csDecimal = new BigDecimal("100");
                double value = bigDecimal.divide(csDecimal, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                map.put("amount", String.valueOf(value));
            }
            if (map.containsKey("payType")) {
                String value = String.valueOf(map.get("payType"));
                if ("0".equals(value)) {
                    map.put("payType", "全款支付");
                } else if ("1".equals(value)) {
                    map.put("payType", "分次支付");
                } else if ("2".equals(value)) {
                    map.put("payType", "全额贷款");
                }
            }
        }
        //生成待下载文件
        XSSFWorkbook xls = XlsUtil.buildExcelDocument(mapList, "支付流水", "支付状态", "支付金额(元)", "支付渠道", "支付时间", "订单内容", "订单号", "订单价格(元)", "支付方式", "学员姓名", "联系方式");
        //设置下载时客户端Excel的名称
        String filename = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(("pay flow(" + filename + ").xls").getBytes(), "utf-8"));

        try (OutputStream ouputStream = response.getOutputStream()) {
            xls.write(ouputStream);
            ouputStream.flush();
        } catch (IOException e) {
            logUtils.error("文件导出异常:{}", e);
        }
    }

}
