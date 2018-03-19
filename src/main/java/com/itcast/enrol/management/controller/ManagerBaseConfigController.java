package com.itcast.enrol.management.controller;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.entity.BankAccount;
import com.itcast.enrol.management.service.ManagerBankAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuzs
 * Created by liuzhongshuai on 2018/1/19.
 */
@RestController
@RequestMapping("/managenment/config")
public class ManagerBaseConfigController{

    @Autowired
    private ManagerBankAccountService bankAccountService;


    /**
     * 后台补录订单选择收款校区
     *
     * @return {@link BaseBody}
     */
    @GetMapping("/selectAcceptCamputs")
    public BaseBody selectAcceptCamputs() {
        BaseBody baseBody = new BaseBody();

        BankAccount baConditions = new BankAccount();
        baConditions.setPayType(2);

        List<BankAccount> bankAccountList = bankAccountService.select(baConditions);
        baseBody.setContent(bankAccountList);

        return baseBody;
    }


}
