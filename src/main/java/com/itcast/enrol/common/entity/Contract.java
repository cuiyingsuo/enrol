package com.itcast.enrol.common.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name = "enrol_contract")
public class Contract {
	/**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 主订单号
     */
    @Column(name = "merge_order_no")
    private String orderMainNo;
    
    /**
     * 合同存放url
     */
    @Column(name = "contract_url")
    private String contractUrl;
    
    /**
     * 合同编号
     */
    @Column(name = "contract_code")
    private String contractCode;
    
    /**
     * 合同状态，0、不允许签订，1、允许签订，2、已签订，3、合同废弃
     */
    @Column(name = "contract_status")
    private Integer contractStatus;
    /**
     * 签订合同验证码发送手机号
     */
    @Column(name = "sign_sms_mobile")
    private String signSmsMobile;
    /**
     * 签订合同手机验证码发送时间
     */
    @Column(name = "sign_sms_time")
    private Long signSmsTime;
    /**
     * 签订合同手机验证码发送内容
     */
    @Column(name = "sign_sms_content")
    private String signSmsContent;
    
    /**
     * 天印合同签订，签订记录id
     */
    @Column(name = "ty_signServiceId")
    private String tySignServiceId;
    /**
     * 作废合同验证码发送手机号
     */
    @Column(name = "cancel_sms_mobile")
    private String cancelSmsMobile;
    /**
     * 作废合同手机验证码发送时间
     */
    @Column(name = "cancel_sms_time")
    private Long cancelSmsTime;
    /**
     * 作废合同平台接收的手机验证码
     */
    @Column(name = "cancel_sms_content")
    private String cancelSmsContent;
    /**
     * 合同签订时间
     */
    @Column(name = "contract_sign_time")
    private Long contractSignTime;
    /**
     * 作废合同时间
     */
    @Column(name = "contract_cancel_time")
    private Long contractCancelTime;
    /**
     * 天印作废合同，签订记录id
     */
    @Column(name = "ty_cancelServiceId")
    private String tyCancelServiceId;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;
    
    /**
     * 创建人
     */
    private String creater;
    		
    
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getOrderMainNo() {
		return orderMainNo;
	}
	public void setOrderMainNo(String orderMainNo) {
		this.orderMainNo = orderMainNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContractUrl() {
		return contractUrl;
	}
	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public Integer getContractStatus() {
		return contractStatus;
	}
	public void setContractStatus(Integer contractStatus) {
		this.contractStatus = contractStatus;
	}
	public String getTySignServiceId() {
		return tySignServiceId;
	}
	public void setTySignServiceId(String tySignServiceId) {
		this.tySignServiceId = tySignServiceId;
	}
	public Long getContractSignTime() {
		return contractSignTime;
	}
	public void setContractSignTime(Long contractSignTime) {
		this.contractSignTime = contractSignTime;
	}
	public Long getContractCancelTime() {
		return contractCancelTime;
	}
	public void setContractCancelTime(Long contractCancelTime) {
		this.contractCancelTime = contractCancelTime;
	}
	public String getTyCancelServiceId() {
		return tyCancelServiceId;
	}
	public void setTyCancelServiceId(String tyCancelServiceId) {
		this.tyCancelServiceId = tyCancelServiceId;
	}
	public String getSignSmsMobile() {
		return signSmsMobile;
	}
	public void setSignSmsMobile(String signSmsMobile) {
		this.signSmsMobile = signSmsMobile;
	}
	public Long getSignSmsTime() {
		return signSmsTime;
	}
	public void setSignSmsTime(Long signSmsTime) {
		this.signSmsTime = signSmsTime;
	}
	public String getSignSmsContent() {
		return signSmsContent;
	}
	public void setSignSmsContent(String signSmsContent) {
		this.signSmsContent = signSmsContent;
	}
	public String getCancelSmsMobile() {
		return cancelSmsMobile;
	}
	public void setCancelSmsMobile(String cancelSmsMobile) {
		this.cancelSmsMobile = cancelSmsMobile;
	}
	public Long getCancelSmsTime() {
		return cancelSmsTime;
	}
	public void setCancelSmsTime(Long cancelSmsTime) {
		this.cancelSmsTime = cancelSmsTime;
	}
	public String getCancelSmsContent() {
		return cancelSmsContent;
	}
	public void setCancelSmsContent(String cancelSmsContent) {
		this.cancelSmsContent = cancelSmsContent;
	}
	
}