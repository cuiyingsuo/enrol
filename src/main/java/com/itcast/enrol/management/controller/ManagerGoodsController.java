package com.itcast.enrol.management.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itcast.enrol.common.annotation.Session;
import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.ManageBaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.entity.Department;
import com.itcast.enrol.common.entity.Goods;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.entity.Subject;
import com.itcast.enrol.common.entity.User;
import com.itcast.enrol.common.utils.FileUtils;
import com.itcast.enrol.common.utils.HttpHelper;
import com.itcast.enrol.common.utils.HttpUtil;
import com.itcast.enrol.common.utils.JsonUtil;
import com.itcast.enrol.common.utils.RedisUtil;
import com.itcast.enrol.management.service.ManagerCampusService;
import com.itcast.enrol.management.service.ManagerDepartmentService;
import com.itcast.enrol.management.service.ManagerDictService;
import com.itcast.enrol.management.service.ManagerGoodsService;
import com.itcast.enrol.management.service.ManagerMarketingService;
import com.itcast.enrol.management.service.ManagerOrderMainService;
import com.itcast.enrol.management.service.ManagerSubjectService;
import com.itcast.enrol.management.service.ManagerUserService;
import com.itcast.enrol.management.vo.GoodsEx;
import com.itcast.enrol.management.vo.StuGoodsEx;
import com.itcast.enrol.management.vo.UserEx;

/**
 * @author Created by liuzhongshuai on 2017/10/23.
 */
@RestController
@RequestMapping("/managenment/goods")
public class ManagerGoodsController {

	private Logger logger = LoggerFactory.getLogger("enrol");
	
    @Autowired
    private ManagerGoodsService managerGoodsService;

    @Autowired
    private ManagerDictService dictService;

    @Autowired
    private ManagerSubjectService subjectService;

    @Autowired
    private ManagerOrderMainService orderMainService;

    @Autowired
    private ManagerCampusService campusService;

    @Autowired
    private ManagerMarketingService marketingService;

    @Autowired
    private ManagerDepartmentService departmentService;

    @Autowired
    private ManagerUserService userService;

    @Value("${file.goodsImg.basePath}")
    private String goodsImgPath;

    @Value("${file.goodsImg.goodsUri}")
    private String goodsImgUri;

    @Value("${enrol.mobile.loginUrl}")
    private String mobileLoginUrl;

    @Value("${ems.stu-get-url}")
    private String stuGetUrl;

    @Value("${ems.api.appid}")
    private String emsAppId;

    @Value("${ems.api.key}")
    private String emsKey;




    @GetMapping("/getStuInfoByEms")
    public BaseBody getStuInfoByEms(String mobile, String classId) throws Exception {
        BaseBody baseBody = new BaseBody();
        //组织参数
        Map<String, String> param = new LinkedHashMap<>(6);
        param.put("cid", emsAppId);
        param.put("method", "findByPhone");
        param.put("phone", mobile);
        param.put("ts", String.valueOf(System.currentTimeMillis()));
        //生成签名
        HttpHelper.handlerSigUrlByMd5(param, emsKey);
        
        //发送请求，得到结果
        String result = HttpUtil.httpPost(stuGetUrl, param);
        logger.info("获取学生班级信息，ems返回：{}", result);
        Map<String, Object> resultMap = JsonUtil.jsonToObject(result, Map.class);
        if (null == resultMap || String.valueOf(resultMap.get("success")).equals("false")) {
            baseBody.setMessage("ems 查询失败，或不存在该学员信息!");
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }
        Map<String, Object> childMap = (Map) resultMap.get("resultObject");
        String classIds = String.valueOf(childMap.get("classIds"));
        if (classIds.indexOf(classId) <= -1) {
            baseBody.setMessage("报名班级不匹配!");
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }
        //咨询老师
        int jobNo = (Integer)childMap.get("logerJobNumber");
        String logerRealename = String.valueOf(childMap.get("logerRealename"));
        try{
	        if(0==jobNo){
	        	User user = new User();
	        	user.setName(logerRealename);
	        	user = userService.selectOne(user);
	        	childMap.put("logerId", user.getId());
	        	childMap.put("logerRealename", user.getName());
	        }else{
	        	User user = new User();
	        	user.setJobNo((Integer)jobNo);
	        	user = userService.selectOne(user);
	        	childMap.put("logerId", user.getId());
	        	childMap.put("logerRealename", user.getName());
	        }
        }catch(Exception e){
        	logger.error("查询咨询老师信息失败");
        	childMap.put("logerId", 5);
        	childMap.put("logerRealename", "admin");
        }
        //班级金额
        List<Object> depositsList = (List) childMap.get("clazzFees");
        for(Object deposit:depositsList){
        	Map<String,Object> depositMap = (Map)deposit;
        	if(classId.equals(String.valueOf(depositMap.get("clazzId")))){
        		childMap.put("deposit", depositMap.get("deposit"));
        		childMap.put("tuition", depositMap.get("tuition"));
        		break;
        	}
        }
        //将学生班级关联信息存入redis，下订单时使用
        RedisUtil.set(mobile+classId, childMap, 60 * 60);
        childMap.remove("clazzFees");
        baseBody.setContent(childMap);
        return baseBody;
    }



    /**
     * 商品 生成订单
     *
     * @param stuGoodsEx
     * @return
     */
    @PostMapping("/genOrderMain")
    public BaseBody genOrderMain(HttpServletRequest request, @Valid StuGoodsEx stuGoodsEx, BindingResult bindingResult, @Session UserEx userEx) {
        BaseBody baseBody = new BaseBody();
        if (bindingResult.hasErrors()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return baseBody;
        }
        if (stuGoodsEx.getCreatorId() == null) {
            stuGoodsEx.setCreator(userEx.getName());
            stuGoodsEx.setCreatorId(userEx.getId());
        }
        OrderMain result = orderMainService.generateOrder(stuGoodsEx);
        if (null == result) {
            baseBody.setCode(1005);
            baseBody.setMessage("订单生成失败");
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }
        baseBody.setMessage("订单生成成功!");
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("loginUrl", mobileLoginUrl);
        resultMap.put("account", stuGoodsEx.getStuMobile());
        baseBody.setContent(resultMap);
        return baseBody;
    }


    /**
     * 新增商品
     *
     * @param goodsEx
     * @return
     */
    @PostMapping("/addGoods")
    public BaseBody<Goods> addGoods(@Session UserEx userEx, HttpServletRequest request, @Valid GoodsEx goodsEx, BindingResult bindingResult, @RequestParam("coverImg") MultipartFile coverImg, @RequestParam("detailImg") MultipartFile detailImg) throws IOException {

        BaseBody<Goods> baseBody = new BaseBody<Goods>();
        if (bindingResult.hasErrors()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return baseBody;
        }
        if (null == coverImg || null == coverImg.getOriginalFilename()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("请上传封面图片");
            return baseBody;
        }
        if (null == detailImg || null == detailImg.getOriginalFilename()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("请上传商品详情图片!");
            return baseBody;
        }
        //创建本地文件
        String prefx = "/goods_";
        String coverImgName = coverImg.getOriginalFilename();
        String coverImguffix = coverImgName.substring(coverImgName.lastIndexOf("."));
        String fileName = String.valueOf(System.currentTimeMillis());
        prefx = prefx + "01";
        FileUtils.createFile(goodsImgPath, prefx + fileName + coverImguffix, coverImg.getBytes());
        goodsEx.setCoverImgUrl(goodsImgUri + prefx + fileName + coverImguffix);


        String detailImgName = detailImg.getOriginalFilename();
        String detailImgSuffix = detailImgName.substring(detailImgName.lastIndexOf("."));
        prefx = prefx + "02";
        FileUtils.createFile(goodsImgPath, prefx + fileName + detailImgSuffix, detailImg.getBytes());
        goodsEx.setDetailImgUrl(goodsImgUri + prefx + fileName + detailImgSuffix);

        goodsEx.setCreator(userEx.getName());
        goodsEx.setPrice(goodsEx.getPrice().intValue() * 100);
        goodsEx.setOtherExpense(goodsEx.getOtherExpense().intValue() * 100);

        managerGoodsService.batchAddGoodsClass(goodsEx);
        baseBody.setMessage("保存成功!");
        return baseBody;
    }


    /**
     * 编辑商品
     *
     * @param goodsEx
     * @return
     */
    @PostMapping("/editGoods")
    public BaseBody<Goods> editGoods(@Session UserEx userEx, HttpServletRequest request, @Valid GoodsEx goodsEx, BindingResult bindingResult, @RequestParam(value = "coverImg", required = false) MultipartFile coverImg, @RequestParam(value = "detailImg", required = false) MultipartFile detailImg) throws IOException {

        BaseBody<Goods> baseBody = new BaseBody<Goods>();
        if (bindingResult.hasErrors()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return baseBody;
        }
        //查询商品
        Goods goods = managerGoodsService.selectByPrimaryKey(goodsEx.getId());

        if (!(null == coverImg || null == coverImg.getOriginalFilename())) {
            FileUtils.createFile(goodsImgPath, goods.getCoverImgUrl().substring(goods.getCoverImgUrl().lastIndexOf("/") + 1), coverImg.getBytes());
        }
        if (!(null == detailImg || null == detailImg.getOriginalFilename())) {
            FileUtils.createFile(goodsImgPath, goods.getDetailImgUrl().substring(goods.getDetailImgUrl().lastIndexOf("/") + 1), detailImg.getBytes());
        }
        goodsEx.setCreator(userEx.getName());
        goodsEx.setPrice(goodsEx.getPrice() * 100);
        goodsEx.setOtherExpense(goodsEx.getOtherExpense().intValue() * 100);

        managerGoodsService.editGoodsClass(goodsEx);
        baseBody.setMessage("保存成功!");
        return baseBody;
    }


    /**
     * 商品管理，查询商品列表 分页
     *
     * @param conditions
     * @return
     */
    @GetMapping("/queryGoodsList")
    public ManageBaseBody queryGoodsList(@RequestParam Map<String, String> conditions, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) throws ParseException {

        ManageBaseBody basePageBaseBody = new ManageBaseBody();

        if (null == conditions || conditions.size() == 0) {
            conditions = new HashMap<String, String>(16);
        }
        BasePage<Map<String, Object>> basePage = managerGoodsService.queryByConditions(page, limit, conditions);

        marketingService.goodsMarket(basePage.getPageData());

        basePageBaseBody.setCode(0);
        basePageBaseBody.setCount(basePage.getCount());
        basePageBaseBody.setData(basePage.getPageData());
        basePageBaseBody.setSuccess(ReturnStatus.SUCCESS);
        return basePageBaseBody;
    }

    /**
     * 查询部门下人员
     *
     * @param departCode
     * @return
     */
    @GetMapping("/queryUsersBydepart")
    public BaseBody queryUsersBydepart(String departCode) {
        BaseBody baseBody = new BaseBody();
        if (org.springframework.util.StringUtils.isEmpty(departCode)) {

            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("参数值为空!");
            return baseBody;
        }
        Department departmentQuery = new Department();
        departmentQuery.setDepartmentCode(departCode);

        Department department = departmentService.selectOne(departmentQuery);

        if (null == department) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("查询不到相关部门!");
            return baseBody;
        }

        //通过部门查询人员
        User userQuery = new User();
        userQuery.setDepartmentId(department.getId());
        List<User> users = userService.select(userQuery);

        baseBody.setContent(users);
        return baseBody;
    }

    /**
     * 商品上下架
     *
     * @param goodsId
     * @return
     */
    @DeleteMapping("/upOrDownGoods")
    public BaseBody upOrDownGoods(@RequestParam(defaultValue = "0") String goodsId, @RequestParam(defaultValue = "0") String status) {
        BaseBody baseBody = new BaseBody();
        if ("0".equals(goodsId)) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("商品编号不正确!");
            return baseBody;
        }
        //验证参数值范围
        if (!("0".equals(status) || "1".equals(status))) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("参数值不正确!");
            return baseBody;
        }
        //查询商品是否存在
        Goods goodsTmp = managerGoodsService.selectByPrimaryKey(Long.parseLong(goodsId));
        if (null == goodsTmp) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("未能查询到该商品!");
            return baseBody;
        }
        if (goodsTmp.getIsDel() == 1) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("该商品已经被删除!");
            return baseBody;
        }
        if (goodsTmp.getStatus() == Byte.valueOf(status).byteValue()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("已经是目标状态!");
            return baseBody;
        }
        goodsTmp.setStatus(Byte.valueOf(status).byteValue());
        goodsTmp.setEditTime(System.currentTimeMillis());
        managerGoodsService.updateByPk(goodsTmp);

        baseBody.setMessage("操作成功!");
        return baseBody;

    }

    /**
     * 通过商品编号删除 商品
     *
     * @param goodsNo
     * @return
     */
    @DeleteMapping("/delGoods")
    public BaseBody delGoods(@RequestParam(defaultValue = "0") String goodsId) {
        BaseBody baseBody = new BaseBody();
        if ("0".equals(goodsId)) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("商品编号不正确!");
            return baseBody;
        }
        //查询商品是否存在
        Goods goodsTmp = managerGoodsService.selectByPrimaryKey(Long.parseLong(goodsId));
        if (null == goodsTmp) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("未能查询到该商品!");
            return baseBody;
        }
        if (goodsTmp.getStatus().byteValue() == 1) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("只能删除已下架的商品!");
            return baseBody;
        }
        if (goodsTmp.getIsDel() == 1) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage("该商品已经被删除!");
            return baseBody;
        }
        goodsTmp.setIsDel((byte) 1);
        managerGoodsService.updateByPk(goodsTmp);

        baseBody.setMessage("删除成功!");
        return baseBody;
    }

    /**
     * 商品详情
     *
     * @param goodsId
     * @return
     */
    @GetMapping("/goodsDetail")
    public BaseBody<Map<String, String>> goodsDetail(Long goodsId) {

        BaseBody<Map<String, String>> baseBody = new BaseBody();
        if (null == goodsId) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("参数不正确!");
            return baseBody;
        }
        Map<String, String> goodsDetail = managerGoodsService.getGoodsDetail(goodsId);

        Subject subjectQuery = new Subject();
        subjectQuery.setId(Long.parseLong(String.valueOf(goodsDetail.get("subjectId"))));

        Subject subject = subjectService.selectOne(subjectQuery);
        goodsDetail.put("subjectCode", subject.getCode());

        baseBody.setContent(goodsDetail);
        return baseBody;
    }

}
