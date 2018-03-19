package com.itcast.enrol.common.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by liuzhongshuai on 2018/1/16.
 */
@Table(name = "enrol_task_record")
public class Task {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskCode;

    private Date taskExeDate;

    private Date taskExeDateTime;

    private String remark;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public Date getTaskExeDate() {
        return taskExeDate;
    }

    public void setTaskExeDate(Date taskExeDate) {
        this.taskExeDate = taskExeDate;
    }

    public Date getTaskExeDateTime() {
        return taskExeDateTime;
    }

    public void setTaskExeDateTime(Date taskExeDateTime) {
        this.taskExeDateTime = taskExeDateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



}