package com.itcast.enrol.management.controller;

import com.itcast.enrol.common.annotation.CheckIntValues;
import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.entity.Subject;
import com.itcast.enrol.management.service.ManagerSubjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/10/24.
 */
@RestController
@RequestMapping("/managenment/subject")
@Validated
public class ManagerSubjectController {

    @Autowired
    private ManagerSubjectService managerSubjectService;


    /**
     * 学科查询
     *
     * @param status
     * @return
     *
     */
    @GetMapping("/queryList")
    public BaseBody<List<Subject>> querySubjectList(@CheckIntValues(values = {0, 1}, ableNull = false, message = "不支持的参数值") Integer status) {

        BaseBody<List<Subject>> baseBody = new BaseBody<>();

        List<Subject> subjects = null;
        //查询所有
        if (null == status) {
            subjects = managerSubjectService.selectAll();
        } else {
            //查询相应状态的学科列表
            Subject subject = new Subject();
            subject.setStatus(status.byteValue());
            subjects = managerSubjectService.select(subject);
        }
        baseBody.setMessage("查询成功!");
        baseBody.setContent(subjects);
        return baseBody;
    }
}
