package com.itcast.enrol.common.exception;

/**
 * @author liuzs
 * Created by liuzhongshuai on 2017/10/27.
 */
public class LoginException extends BaseException {


    public LoginException(String message, Integer code) {
        super(message);
        super.setCode(code);
    }
}