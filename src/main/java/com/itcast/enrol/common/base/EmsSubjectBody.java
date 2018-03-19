package com.itcast.enrol.common.base;

import java.util.List;

import com.itcast.enrol.management.vo.EmsSubject;

/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/12/22.
 */
public class EmsSubjectBody {

    private Boolean success;


    private String errorMessage;

    private List<EmsSubject> resultObject;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<EmsSubject> getResultObject() {
        return resultObject;
    }

    public void setResultObject(List<EmsSubject> resultObject) {
        this.resultObject = resultObject;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
