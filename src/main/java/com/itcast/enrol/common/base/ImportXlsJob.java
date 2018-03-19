package com.itcast.enrol.common.base;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.itcast.enrol.management.service.ManagerOrderFlowService;
import com.itcast.enrol.management.vo.PayFlowQueryEx;

/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/11/1.
 */
public class ImportXlsJob implements Runnable {

    private PayFlowQueryEx payFlowQueryEx;

    private XSSFSheet xssfSheet;

    private String[] heads;

    private ManagerOrderFlowService orderFlowService;

    private CountDownLatch countDownLatch;

    public ImportXlsJob(ManagerOrderFlowService orderFlowService, XSSFSheet xssfSheet, PayFlowQueryEx payFlowQueryEx, CountDownLatch latch, String... heads) {
        this.payFlowQueryEx = payFlowQueryEx;
        this.orderFlowService = orderFlowService;
        this.countDownLatch = latch;
        this.xssfSheet = xssfSheet;
        this.heads=heads;
    }

    @Override
    public void run() {

        BasePage<Map> basePage = orderFlowService.queryFlowToPage(payFlowQueryEx);


        countDownLatch.countDown();
    }
}
