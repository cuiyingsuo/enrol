package com.itcast.enrol.common.base;


/**
 * Created by liuzhongshuai on 2017/12/28.
 */
public class EmsClassBody {

    private Boolean success;

    private String errorMessage;

    private EmsPageClass resultObject;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public EmsPageClass getResultObject() {
        return resultObject;
    }

    public void setResultObject(EmsPageClass resultObject) {
        this.resultObject = resultObject;
    }

}
