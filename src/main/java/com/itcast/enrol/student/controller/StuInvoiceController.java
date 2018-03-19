package com.itcast.enrol.student.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.ems.SynchBaseDataForEms;
import com.itcast.enrol.common.entity.Campus;
import com.itcast.enrol.common.entity.Invoice;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.entity.Student;
import com.itcast.enrol.student.service.StuCampusService;
import com.itcast.enrol.student.service.StuInvoiceService;
import com.itcast.enrol.student.service.StuOrderMainService;
import com.itcast.enrol.student.service.StuStudentService;
import com.itcast.enrol.student.vo.StuInfoOfRedis;

/**
 * 
 * 发票表 访问控制器类
 * 
 **/
@RestController
@RequestMapping("/invoiceController")
public class StuInvoiceController extends StuBaseController{

	@Autowired
	// 当前业务操作接口bo
	private StuInvoiceService invoiceService;
	
	@Autowired
	private StuOrderMainService orderMainService;
	
	@Autowired
	private StuStudentService studentService;
	
	@Autowired
	private StuCampusService campusService;
	
	@Autowired
	private SynchBaseDataForEms synchBaseDataForEms;
	
	@Value("${server.token-key-mobile}")
	private String loginToken;

	/**
	 * 查询发票信息
	 * @param orderMainNo
	 * @return
	 */
	@RequestMapping(value = "/getInvoiceInfo", method = RequestMethod.GET)
	public BaseBody<T> getInvoiceInfo(@RequestParam String orderNo) {

		Map<String,Object> invoiceInfo = invoiceService.selectByOrderMainNo(orderNo);
		if (null != invoiceInfo && invoiceInfo.size()>0) {
			BigDecimal bigDecimal = new BigDecimal(Integer.parseInt(String.valueOf(invoiceInfo.get("price"))));
			String price = bigDecimal.movePointLeft(2).toString();
			invoiceInfo.put("price", price);
			return success(invoiceInfo);
		} else {
			Map<String,Object> invoiceInfoMap=new HashMap<String,Object>();
			
			OrderMain orderMain = new OrderMain();
			orderMain.setMergeOrderNo(orderNo);
			orderMain = orderMainService.select(orderMain).get(0);
			
			if(null==orderMain || orderMain.getStatus()!=2){
				return fail(ReturnStatus.STATUS_ERROR,"订单状态不允许申请发票");
			}
			
			BigDecimal bigDecimal = new BigDecimal(orderMain.getOrderPrice());
			String price = bigDecimal.movePointLeft(2).toString();
			invoiceInfoMap.put("price", price);
			invoiceInfoMap.put("content", "培训费");
			
			Student student = studentService.selectByPrimaryKey(orderMain.getStudentId());
			invoiceInfoMap.put("email",student.getEmail());
			
			Campus campus = campusService.selectByPrimaryKey(orderMain.getCampusId());
			invoiceInfoMap.put("eInvoice",campus.geteInvoice());
			
			return success(invoiceInfoMap);
		}
	}

	/**
	 * 查询发票列表
	 * 
	 * @param orderMainNo
	 *            订单编号
	 * @return
	 */
	@RequestMapping(value = "/addInvoiceInfo", method = RequestMethod.POST)
	public BaseBody<T> addInvoiceInfo(Invoice invoice,HttpServletRequest request,
			HttpServletResponse response) {
		
		String userToken = request.getHeader(loginToken);
		StuInfoOfRedis userInfo = getUserInfoByRedis(userToken);
		
		//申请发票的订单号
		String orderMainNo = invoice.getOrderNo();
		if(null==orderMainNo || "".equals(orderMainNo)){
			return fail(ReturnStatus.PARAM_ERROR,"参数orderNo不能为空");
		}
		
		Map<String,Object> invoiceInfo = invoiceService.selectByOrderMainNo(orderMainNo);
		int isApply = invoice.getIsApply();//Integer.parseInt(invoiceInfo.get("isApply").toString());
		if(isApply==0){
			return fail(ReturnStatus.PARAM_ERROR,"不开发票不允许提交发票信息");
		}
		
		if (invoiceInfo!=null && invoiceInfo.size()>0) {
			//一个主订单只允许申请一次发票，且申请后，发票信息不得修改
			return fail(ReturnStatus.STATUS_ERROR,"发票不允许重复申请");
		}else{
			//验证参数
			String title = invoice.getTitle();
			Byte type = invoice.getType();
			Byte titleType = invoice.getTitleType();
			
			if(null==title || "".equals(title) || null==type || null==titleType){
				return fail(ReturnStatus.PARAM_ERROR,"参数：title,type,titleType 不可为空");
			}
			
			OrderMain orderMain = new OrderMain();
			orderMain.setMergeOrderNo(orderMainNo);
			orderMain = orderMainService.selectOne(orderMain);
			
			
			if(orderMain==null){
				return fail(ReturnStatus.DATA_NULL,"查不到相应订单");
			}
			int orderStatus =orderMain.getStatus();
			//查不到主订单、主订单状态不是支付完成
			if(orderStatus!=2){
				return fail(ReturnStatus.STATUS_ERROR,"请完成订单支付后再申请发票");
			}
			//设置发票金额
			invoice.setPrice(orderMain.getOrderPrice());
			
			invoice.setContent("培训费");
			invoice.setCreateTime(System.currentTimeMillis());
			invoice.setCreator(userInfo.getName());
			
			//if(null==invoiceInfo||invoiceInfo.size()==0){
				//不存在发票信息，存储发票信息
				int rel = invoiceService.addInvoiceInfo(invoice);
				if(rel!=0){
					return success("发票信息保存成功");
				}else{
					return fail(500,"发票信息保存失败");
				}
			/*}else{
				invoice.setId(Long.parseLong(invoiceInfo.get("id").toString()));
				//存在发票信息，修改发票信息
				int rel = invoiceService.updateInvoiceInfo(invoice);
				if(rel!=0){
					baseBody.setSuccess(ReturnStatus.SUCCESS);
					baseBody.setCode(200);
					baseBody.setMessage("发票信息修改成功");
				}else{
					baseBody.setSuccess(ReturnStatus.FAILD);
					baseBody.setCode(500);
					baseBody.setMessage("发票信息修改失败");
				}
			}*/
			
		}
	}
}