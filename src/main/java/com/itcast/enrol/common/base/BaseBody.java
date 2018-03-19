package com.itcast.enrol.common.base;

/**
 * 请求返回实体封装
 * Created by liuzhongshuai on 2017/9/14.
 *
 * @author liuzhongshuai
 */
public class BaseBody<T> {


    /**
     * 返回成功得到了预期结果：SUCCESS
     * 返回成功未得到预期结果：FAILD
     */
    protected boolean success = true;

    /**
     * 返回的状态码
     */
	protected Integer code=200;

    /**
     * 返回的信息
     */
	protected String message;


    /**
     * 返回的数据
     */
    private T content;

    

    public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
