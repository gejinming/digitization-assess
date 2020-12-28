package com.assess.entity;

/**
 * @program: digitization-assess
 * @description: 专业材料提交实体类
 * @author: Gjm
 * @create: 2020-03-27 09:33
 **/

public class MajorData {
    private  Integer dataId;
    /*指标模板id*/
    private  Integer targetDetailId;
    /*专业id*/
    private  Integer majorId;
    /*填写项1数据*/
    private  Double itemAData;
    /*填写项2数据*/
    private  Double itemBData;
    /*上传文件地址*/
    private  String stuffUrl;
    /*当前指标成绩*/
    private  Double score;
    /*总成绩*/
    private  Double sumScore;
    /*评估体系id*/
    private  Integer targetId;
    /*默认为0，1是第一次评估时申诉改变数据，2是第二次申诉改变数据*/
    private  Integer stateId;
    /*算法四填写的数据String*/
    private  String arithmeticFourData;
    /*算法四填写的数据Json*/
    private Object[] arithmeticFourDataJson;
    /*算法id*/
    private Integer arithmeticId;

    public Object[] getArithmeticFourDataJson() {
        return arithmeticFourDataJson;
    }

    public void setArithmeticFourDataJson(Object[] arithmeticFourDataJson) {
        this.arithmeticFourDataJson = arithmeticFourDataJson;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Double getSumScore() {
        return sumScore;
    }

    public void setSumScore(Double sumScore) {
        this.sumScore = sumScore;
    }


    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getTargetDetailId() {
        return targetDetailId;
    }

    public void setTargetDetailId(Integer targetDetailId) {
        this.targetDetailId = targetDetailId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public Double getItemAData() {
        return itemAData;
    }

    public void setItemAData(Double itemAData) {
        this.itemAData = itemAData;
    }

    public Double getItemBData() {
        return itemBData;
    }

    public void setItemBData(Double itemBData) {
        this.itemBData = itemBData;
    }

    public String getStuffUrl() {
        return stuffUrl;
    }

    public void setStuffUrl(String stuffUrl) {
        this.stuffUrl = stuffUrl;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getArithmeticFourData() {
        return arithmeticFourData;
    }

    public void setArithmeticFourData(String arithmeticFourData) {
        this.arithmeticFourData = arithmeticFourData;
    }

    public Integer getArithmeticId() {
        return arithmeticId;
    }

    public void setArithmeticId(Integer arithmeticId) {
        this.arithmeticId = arithmeticId;
    }
}
