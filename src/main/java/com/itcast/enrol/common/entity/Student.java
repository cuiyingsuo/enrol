package com.itcast.enrol.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "enrol_student")
public class Student {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学员名称
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否重读(0:否，1:是)
     */
    @Column(name = "is_again")
    private Byte isAgain;

    /**
     * 学员状态(0-在校，1-已毕业，2-在职，3-培训中，4-其它)
     */
    private Byte status;

    /**
     * 是否院校(0:否，1:是)
     */
    @Column(name = "is_academy")
    private Byte isAcademy;

    /**
     * 性别(0-男，1-女)
     */
    private Byte gender;

    /**
     * 手机号
     */
    @Pattern(regexp="(\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$",message = "请填写正确的手机号!")
    private String mobile;

    /**
     * QQ号
     */
    private String qq;

    /**
     * 邮箱
     */
    //@Email(message = "请输入正确的邮箱!")
    private String email;

    /**
     * 是否住宿(0:否,1:是)
     */
    @Column(name = "is_quarter")
    private Byte isQuarter;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "pre_come_time")
    private Date preComeTime;

    /**
     * 毕业时间
     */
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    @Column(name = "graduation_time")
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
    @Column(name = "card_no")
    private String cardNo;

    /**
     * 证件地址
     */
    @Column(name = "card_addr")
    private String cardAddr;

    /**
     * 居住地
     */
    @Column(name = "live_addr")
    private String liveAddr;

    /**
     * 详细地址
     */
    @Column(name = "live_addr_detail")
    private String liveAddrDetail;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系人的联系方式
     */
    @Column(name = "contacts_mobile")
    private String contactsMobile;

    /**
     * 来源渠道Code
     */
    @Column(name = "from_channel_code")
    private String fromChannelCode;

    /**
     * 来源渠道
     */
    @Column(name = "from_channel")
    private String fromChannel;

    /**
     * 出生日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    private Date birthday;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 编辑者
     */
    private String editor;

    /**
     * 编辑时间
     */
    @Column(name = "edit_time")
    private Long editTime;

    /**
     * 头像
     */
    private String image;

    /**
     * 通联支付注册的会员id
     */
    @Column(name = "allinpay_userid")
    private String allinpayUserid;


    /**
     * 是否激活
     */
    @Column(name = "is_actived")
    private Integer isActived;
    
    /**
     * 天印账户id
     */
    @Column(name = "ty_accountId")
    private String tyAccountId;
    /**
     * ca证书id
     */
    @Column(name = "ty_certId")
    private String tyCertId;
    
    /**
     * 天印印章id
     * @return
     */
    @Column(name = "ty_sealId")
    private String tySealId;
    
    
    
	public String getTySealId() {
		return tySealId;
	}

	public void setTySealId(String tySealId) {
		this.tySealId = tySealId;
	}

	public String getTyAccountId() {
		return tyAccountId;
	}

	public void setTyAccountId(String tyAccountId) {
		this.tyAccountId = tyAccountId;
	}

	public String getTyCertId() {
		return tyCertId;
	}

	public void setTyCertId(String tyCertId) {
		this.tyCertId = tyCertId;
	}

	/**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取学员名称
     *
     * @return name - 学员名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置学员名称
     *
     * @param name 学员名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取是否重读(0:否，1:是)
     *
     * @return is_again - 是否重读(0:否，1:是)
     */
    public Byte getIsAgain() {
        return isAgain;
    }

    /**
     * 设置是否重读(0:否，1:是)
     *
     * @param isAgain 是否重读(0:否，1:是)
     */
    public void setIsAgain(Byte isAgain) {
        this.isAgain = isAgain;
    }

    /**
     * 获取学员状态(0-在校，1-已毕业，2-在职，3-培训中，4-其它)
     *
     * @return status - 学员状态(0-在校，1-已毕业，2-在职，3-培训中，4-其它)
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置学员状态(0-在校，1-已毕业，2-在职，3-培训中，4-其它)
     *
     * @param status 学员状态(0-在校，1-已毕业，2-在职，3-培训中，4-其它)
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取是否院校(0:否，1:是)
     *
     * @return is_academy - 是否院校(0:否，1:是)
     */
    public Byte getIsAcademy() {
        return isAcademy;
    }

    /**
     * 设置是否院校(0:否，1:是)
     *
     * @param isAcademy 是否院校(0:否，1:是)
     */
    public void setIsAcademy(Byte isAcademy) {
        this.isAcademy = isAcademy;
    }

    /**
     * 获取性别(0-男，1-女)
     *
     * @return gender - 性别(0-男，1-女)
     */
    public Byte getGender() {
        return gender;
    }

    /**
     * 设置性别(0-男，1-女)
     *
     * @param gender 性别(0-男，1-女)
     */
    public void setGender(Byte gender) {
        this.gender = gender;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取QQ号
     *
     * @return qq - QQ号
     */
    public String getQq() {
        return qq;
    }

    /**
     * 设置QQ号
     *
     * @param qq QQ号
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取是否住宿(0:否,1:是)
     *
     * @return is_quarter - 是否住宿(0:否,1:是)
     */
    public Byte getIsQuarter() {
        return isQuarter;
    }

    /**
     * 设置是否住宿(0:否,1:是)
     *
     * @param isQuarter 是否住宿(0:否,1:是)
     */
    public void setIsQuarter(Byte isQuarter) {
        this.isQuarter = isQuarter;
    }

    /**
     * @return pre_come_time
     */
    public Date getPreComeTime() {
        return preComeTime;
    }

    /**
     * @param preComeTime
     */
    public void setPreComeTime(Date preComeTime) {
        this.preComeTime = preComeTime;
    }

    /**
     * 获取毕业时间
     *
     * @return graduation_time - 毕业时间
     */
    public Date getGraduationTime() {
        return graduationTime;
    }

    /**
     * 设置毕业时间
     *
     * @param graduationTime 毕业时间
     */
    public void setGraduationTime(Date graduationTime) {
        this.graduationTime = graduationTime;
    }

    /**
     * 获取毕业学校
     *
     * @return school - 毕业学校
     */
    public String getSchool() {
        return school;
    }

    /**
     * 设置毕业学校
     *
     * @param school 毕业学校
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * 获取学历(0-本科以上，1-本科，2-大专，3-高中，4-初中，5-初中以下）
     *
     * @return education - 学历(0-本科以上，1-本科，2-大专，3-高中，4-初中，5-初中以下）
     */
    public Byte getEducation() {
        return education;
    }

    /**
     * 设置学历(0-本科以上，1-本科，2-大专，3-高中，4-初中，5-初中以下）
     *
     * @param education 学历(0-本科以上，1-本科，2-大专，3-高中，4-初中，5-初中以下）
     */
    public void setEducation(Byte education) {
        this.education = education;
    }

    /**
     * 获取专业
     *
     * @return major - 专业
     */
    public String getMajor() {
        return major;
    }

    /**
     * 设置专业
     *
     * @param major 专业
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * 获取证件号码
     *
     * @return card_no - 证件号码
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * 设置证件号码
     *
     * @param cardNo 证件号码
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * 获取证件地址
     *
     * @return card_addr - 证件地址
     */
    public String getCardAddr() {
        return cardAddr;
    }

    /**
     * 设置证件地址
     *
     * @param cardAddr 证件地址
     */
    public void setCardAddr(String cardAddr) {
        this.cardAddr = cardAddr;
    }

    /**
     * 获取居住地
     *
     * @return live_addr - 居住地
     */
    public String getLiveAddr() {
        return liveAddr;
    }

    /**
     * 设置居住地
     *
     * @param liveAddr 居住地
     */
    public void setLiveAddr(String liveAddr) {
        this.liveAddr = liveAddr;
    }

    /**
     * 获取详细地址
     *
     * @return live_addr_detail - 详细地址
     */
    public String getLiveAddrDetail() {
        return liveAddrDetail;
    }

    /**
     * 设置详细地址
     *
     * @param liveAddrDetail 详细地址
     */
    public void setLiveAddrDetail(String liveAddrDetail) {
        this.liveAddrDetail = liveAddrDetail;
    }

    /**
     * 获取联系人
     *
     * @return contacts - 联系人
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 设置联系人
     *
     * @param contacts 联系人
     */
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    /**
     * 获取联系人的联系方式
     *
     * @return contacts_mobile - 联系人的联系方式
     */
    public String getContactsMobile() {
        return contactsMobile;
    }

    /**
     * 设置联系人的联系方式
     *
     * @param contactsMobile 联系人的联系方式
     */
    public void setContactsMobile(String contactsMobile) {
        this.contactsMobile = contactsMobile;
    }

    /**
     * 获取来源渠道Code
     *
     * @return from_channel_code - 来源渠道Code
     */
    public String getFromChannelCode() {
        return fromChannelCode;
    }

    /**
     * 设置来源渠道Code
     *
     * @param fromChannelCode 来源渠道Code
     */
    public void setFromChannelCode(String fromChannelCode) {
        this.fromChannelCode = fromChannelCode;
    }

    /**
     * 获取来源渠道
     *
     * @return from_channel - 来源渠道
     */
    public String getFromChannel() {
        return fromChannel;
    }

    /**
     * 设置来源渠道
     *
     * @param fromChannel 来源渠道
     */
    public void setFromChannel(String fromChannel) {
        this.fromChannel = fromChannel;
    }

    /**
     * 获取出生日期
     *
     * @return birthday - 出生日期
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 设置出生日期
     *
     * @param birthday 出生日期
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取创建者
     *
     * @return creator - 创建者
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建者
     *
     * @param creator 创建者
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取编辑者
     *
     * @return editor - 编辑者
     */
    public String getEditor() {
        return editor;
    }

    /**
     * 设置编辑者
     *
     * @param editor 编辑者
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * 获取编辑时间
     *
     * @return edit_time - 编辑时间
     */
    public Long getEditTime() {
        return editTime;
    }

    /**
     * 设置编辑时间
     *
     * @param editTime 编辑时间
     */
    public void setEditTime(Long editTime) {
        this.editTime = editTime;
    }

    /**
     * 获取头像
     *
     * @return image - 头像
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置头像
     *
     * @param image 头像
     */
    public void setImage(String image) {
        this.image = image;
    }

    public String getAllinpayUserid() {
        return allinpayUserid;
    }

    public void setAllinpayUserid(String allinpayUserid) {
        this.allinpayUserid = allinpayUserid;
    }


    public Integer getIsActived() {
        return isActived;
    }

    public void setIsActived(Integer isActived) {
        this.isActived = isActived;
    }


}