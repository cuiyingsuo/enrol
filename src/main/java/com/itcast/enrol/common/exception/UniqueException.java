package com.itcast.enrol.common.exception;

/**
 * Created by liuzhongshuai on 2017/10/26.
 */
public class UniqueException extends BaseException {

    public UniqueException(String message) {
        super(message);
        super.setCode(2001);
    }
}
