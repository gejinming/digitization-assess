package com.assess.entity;


/**
 * @program: digitization-assess
 * @description: 发起评估实体类
 * @author: Gjm
 * @create: 2020-03-25 14:44
 **/

public class StartAssess {
    private Integer assessId;
    private String  assessCenter;
    private Integer targetId;
    private String  firstPublictime;
    private String  firstEndtime;
    private String  secondPublictime;
    private String  secondEndtime;
    private String  stuffUrl;
    private String  createDate;
    private Integer createUserId;
    private String  endTime;
    private Integer[] majorIds;
    private Integer startYear;
    private Integer targetYear;

    public Integer getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(Integer targetYear) {
        this.targetYear = targetYear;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer[] getMajorIds() {
        return majorIds;
    }

    public void setMajorIds(Integer[] majorIds) {
        this.majorIds = majorIds;
    }

    public Integer getAssessId() {
        return assessId;
    }

    public void setAssessId(Integer assessId) {
        this.assessId = assessId;
    }

    public String getAssessCenter() {
        return assessCenter;
    }

    public void setAssessCenter(String assessCenter) {
        this.assessCenter = assessCenter;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getFirstPublictime() {
        return firstPublictime;
    }

    public void setFirstPublictime(String firstPublictime) {
        this.firstPublictime = firstPublictime;
    }

    public String getFirstEndtime() {
        return firstEndtime;
    }

    public void setFirstEndtime(String firstEndtime) {
        this.firstEndtime = firstEndtime;
    }

    public String getSecondPublictime() {
        return secondPublictime;
    }

    public void setSecondPublictime(String secondPublictime) {
        this.secondPublictime = secondPublictime;
    }

    public String getSecondEndtime() {
        return secondEndtime;
    }

    public void setSecondEndtime(String secondEndtime) {
        this.secondEndtime = secondEndtime;
    }

    public String getStuffUrl() {
        return stuffUrl;
    }

    public void setStuffUrl(String stuffUrl) {
        this.stuffUrl = stuffUrl;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
