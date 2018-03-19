package com.itcast.enrol.student.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.itcast.enrol.common.entity.Contract;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.entity.Student;
import com.itcast.enrol.common.utils.RedisUtil;
import com.itcast.enrol.student.service.StuOrderMainService;
import com.itcast.enrol.student.service.StuStudentService;
import com.itcast.enrol.student.service.plugins.PDFParams;
import com.itcast.enrol.student.service.plugins.StuContractService;
import com.itcast.enrol.student.service.plugins.StuPersonAuthService;
import com.itcast.enrol.student.service.plugins.StuSMSService;
import com.itcast.enrol.student.vo.StuInfoOfRedis;
@RestController
@RequestMapping("/contractController")
public class StuContractController extends StuBaseController{
	
	// 统一记录日志类
	private static Logger logger = LoggerFactory.getLogger("enrol");
	
	@Autowired
	private StuContractService contractService;
	
	@Autowired
	private StuOrderMainService orderMainService;
	
	@Autowired
	private StuStudentService studentService;
	
	@Autowired
	private StuSMSService smsService;
	
	@Autowired
	private StuPersonAuthService personAuthService;
	
	@Value("${server.token-key-mobile}")
    private String loginToken;
	
	/**
	 * 获取合同列表
	 * @param orderMainNo
	 * @return
	 */
	@RequestMapping(value = "/getContractList", method = RequestMethod.GET)
	public BaseBody<T> getContractList(@RequestParam String orderMainNo,HttpServletRequest request){
		
		String userToken = request.getHeader(loginToken);
		StuInfoOfRedis userInfo = getUserInfoByRedis(userToken);
		
		String mobile = userInfo.getMobile();
		
		List<Map<String,Object>> contractList = contractService.getContractList(orderMainNo, mobile);
		
		if(null==contractList || contractList.size()==0){
			return fail(ReturnStatus.DATA_NULL,"查不到合同信息");
		}
    	
		return success(contractList);
	}
	/**
	 * 获取合同信息
	 * @param orderMainNo
	 * @return
	 */
	@RequestMapping(value = "/getContractPDF", method = RequestMethod.POST)
	public BaseBody<T> getContractPDF(@RequestParam String orderMainNo,HttpServletRequest request){
		
		String userToken = request.getHeader(loginToken);
		StuInfoOfRedis userInfo = getUserInfoByRedis(userToken);
		
		Long studentId = userInfo.getId();
		
		Map<String, Object> contractMap=null;
		
		Student student = studentService.selectByPrimaryKey(studentId);
		
		PDFParams pdfParams = PDFParams.getPDFParams(student);
		
		if(null==pdfParams){
			return fail(ReturnStatus.STATUS_ERROR,"请完善个人信息");
		}
		
		try {
			boolean authRel = personAuthService.requestPerson(student.getName(), student.getCardNo());
			if(!authRel){
				return fail(ReturnStatus.STATUS_ERROR,"实名认证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			contractMap = contractService.getContractPDF(orderMainNo, pdfParams);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("订单：{}  合同生成失败{}",orderMainNo,e);
		}
    	
		if(null==contractMap){
			return fail(ReturnStatus.DATA_NULL,"合同生成失败");
		}
		return success(contractMap);
	}
	/**
	 * 查看合同信息
	 * @param orderMainNo
	 * @return
	 */
	@RequestMapping(value = "/seeContract", method = RequestMethod.GET)
	public BaseBody<T> seeContract(@RequestParam Long contractId,HttpServletRequest request){
		
		Map<String,Object> contractMap = contractService.seeContractInfo(contractId);
		
		if(null==contractMap){
			return fail(ReturnStatus.DATA_NULL,"合同生成失败");
		}
		return success(contractMap);
	}
	/**
	 * 发送签订合同操作验证码
	 * @param mobile
	 * @param orderMainNo
	 * @return
	 */
	@RequestMapping(value = "/sendContractSms", method = RequestMethod.POST)
	public BaseBody<T> sendContractSms(@RequestParam String orderMainNo,HttpServletRequest request){
		
		String userToken = request.getHeader(loginToken);
		StuInfoOfRedis userInfo = getUserInfoByRedis(userToken);
		
		String mobile = userInfo.getMobile();
		
		OrderMain orderMain = new OrderMain();
		orderMain.setMergeOrderNo(orderMainNo);
		orderMain = orderMainService.selectOne(orderMain);
		
		if(null!=orderMain && mobile.equals(orderMain.getStudentMobile())){
			String code = smsService.sendContractSms(mobile);
			logger.info("手机验证码：{}",code);
			if(null==code||"".equals(code)){
				return fail(500,"短信发送失败");
			}
			try{
				RedisUtil.set(code+mobile, mobile,10 * 60);
			}catch(Exception e){
				logger.error("redis服务器异常，{}",e.getMessage());
			}
			Contract contract = contractService.getContractLastTime(orderMainNo);
			if(contract.getContractStatus().intValue()==1){
				contract.setSignSmsMobile(mobile);
				contract.setSignSmsTime(System.currentTimeMillis());
			}
			if(contract.getContractStatus().intValue()==3){
				contract.setCancelSmsMobile(mobile);
				contract.setCancelSmsTime(System.currentTimeMillis());
			}
			contractService.updateByPk(contract);
			return success("验证码发送成功");
		}else{
			return fail(ReturnStatus.DATA_NULL,"登录信息与请求数据不匹配");
		}
	}
	/**
	 * 签订合同
	 * @param mobile
	 * @param orderMainNo
	 * @param smsCode
	 * @return
	 */
	@RequestMapping(value = "/signContract", method = RequestMethod.POST)
	public BaseBody<T> signContract(@RequestParam Long contractId,@RequestParam String smsCode,HttpServletRequest request){
		
		String userToken = request.getHeader(loginToken);
		StuInfoOfRedis userInfo = getUserInfoByRedis(userToken);
		
		String mobile = userInfo.getMobile();
		Long studentId = userInfo.getId();
		
		if(!RedisUtil.hasKey(smsCode+mobile)){
			return fail(500,"验证码无效");
		}
		
		boolean caRel = contractService.createStudentCA(studentId);
		if(!caRel){
			return fail(500,"个人CA证书生成失败");
		}
		Contract contract = contractService.selectByPrimaryKey(contractId);
		contract.setSignSmsContent("【传智播客】尊敬的学员：您的签约验证码为"+smsCode+"，有效时间为10分钟，请及时输入。为确保签约及交易安全，请勿向任何人泄露该验证码");
		contractService.updateByPk(contract);
		boolean signRel = contractService.signContract(contractId, mobile,studentId);
		if(signRel) {
			return success("合同签订成功");
		}
		return fail(500,"合同签订失败");
	}
	/**
	 * 废弃合同
	 * @param orderMainNo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cancelContract", method = RequestMethod.POST)
	public BaseBody<T> cancelContract(@RequestParam Long contractId,@RequestParam String smsCode,HttpServletRequest request){

		String userToken = request.getHeader(loginToken);
		StuInfoOfRedis userInfo = getUserInfoByRedis(userToken);
		
		String mobile = userInfo.getMobile();
		
		if(!RedisUtil.hasKey(smsCode+mobile)){
			return fail(500,"验证码无效");
		}
		
		boolean b = contractService.cancelContract(contractId, mobile);
		Contract contract = contractService.selectByPrimaryKey(contractId);
		contract.setCancelSmsContent(smsCode);
		contractService.updateByPk(contract);
		if(b) {
			return success("成功");
		}
		return fail(500,"合同作废失败");
	}
}