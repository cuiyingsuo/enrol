package com.itcast.enrol.common.base;

/**
 * Created by liuzhongshuai on 2017/12/28.
 */
public class EmsClass {

    private String phase;

    private String lessonModel;

    private String isDelete;

    private String subjectId;

    private String classTypeId;

    private String teachingModel;

    private String classteacher;

    private String schedule;

    private String name;

    private Long id;

    private String brand;

    private String startDate;

    private String predictionGraduationDate;

    private String creatorName;

    private String schoolCode;

    private String jobNumber;

    private Integer status;

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getLessonModel() {
        return lessonModel;
    }

    public void setLessonModel(String lessonModel) {
        this.lessonModel = lessonModel;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String delete) {
        this.isDelete = delete;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(String classTypeId) {
        this.classTypeId = classTypeId;
    }

    public String getTeachingModel() {
        return teachingModel;
    }

    public void setTeachingModel(String teachingModel) {
        this.teachingModel = teachingModel;
    }

    public String getClassteacher() {
        return classteacher;
    }

    public void setClassteacher(String classteacher) {
        this.classteacher = classteacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public Integer getStatus() {
        return status;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPredictionGraduationDate() {
        return predictionGraduationDate;
    }

    public void setPredictionGraduationDate(String predictionGraduationDate) {
        this.predictionGraduationDate = predictionGraduationDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }


    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    private String graduationDate;


    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

}
