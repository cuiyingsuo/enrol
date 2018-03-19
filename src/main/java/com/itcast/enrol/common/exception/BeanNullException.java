package com.itcast.enrol.common.exception;

/**
 * 查询bean时候 为null
 * @author liuzs
 * Created by liuzhongshuai on 2017/10/22.
 */
public class BeanNullException extends BaseException {


    public BeanNullException(String message) {
        super(message);
        super.setCode(1005);
    }
}
