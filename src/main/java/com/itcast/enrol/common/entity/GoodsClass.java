package com.itcast.enrol.common.entity;

import javax.persistence.*;

@Table(name = "enrol_goods_class")
public class GoodsClass {
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品Id
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 班级Id
     */
    @Column(name = "class_id")
    private Long classId;

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
     * 获取主键Id
     *
     * @return id - 主键Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键Id
     *
     * @param id 主键Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取商品Id
     *
     * @return goods_id - 商品Id
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * 设置商品Id
     *
     * @param goodsId 商品Id
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取班级Id
     *
     * @return class_id - 班级Id
     */
    public Long getClassId() {
        return classId;
    }

    /**
     * 设置班级Id
     *
     * @param classId 班级Id
     */
    public void setClassId(Long classId) {
        this.classId = classId;
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

}