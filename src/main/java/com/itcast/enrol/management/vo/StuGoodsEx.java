package com.itcast.enrol.management.vo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/10/26.
 *         对应 后台 下订单 表单
 */
public class StuGoodsEx {


    /**
     * 学生Id
     */
    @NotNull(message = "缺少学生Id!")
    private Long studentId;

    /**
     *
     */
    private Long goodsId;

    /**
     * 班级id
     */
    private Long classId;

    /**
     * 学生姓名
     */
    @NotEmpty(message = "请填写学生姓名!")
    private String stuName;

    /**
     * 学生身份证号
     */
    //@NotEmpty(message = "请填写学生身份证信息!")
    private String cardNo;

    /**
     * 创建人Id
     */
    private Long creatorId;

    /**
     * 付款方式
     */
    private String payType;

    /**
     * 是否补录订单
     */
    private Byte isAft;

    /**
     * 支付金额
     */
    private String price = "0";

    /**
     * 创建人信息
     */
    private String creator;


    public String getCampusCode() {
        return campusCode;
    }

    public void setCampusCode(String campusCode) {
        this.campusCode = campusCode;
    }

    /**
     * 收款校区
     */
    private String campusCode;

    /**
     * 学生手机号
     */
    @NotEmpty(message = "请填写手机号!")
    @Pattern(regexp = "(\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$", message = "请填写正确的手机号!")
    private String stuMobile;
    
    /**
     * 押金
     */
    private int deposit;
    

    public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuMobile() {
        return stuMobile;
    }

    public void setStuMobile(String stuMobile) {
        this.stuMobile = stuMobile;
    }

    public Byte getIsAft() {
        return isAft;
    }

    public void setIsAft(Byte isAft) {
        this.isAft = isAft;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }


}
