package com.itcast.enrol.common.exception;

/**
 * @author liuzs
 * Created by liuzhongshuai on 2017/10/22.
 */
public class FileValueNoMatchException extends BaseException {


    public FileValueNoMatchException(String message, Integer code) {
        super(message);
        super.setCode(code);
    }
}
