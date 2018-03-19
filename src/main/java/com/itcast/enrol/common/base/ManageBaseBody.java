package com.itcast.enrol.common.base;

import java.util.List;

/**
 * @author liuzs
 * Created by liuzhongshuai on 2017/11/4.
 */
public class ManageBaseBody<T> extends BaseBody<Object> {

    private String msg;

    private Long count;

    private List<T>  data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }


}
