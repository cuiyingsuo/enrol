package com.itcast.enrol.management.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.annotation.Session;
import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.base.UserPermiss;
import com.itcast.enrol.common.entity.ClassType;
import com.itcast.enrol.common.entity.Subject;
import com.itcast.enrol.common.utils.BeanUtils;
import com.itcast.enrol.common.utils.MD5Util;
import com.itcast.enrol.common.utils.RedisUtil;
import com.itcast.enrol.common.utils.TokenUtil;
import com.itcast.enrol.management.service.ManagerCampusService;
import com.itcast.enrol.management.service.ManagerClassService;
import com.itcast.enrol.management.service.ManagerClassTypeService;
import com.itcast.enrol.management.service.ManagerDictService;
import com.itcast.enrol.management.service.ManagerSubjectService;
import com.itcast.enrol.management.service.ManagerUserService;
import com.itcast.enrol.management.service.ManagerMarketingService;
import com.itcast.enrol.management.vo.ClassEx;
import com.itcast.enrol.management.vo.UserEx;

/**
 * Created by liuzhongshuai on 2017/10/27.
 */
@RestController
@RequestMapping("/managenment/common")
public class ManagerCommonController {

    @Autowired
    private ManagerSubjectService subjectService;
    @Autowired
    private ManagerCampusService campusService;
    @Autowired
    private ManagerDictService dictService;
    @Autowired
    private ManagerClassService classService;
    @Autowired
    private ManagerMarketingService marketingService;
    @Autowired
    private ManagerUserService userService;
    @Autowired
    private ManagerClassTypeService classTypeService;


    /**
     * 初始化通用查询条件
     *
     * @return
     */
    @GetMapping("/initrerequisiteInfo")
    public BaseBody<Map> initrerequisiteInfo() {
        BaseBody<Map> baseBody = new BaseBody<Map>();
        //查询学科下拉选
        List subjects = subjectService.querySubjectListByStatus((byte) 1);
        //查询校区
        List campuses = campusService.queryCampusListByStatus((byte) 1);
        //查询营销政策
        List<Map<String, String>> marketList = marketingService.marketingDownSelect();

        //班级类型
        Map classTypes = dictService.getDetailType("class_type");
        //授课模式
        Map teachTypes = dictService.getDetailType("teach_type");
        Map stringListMap = new HashMap<String, List<Map<String, Object>>>(16);

        stringListMap.put("marketList", marketList);
        stringListMap.put("subjectList", subjects);
        stringListMap.put("camputList", campuses);
        stringListMap.put("classType", classTypes.get("class_type"));
        stringListMap.put("teachType", teachTypes.get("teach_type"));
        baseBody.setContent(stringListMap);
        return baseBody;
    }

    /**
     * 级联查询班级信息
     *
     * @param classEx
     * @return
     */
    @GetMapping("/queryClassByConditions")
    public BaseBody<List<Map>> queryClassByConditions(ClassEx classEx) {
        BaseBody<List<Map>> baseBody = new BaseBody<>();

        if (StringUtils.isEmpty(classEx.getCampusIds())) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("请选择校区!");
            return baseBody;
        }

        List<Map> result = classService.queryClassByMulitField(classEx);
        baseBody.setContent(result);
        return baseBody;
    }

    /**
     * 根据登入人手机号  查询相应的菜单权限
     *
     * @return
     */
    @GetMapping("/queryMenus")
    public BaseBody<UserPermiss> queryMenus(@Session UserEx userEx) throws Exception {
        BaseBody<UserPermiss> baseBody = new BaseBody<>();

        if (userEx == null) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("参数值不正确!");
            return baseBody;
        }
        String md5MenusStr = MD5Util.encryption(userEx.getMobile() + TokenUtil.PERMISS_SUFFIX);
        byte[] menusObjectStr = (byte[]) RedisUtil.get(md5MenusStr);

        if (null == menusObjectStr || menusObjectStr.length == 0) {
            //重新放菜单到redis
            UserPermiss userPermiss = userService.roleAndMenus(userEx.getId());
            //序列化
            byte[] permsObjectStr = BeanUtils.objectSerialiable(userPermiss);
            RedisUtil.set(md5MenusStr, permsObjectStr, (long) 4000);
            menusObjectStr = permsObjectStr;
        }

        Object obj = BeanUtils.objectDeserialization(menusObjectStr);

        if (obj == null) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("获取菜单失败!");
            return baseBody;
        }
        UserPermiss userPermiss = (UserPermiss) obj;
        userPermiss.setCurrentName(userEx.getName());

        baseBody.setContent(userPermiss);
        return baseBody;
    }

    /**
     * 查询学科通过 授课模式
     *
     * @param teachModelCode
     * @return
     */
    @GetMapping("/querySubjectByTeachModel")
    public BaseBody querySubjectByTeachModel(@RequestParam(defaultValue = "") String teachModelCode) {
        BaseBody baseBody = new BaseBody<>();

        if (StringUtils.isEmpty(teachModelCode)) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }
        Subject subjectQuery = new Subject();
        subjectQuery.setTeachModeCode(teachModelCode);

        List<Subject> subjectList = subjectService.select(subjectQuery);

        if (null == subjectList) {
            subjectList = new ArrayList<>();
        }
        baseBody.setContent(subjectList);
        return baseBody;
    }


    /**
     * 通过学科编码查询班级类型
     *
     * @param subjectCode
     * @return
     */
    @GetMapping("/queryClassTypeBySubject")
    public BaseBody queryClassTypeBySubject(@RequestParam(defaultValue = "") String subjectCode) {

        BaseBody baseBody = new BaseBody();

        if (StringUtils.isEmpty(subjectCode)) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }
        ClassType classTypeQuery = new ClassType();
        classTypeQuery.setSubjectId(subjectCode);

        List<ClassType> classTypeList = classTypeService.select(classTypeQuery);

        if (null == classTypeList) {
            classTypeList = new ArrayList<>();
        }
        baseBody.setContent(classTypeList);
        return baseBody;
    }




}
