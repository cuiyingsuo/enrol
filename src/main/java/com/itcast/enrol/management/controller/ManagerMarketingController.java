package com.itcast.enrol.management.controller;

import com.itcast.enrol.common.annotation.CheckIntValues;
import com.itcast.enrol.common.annotation.Session;
import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.ManageBaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.entity.Marketing;
import com.itcast.enrol.management.service.ManagerCampusService;
import com.itcast.enrol.management.service.ManagerMarketingService;
import com.itcast.enrol.management.service.ManagerSubjectService;
import com.itcast.enrol.management.vo.UserEx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzhongshuai on 2017/11/8.
 */
@Validated
@RestController
@RequestMapping("/managenment/marketing")
public class ManagerMarketingController {

    @Autowired
    private ManagerMarketingService marketingService;

    @Autowired
    private ManagerCampusService campusService;

    @Autowired
    private ManagerSubjectService subjectService;


    /**
     * 分页查询营销政策
     *
     * @return
     */
    @GetMapping("/queryMarketing")
    public ManageBaseBody<Map> queryMarketing(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {

        ManageBaseBody<Map> mapManageBaseBody = new ManageBaseBody<>();

        BasePage basePage = marketingService.queryMarking(page, limit);

        mapManageBaseBody.setData(basePage.getPageData());
        mapManageBaseBody.setCount(basePage.getCount());
        mapManageBaseBody.setCode(0);
        return mapManageBaseBody;
    }

    /**
     * 添加、编辑
     *
     * @param marketing
     * @return
     */
    @PostMapping("/mergeMarketing")
    public BaseBody mergeMarketing(@Session UserEx userEx, @Valid Marketing marketing, BindingResult bindingResult) {
        BaseBody baseBody = new BaseBody();
        if (bindingResult.hasErrors()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return baseBody;
        }

        marketing.setReductionPrice(marketing.getReductionPrice() * 100);
        marketingService.mergeMarketing(marketing, userEx);
        baseBody.setMessage("保存成功!");
        return baseBody;
    }

    /**
     * 添加活动表单 初始化信息
     *
     * @return
     */
    @GetMapping("/marketInit")
    public BaseBody marketInit() {
        BaseBody baseBody = new BaseBody();
        List campusArray = campusService.queryCampusListByStatus((byte) 1);
        //查询学科全集
        List subjectArray = subjectService.querySubjectListByStatus((byte) 1);

        Map result = new HashMap();
        result.put("campusArray", campusArray);
        result.put("subjectArray", subjectArray);

        baseBody.setContent(result);
        return baseBody;
    }

    /**
     * 查询 活动
     *
     * @param id
     * @return
     */
    @GetMapping("/marketDetail")
    public BaseBody marketDetail(Long id) {
        BaseBody baseBody = new BaseBody();

        if (null == id) {
            baseBody.setMessage("参数不正确!");
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }

        Marketing marketing = marketingService.selectByPrimaryKey(id);
        if (null == marketing) {
            baseBody.setMessage("查询不到相应的活动!");
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }

        String[] campusIds = marketing.getCampusIds().split(",");
        String[] campusNames = marketing.getCampusNames().split(",");
        String[] subjectIds = marketing.getSubjectIds().split(",");
        String[] subjectNames = marketing.getSubjectNames().split(",");
        String[] classCodes = marketing.getClassTypeCodes().split(",");
        String[] classNames = marketing.getClassTypeNames().split(",");
        String[] teachCodes = marketing.getTeachModelCodes().split(",");
        String[] teachNames = marketing.getTeachModelNames().split(",");

        List<Map> campusList = new ArrayList<>();
        for (int i = 0; i < campusIds.length; i++) {
            Map<String, String> campuMap = new HashMap<>(2);
            campuMap.put("id", campusIds[i]);
            campuMap.put("name", campusNames[i]);
            campusList.add(campuMap);
        }

        List<Map> subjectList = new ArrayList<>();
        for (int i = 0; i < subjectIds.length; i++) {
            Map<String, String> subjectMap = new HashMap<>(2);
            subjectMap.put("id", subjectIds[i]);
            subjectMap.put("name", subjectNames[i]);
            subjectList.add(subjectMap);
        }

        List<Map> classList = new ArrayList<>();
        for (int i = 0; i < classCodes.length; i++) {
            Map<String, String> classMap = new HashMap<>(2);
            classMap.put("code", classCodes[i]);
            classMap.put("name", classNames[i]);
            classList.add(classMap);
        }

        List<Map> teachList = new ArrayList<>();
        for (int i = 0; i < teachCodes.length; i++) {
            Map<String, String> teachMap = new HashMap<>(2);
            teachMap.put("code", teachCodes[i]);
            teachMap.put("name", teachNames[i]);
            teachList.add(teachMap);
        }
        //查询校区全集
        List campusArray = campusService.queryCampusListByStatus((byte) 1);
        //查询学科全集
        List subjectArray = subjectService.querySubjectListByStatus((byte) 1);

        Map result = new HashMap();

        result.put("market", marketing);
        result.put("campus", campusList);
        result.put("subjects", subjectList);
        result.put("classTypes", classList);
        result.put("teachModes", teachList);
        result.put("campusArray", campusArray);
        result.put("subjectArray", subjectArray);
        baseBody.setContent(result);

        return baseBody;
    }

    /**
     * 删除营销
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delMarketing")
    public BaseBody delMarketing(Long id) {
        BaseBody baseBody = new BaseBody();

        if (null == id) {
            baseBody.setMessage("参数值不正确!");
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }
        Marketing marketing = new Marketing();
        marketing.setId(id);
        marketing.setIsDel(0);

        marketing = marketingService.selectOne(marketing);
        if (null == marketing) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("查询不到要删除的数据!");
            return baseBody;
        }
        marketing.setIsDel(1);

        marketingService.updateByPk(marketing);

        baseBody.setMessage("删除成功!");
        return baseBody;
    }


    /**
     * 启用或停用营销
     *
     * @return
     */
    @PutMapping("/enableMarketingOrNo")
    public BaseBody enableMarketingOrNo(Long id, @CheckIntValues(ableNull = false, values = {0, 1}) Integer status) {
        BaseBody baseBody = new BaseBody();

        if (id == null || status == null) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("参数值不正确!");
            return baseBody;
        }
        Marketing marketing = marketingService.selectByPrimaryKey(id);
        if (marketing == null) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("查询不到要更改的数据!");
            return baseBody;
        }
        //保证密等
        if (marketing.getStatus().intValue() == status.intValue()) {
            baseBody.setMessage("修改成功!");
            return baseBody;
        }
        marketing.setStatus(status);

        marketingService.updateByPk(marketing);

        baseBody.setMessage("修改成功!");
        return baseBody;
    }


}
