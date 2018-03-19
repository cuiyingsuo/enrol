package com.itcast.enrol.management.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itcast.enrol.common.annotation.CheckIntValues;
import com.itcast.enrol.common.annotation.Session;
import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.ManageBaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.entity.Student;
import com.itcast.enrol.common.utils.BeanUtils;
import com.itcast.enrol.common.utils.MD5Util;
import com.itcast.enrol.management.service.ManagerClassStudentService;
import com.itcast.enrol.management.service.ManagerOrderMainService;
import com.itcast.enrol.management.service.ManagerStudentService;
import com.itcast.enrol.management.vo.StudentQueryEx;
import com.itcast.enrol.management.vo.UserEx;
import com.itcast.enrol.student.service.plugins.StuContractService;

/**
 * Created by liuzhongshuai on 2017/11/10.
 */
@RestController
@RequestMapping("/managenment/school")
@Validated
public class ManagerStartSchoolController {

    @Autowired
    private ManagerStudentService studentService;

    @Autowired
    private ManagerClassStudentService classStudentService;

    @Autowired
    private ManagerOrderMainService orderMainService;

    @Autowired
    private StuContractService contractService;


    @Value("${enrol.user.login.key}")
    private String key;

    /**
     * 入学管理：查询
     *
     * @param studentQueryEx
     * @return
     */
    @GetMapping("/querySchoolStus")
    public ManageBaseBody<Map> querySchoolStus(@Valid StudentQueryEx studentQueryEx, BindingResult bindingResult) {

        ManageBaseBody<Map> baseBody = new ManageBaseBody<>();
        if (bindingResult.hasErrors()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMsg(bindingResult.getFieldError().getDefaultMessage());
            return baseBody;
        }
        if (StringUtils.isEmpty(studentQueryEx.getStatus())) {
            studentQueryEx.setStatus("3,4,5,6,7,8");
        }
        BasePage<Map> basePage = studentService.queryBeStartSchoolStus(studentQueryEx);

        baseBody.setCode(0);
        baseBody.setCount(basePage.getCount());
        baseBody.setData(basePage.getPageData());
        return baseBody;
    }


    /**
     * 重置学生密码
     *
     * @param id
     * @return
     */
    @PutMapping("/resetPass")
    public BaseBody resetPass(Long id) {

        BaseBody baseBody = new BaseBody();
        if (null == id) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("参数不能为null");
            return baseBody;
        }
        //查询学生
        Student student = studentService.selectByPrimaryKey(id);
        if (null == student) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("查询不到该学员信息!");
            return baseBody;
        }
        //重置密码
        String initPass = "123456";
        String md5Str = MD5Util.encryption(student.getMobile() + initPass + key).trim();

        student.setPassword(md5Str);

        studentService.updateByPk(student);

        baseBody.setMessage("重置密码成功!");
        return baseBody;
    }

    /**
     * 查询学生对应的班级信息
     *
     * @return
     */
    @GetMapping("/getBeSchoolForClass")
    public BaseBody<List<Map>> getBeSchoolForClass(Long studentId) {
        BaseBody<List<Map>> baseBody = new BaseBody();

        if (null == studentId) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("参数不能为null");
            return baseBody;
        }
        List<Map> mapList = classStudentService.getBeSchoolForClass(studentId);
        List<Map> mapResult = mapList.stream().filter(s -> !(String.valueOf(s.get("status")).equals("2") || String.valueOf(s.get("status")).equals("1"))).collect(Collectors.toList());

        baseBody.setContent(mapResult);
        return baseBody;
    }

    /**
     * 批量修改学生状态
     *
     * @param studentId
     * @param classId
     * @return
     */
    @PutMapping("/changeStuStatus")
    public BaseBody changeStu(String studentIds, Long classId, Integer status) {

        BaseBody baseBody = new BaseBody();

        if (null == studentIds || null == classId) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("参数不能为null!");
            return baseBody;
        }
        classStudentService.changeStuStatus(studentIds, classId, status);
        baseBody.setMessage("操作成成功!");
        return baseBody;
    }


    /**
     * 编辑学籍信息
     *
     * @return
     */
    @PutMapping("/middfyStuInfo")
    public BaseBody middfyStuInfo(@Session UserEx userEx, HttpServletRequest request, @Valid Student student, BindingResult bindingResult) {

        BaseBody baseBody = new BaseBody();

        if (bindingResult.hasErrors()) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setCode(1006);
            baseBody.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return baseBody;
        }

        if (student.getId() == null) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("学生id不能为null!");
            return baseBody;
        }
        Student rstuResult = studentService.selectByPrimaryKey(student.getId());
        if (rstuResult == null) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("查询不到该学生信息!");
            return baseBody;
        }
        BeanUtils.copyProperties(student, rstuResult);
        rstuResult.setEditTime(System.currentTimeMillis());
        rstuResult.setEditor(userEx.getName());

        studentService.updateByPk(rstuResult);
        baseBody.setMessage("修改成功!");
        return baseBody;
    }

    /**
     * 查看学生学籍
     *
     * @param id
     * @return
     */
    @GetMapping("/stuDetail")
    public BaseBody<Map> stuDetail(Long studentId) {
        BaseBody<Map> baseBody = new BaseBody<>();
        if (studentId == null) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("参数值不正确!");
            return baseBody;
        }
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("无法查询该学员信息!");
            return baseBody;
        }
        //查询学生班级信息
        List<Map> mapList = classStudentService.getBeSchoolForClass(studentId);

        if (CollectionUtils.isEmpty(mapList)) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            baseBody.setMessage("无法查询该学员报名的班级信息!");
            return baseBody;
        }
        //组织学员状态结果集
        Map<String, String> stuStatus = new HashMap<>(3);
        //得到当前班级对应学生列表
        List<Map> mapListBm = mapList.stream().filter(s -> String.valueOf(s.get("status")).equals("5") || String.valueOf(s.get("status")).equals("4") || String.valueOf(s.get("status")).equals("3")).collect(Collectors.toList());
        //得到留级的学生班级信息
        List<Map> mapListLj = mapList.stream().filter(s -> String.valueOf(s.get("status")).equals("6")).collect(Collectors.toList());
        stuStatus.put("isRepeat", "否");
        mapListBm.stream().forEach(mapBm -> {
            mapListLj.stream().forEach(mapLj -> {
                String subjectIdBm = String.valueOf(mapBm.get("subjectId"));
                String subjectIdLj = String.valueOf(mapLj.get("subjectId"));

                String classTypeBm = String.valueOf(mapBm.get("classTypeCode"));
                String classTypeLj = String.valueOf(mapLj.get("classTypeCode"));

                if (subjectIdBm.equals(subjectIdLj) && classTypeBm.equals(classTypeLj)) {
                    stuStatus.put("isRepeat", "是");
                }
            });
        });
        stuStatus.put("stuStatus", String.valueOf(mapListBm.get(0).get("status")));
        stuStatus.put("startDate", String.valueOf(mapListBm.get(0).get("startDate")));

        List<Map> classMaps = classStudentService.getBeSchoolForClass(studentId);
        List<Map> mapResult = classMaps.stream().filter(s -> !(String.valueOf(s.get("status")).equals("2") || String.valueOf(s.get("status")).equals("1"))).collect(Collectors.toList());

        Map<String, Object> objectMap = new HashMap<>(3);

        objectMap.put("classList", mapResult);
        objectMap.put("studentInfo", student);
        objectMap.put("stuStatus", stuStatus);

        baseBody.setContent(objectMap);
        return baseBody;
    }


    /**
     * 开放、作废合同、申请
     *
     * @param studentId
     * @param classId
     * @return
     */
    @PutMapping("/htHandler")
    public BaseBody htHandler(String studentIds, Long classId, @CheckIntValues(ableNull = false, values = {1, 3}) Integer status) {

        BaseBody baseBody = new BaseBody();

        if (null == studentIds || null == classId) {
            baseBody.setMessage("参数不正确!");
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }

        classStudentService.htOpenOrCloss(studentIds, classId, status);

        baseBody.setMessage("操作成功!");

        return baseBody;
    }

    /**
     * 班级查询学生，通过状态
     *
     * @param classId
     * @param stuStatus
     * @return
     */
    @GetMapping("/showStuByClass")
    public BaseBody showStuByClass(Long classId, @CheckIntValues(ableNull = true, values = {2, 3, 4, 5, 6, 7, 8}) Integer stuStatus, @CheckIntValues(ableNull = true, values = {0, 2}) Integer contractStatus) {
        BaseBody baseBody = new BaseBody();

        if (null == classId) {
            baseBody.setSuccess(ReturnStatus.FAILD);
            return baseBody;
        }
        List<Map> mapList = classStudentService.showStuByClass(classId, stuStatus, contractStatus);

        baseBody.setContent(mapList);
        return baseBody;
    }

}
