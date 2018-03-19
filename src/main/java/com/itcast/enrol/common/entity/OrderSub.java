package com.itcast.enrol.common.entity;

import javax.persistence.*;

@Table(name = "enrol_order_sub")
public class OrderSub {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单号；
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 订单价格
     */
    @Column(name = "order_price")
    private Integer orderPrice;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private Long payTime;

    /**
     * 订单状态(0:未支付、1:支付中、2:支付完成、3:退费中、4:退费成功)
     */
    private Integer status;
    
    /**
     * 收据号
     */
    @Column(name = "receipt_no")
    private String receiptNo;

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

    @Column(name = "merge_order_no")
    private String mergeOrderNo;
    
    @Column(name = "is_aft")
    private Integer isAft;

    public Integer getIsAft() {
		return isAft;
	}

	public void setIsAft(Integer isAft) {
		this.isAft = isAft;
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
     * 获取订单号；
     *
     * @return order_no - 订单号；
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单号；
     *
     * @param orderNo 订单号；
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取订单价格
     *
     * @return order_price - 订单价格
     */
    public Integer getOrderPrice() {
        return orderPrice;
    }

    /**
     * 设置订单价格
     *
     * @param orderPrice 订单价格
     */
    public void setOrderPrice(Integer orderPrice) {
        this.orderPrice = orderPrice;
    }

    /**
     * 获取支付时间
     *
     * @return pay_time - 支付时间
     */
    public Long getPayTime() {
        return payTime;
    }

    /**
     * 设置支付时间
     *
     * @param payTime 支付时间
     */
    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    /**
     * 获取订单状态(0:未支付、1:支付中、2:支付完成、3:退费中、4:退费成功)
     *
     * @return status - 订单状态(0:未支付、1:支付中、2:支付完成、3:退费中、4:退费成功)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置订单状态(0:未支付、1:支付中、2:支付完成、3:退费中、4:退费成功)
     *
     * @param status 订单状态(0:未支付、1:支付中、2:支付完成、3:退费中、4:退费成功)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
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
     * @return merge_order_no
     */
    public String getMergeOrderNo() {
        return mergeOrderNo;
    }

    /**
     * @param mergeOrderNo
     */
    public void setMergeOrderNo(String mergeOrderNo) {
        this.mergeOrderNo = mergeOrderNo;
    }
}