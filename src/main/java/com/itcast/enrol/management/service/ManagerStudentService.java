package com.itcast.enrol.management.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.BaseService;
import com.itcast.enrol.common.dao.order.OrderMainMapper;
import com.itcast.enrol.common.dao.organize.CampusMapper;
import com.itcast.enrol.common.dao.organize.ClassMapper;
import com.itcast.enrol.common.dao.student.ClassStudentMapper;
import com.itcast.enrol.common.dao.student.StudentMapper;
import com.itcast.enrol.common.entity.Student;
import com.itcast.enrol.common.utils.BeanUtils;
import com.itcast.enrol.management.vo.StudentQueryEx;
import com.itcast.enrol.management.vo.StudentVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 学员表 服务类
 **/
@Service
public class ManagerStudentService extends BaseService<Student> {
	// 统一记录日志类
	private Logger logger = LoggerFactory.getLogger("enrol");
	@Autowired
	private StudentMapper studentDao;
	
	@Autowired
	private ClassMapper classDao;
	
	@Autowired
	private CampusMapper campusDao;

	@Autowired
	private ClassStudentMapper classStudentMapper;

	@Autowired
	private OrderMainMapper orderMainDao;

	@Autowired
	private ManagerOrderMainService orderMainService;

	@Value("${file.contract.savePath}")
	private String contractPath;

	/**
	 * 查询学生信息
	 *
	 * @param mobile
	 *            手机号码
	 * @return
	 */
	public Map<String, Object> queryStudentByMobile(String mobile) {
		Student student = new Student();
		student.setMobile(mobile);
		student = studentDao.selectOne(student);

		Map<String, Object> studentMap = BeanUtils.convertBeanToMap(student);
		Date preComeTime = student.getPreComeTime();
		Date birthday = student.getBirthday();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (null != preComeTime) {
			studentMap.put("preComeTime", sdf.format(preComeTime));
		}

		if (null != birthday) {
			studentMap.put("birthday", sdf.format(birthday));
		}

		sdf = new SimpleDateFormat("yyyy-MM");

		Date graduationTime = student.getGraduationTime();
		if (null != graduationTime) {
			studentMap.put("graduationTime", sdf.format(graduationTime));
		}
		return studentMap;
	}

	/**
	 * 检查密码
	 *
	 * @param mobile
	 *            手机号码
	 * @param password
	 *            密码
	 * @return
	 */
	public Student checkPassword(String mobile) {
		Student student = new Student();
		student.setMobile(mobile);
		Student studentInfo = studentDao.selectOne(student);
		return studentInfo;
	}

	/**
	 * 修改密码
	 *
	 * @param mobile
	 *            手机号码
	 * @param password
	 *            新密码
	 * @return
	 */
	@Transactional
	public int modifyPassword(String mobile, String password) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("password", password);
		int rel = studentDao.updatePasswordByMobile(params);
		return rel;
	}

	/**
	 * 修改学生信息
	 *
	 * @param sv
	 *            学生信息封装类
	 * @return
	 */
	@Transactional
	public int updateByMobile(StudentVo sv) {
		Student student = new Student();
		student.setMobile(sv.getMobile());
		student = studentDao.selectOne(student);

		student.setImage(sv.getImage());
		student.setStatus(sv.getStatus());
		student.setGender(sv.getGender());
		student.setQq(sv.getQq());
		student.setEmail(sv.getEmail());
		student.setPreComeTime(sv.getPreComeTime());
		student.setIsQuarter(sv.getIsQuarter());
		student.setLiveAddr(sv.getLiveAddr());
		student.setLiveAddrDetail(sv.getLiveAddrDetail());
		student.setCardNo(sv.getCardNo());
		student.setCardAddr(sv.getCardAddr());
		student.setEducation(sv.getEducation());
		student.setGraduationTime(sv.getGraduationTime());
		student.setSchool(sv.getSchool());
		student.setMajor(sv.getMajor());
		student.setContacts(sv.getContacts());
		student.setContactsMobile(sv.getContactsMobile());
		student.setFromChannel(sv.getFromChannel());
		student.setIsActived(1);
		// student.setBirthday(sv.getBirthday());

		return studentDao.updateByPrimaryKeySelective(student);
	}

	@Transactional
	public int updateByStudent(Student student) {
		return studentDao.updateByPrimaryKeySelective(student);
	}

	/**
	 * 待入学学生查询
	 *
	 * @return
	 */
	public BasePage<Map> queryBeStartSchoolStus(
			StudentQueryEx studentQueryEx) {

		PageHelper.startPage(studentQueryEx.getPage(),
				studentQueryEx.getLimit());

		List resultList = classStudentMapper
				.queryBeStartSchoolStus(studentQueryEx);
		PageInfo<Map> pageInfo = new PageInfo(resultList);

		BasePage<Map> basePage = new BasePage();
		basePage.setPageData(pageInfo.getList());
		basePage.setPageSize(studentQueryEx.getLimit());
		basePage.setCurrentPage(studentQueryEx.getPage());
		basePage.setTotalPage(pageInfo.getPages());
		basePage.setCount(pageInfo.getTotal());

		return basePage;
	}
}
