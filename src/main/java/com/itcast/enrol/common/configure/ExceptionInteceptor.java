package com.itcast.enrol.common.configure;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.exception.BaseException;
import com.itcast.enrol.common.exception.UnLoginException;
import com.itcast.enrol.common.utils.BusLogUtil;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * @Author liuzhongshuai
 * Created by liuzhongshuai on 2017/10/20.
 */
@ControllerAdvice
public class ExceptionInteceptor {

    private final static BusLogUtil LOGGER = new BusLogUtil(ExceptionInteceptor.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseBody<Object> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        BaseBody<Object> baseBody = new BaseBody<>();
        baseBody.setCode(500);
        baseBody.setSuccess(ReturnStatus.FAILD);
        if (e instanceof BaseException) {
            BaseException baseException = (BaseException) e;
            baseBody.setCode(baseException.getCode());
        }

        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) e;

            String strMess = "";
            while (constraintViolationException.getConstraintViolations().iterator().hasNext()) {
                strMess = constraintViolationException.getConstraintViolations().iterator().next().getMessage();
                break;
            }
            baseBody.setMessage(strMess);
            return baseBody;
        }

        baseBody.setMessage(e.getMessage());
        if (!(e instanceof UnLoginException)) {
            LOGGER.error("enrol系统异常:", e);
        }
        return baseBody;
    }
}