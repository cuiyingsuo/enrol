package com.itcast.enrol.common.dao.student;

import java.util.List;
import java.util.Map;

import com.itcast.enrol.common.base.BaseDao;
import com.itcast.enrol.common.entity.ClassStudent;
import com.itcast.enrol.management.vo.StuClassDetailVo;
import com.itcast.enrol.management.vo.StudentQueryEx;

/**
 * EnrolClassStudentMapper数据库操作接口类
 **/
public interface ClassStudentMapper extends BaseDao<ClassStudent> {


    /**
     * 待入学 学生查询
     *
     * @param studentQueryEx
     * @return
     */
    List<Map> queryBeStartSchoolStus(StudentQueryEx studentQueryEx);

    /**
     * 查询待入学学生对应的班级信息
     *
     * @param studentId
     * @return
     */
    List<Map> getBeSchoolForClass(Long studentId);

    /**
     * 查询学生班级详情
     *
     * @param map
     * @return
     */
    List<StuClassDetailVo> stuClassDetail(Map<String, Long> map);


    /**
     * 通过班级查询学生
     *
     * @param classId
     * @return
     */
    List<Map> showStuByClass(Long classId);

    /**
     * 通过状态查询合同
     * @param studentQueryEx
     * @return
     */
    List<Map<String,Object>> queryContractByStatus(StudentQueryEx studentQueryEx);


}