package com.itcast.enrol.common.exception;

/**
 * Created by liuzhongshuai on 2017/11/17.
 */
public class PermissException extends BaseException {


    public PermissException(String message, Integer code) {
        super(message);
        super.setCode(code);
    }
}
