package com.itcast.enrol.common.entity;

import javax.persistence.*;

@Table(name = "enrol_role")
public class Role {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 所属校区（0：表示不区分校区）
     */
    @Column(name = "campus_id")
    private Long campusId;

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
     * 角色code
     */
    @Column(name = "role_code")
    private String roleCode;

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
     * 获取角色名称
     *
     * @return name - 角色名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置角色名称
     *
     * @param name 角色名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取所属校区（0：表示不区分校区）
     *
     * @return campus_id - 所属校区（0：表示不区分校区）
     */
    public Long getCampusId() {
        return campusId;
    }

    /**
     * 设置所属校区（0：表示不区分校区）
     *
     * @param campusId 所属校区（0：表示不区分校区）
     */
    public void setCampusId(Long campusId) {
        this.campusId = campusId;
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
     * 获取角色code
     *
     * @return role_code - 角色code
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * 设置角色code
     *
     * @param roleCode 角色code
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}