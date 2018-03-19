package com.itcast.enrol.management.vo;

/**
 * Created by liuzhongshuai on 2017/11/7.
 */
public class StuClassDetailVo {

    private String subjectName;

    private String teachMOdeName;

    private String classTypeName;

    private String campusName;

    private String className;

    private Integer payType;

    private Long costPrice;

    private String marketingName;

    private Long prefAmount;

    private Long orderPrice;

    private Long arrearage;

    private Long returnMoney;

    public Long getPaid() {
        return paid;
    }

    public void setPaid(Long paid) {
        this.paid = paid;
    }

    private Long paid;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeachMOdeName() {
        return teachMOdeName;
    }

    public void setTeachMOdeName(String teachMOdeName) {
        this.teachMOdeName = teachMOdeName;
    }

    public String getClassTypeName() {
        return classTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Long costPrice) {
        this.costPrice = costPrice;
    }

    public String getMarketingName() {
        return marketingName;
    }

    public void setMarketingName(String marketingName) {
        this.marketingName = marketingName;
    }

    public Long getPrefAmount() {
        return prefAmount;
    }

    public void setPrefAmount(Long prefAmount) {
        this.prefAmount = prefAmount;
    }

    public Long getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Long orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Long getArrearage() {
        return arrearage;
    }

    public void setArrearage(Long arrearage) {
        this.arrearage = arrearage;
    }

    public Long getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(Long returnMoney) {
        this.returnMoney = returnMoney;
    }

}
