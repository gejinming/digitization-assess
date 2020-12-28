package com.assess.entity;

import lombok.Data;

import java.util.List;

/**
 * @program: digitization-assess
 * @description: 评估模板标题及数据实体类
 * @author: Gjm
 * @create: 2020-05-16 21:46
 **/
@Data
public class TargetDetailData {

    private  Integer assessId;
    private  String  stuff ;

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

    //以下数据
    private  Integer dataId;

    /*专业id*/
    private  Integer majorId;
    /*填写项1数据*/
    private  Double itemAData;
    /*填写项2数据*/
    private  Double itemBData;

    /*总成绩*/
    private  Double sumScore;

    /*默认为0，1是第一次评估时申诉改变数据，2是第二次申诉改变数据*/
    private  Integer stateId;
    /*算法四填写的数据String*/
    private  String arithmeticFourData;
    /*算法四填写的数据Json*/
    private Object[] arithmeticFourDataJson;
}
