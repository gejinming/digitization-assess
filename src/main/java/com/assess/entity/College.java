package com.assess.entity;

/**
 * @program: digitization-assess
 * @description: 学校院系实体类
 * @author: Gjm
 * @create: 2020-04-16 11:05
 **/

public class College {
    private  Integer collegeId;
    private  Integer schoolId;
    private  String  collegeName;
    private  Integer deleId;
    private String collegeCode;

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Integer getDeleId() {
        return deleId;
    }

    public void setDeleId(Integer deleId) {
        this.deleId = deleId;
    }
}
