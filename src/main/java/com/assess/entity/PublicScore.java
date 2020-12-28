package com.assess.entity;

/**
 * @program: digitization-assess
 * @description: 第一次和第二次公示成绩实体类
 * @author: Gjm
 * @create: 2020-03-27 17:03
 **/

public class PublicScore {
    /*公示id*/
    private Integer publicId;
    /*发起评估id*/
    private Integer assessId;
    /*专业id*/
    private Integer majorId;
    /*总成绩*/
    private Double  countScore;
    /*附件总数*/
    private Integer affixCount;
    /*排名*/
    private Integer ranking;
    /*审核状态*/
    private Integer stateId;
    /*申诉状态*/
    private Integer appelState;
    /*评估体系id*/
    private Integer targetId;
    /*公示时间*/
    private String  publicTime;
    /*星级数*/
    private Integer starLevel;
    /*红榜0否1是*/
    private Integer redCard;
    /*黄榜0否1是*/
    private Integer yellowCard;
    /*橙榜0否1是*/
    private Integer orangeCard;

    public String getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(String publicTime) {
        this.publicTime = publicTime;
    }

    public Integer getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(Integer starLevel) {
        this.starLevel = starLevel;
    }

    public Integer getRedCard() {
        return redCard;
    }

    public void setRedCard(Integer redCard) {
        this.redCard = redCard;
    }

    public Integer getYellowCard() {
        return yellowCard;
    }

    public void setYellowCard(Integer yellowCard) {
        this.yellowCard = yellowCard;
    }

    public Integer getOrangeCard() {
        return orangeCard;
    }

    public void setOrangeCard(Integer orangeCard) {
        this.orangeCard = orangeCard;
    }

    public Integer getPublicId() {
        return publicId;
    }

    public void setPublicId(Integer publicId) {
        this.publicId = publicId;
    }

    public Integer getAssessId() {
        return assessId;
    }

    public void setAssessId(Integer assessId) {
        this.assessId = assessId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public Double getCountScore() {
        return countScore;
    }

    public void setCountScore(Double countScore) {
        this.countScore = countScore;
    }

    public Integer getAffixCount() {
        return affixCount;
    }

    public void setAffixCount(Integer affixCount) {
        this.affixCount = affixCount;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getAppelState() {
        return appelState;
    }

    public void setAppelState(Integer appelState) {
        this.appelState = appelState;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }
}
