package com.itcast.enrol.common.exception;

/**
 * @author liuzs
 * Created by liuzhongshuai on 2017/12/7.
 */
public class BusinessException extends BaseException {


    public BusinessException(String message) {
        super(message);
        super.setCode(6000);
    }
}
