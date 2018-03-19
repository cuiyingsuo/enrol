package com.itcast.enrol.student.service.plugins;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.contract.ContractMapper;
import com.itcast.enrol.common.dao.order.OrderMainMapper;
import com.itcast.enrol.common.dao.organize.CampusMapper;
import com.itcast.enrol.common.dao.organize.ClassMapper;
import com.itcast.enrol.common.dao.sequence.SeqMapper;
import com.itcast.enrol.common.dao.student.ClassStudentMapper;
import com.itcast.enrol.common.dao.student.StudentMapper;
import com.itcast.enrol.common.entity.Campus;
import com.itcast.enrol.common.entity.Contract;
import com.itcast.enrol.common.entity.EnrolClass;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.entity.Student;
import com.itcast.enrol.common.exception.BaseException;
import com.itcast.enrol.common.exception.BeanNullException;
import com.itcast.enrol.common.utils.PriceUtil;
import com.timevale.esign.sdk.esignpro.bean.HttpConnectConfig;
import com.timevale.esign.sdk.esignpro.bean.PositionBean;
import com.timevale.esign.sdk.esignpro.bean.ProjectConfig;
import com.timevale.esign.sdk.esignpro.bean.SignPDFFileBean;
import com.timevale.esign.sdk.esignpro.bean.SignerAutoBean;
import com.timevale.esign.sdk.esignpro.bean.result.AccountResult;
import com.timevale.esign.sdk.esignpro.bean.result.CertResult;
import com.timevale.esign.sdk.esignpro.bean.result.FileDigestSignResult;
import com.timevale.esign.sdk.esignpro.bean.result.Result;
import com.timevale.esign.sdk.esignpro.bean.result.SealResult;
import com.timevale.esign.sdk.esignpro.constants.AccountType;
import com.timevale.esign.sdk.esignpro.constants.HttpType;
import com.timevale.esign.sdk.esignpro.constants.LicenseOrganizeType;
import com.timevale.esign.sdk.esignpro.constants.LicenseType;
import com.timevale.esign.sdk.esignpro.constants.PosType;
import com.timevale.esign.sdk.esignpro.constants.SealColorType;
import com.timevale.esign.sdk.esignpro.constants.SealType;
import com.timevale.esign.sdk.esignpro.outer.bean.OrganizeOuterBean;
import com.timevale.esign.sdk.esignpro.outer.bean.PersonalOuterBean;
import com.timevale.esign.sdk.esignpro.outer.bean.TemplateSealOuterBean;
import com.timevale.esign.sdk.esignpro.outer.service.AccountService;
import com.timevale.esign.sdk.esignpro.outer.service.CertService;
import com.timevale.esign.sdk.esignpro.outer.service.SealService;
import com.timevale.esign.sdk.esignpro.outer.service.factory.AccountServiceFactory;
import com.timevale.esign.sdk.esignpro.outer.service.factory.CertServiceFactory;
import com.timevale.esign.sdk.esignpro.outer.service.factory.SealServiceFactory;
import com.timevale.esign.sdk.esignpro.service.BackSignService;
import com.timevale.esign.sdk.esignpro.service.SDKService;
import com.timevale.esign.sdk.esignpro.service.factory.SDKServiceFactory;
@Service
public class StuContractService  extends BaseService<Contract>{
	// 统一记录日志类
	private static Logger logger = LoggerFactory.getLogger("enrol");

	@Autowired
	private OrderMainMapper orderMainDao;
	
	@Autowired
	private StudentMapper studentDao;
	
	@Autowired
	private CampusMapper campusDao;
	
	@Autowired
	private ClassStudentMapper classStudentDao;
	
	@Autowired
	private ClassMapper classDao;
	
	@Autowired
	private ContractMapper contractDao;
	
	@Autowired
	private SeqMapper seqDao;
	
	/* 天印签订合同参数 start */
	@Value("${file.contract.projectId}")
	private String projectId;
	@Value("${file.contract.projectSecret}")
	private String projectSecret;

	@Value("${file.contract.serverIp}")
	private String serverIp;
	@Value("${file.contract.serverPor}")
	private int serverPort;
	@Value("${file.contract.parentOrg}")
	private String contractCZBK;
	@Value("${file.contract.sealId}")
	private String contractCancelSealId;
	@Value("${file.contract.personCancelSealId}")
	private String personCancelSealId;

	@Value("${file.contract.savePath}")
	private String contractSavePath;

	/* 天印签订合同参数 end */

	public List<Contract> getContractListByOrderMainNo(String orderMainNo){
		return contractDao.selectListByOrderMainNo(orderMainNo);
	}
	
	public Contract getContractLastTime(String orderMainNo){
		return contractDao.selectContractLastTime(orderMainNo);
	}
	
	public int getCountsByOrderMainNo(String orderMainNo){
		Contract contract = new Contract();
		contract.setOrderMainNo(orderMainNo);
		return contractDao.selectCount(contract);
	}
	
	/**
	 * 获取合同列表
	 * @param orderMainNo  主订单号
	 * @param mobile
	 * @return
	 */
	public List<Map<String,Object>> getContractList(String orderMainNo,String mobile){
		List<Map<String,Object>> contractListMap = new ArrayList<Map<String,Object>>();
		
		OrderMain orderMain = new OrderMain();
		orderMain.setMergeOrderNo(orderMainNo);
		
		orderMain = orderMainDao.selectOne(orderMain);
		
		List<Contract> contractList = contractDao.selectListByOrderMainNo(orderMainNo);
		
		for(Contract c:contractList){
			Map<String,Object> contractInfo= new HashMap<String,Object>();
			contractInfo.put("contractId", c.getId());
			contractInfo.put("contractUrl", c.getContractUrl());
			contractInfo.put("mobile", mobile);
			contractInfo.put("contractStatus", c.getContractStatus());//0、不允许签订，1、允许签订，2、已签订，3、允许废弃 4、已废弃
			contractInfo.put("contractSignTime", c.getContractSignTime());
			contractInfo.put("contractCancelTime", c.getContractCancelTime());
			
			contractListMap.add(contractInfo);
		}
		
		return contractListMap;
	}
	
	public Map<String,Object> seeContractInfo(Long contractId){
		Contract contract = contractDao.selectByPrimaryKey(contractId);
		
		if(null==contract){
			return null;
		}
		Map<String,Object> contractInfo= new HashMap<String,Object>();
		contractInfo.put("contractId", contract.getId());
		contractInfo.put("contractUrl", contract.getContractUrl());
		contractInfo.put("contractStatus", contract.getContractStatus());//0、不允许签订，1、允许签订，2、已签订，3、允许废弃 4、已废弃
		contractInfo.put("contractSignTime", contract.getContractSignTime());
		contractInfo.put("contractCancelTime", contract.getContractCancelTime());
		
		return contractInfo;
	}
	
	@Transactional
	public Map<String,Object> getContractPDF(String orderMainNo,PDFParams pdfParams) throws Exception{
		Map<String,Object> contractInfo = new HashMap<String,Object>();
		
		OrderMain orderMain = new OrderMain();
		orderMain.setMergeOrderNo(orderMainNo);
		
		orderMain = orderMainDao.selectOne(orderMain);
		String mobile = pdfParams.getStudentTel();
		
		if(null==orderMain) {
			throw new BeanNullException("查不到相应订单");
		}
		if(!mobile.equals(orderMain.getStudentMobile())) {
			throw new BaseException("登录信息与订单不匹配");
		}
		
		Contract contract = contractDao.selectContractLastTime(orderMainNo);
		boolean isNewContract = false;
		
		if(null==contract){
			isNewContract=true;
		}
		if(null!=contract && contract.getContractStatus().intValue()==4){
			isNewContract=true;
		}
		if(null!=contract && "".equals(contract.getContractUrl())){
			isNewContract=true;
		}
		if(isNewContract) {
			if(null!=contract&&contract.getContractStatus().intValue()==4){
				contract = new Contract();
			}
			
			Long studentId = orderMain.getStudentId();
			Long campusId = orderMain.getCampusId();
			
			
			Campus campus = campusDao.selectByPrimaryKey(campusId);
			EnrolClass enrolClass = classDao.selectByPrimaryKey(orderMain.getClassId());
			
			Long subjectId = orderMain.getSubjectId();
			String classTypeCode = enrolClass.getClassTypeCode();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String subjectIdStr = "";
			if(subjectId<10) {
				subjectIdStr="0"+subjectId;
			}else {
				subjectIdStr=String.valueOf(subjectId);
			}
			String classType = "";
			if("class_detail_base".equals(classTypeCode)) {
				classType="1";
			}else {
				classType="2";
			}
			
			String[] contractNo = seqDao.nextVal("contract_no").split(","); 
			
			String serial = "ITCAST-"+contractNo[0]+"-"+subjectIdStr+classType+"-"+String.format("%06d", Integer.parseInt(contractNo[1]));
			pdfParams.setPdfSerial(serial);
			pdfParams.setOrgName(campus.getOrgName());
			
			pdfParams.setGoodsName(orderMain.getGoodsName());
			BigDecimal bdc = new BigDecimal(orderMain.getCostPrice());
			pdfParams.setPrice(bdc.movePointLeft(2).toString());
			pdfParams.setPriceCN(PriceUtil.priceToCN(bdc.movePointLeft(2)));
			
			BigDecimal bdo = new BigDecimal(orderMain.getCostPrice());
			pdfParams.setOrderPrice(bdo.movePointLeft(2).toString());
			pdfParams.setOrderPriceCN(PriceUtil.priceToCN(bdo.movePointLeft(2)));
			pdfParams.setCampusAddr(campus.getAddress());
			pdfParams.setStudyAddress(campus.getStudyAddress());
			
			sdf = new SimpleDateFormat("yyyy年MM月dd日");
			pdfParams.setStartDate(sdf.format(enrolClass.getStartDate()));
			pdfParams.setEndDate(sdf.format(enrolClass.getEndDate()));
			
			pdfParams.setSignDate(sdf.format(new Date()));
			
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			//数据库存储路径
			String contractUrl = "/doc/"+sdf.format(enrolClass.getStartDate())+"/"+orderMain.getClassId()+"/";
			//文件写路径
			String contractPath = contractSavePath+contractUrl;
			File pdfFile = new File(contractPath);
			if(!pdfFile.exists()) {
				pdfFile.mkdirs();
			}
			contractUrl += serial+".pdf";
			contractPath += serial+".pdf";
			
			if("class_detail_base".equals(classTypeCode)) {
				new PDFUtil().createPDFBase(contractPath, pdfParams);
			}else {
				new PDFUtil().createPDFEmployment(contractPath, pdfParams);
			}
			
			contract.setContractCode(serial);
			contract.setContractUrl(contractUrl+"?status=init");
			contract.setOrderMainNo(orderMainNo);
			contract.setContractStatus(1);
			contract.setCreater(pdfParams.getStudentName());
			contract.setCreateTime(System.currentTimeMillis());
			if(null!=contract.getId()){
				contractDao.updateByPrimaryKey(contract);
			}else{
				contractDao.insert(contract);
			}
			
			contract = contractDao.selectContractLastTime(orderMainNo);
		}
		
		contractInfo.put("contractId", contract.getId());
		contractInfo.put("contractUrl", contract.getContractUrl());
		contractInfo.put("mobile", mobile);
		contractInfo.put("contractStatus", contract.getContractStatus());//0、不允许签订，1、允许签订，2、已签订，3、允许废弃 4、已废弃
		contractInfo.put("contractSignTime", contract.getContractSignTime());
		contractInfo.put("contractCancelTime", contract.getContractCancelTime());
		
		return contractInfo;
	}
	
	@Transactional
	public boolean createStudentCA(Long studentId){
		Student student = studentDao.selectByPrimaryKey(studentId);
		
		String studentAccountId = student.getTyAccountId();
		String studentCertId = student.getTyCertId();
		String studentSealId = student.getTySealId();
		initService();
		if(StringUtils.isEmpty(studentAccountId)) {
			
			studentAccountId=tyCreateOutAccount(student.getName(), student.getCardNo(), 0);
			if(null==studentAccountId) {
				return false;
			}
			student.setTyAccountId(studentAccountId);
		}
		if(StringUtils.isEmpty(studentCertId)) {
			studentCertId=tyCreateOutCert(studentAccountId);
			if(null==studentCertId) {
				return false;
			}
			student.setTyCertId(studentCertId);
		}
		if(StringUtils.isEmpty(studentSealId)){
			studentSealId=tyCreateOutSeal(studentAccountId,student.getName());
			if(null==studentSealId){
				return false;
			}
			student.setTySealId(studentSealId);
		}
		studentDao.updateByPrimaryKeySelective(student);
		
		return true;
	}
	
	@Transactional
	public boolean signContract(Long contractId,String mobile,Long studentId) {
		logger.info("准备签订合同：{},{},{}",contractId,mobile,studentId);
		
		Student student = studentDao.selectByPrimaryKey(studentId);
		
		String studentAccountId = student.getTyAccountId();
		String studentCertId = student.getTyCertId();
		String studentSealId = student.getTySealId();
		
		Contract contract = contractDao.selectByPrimaryKey(contractId);
		
		String orderMainNo = contract.getOrderMainNo();
		
		OrderMain orderMain = new OrderMain();
		orderMain.setMergeOrderNo(orderMainNo);
		
		orderMain = orderMainDao.selectOne(orderMain);
		if(null==orderMain) {
			throw new BeanNullException("查不到相应订单");
		}
		if(!mobile.equals(orderMain.getStudentMobile())) {
			throw new BaseException("登录信息与订单不匹配");
		}
		
		/*ClassStudent classStudent = new ClassStudent();
		classStudent.setClassId(orderMain.getClassId());
		classStudent.setStudentId(orderMain.getStudentId());
		
		classStudent = classStudentDao.selectOne(classStudent);*/
		if(1!=contract.getContractStatus().intValue()) {
			throw new BaseException("合同状态不允许签订");
		}
		String contractUrl = contract.getContractUrl();
		if(null==contractUrl||"".equals(contractUrl)){
			throw new BaseException("合同未创建，请先调用合同获取接口");
		}
		
		EnrolClass enrolClass = classDao.selectByPrimaryKey(orderMain.getClassId());
		boolean isOKSign = false;
		if(2==orderMain.getStatus()) {
			//支付完成，判断是否过了开班时间当天的9点
			isOKSign=isCanSignContract(enrolClass.getStartDate());
		}else {
			//支付中，合同状态为可签订（后台管理端可设置为该状态）
			if(1==orderMain.getStatus()&&1==contract.getContractStatus()) {
				isOKSign=true;
			}
		}
		
		//合同状态为可签订（后台管理端可设置为该状态）
		if(1==contract.getContractStatus()) {
			isOKSign=true;
		}
		
		if(!isOKSign) {
			throw new BaseException("订单状态不能签订合同");
		}
		
		Long campusId = orderMain.getCampusId();
		Campus campus = campusDao.selectByPrimaryKey(campusId);
		logger.info(contractSavePath+contractUrl);
		
		contractUrl=contractUrl.substring(0,contractUrl.lastIndexOf('?'));
		String fileName = contractUrl.substring(contractUrl.lastIndexOf('/')+1,contractUrl.length());
		initService();
		String signServiceId0 = tySignToContract(contractCZBK, "0", "0", contractSavePath+contractUrl,fileName, 0);
		String signServiceId1 = tySignToContract(campus.getSerialCode(), "0", "0", contractSavePath+contractUrl,fileName, 1);
		String signServiceIdStu = tyAotoSignToContract(studentAccountId,studentCertId,studentSealId,contractSavePath+contractUrl,fileName,0);
		
		if(null==signServiceId0||null==signServiceId1||null==signServiceIdStu) {
			throw new BaseException("合同盖章失败");
		}
		contract.setTySignServiceId(signServiceId0+","+signServiceId1+","+signServiceIdStu);
		contract.setContractStatus(2);
		contract.setContractSignTime(System.currentTimeMillis());
		contract.setContractUrl(contractUrl+"?status=sign");
		contractDao.updateByPrimaryKey(contract);
		
		return true;
	}
	
	@Transactional
	public boolean cancelContract(Long contractId,String mobile) {
		Contract contract = contractDao.selectByPrimaryKey(contractId);
		
		String orderMainNo=contract.getOrderMainNo();
		OrderMain orderMain = new OrderMain();
		orderMain.setMergeOrderNo(orderMainNo);
		
		orderMain = orderMainDao.selectOne(orderMain);
		if(null==orderMain) {
			throw new BeanNullException("查不到相应订单");
		}
		if(!mobile.equals(orderMain.getStudentMobile())) {
			throw new BaseException("登录信息与订单不匹配");
		}
		
		/*ClassStudent classStudent = new ClassStudent();
		classStudent.setClassId(orderMain.getClassId());
		classStudent.setStudentId(orderMain.getStudentId());
		
		classStudent = classStudentDao.selectOne(classStudent);*/
		if(3!=contract.getContractStatus()) {
			throw new BaseException("不允许单方废弃合同");
		}
		
		Long studentId = orderMain.getStudentId();
		Long campusId = orderMain.getCampusId();
		
		Student student = studentDao.selectByPrimaryKey(studentId);
		String studentAccountId = student.getTyAccountId();
		String studentCertId = student.getTyCertId();
		String studentSealId = student.getTySealId();
		
		
		
		Campus campus = campusDao.selectByPrimaryKey(campusId);
		initService();
		
		String contractUrl = contract.getContractUrl();
		contractUrl=contractUrl.substring(0,contractUrl.lastIndexOf('?'));
		String fileName = contractUrl.substring(contractUrl.lastIndexOf('/')+1,contractUrl.length());
		
		String cancelServiceId0 = tySignToContract(contractCZBK, "0", contractCancelSealId, contractSavePath+contractUrl,fileName, 2);
		String cancelServiceIdStu = tyAotoSignToContract(studentAccountId,"0",personCancelSealId,contractSavePath+contractUrl,fileName,1);
		
		if(null==cancelServiceId0||null==cancelServiceIdStu) {
			throw new BaseException("取消合同盖章失败");
		}
		contract.setTyCancelServiceId(cancelServiceId0+","+cancelServiceIdStu);
		contract.setContractStatus(4);
		contract.setContractCancelTime(System.currentTimeMillis());
		contract.setContractUrl(contractUrl+"?status=cancel");
		contractDao.updateByPrimaryKey(contract);
		
		return true;
	}
	
	
	private boolean isCanSignContract(Date startDate) {
		boolean isOKSign = false;
    	
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date t9 = c.getTime();
			if(t9.getTime()<System.currentTimeMillis()) {
				isOKSign = true;
			}
		
		
		return isOKSign;
	}
	
	/**
	 * 初始化天印服务
	 */
	private void initService() {
		logger.info("天印服务器初始化");
		// 初始化项目
		SDKService sdkService = SDKServiceFactory.instance();
		ProjectConfig projectConfig = new ProjectConfig();
		projectConfig.setProjectId(projectId);// 项目projectid
		projectConfig.setProjectSecret(projectSecret); // 项目projectsecret

		HttpConnectConfig httpConnectConfig = new HttpConnectConfig();
		httpConnectConfig.setServerIp(serverIp);// 天印签章服务IP地址
		httpConnectConfig.setServerPort(serverPort);// 天印签章服务端口
		httpConnectConfig.setHttpType(HttpType.HTTP);
		Result result = sdkService.init(projectConfig, httpConnectConfig);
		logger.info("天印服务初始化,errCode：{}   msg：{}",result.getErrCode(),result.getMsg());
	}

	/**
	 * 创建账户
	 * 
	 * @param name
	 *            账户名称（个人：姓名，公司：公司名）
	 * @param cardNo
	 *            账号证件号（个人：身份证号，公司：社会信用代码）
	 * @param type
	 *            账户类型（0、个人，1、机构）
	 * @return 账户id
	 */
	private String tyCreateOutAccount(String name, String cardNo, int type) {
		String accountId = null;

		// 创建账户
		AccountService accountService = AccountServiceFactory.instance();
		if (type == 0) {
			PersonalOuterBean personalOuterBean = new PersonalOuterBean();
			personalOuterBean.setName(name);
			personalOuterBean.setLicenseNumber(cardNo);

			personalOuterBean.setLicenseType(LicenseType.IDCard);
			AccountResult accountResult = accountService.createAccount(personalOuterBean);// 创建个人证书
			accountId = accountResult.getAccountId();
			logger.info("创建天印个人账户：{}",accountId);

		} else {
			OrganizeOuterBean organizeOuterBean = new OrganizeOuterBean();
			organizeOuterBean.setName(name);
			organizeOuterBean.setLicenseNumber(cardNo);
			organizeOuterBean.setLicenseType(LicenseOrganizeType.SOCNO);

			AccountResult accountResult = accountService.createAccount(organizeOuterBean);// 创建企业证书
			accountId = accountResult.getAccountId();
			logger.info("创建天印企业账户：{}",accountId);
		}

		return accountId;
	}

	/**
	 * 创建CA证书
	 * @param accountId		账户id
	 * @return				CA证书id
	 */
	private String tyCreateOutCert(String accountId) {
		String certId = null;

		// 创建证书并做关联
		CertService certService = CertServiceFactory.instance();
		CertResult certResult = certService.createCert(accountId, Boolean.TRUE);
		certId = certResult.getCertId();// 证书id
		logger.info("accountId：（{}）创建CA证书：{}",accountId,certId);

		return certId;
	}
	
	/**
	 * 创建外部用户签订印章
	 */
	private String tyCreateOutSeal(String accountId,String stuName){
		String sealId = null;
		//创建印章
        SealService sealService = SealServiceFactory.instance();
        TemplateSealOuterBean templateSealOuterBean = new TemplateSealOuterBean();
        templateSealOuterBean.setSealName(stuName);
        templateSealOuterBean.setColor(SealColorType.RED);
        templateSealOuterBean.setType(SealType.SEAL_PERSON);
        SealResult sealResult = sealService.createSeal(accountId,templateSealOuterBean, Boolean.TRUE);
        sealId=sealResult.getSealId();
        logger.info("sealId:({})创建印章");
        return sealId;
	}
	
	/**
	 * 外部用户盖章操作
	 */
	private String tyAotoSignToContract(String accountId, String certId, String sealId, String fileUrl,String fileName,int type){
		String signServiceId = null;
		
		com.timevale.esign.sdk.esignpro.outer.service.BackSignService backSignService = com.timevale.esign.sdk.esignpro.outer.service.factory.BackSignServiceFactory.instance();
		SignerAutoBean signerAutoBean = new SignerAutoBean();
		signerAutoBean.setAccountId(accountId);
		signerAutoBean.setCertId(certId);
		signerAutoBean.setSealId(sealId);
		signerAutoBean.setAccountType(AccountType.Person);

		SignPDFFileBean signPDFFileBean = new SignPDFFileBean();
		signPDFFileBean.setSrcPdfFile(fileUrl);
		signPDFFileBean.setDstPdfFile(fileUrl);
		signPDFFileBean.setFileName(fileName);
		PositionBean positionBean = new PositionBean();
		positionBean.setPosType(PosType.Multi);
		
		if(type==0){
			positionBean.setPosPage("4");
			positionBean.setPosX(150);
			positionBean.setPosY(180);
		}
		if(type==1){
			signerAutoBean.setUseCancelledSeal(true);
			positionBean.setPosPage("4");
			positionBean.setPosX(200);
			positionBean.setPosY(180);
		}

		logger.info("个人：（{}）（{}）（{}）",accountId,certId,sealId);
		
		FileDigestSignResult fileDigestSignResult = backSignService.pdfSign(signerAutoBean, signPDFFileBean,positionBean);
		logger.info("盖章返回：{}/{}/{}",fileDigestSignResult.getMsg(),fileDigestSignResult.getErrCode(),fileDigestSignResult.getSignServiceId());
		
		
		signServiceId=fileDigestSignResult.getSignServiceId();//签署记录id
		
		return signServiceId;
	}
	/**
	 * 签订合同，盖章操作（只盖公司章）
	 * @param accountId		账户id
	 * @param certId		ca证书id（使用默认证书传0）
	 * @param sealId		印章id（使用默认印章传0，作废合同时传入作废印章id）
	 * @param fileUrl		文件地址
	 * @param fileUrl		印章类型（0、总公司，1、分公司，2，作废），主要区别在于盖章位置，及作废章需传入印章id
	 * @return 				签订记录id
	 */
	public String tySignToContract(String accountId, String certId, String sealId, String fileUrl,String fileName,int type) {
		String signServiceId = null;

		// 盖章操作(签订合同)
		BackSignService backSignService = com.timevale.esign.sdk.esignpro.service.factory.BackSignServiceFactory.instance();
		SignerAutoBean signerAutoBean = new SignerAutoBean();
		signerAutoBean.setAccountId(accountId);
		signerAutoBean.setCertId(certId);
		signerAutoBean.setSealId(sealId);
		signerAutoBean.setAccountType(AccountType.Department);
		
		SignPDFFileBean signPDFFileBean = new SignPDFFileBean();
		signPDFFileBean.setSrcPdfFile(fileUrl);
		signPDFFileBean.setDstPdfFile(fileUrl);
		signPDFFileBean.setFileName(fileName);
		PositionBean positionBean = new PositionBean();
		positionBean.setPosType(PosType.Multi);
		
		if(type==0) {
			positionBean.setPosPage("4");
			positionBean.setPosX(130);
			positionBean.setPosY(280);
		}
		if(type==1) {
			positionBean.setPosPage("4");
			positionBean.setPosX(370);
			positionBean.setPosY(280);
		}
		if(type==2) {
			positionBean.setPosPage("4");
			positionBean.setPosX(250);
			positionBean.setPosY(280);
			backSignService.pdfSign(signerAutoBean, signPDFFileBean,positionBean);
			positionBean.setPosPage("4");
			positionBean.setPosX(475);
			positionBean.setPosY(280);
			backSignService.pdfSign(signerAutoBean, signPDFFileBean,positionBean);
			positionBean.setPosPage("1");
			positionBean.setPosX(150);
			positionBean.setPosY(770);
			
		}
		
		logger.info("公司：（{}）（{}）（{}）",accountId,certId,sealId);
		
		FileDigestSignResult fileDigestSignResult = backSignService.pdfSign(signerAutoBean, signPDFFileBean,positionBean);
		logger.info("盖章返回：{}/{}/{}",fileDigestSignResult.getMsg(),fileDigestSignResult.getErrCode(),fileDigestSignResult.getSignServiceId());
		
		
		signServiceId=fileDigestSignResult.getSignServiceId();//签署记录id
		
		return signServiceId;
	}
}
