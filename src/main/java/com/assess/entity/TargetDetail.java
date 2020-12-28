package com.assess.entity;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.util.List;

/**
 * @program: digitization-assess
 * @description: 创建评估类型的指标
 * @author: Gjm
 * @create: 2020-03-26 13:58
 **/

public class TargetDetail {
    //指标id
    private Integer targetDetailId;
    //指标类型id
    private Integer targetTypeId;
    private  double indexNum;
    //指标内容
    private String  targetContent;
    //指标分数
    private  double score;
    //指标单位
    private String  scoreUnit;
    //指标说明
    private String  targetExplain;
    //算法id
    private Integer arithmeticId;
    //第一个填写项
    private String  itemA;
    //第二个填写项
    private String  itemB;
    //上传材料url
    private String stuffUrl;
    //选项（用于算法3）数组形式
    private List<Item> arithmeticThreeo;

    //算法4填写值数组形式
    private List<Item> arithmeticFourTitleo;

    //选项（用于算法3）字符串形式
    private String arithmeticThree;

    //算法4填写值字符串形式
    private String arithmeticFourTitle;
    //评估体系id
    private Integer targetId;

    //状态 0未删，1删除
    private String delState;

    public double getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(double indexNum) {
        this.indexNum = indexNum;
    }

    public Integer getTargetDetailId() {
        return targetDetailId;
    }

    public void setTargetDetailId(Integer targetDetailId) {
        this.targetDetailId = targetDetailId;
    }

    public Integer getTargetTypeId() {
        return targetTypeId;
    }

    public void setTargetTypeId(Integer targetTypeId) {
        this.targetTypeId = targetTypeId;
    }

    public String getTargetContent() {
        return targetContent;
    }

    public void setTargetContent(String targetContent) {
        this.targetContent = targetContent;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getScoreUnit() {
        return scoreUnit;
    }

    public void setScoreUnit(String scoreUnit) {
        this.scoreUnit = scoreUnit;
    }

    public String getTargetExplain() {
        return targetExplain;
    }

    public void setTargetExplain(String targetExplain) {
        this.targetExplain = targetExplain;
    }

    public Integer getArithmeticId() {
        return arithmeticId;
    }

    public void setArithmeticId(Integer arithmeticId) {
        this.arithmeticId = arithmeticId;
    }

    public String getItemA() {
        return itemA;
    }

    public void setItemA(String itemA) {
        this.itemA = itemA;
    }

    public String getItemB() {
        return itemB;
    }

    public void setItemB(String itemB) {
        this.itemB = itemB;
    }

    public String getStuffUrl() {
        return stuffUrl;
    }

    public void setStuffUrl(String stuffUrl) {
        this.stuffUrl = stuffUrl;
    }

    public Object getArithmeticThree() {
        return arithmeticThree;
    }

    public List<Item> getArithmeticThreeo() {
        return arithmeticThreeo;
    }

    public void setArithmeticThreeo(List<Item> arithmeticThreeo) {
        this.arithmeticThreeo = arithmeticThreeo;
    }

    public List<Item> getArithmeticFourTitleo() {
        return arithmeticFourTitleo;
    }

    public void setArithmeticFourTitleo(List<Item> arithmeticFourTitleo) {
        this.arithmeticFourTitleo = arithmeticFourTitleo;
    }

    public void setArithmeticThree(String arithmeticThree) {
        this.arithmeticThree = arithmeticThree;
    }

    public String getArithmeticFourTitle() {
        return arithmeticFourTitle;
    }

    public void setArithmeticFourTitle(String arithmeticFourTitle) {
        this.arithmeticFourTitle = arithmeticFourTitle;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getDelState() {
        return delState;
    }

    public void setDelState(String delState) {
        this.delState = delState;
    }
}
