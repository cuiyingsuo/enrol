package com.itcast.enrol.management.vo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class StudentVo {
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 学员状态(0-在校，1-已毕业，2-在职，3-培训中，4-其它)
	 */
	private Byte status;

	/**
	 * 是否院校(0:否，1:是)
	 */
	private Byte isAcademy;

	/**
	 * 性别(0-男，1-女)
	 */
	private Byte gender;

	/**
	 * QQ号
	 */
	private String qq;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 是否住宿(0:否,1:是)
	 */
	private Byte isQuarter;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date preComeTime;

	/**
	 * 毕业时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM")
	private Date graduationTime;

	/**
	 * 毕业学校
	 */
	private String school;

	/**
	 * 学历(0-本科以上，1-本科，2-大专，3-高中，4-初中，5-初中以下）
	 */
	private Byte education;

	/**
	 * 专业
	 */
	private String major;

	/**
	 * 证件号码
	 */
	private String cardNo;

	/**
	 * 证件地址
	 */
	private String cardAddr;

	/**
	 * 居住地
	 */
	private String liveAddr;

	/**
	 * 详细地址
	 */
	private String liveAddrDetail;

	/**
	 * 联系人
	 */
	private String contacts;

	/**
	 * 联系人的联系方式
	 */
	private String contactsMobile;

	/**
	 * 来源渠道Code
	 */
	private String fromChannelCode;

	/**
	 * 来源渠道
	 */
	private String fromChannel;

	/**
	 * 出生日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	/**
	 * 头像
	 */
	private String image;
	

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getIsAcademy() {
		return isAcademy;
	}

	public void setIsAcademy(Byte isAcademy) {
		this.isAcademy = isAcademy;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Byte getIsQuarter() {
		return isQuarter;
	}

	public void setIsQuarter(Byte isQuarter) {
		this.isQuarter = isQuarter;
	}

	public Date getPreComeTime() {
		return preComeTime;
	}

	public void setPreComeTime(Date preComeTime) {
		this.preComeTime = preComeTime;
	}

	public Date getGraduationTime() {
		return graduationTime;
	}

	public void setGraduationTime(Date graduationTime) {
		this.graduationTime = graduationTime;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public Byte getEducation() {
		return education;
	}

	public void setEducation(Byte education) {
		this.education = education;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardAddr() {
		return cardAddr;
	}

	public void setCardAddr(String cardAddr) {
		this.cardAddr = cardAddr;
	}

	public String getLiveAddr() {
		return liveAddr;
	}

	public void setLiveAddr(String liveAddr) {
		this.liveAddr = liveAddr;
	}

	public String getLiveAddrDetail() {
		return liveAddrDetail;
	}

	public void setLiveAddrDetail(String liveAddrDetail) {
		this.liveAddrDetail = liveAddrDetail;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactsMobile() {
		return contactsMobile;
	}

	public void setContactsMobile(String contactsMobile) {
		this.contactsMobile = contactsMobile;
	}

	public String getFromChannelCode() {
		return fromChannelCode;
	}

	public void setFromChannelCode(String fromChannelCode) {
		this.fromChannelCode = fromChannelCode;
	}

	public String getFromChannel() {
		return fromChannel;
	}

	public void setFromChannel(String fromChannel) {
		this.fromChannel = fromChannel;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
}
