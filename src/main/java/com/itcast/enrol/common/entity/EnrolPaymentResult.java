package com.itcast.enrol.common.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "enrol_payment_result")
public class EnrolPaymentResult {
	/**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "merchantId")
    private String merchantId;
    
    @Column(name = "version")
    private String version;
    
    @Column(name = "language")
    private String language;
    
    @Column(name = "signType")
    private String signType;
    
    @Column(name = "payType")
    private String payType;
    
    @Column(name = "issuserId")
    private String issuserId;
    
    @Column(name = "paymentOrderId")
    private String paymentOrderId;
    
    @Column(name = "orderNo")
    private String orderNo;
    
    @Column(name = "orderDateTime")
    private String orderDateTime;
    
    @Column(name = "orderAmount")
    private String orderAmount;
    
    @Column(name = "payDateTime")
    private String payDateTime;
    
    @Column(name = "payAmount")
    private String payAmount;
    
    @Column(name = "ext1")
    private String ext1;
    
    @Column(name = "ext2")
    private String ext2;
    
    @Column(name = "payResult")
    private String payResult;
    
    @Column(name = "errorCode")
    private String errorCode;
    
    @Column(name = "returnDateTime")
    private String returnDateTime;
    
    @Column(name = "signMsg")
    private String signMsg;
    
    @Column(name = "verifyResult")
    private boolean verifyResult;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getIssuserId() {
		return issuserId;
	}

	public void setIssuserId(String issuserId) {
		this.issuserId = issuserId;
	}

	public String getPaymentOrderId() {
		return paymentOrderId;
	}

	public void setPaymentOrderId(String paymentOrderId) {
		this.paymentOrderId = paymentOrderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getPayDateTime() {
		return payDateTime;
	}

	public void setPayDateTime(String payDateTime) {
		this.payDateTime = payDateTime;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getReturnDateTime() {
		return returnDateTime;
	}

	public void setReturnDateTime(String returnDateTime) {
		this.returnDateTime = returnDateTime;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	public boolean isVerifyResult() {
		return verifyResult;
	}

	public void setVerifyResult(boolean verifyResult) {
		this.verifyResult = verifyResult;
	}


}
