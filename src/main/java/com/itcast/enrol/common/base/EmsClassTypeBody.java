package com.itcast.enrol.common.base;

import java.util.List;

import com.itcast.enrol.common.entity.ClassType;

/**
 * Created by liuzhongshuai on 2017/12/25.
 */
public class EmsClassTypeBody {


    private Boolean success;


    private String errorMessage;

    private List<ClassType> resultObject;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ClassType> getResultObject() {
        return resultObject;
    }

    public void setResultObject(List<ClassType> resultObject) {
        this.resultObject = resultObject;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
