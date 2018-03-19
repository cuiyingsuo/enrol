package com.itcast.enrol.student.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.itcast.enrol.common.base.DictType;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.entity.Student;
import com.itcast.enrol.common.utils.MD5Util;
import com.itcast.enrol.common.utils.RedisUtil;
import com.itcast.enrol.management.vo.StudentVo;
import com.itcast.enrol.student.service.StuDictService;
import com.itcast.enrol.student.service.StuStudentService;
import com.itcast.enrol.student.service.plugins.StuPersonAuthService;
import com.itcast.enrol.student.vo.StuInfoOfRedis;

/**
 * 
 * 学员表 访问控制器类
 * 
 **/
@RestController
@RequestMapping("/myInfoController")
public class StuStudentController extends StuBaseController{

	// 统一记录日志类
	private Logger logger = LoggerFactory.getLogger("enrol");
	@Autowired
	// 当前业务操作接口bo
	private StuStudentService studentService;
	
	@Autowired
	private StuDictService dictService;
	
	@Autowired
	private StuPersonAuthService personAuthService;
	
	
	@Value("${server.token-key-mobile}")
    private String loginToken;
	
	@Value("${enrol.user.login.key}")
    private String passwordKey;

	/**
	 * 查询学生信息
	 * @param mobile	学生手机号码
	 * @return
	 */
	@RequestMapping(value = "/getStudentInfo", method = RequestMethod.GET)
	public BaseBody<T> getStudentInfo(@RequestParam String mobile) {

		Map<String,Object> studentInfo = studentService.queryStudentByMobile(mobile);
		if (null != studentInfo) {
			return success(studentInfo);
		} 
		
		return fail();
	}

	/**
	 * 编辑学生信息页面
	 * @param mobile	学生手机号码
	 * @return
	 */
	@RequestMapping(value = "/getStudentEditInfo", method = RequestMethod.GET)
	public BaseBody<T> editStudentPage(@RequestParam String mobile) {

		Map<String,Object> studentInfo = studentService.queryStudentByMobile(mobile);
		Map<String, List<Map<String, String>>> channelList = dictService.getDetailType(DictType.CHANNEL);
		
		Map<String,Object> relMap = new HashMap<String,Object>();
		relMap.put("studentInfo", studentInfo);
		relMap.put("channelList", channelList);
		if (null != channelList&&channelList.size()>0) {
			return success(relMap);
		}

		return fail();
	}

	/**
	 * 更新学生信息
	 * @param sv	学生信息封装类
	 * @return
	 */
	@RequestMapping(value = "/updateStudentInfo", method = RequestMethod.POST)
	public BaseBody<Student> updateStudentInfo(StudentVo sv) {
		BaseBody<Student> baseBody = new BaseBody<Student>();

		if (null == sv.getMobile() || "".equals(sv.getMobile())) {
			baseBody.setSuccess(ReturnStatus.FAILD);
			baseBody.setCode(500);
			baseBody.setMessage("手机号码为必传参数");
			return baseBody;
		}
		int rel = studentService.updateByMobile(sv);
		if (rel > 0) {
			baseBody.setSuccess(ReturnStatus.SUCCESS);
			baseBody.setCode(200);
			baseBody.setMessage("学生信息更新成功");
		} else {
			baseBody.setSuccess(ReturnStatus.FAILD);
			baseBody.setCode(500);
			baseBody.setMessage("学生信息更新失败");
		}
		return baseBody;
	}


	/**
	 * 密码修改
	 * @param oldPassword	原密码
	 * @param newPassword	新密码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
	public BaseBody<String> modifyPassword(@RequestParam String oldPassword,
			@RequestParam String newPassword, HttpServletRequest request,
			HttpServletResponse response) {
		BaseBody<String> baseBody = new BaseBody<String>();

		String token = request.getHeader(loginToken);
		logger.info("修改密码，token："+token);
		
		if (null != token && RedisUtil.hasKey(token)) {
			logger.info("从redis里获取登录信息,token：{}",token);
			StuInfoOfRedis userInfo = getUserInfoByRedis(token);
			logger.info("从redis里获取登录信息,redisMobile：{}",userInfo.getMobile());
			String redisMobile = userInfo.getMobile();
			logger.info("修改密码，获取根据手机号获取学生信息：{}",redisMobile);
			Student student = studentService.checkPassword(redisMobile);
			logger.info("修改密码，获取根据手机号获取学生信息：{}",student.getMobile());
			String md5OldPass = MD5Util.encryption(redisMobile+oldPassword+passwordKey);
			logger.info("检查密码：{},{}",student.getPassword(),md5OldPass);
			if (null==student||!student.getPassword().equals(md5OldPass)) {
				baseBody.setSuccess(ReturnStatus.FAILD);
				baseBody.setCode(500);
				baseBody.setMessage("原密码不正确");
				return baseBody;
			}
			String md5Pass = MD5Util.encryption(redisMobile+newPassword+passwordKey);
			logger.info("修改密码：{}",md5Pass);
			int rel = studentService.modifyPassword(redisMobile, md5Pass);
			if (rel != 0) {
				logger.info("修改密码成功：{}",md5Pass);
				baseBody.setSuccess(ReturnStatus.SUCCESS);
				baseBody.setCode(200);
				baseBody.setMessage("密码修改成功");
			} else {
				logger.info("修改密码失败：{}",md5Pass);
				baseBody.setSuccess(ReturnStatus.FAILD);
				baseBody.setCode(500);
				baseBody.setMessage("密码修改失败 ");
			}
		}else {
			baseBody.setSuccess(ReturnStatus.FAILD);
			baseBody.setCode(500);
			baseBody.setMessage("用户未登录 ");
		}
		
		return baseBody;
	}
	

	@RequestMapping(value = "/personAuth", method = RequestMethod.POST)
	public BaseBody<T> personAuth(String cardNo,HttpServletRequest request){
		
		String userToken = request.getHeader(loginToken);
		StuInfoOfRedis userInfo = getUserInfoByRedis(userToken);
		
		String name = userInfo.getName();
		String mobile = userInfo.getMobile();
		
		try {
			boolean authRel = personAuthService.requestPerson(name, cardNo);
			if(authRel){
				Student student = new Student();
				student.setMobile(mobile);
				student = studentService.selectOne(student);
				student.setCardNo(cardNo);
				
				studentService.updateByPk(student);
				
				return success("认证成功");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fail(ReturnStatus.STATUS_ERROR,"个人认证失败");
	}
}
