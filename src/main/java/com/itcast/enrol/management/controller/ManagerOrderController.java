package com.itcast.enrol.management.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.annotation.Session;
import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.ManageBaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.entity.Invoice;
import com.itcast.enrol.common.entity.OrderFlow;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.utils.PriceUtil;
import com.itcast.enrol.management.service.ManagerInvoiceService;
import com.itcast.enrol.management.service.ManagerOrderFlowService;
import com.itcast.enrol.management.service.ManagerOrderMainService;
import com.itcast.enrol.management.service.ManagerOrderSubService;
import com.itcast.enrol.management.vo.OrderQueryEx;
import com.itcast.enrol.management.vo.UserEx;
import com.itcast.enrol.student.controller.StuBaseController;

/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/10/26.
 */
@RestController
@RequestMapping("/managenment/orderMain")
public class ManagerOrderController extends StuBaseController {

    @Autowired
    private ManagerOrderMainService orderMainService;

    @Autowired
    private ManagerOrderFlowService orderFlowService;

    @Autowired
    private ManagerInvoiceService invoiceService;

    @Autowired
    private ManagerOrderSubService orderSubService;


    /**
     * 收据申请
     *
     * @param orderNo
     * @return
     */
    @PutMapping("/requestReceipt")
    public BaseBody requestReceipt(String orderNo) {

        BaseBody baseBody = new BaseBody();

        OrderMain orderMainConditions = new OrderMain();
        orderMainConditions.setMergeOrderNo(orderNo);
        OrderMain orderMain = orderMainService.selectOne(orderMainConditions);
        if (null == orderMain) {
            baseBody.setCode(1005);
            baseBody.setMessage("查无此订单!");
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }

        if (orderMain.getElecReceipt().intValue() == 1) {
            baseBody.setMessage("申请成功!");
            return baseBody;
        }
        orderMain.setElecReceipt(1);
        int effRo = orderMainService.updateByPk(orderMain);
        if (effRo > 0) {
            baseBody.setMessage("申请成功!");
            return baseBody;
        }
        baseBody.setMessage("申请失败!");
        baseBody.setSuccess(ReturnStatus.FAILD);
        return baseBody;
    }


    /**
     * 订单管理 分页查询列表
     *
     * @param queryEx
     * @return
     */
    @GetMapping("/queryOrderMains")
    public ManageBaseBody<BasePage<List<Map>>> queryOrderMains(@Valid OrderQueryEx queryEx, BindingResult bindingResult) {
        ManageBaseBody<BasePage<List<Map>>> baseBody = new ManageBaseBody<>();
        if (bindingResult.hasErrors()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return baseBody;
        }
        BasePage basePage = orderMainService.queryOrderMainsToPage(queryEx);
        baseBody.setCount(basePage.getCount());
        baseBody.setCode(0);
        baseBody.setData(basePage.getPageData());
        return baseBody;
    }

    /**
     * 订单详情
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/orderMainDetail")
    public BaseBody<Map> orderMainDetail(@RequestParam(defaultValue = "0") String orderNo) {

        BaseBody<Map> baseBody = new BaseBody<Map>();
        //查询订单通过订单编号
        OrderMain orderMainConditions = new OrderMain();
        orderMainConditions.setMergeOrderNo(orderNo);
        OrderMain orderMain = orderMainService.selectOne(orderMainConditions);
        if (null == orderMain) {
            baseBody.setCode(1005);
            baseBody.setMessage("查无此订单!");
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }
        Map resultMap = new HashMap(4);
        //订单信息
        resultMap.put("orderInfo", orderMain);
        //支付流水
        OrderFlow orderFlow = new OrderFlow();
        orderFlow.setMargeOrderNo(orderNo);
        resultMap.put("orderFlow", orderFlowService.select(orderFlow));
        //发票信息
        Invoice invoice = new Invoice();
        invoice.setOrderNo(orderNo);
        resultMap.put("invoiceInfo", invoiceService.select(invoice));
        baseBody.setContent(resultMap);
        return baseBody;
    }


    /**
     * 补录订单追加补录流水
     *
     * @param orderNo 主订单编号
     * @return
     */
    @PostMapping("/appendAftOrder")
    public BaseBody appendAftOrder(String orderNo, Integer payPrice, @Session UserEx userEx) {

        BaseBody<Map> baseBody = new BaseBody<>();

        if (StringUtils.isEmpty(orderNo) || null == payPrice || payPrice.intValue() <= 0) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("参数值不正确!");
            return baseBody;
        }
        orderFlowService.appendAftOrder(orderNo, payPrice, userEx);
        baseBody.setMessage("添加流水成功!");
        return baseBody;
    }

    /**
     * 获取凭证信息
     *
     * @return
     */
    @RequestMapping(value = "/getReceipt", method = RequestMethod.GET)
    public BaseBody<T> getReceipt(@RequestParam String orderSubNo) {

        BaseBody baseBody = new BaseBody();

        Map<String, Object> receiptInfo = orderSubService
                .queryReceiptInfo(orderSubNo);
        if (null == receiptInfo || receiptInfo.size() == 0) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("找不到订单号相关订单信息");
            return baseBody;
        } else {
            if (0 == Integer.parseInt(String.valueOf(receiptInfo
                    .get("receiptStatus")))) {
                baseBody.setSuccess(ReturnStatus.FAILD);
                baseBody.setMessage("没有申请电子收据");
                return baseBody;
            }
            if (!(boolean) receiptInfo.get("billStatus")) {
                baseBody.setSuccess(ReturnStatus.FAILD);
                baseBody.setMessage("没有进行支付对账");
                return baseBody;
            }
            // 金额转为元，增加汉字金额
            int price = Integer.valueOf(receiptInfo.get("orderPrice").toString());
            BigDecimal bigDecimal = new BigDecimal(price);
            float priceStr = bigDecimal.movePointLeft(2).floatValue();
            String priceCN = PriceUtil.priceToCN(new BigDecimal(priceStr));

            receiptInfo.put("orderPrice", priceStr);
            receiptInfo.put("priceCN", priceCN);
            return success(receiptInfo);
        }
    }

}
