package com.itcast.enrol.common.entity;

import javax.persistence.*;

@Table(name = "enrol_subject")
public class Subject {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teachModeCode;


    private String code;
    /**
     * 学科名称
     */
    private String name;

    /**
     * 学科状态(0:关闭，1:开启)
     */
    private Byte status;

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
     * 学科介绍
     */
    private String introduce;

    /**
     * 备注；
     */
    private String memo;

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
     * 获取学科名称
     *
     * @return name - 学科名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置学科名称
     *
     * @param name 学科名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取学科状态(0:关闭，1:开启)
     *
     * @return status - 学科状态(0:关闭，1:开启)
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置学科状态(0:关闭，1:开启)
     *
     * @param status 学科状态(0:关闭，1:开启)
     */
    public void setStatus(Byte status) {
        this.status = status;
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
     * 获取学科介绍
     *
     * @return introduce - 学科介绍
     */
    public String getIntroduce() {
        return introduce;
    }

    /**
     * 设置学科介绍
     *
     * @param introduce 学科介绍
     */
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    /**
     * 获取备注；
     *
     * @return memo - 备注；
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置备注；
     *
     * @param memo 备注；
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTeachModeCode() {
        return teachModeCode;
    }

    public void setTeachModeCode(String teachModeCode) {
        this.teachModeCode = teachModeCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}