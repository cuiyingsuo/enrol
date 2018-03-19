package com.itcast.enrol.common.configure;

import com.itcast.enrol.common.ems.SynchBaseDataForEms;
import com.itcast.enrol.common.entity.Bill;
import com.itcast.enrol.management.service.ManagerBillHandlerService;
import com.itcast.enrol.management.service.ManagerGoodsHotService;
import com.itcast.enrol.management.service.ManagerOrderMainService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by liuzhongshuai on 2017/11/1.
 */
@Component
public class SchedulingConfigure {


    private Logger logger = LoggerFactory.getLogger("enrol");
    @Autowired
    private ManagerOrderMainService orderMainService;

    @Autowired
    private ManagerGoodsHotService goodsHotService;

    @Autowired
    private SynchBaseDataForEms synchBaseDataForEms;

    @Autowired
    private ManagerBillHandlerService billHandlerService;

    /**
     * 是否开启定时器
     */
    @Value("${enrol.enable-schedule}")
    private Boolean enableSchedule;

    /**
     * 订单状态扫描 任务执行时间大于 配置 时间 时候 会 顺延
     */
    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void orderScheduler() {
        if (enableSchedule.booleanValue()) {
            int num = orderMainService.updateOrderCancle();
            if (num > 0) {
                logger.info("订单监控定时器，检测到{}条订单超时未支付，取消订单。", num);
            }
        }
    }

    /**
     * 每天凌晨1点更新
     */
    @Scheduled(cron = "0 00 01 * * ?")
    public void goodsHotScheduler() {
        if (enableSchedule.booleanValue()) {
            logger.info("#####################################开始更新热门课程##########################################");
            goodsHotService.updateGoodshot();
            logger.info("#####################################更新热门课程结束##########################################");
        }
    }


    /**
     * 每天凌晨两点同步一次
     */
    @Scheduled(cron = "0 0 02 * * ?")
    public void synClass() {
        if (enableSchedule.booleanValue()) {
            logger.info("###########################################开始同步班级.######################################");
            try {
                synchBaseDataForEms.synchClassForEms();
                logger.info("###########################################同步班级完成.####################################");
            } catch (ParseException e) {
                logger.error("同步ems班级异常，{}", e);
            }
        }
    }

    /**
     * 每天12点拉取对账结果，并同步到ems
     */
    @Scheduled(cron = "0 02 18 * * ?")
    public void dowonBill() {
        if (enableSchedule.booleanValue()) {
            logger.info("##############################开始拉取对账结果##################################");
            //当前时间减去一天
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, -1);
            Date billDate = calendar.getTime();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String strDate = simpleDateFormat.format(billDate);
            List<Bill> billList = billHandlerService.getBillToLocal(strDate);
            synchBaseDataForEms.pushBillToEms(billList);
            logger.info("##############################拉取对账结果结束,共拉取:{}条流水##################################", billList.size());
        }
    }


}
