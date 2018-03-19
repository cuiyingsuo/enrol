package com.itcast.enrol.common.entity;

import javax.persistence.*;

@Table(name = "enrol_permission")
public class Permission {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源类型（1:menu,2:button,3:数据权限）
     */
    private Byte type;

    /**
     * 资源code
     */
    private String code;

    /**
     * 父节点Id
     */
    @Column(name = "parent_id")
    private Long parentId;

    private Long creator;

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
     * 资源/权限访问链接
     */
    @Column(name = "link")
    private String link;
    
    /**
     * 父资源/权限编码
     */
    @Column(name = "parent_code")
    private String parentCode;
    
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
     * 获取资源名称
     *
     * @return name - 资源名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置资源名称
     *
     * @param name 资源名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取资源类型（1:menu,2:button,3:数据权限）
     *
     * @return type - 资源类型（1:menu,2:button,3:数据权限）
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置资源类型（1:menu,2:button,3:数据权限）
     *
     * @param type 资源类型（1:menu,2:button,3:数据权限）
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取资源code
     *
     * @return code - 资源code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置资源code
     *
     * @param code 资源code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取父节点Id
     *
     * @return parent_id - 父节点Id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父节点Id
     *
     * @param parentId 父节点Id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return creator
     */
    public Long getCreator() {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(Long creator) {
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
    
    public String getLink() {
 		return link;
 	}

 	public void setLink(String link) {
 		this.link = link;
 	}

 	public String getParentCode() {
 		return parentCode;
 	}

 	public void setParentCode(String parentCode) {
 		this.parentCode = parentCode;
 	}
}