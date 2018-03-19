package com.itcast.enrol.common.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name = "enrol_goods_hot")
public class GoodsHot {
	/* 主键 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "goods_id")
	private Long goodsId;
	
	private String goodsNo;
	private String name;
	private int price;
	private String coverImg;
	private String synopsis;
	private String detail;
	private int currentNum;


	private int isFree;
	private int isOtherExpense;
	private int otherExpense;
	private String otherExpenseRemark;
	
	private String campusId;
	private String subjectId;
	private String classTypeCode;
	private String teachModeCode;

	private String startDate;
	
	
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getClassTypeCode() {
		return classTypeCode;
	}
	public void setClassTypeCode(String classTypeCode) {
		this.classTypeCode = classTypeCode;
	}
	public String getTeachModeCode() {
		return teachModeCode;
	}
	public void setTeachModeCode(String teachModeCode) {
		this.teachModeCode = teachModeCode;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public int getIsFree() {
		return isFree;
	}
	public void setIsFree(int isFree) {
		this.isFree = isFree;
	}
	public int getIsOtherExpense() {
		return isOtherExpense;
	}
	public void setIsOtherExpense(int isOtherExpense) {
		this.isOtherExpense = isOtherExpense;
	}
	public int getOtherExpense() {
		return otherExpense;
	}
	public void setOtherExpense(int otherExpense) {
		this.otherExpense = otherExpense;
	}
	public String getOtherExpenseRemark() {
		return otherExpenseRemark;
	}
	public void setOtherExpenseRemark(String otherExpenseRemark) {
		this.otherExpenseRemark = otherExpenseRemark;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCoverImg() {
		return coverImg;
	}
	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public int getCurrentNum() {
		return currentNum;
	}
	public void setCurrentNum(int currentNum) {
		this.currentNum = currentNum;
	}
	
}