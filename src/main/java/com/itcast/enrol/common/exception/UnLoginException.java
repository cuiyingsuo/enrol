package com.itcast.enrol.common.exception;

/**
 * Created by liuzhongshuai on 2017/10/20.
 */
public class UnLoginException extends BaseException {


    public UnLoginException(String message, Integer code) {
        super(message);
        super.setCode(code);
    }
}