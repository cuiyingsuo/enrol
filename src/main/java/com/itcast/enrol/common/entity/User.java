package com.itcast.enrol.common.entity;

import javax.persistence.*;

@Table(name = "enrol_user")
public class User {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 工号
     */
    @Column(name = "job_no")
    private Integer jobNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 校区Id
     */
    @Column(name = "campus_id")
    private Long campusId;

    /**
     * 部门code
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * 部门
     */
    @Column(name = "post_id")
    private Long postId;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态(0:禁用，1：启用)
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

    public Byte getIsDel() {
        return isDel;
    }

    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }

    @Column(name="is_del")
    private Byte isDel;

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
     * 获取工号
     *
     * @return job_no - 工号
     */
    public Integer getJobNo() {
        return jobNo;
    }

    /**
     * 设置工号
     *
     * @param jobNo 工号
     */
    public void setJobNo(Integer jobNo) {
        this.jobNo = jobNo;
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取校区Id
     *
     * @return campus_id - 校区Id
     */
    public Long getCampusId() {
        return campusId;
    }

    /**
     * 设置校区Id
     *
     * @param campusId 校区Id
     */
    public void setCampusId(Long campusId) {
        this.campusId = campusId;
    }

    /**
     * 获取部门code
     *
     * @return department_id - 部门code
     */
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置部门code
     *
     * @param departmentId 部门code
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 获取部门
     *
     * @return post_id - 部门
     */
    public Long getPostId() {
        return postId;
    }

    /**
     * 设置部门
     *
     * @param postId 部门
     */
    public void setPostId(Long postId) {
        this.postId = postId;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取状态(0:禁用，1：启用)
     *
     * @return status - 状态(0:禁用，1：启用)
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态(0:禁用，1：启用)
     *
     * @param status 状态(0:禁用，1：启用)
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
}