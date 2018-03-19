package com.itcast.enrol.student.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.contract.ContractMapper;
import com.itcast.enrol.common.dao.order.OrderMainMapper;
import com.itcast.enrol.common.dao.student.ClassStudentMapper;
import com.itcast.enrol.common.entity.ClassStudent;
import com.itcast.enrol.common.entity.Contract;
import com.itcast.enrol.common.entity.OrderMain;
import com.itcast.enrol.common.utils.BusLogUtil;
import com.itcast.enrol.management.vo.StuClassDetailVo;
import com.itcast.enrol.management.vo.StudentQueryEx;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 班级与学生关联表 服务类
 **/
@Service
public class ClassStudentService extends BaseService<ClassStudent> {


    private BusLogUtil logger = new BusLogUtil(ClassStudentService.class);

    @Autowired
    private ClassStudentMapper classStudentDao;

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private ContractMapper contractMapper;

    /**
     * 查询待入学学生对应的班级信息
     *
     * @param studentId
     * @return
     */
    public List<Map> getBeSchoolForClass(Long studentId) {

        return classStudentDao.getBeSchoolForClass(studentId);
    }

    /**
     * 通过订单状态修改关联状态
     *
     * @param orderStatus
     * @param classId
     * @param studentId
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateByOrderStatus(int payTimes, int orderStatus, Long classId, Long studentId) {

        ClassStudent classStudent = new ClassStudent();
        classStudent.setClassId(classId);
        classStudent.setStudentId(studentId);
        logger.info("修改学生与班级关联信息：{},{},{},{}", payTimes, orderStatus, classId, studentId);
        classStudent = classStudentDao.select(classStudent).get(0);
        //订单状态与学员状态对应
        switch (orderStatus) {
            case 0:
                //未支付-新注册
                classStudent.setStatus(1);
                break;
            case 1:
                //支付中-报名
                classStudent.setStatus(2);
                //首次支付加入排班时间
                if (payTimes == 1) {
                    classStudent.setSeatTime(System.currentTimeMillis());
                }
                break;
            case 2:
                //支付完成-报名
                classStudent.setStatus(2);
                break;
            default:
                classStudent.setStatus(1);
        }

        classStudentDao.updateByPrimaryKeySelective(classStudent);
    }


    /**
     * 查询学生班级详情
     *
     * @param map
     * @return
     */
    public List<StuClassDetailVo> stuClassDetail(Map<String, Long> map) {
        return classStudentDao.stuClassDetail(map);
    }


    /**
     * 开放或 废弃合同
     *
     * @param studentIds
     * @param classId
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    public void htOpenOrCloss(String studentIds, Long classId, Integer status) {

        String[] studentArry = studentIds.split(",");

        for (String studentId : studentArry) {

            OrderMain orderMain=new OrderMain();
            orderMain.setStudentId(Long.valueOf(studentId));
            orderMain.setClassId(classId);
            orderMain.setIsCancel(0);

            OrderMain orderMainResult = orderMainMapper.selectOne(orderMain);
            if (null == orderMainResult) {
                throw new RuntimeException("查询不到对应的学员订单信息!");
            }
            Contract contract=new Contract();
            contract.setOrderMainNo(orderMainResult.getMergeOrderNo());

            List<Contract> contracts=contractMapper.select(contract);
            contracts.sort((Contract h1, Contract h2) -> h2.getCreateTime().compareTo(h1.getCreateTime()));

            if(CollectionUtils.isEmpty(contracts)){
                throw new RuntimeException("查询不到对应的学员合同信息!");
            }
            Contract contractCurrent=contracts.get(0);
            contractCurrent.setContractStatus(status);
            contractMapper.updateByPrimaryKey(contractCurrent);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeStuStatus(String studentIds, Long classId, Integer status) {
        for (String studentId : studentIds.split(",")) {
            ClassStudent classStudent = new ClassStudent();
            classStudent.setClassId(classId);
            classStudent.setDataStatus(1);
            classStudent.setStudentId(Long.valueOf(studentId));

            ClassStudent classStudentResult = this.selectOne(classStudent);

            if (null == classStudentResult) {
                throw new RuntimeException("查询不到 学员对应的班级，学员Id:" + studentId + ",班级Id:" + classId);
            }

            if(classStudentResult.getStatus().intValue()==status.intValue()){
                break;
            }

            classStudentResult.setStatus(status);
            this.updateByPk(classStudentResult);
        }
    }


    /**
     * 通过班级 展示学员
     *
     * @param classId
     * @param stuStatus
     * @return
     */
    public List<Map> showStuByClass(Long classId, Integer stuStatus, Integer contractStatus) {

        List<Map> mapList = classStudentDao.showStuByClass(classId);

        if (!CollectionUtils.isEmpty(mapList) && null != stuStatus) {
            mapList = mapList.stream().filter(s -> String.valueOf(s.get("status")).equals(stuStatus.toString())).collect(Collectors.toList());
        }
        if (contractStatus != null) {
            mapList = mapList.stream().filter(s -> String.valueOf(s.get("contractStatus")).equals(contractStatus.toString())).collect(Collectors.toList());
        }

        return mapList;
    }

    /**
     * 通过状态查询合同信息
     *
     * @param studentQueryEx
     * @return
     */
    public BasePage queryContractByStatus(StudentQueryEx studentQueryEx) {

        PageHelper.startPage(studentQueryEx.getPage(), studentQueryEx.getLimit());
        List<Map<String,Object>> mapList = classStudentDao.queryContractByStatus(studentQueryEx);

        PageInfo pageInfo = new PageInfo(mapList);

        BasePage<Map<String, Object>> basePage = new BasePage<>();
        basePage.setPageData(pageInfo.getList());
        basePage.setPageSize(studentQueryEx.getLimit());
        basePage.setCurrentPage(studentQueryEx.getPage());
        basePage.setTotalPage(pageInfo.getPages());
        return basePage;
    }


    /**
     * 通过状态查询合同信息（不分页）
     *
     * @param studentQueryEx
     * @return
     */
    public List<Map<String,Object>> importContract(StudentQueryEx studentQueryEx) {

        return classStudentDao.queryContractByStatus(studentQueryEx);

    }
}
