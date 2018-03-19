package com.itcast.enrol.common.exception;

/**
 * Created by liuzhongshuai on 2017/10/26.
 */
public class FileNullException extends BaseException {


    public FileNullException(String message) {
        super(message);
        super.setCode(1009);
    }
}
