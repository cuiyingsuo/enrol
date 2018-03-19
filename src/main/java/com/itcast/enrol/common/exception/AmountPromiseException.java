package com.itcast.enrol.common.exception;

/**
 * @author  liuzs
 * Created by liuzhongshuai on 2017/10/21.
 * 数量约束异常
 */
public class AmountPromiseException extends BaseException {

    public AmountPromiseException(String message) {
        super(message);
        super.setCode(1003);
    }
}