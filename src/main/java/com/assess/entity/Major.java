package com.assess.entity;

/**
 * @program: digitization-assess
 * @description: 专业实体类
 * @author: Gjm
 * @create: 2020-04-16 14:19
 **/

public class Major {
    private Integer majorId;
    private String collegeId;
    private String  majorName;
    private Integer deleId;
    private Integer stateId;
    private String  majorCode;
    private Integer schoolId;

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getMajorId() {
        return majorId;
    }



    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public Integer getDeleId() {
        return deleId;
    }

    public void setDeleId(Integer deleId) {
        this.deleId = deleId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }
    @Override
    public String toString() {
        return "Major{" +
                "majorId=" + majorId +
                ", collegeId='" + collegeId + '\'' +
                ", majorName='" + majorName + '\'' +
                ", deleId=" + deleId +
                ", stateId=" + stateId +
                ", majorCode='" + majorCode + '\'' +
                '}';
    }
}
