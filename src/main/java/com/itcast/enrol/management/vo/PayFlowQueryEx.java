package com.itcast.enrol.management.vo;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

/**
 * @author liuzs
 * Created by liuzhongshuai on 2017/10/31.
 */
public class PayFlowQueryEx {


    /**
     * 学科Id
     */
    private Long subjectId;


    /**
     * 是否补录订单
     */
    private Byte isAft;

    /**
     * 班级类型
     */
    private String classTypeCode;

    /**
     * 授课类型
     */
    private String teachTypeCode;

    /**
     * 区域
     */
    private Long campusId;

    /**
     * 班级
     */
    private Long classId;

    /**
     * 支付方式
     */
    private Long payType;

    /**
     * 优惠策略
     */
    private Long marketId;


    /**
     * 订单状态
     */
    private Integer payStatus;

    /**
     * 模糊匹配多条件
     */
    private String likeName;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * 开始页、
     */
    @Min(value = 1,message = "开始页最小为 1 !")
    private int page;

    /**
     * 每页大小
     */
    @Range(min = 1,max = 50,message = "分页数据过大(每页最大为50条数据!)")
    private int limit;


    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getClassTypeCode() {
        return classTypeCode;
    }

    public void setClassTypeCode(String classTypeCode) {
        this.classTypeCode = classTypeCode;
    }

    public String getTeachTypeCode() {
        return teachTypeCode;
    }

    public void setTeachTypeCode(String teachTypeCode) {
        this.teachTypeCode = teachTypeCode;
    }

    public Long getCampusId() {
        return campusId;
    }

    public void setCampusId(Long campusId) {
        this.campusId = campusId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getPayType() {
        return payType;
    }

    public void setPayType(Long payType) {
        this.payType = payType;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Byte getIsAft() {
        return isAft;
    }

    public void setIsAft(Byte isAft) {
        this.isAft = isAft;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
}