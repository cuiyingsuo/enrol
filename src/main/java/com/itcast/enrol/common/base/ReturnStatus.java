package com.itcast.enrol.common.base;

/**
 * @Author liuzhongshuai
 * Created by liuzhongshuai on 2017/10/20.
 */
public class ReturnStatus {
    /**
     * 请求成功
     */
    public static boolean SUCCESS=true;

    /**
     * 请求失败
     */
    public static boolean FAILD=false;
    
    public static int ERROR = 500;//服务器内部错误
    
    public static int PARAM_ERROR=1001;//参数错误
    
    public static int LOGIN_ERROR=1002;//未登录或登录超时
    
    public static int DATA_NULL=1003;//数据为空
    
    public static int STATUS_ERROR=1004;//数据状态不符合业务操作
    
    public static int LOGIN_NOT = 1005;//未登录
    
    public static int LOGIN_OVER = 1005;//登录超时
}
