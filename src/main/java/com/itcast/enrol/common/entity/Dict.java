package com.itcast.enrol.common.entity;

import javax.persistence.*;

@Table(name = "enrol_dict")
public class Dict {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    private String name;


    /**
     * 字典code
     */
    private String code;

    /**
     * 顺序，可选；
     */
    private Integer order;

    /**
     * 父级Id，若有；
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 状态（0-无效，1-有效）
     */
    private Byte status;

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
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * 获取字典code
     *
     * @return code - 字典code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置字典code
     *
     * @param code 字典code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取顺序，可选；
     *
     * @return order - 顺序，可选；
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * 设置顺序，可选；
     *
     * @param order 顺序，可选；
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * 获取父级Id，若有；
     *
     * @return parent_id - 父级Id，若有；
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父级Id，若有；
     *
     * @param parentId 父级Id，若有；
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取状态（0-无效，1-有效）
     *
     * @return status - 状态（0-无效，1-有效）
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态（0-无效，1-有效）
     *
     * @param status 状态（0-无效，1-有效）
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}