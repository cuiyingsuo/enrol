package com.itcast.enrol.student.service.plugins;

import com.itcast.enrol.common.entity.Student;

public class PDFParams {
	private String pdfSerial;
	private String subSchool;
	private String studentName;
	private String cardNo;
	private String tel;
	private String address;
	private String goodsName;
	private String startDate;
	private String endDate;
	private String price;
	private String priceCN;
	private String orderPrice;
	private String orderPriceCN;
	private String campusAddr;
	private String signLocation;
	private String studentTel;
	private String studentIdentify;
	private String studentAddress;
	private String nowAddress;
	private String signDate;
	private String studyAddress;
	private String orgName;
	
	public static PDFParams getPDFParams(Student student){
		PDFParams pdfParams = new PDFParams();
		String name=student.getName();
		String cardNo = student.getCardNo();
		String mobile = student.getMobile();
		String liveAddress = student.getLiveAddr();

		if(null==cardNo||"".equals(cardNo)){
			return null;
		}
		if(null==liveAddress||"".equals(liveAddress)){
			return null;
		}
		
		pdfParams.setStudentName(name);
		pdfParams.setCardNo(cardNo);
		pdfParams.setTel(mobile);
		pdfParams.setAddress(liveAddress);
		pdfParams.setSignLocation("北京市昌平区");
		pdfParams.setStudentTel(mobile);
		pdfParams.setStudentIdentify(cardNo);
		pdfParams.setNowAddress(liveAddress);
		
		return pdfParams;
	}
	
	
	public String getStudyAddress() {
		return studyAddress;
	}


	public void setStudyAddress(String studyAddress) {
		this.studyAddress = studyAddress;
	}


	public String getOrgName() {
		return orgName;
	}


	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}


	public String getPdfSerial() {
		return pdfSerial;
	}
	public void setPdfSerial(String pdfSerial) {
		this.pdfSerial = pdfSerial;
	}
	public String getSubSchool() {
		return subSchool;
	}
	public void setSubSchool(String subSchool) {
		this.subSchool = subSchool;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPriceCN() {
		return priceCN;
	}
	public void setPriceCN(String priceCN) {
		this.priceCN = priceCN;
	}
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getOrderPriceCN() {
		return orderPriceCN;
	}
	public void setOrderPriceCN(String orderPriceCN) {
		this.orderPriceCN = orderPriceCN;
	}
	public String getCampusAddr() {
		return campusAddr;
	}
	public void setCampusAddr(String campusAddr) {
		this.campusAddr = campusAddr;
	}
	public String getSignLocation() {
		return signLocation;
	}
	public void setSignLocation(String signLocation) {
		this.signLocation = signLocation;
	}
	public String getStudentTel() {
		return studentTel;
	}
	public void setStudentTel(String studentTel) {
		this.studentTel = studentTel;
	}
	public String getStudentIdentify() {
		return studentIdentify;
	}
	public void setStudentIdentify(String studentIdentify) {
		this.studentIdentify = studentIdentify;
	}
	public String getStudentAddress() {
		return studentAddress;
	}
	public void setStudentAddress(String studentAddress) {
		this.studentAddress = studentAddress;
	}
	public String getNowAddress() {
		return nowAddress;
	}
	public void setNowAddress(String nowAddress) {
		this.nowAddress = nowAddress;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	
	
}