package com.itcast.enrol.management.controller;

import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.ManageBaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.utils.BusLogUtil;
import com.itcast.enrol.common.utils.XlsUtil;
import com.itcast.enrol.management.service.ManagerClassStudentService;
import com.itcast.enrol.management.vo.StudentQueryEx;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/11/18.
 */
@Validated
@RestController
@RequestMapping("/managenment/contract")
public class ManagerContractController {

    private final BusLogUtil busLogUtil = new BusLogUtil(ManagerContractController.class);


    @Autowired
    private ManagerClassStudentService classStudentService;


    /**
     * 查询可用状态合同列表
     *
     * @param studentQueryEx
     * @return
     */
    @GetMapping("/queryAbleContract")
    public ManageBaseBody queryAbleContractByStatus(StudentQueryEx studentQueryEx) {

        ManageBaseBody basePageBaseBody = new ManageBaseBody();
        if (!"2".equals(String.valueOf(studentQueryEx.getStatus()))) {
            basePageBaseBody.setSuccess(ReturnStatus.FAILD);
            basePageBaseBody.setCode(111);
            basePageBaseBody.setMsg("只能查询签订状态的合同");
            return basePageBaseBody;
        }
        BasePage basePage = classStudentService.queryContractByStatus(studentQueryEx);
        basePageBaseBody.setCount(basePage.getCount());
        basePageBaseBody.setData(basePage.getPageData());
        basePageBaseBody.setCode(0);

        return basePageBaseBody;
    }


    /**
     * 查询废弃状态合同列表
     *
     * @param studentQueryEx
     * @return
     */
    @GetMapping("/queryUnableContract")
    public ManageBaseBody queryUnableContractByStatus(StudentQueryEx studentQueryEx) {

        ManageBaseBody basePageBaseBody = new ManageBaseBody();
        if (!"4".equals(String.valueOf(studentQueryEx.getStatus()))) {
            basePageBaseBody.setSuccess(ReturnStatus.FAILD);
            basePageBaseBody.setCode(111);
            basePageBaseBody.setMsg("只能查询废弃状态的合同");
            return basePageBaseBody;
        }
        BasePage basePage = classStudentService.queryContractByStatus(studentQueryEx);
        basePageBaseBody.setCount(basePage.getCount());
        basePageBaseBody.setData(basePage.getPageData());
        basePageBaseBody.setCode(0);

        return basePageBaseBody;
    }


    /**
     * 导出合同
     *
     * @param studentQueryEx
     */
    @GetMapping("/importContract")
    public void importContract(StudentQueryEx studentQueryEx, HttpServletResponse response) throws UnsupportedEncodingException {

        List<Map<String, Object>> resultList = classStudentService.importContract(studentQueryEx);
        if (CollectionUtils.isEmpty(resultList)) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        resultList.stream().forEach(mapChild -> {
            mapChild.put("orderPrice", Integer.parseInt(String.valueOf(mapChild.get("orderPrice"))) / 100);
            Date date = new Date();
            date.setTime(Long.parseLong(String.valueOf(mapChild.get("contractSignTime"))));
            mapChild.put("contractSignTime", simpleDateFormat.format(date));
        });
        //生成待下载文件
        XSSFWorkbook xls = XlsUtil.buildExcelDocument(resultList, "协议编号", "学科", "班级类型", "学员姓名", "身份证号", "班级", "学费", "授课模式", "开班时间", "毕业时间", "签约时间");
        //设置下载时客户端Excel的名称
        String filename = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(("contract(" + filename + ").xls").getBytes(), "utf-8"));

        try (OutputStream ouputStream = response.getOutputStream()) {
            xls.write(ouputStream);
            ouputStream.flush();
        } catch (IOException e) {
            busLogUtil.error("文件导出异常:{}", e);
        }

    }


}
