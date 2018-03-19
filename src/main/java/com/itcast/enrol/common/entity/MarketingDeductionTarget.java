package com.itcast.enrol.common.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "enrol_marketing_deduction_target")
public class MarketingDeductionTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marketing_deduction_id")
    private Long marketingDeductionId;

    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name = "create_time")
    private Date createTime;

    private String creator;

    @Column(name = "edit_time")
    private Date editTime;

    private String editor;

    /**
     * 0:删除 1：未删除
     */
    @Column(name = "is_del")
    private Integer isDel;

    /**
     * 优惠维度 1:商品 2:班级 3:学科 4:校区
     */
    private Integer dimension;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return marketing_deduction_id
     */
    public Long getMarketingDeductionId() {
        return marketingDeductionId;
    }

    /**
     * @param marketingDeductionId
     */
    public void setMarketingDeductionId(Long marketingDeductionId) {
        this.marketingDeductionId = marketingDeductionId;
    }

    /**
     * @return goods_id
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * @param goodsId
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return edit_time
     */
    public Date getEditTime() {
        return editTime;
    }

    /**
     * @param editTime
     */
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    /**
     * @return editor
     */
    public String getEditor() {
        return editor;
    }

    /**
     * @param editor
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * 获取0:删除 1：未删除
     *
     * @return is_del - 0:删除 1：未删除
     */
    public Integer getIsDel() {
        return isDel;
    }

    /**
     * 设置0:删除 1：未删除
     *
     * @param isDel 0:删除 1：未删除
     */
    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    /**
     * 获取优惠维度 1:商品 2:班级 3:学科 4:校区
     *
     * @return dimension - 优惠维度 1:商品 2:班级 3:学科 4:校区
     */
    public Integer getDimension() {
        return dimension;
    }

    /**
     * 设置优惠维度 1:商品 2:班级 3:学科 4:校区
     *
     * @param dimension 优惠维度 1:商品 2:班级 3:学科 4:校区
     */
    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }
}