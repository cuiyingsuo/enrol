package com.itcast.enrol.student.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.entity.Student;
import com.itcast.enrol.common.utils.MD5Util;
import com.itcast.enrol.common.utils.RedisUtil;
import com.itcast.enrol.common.utils.TokenUtil;
import com.itcast.enrol.student.service.StuStudentService;
import com.itcast.enrol.student.vo.StuInfoOfRedis;

/**
 * 学员表 访问控制器类
 **/
@RestController
@RequestMapping("/loginController")
public class StuLoginController extends StuBaseController{

    private Logger logger = LoggerFactory.getLogger("enrol");
    @Autowired
    private StuStudentService studentService;

    @Value("${server.token-key-mobile}")
    private String loginToken;
    
    @Value("${enrol.user.login.key}")
    private String passwordKey;

    /**
     * 登录
     *
     * @param mobile   学生手机号码
     * @param password 密码
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseBody<T> login(@RequestParam String mobile,
                          @RequestParam String password, HttpServletRequest request,
                          HttpServletResponse response) throws Exception {

        String token = request.getHeader(loginToken);
        if (null != token && RedisUtil.hasKey(token)) {
            logger.info("用户  {} 已登录.", mobile);
            return success(token);
        }
        
        String md5Pass = MD5Util.encryption(mobile+password+passwordKey);
        // token值不存在或登录信息超时
        Student student = studentService.checkPassword(mobile);
        if (null == student) {
            logger.error("{}用户或密码错误.", mobile);
            return fail(ReturnStatus.LOGIN_ERROR,"账户或密码错误");
        } else {
        	if (!student.getPassword().equals(md5Pass)) {
                logger.error("用户或密码错误.", mobile);
                return fail(ReturnStatus.LOGIN_ERROR,"账户或密码错误");
    		}
        	//未激活用户登录修改为激活状态
        	if(student.getIsActived()==0){
        		student.setIsActived(1);
        		studentService.updateByStudent(student);
        	}
            
        	token = TokenUtil.getToken(mobile);

        	StuInfoOfRedis loginInfo = new StuInfoOfRedis(student.getId(),mobile,student.getName());

            RedisUtil.set(token, loginInfo, 60 * 60);
            
            logger.debug("用户  {} 登录成功.",mobile);
            return success(token);
        }
    }

    /**
     * 检查登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/checkLogin", method = RequestMethod.GET)
    public BaseBody<T> checkLogin(HttpServletRequest request,
                                       HttpServletResponse response) {

    	String token = request.getHeader(loginToken);
        if (null != token && RedisUtil.hasKey(token)) {
            return success("用户已登录");
        }
        
        return fail(ReturnStatus.LOGIN_ERROR,"用户未登录");
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/offLogin", method = RequestMethod.POST)
    public BaseBody<T> offLogin(HttpServletRequest request,
                                     HttpServletResponse response) {

        String token = request.getHeader(loginToken);

        if (null != token && RedisUtil.hasKey(token)) {
            RedisUtil.del(token);
            return success("退出登录成功");
        }
        
        return fail(ReturnStatus.LOGIN_ERROR,"用户未登录");
    }
}
