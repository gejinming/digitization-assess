package com.assess.entity;

/**
 * @program: digitization-assess
 * @description: 评估体系指标类型
 * @author: Gjm
 * @create: 2020-03-26 13:23
 **/

public class TargetType {
    private Integer targetTypeId;
    private Integer targetId;
    private String targetTypeName;

    public Integer getTargetTypeId() {
        return targetTypeId;
    }

    public void setTargetTypeId(Integer targetTypeId) {
        this.targetTypeId = targetTypeId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getTargetTypeName() {
        return targetTypeName;
    }

    public void setTargetTypeName(String targetTypeName) {
        this.targetTypeName = targetTypeName;
    }
}
