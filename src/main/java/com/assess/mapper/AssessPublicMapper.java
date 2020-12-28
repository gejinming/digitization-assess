package com.assess.mapper;

import com.assess.entity.*;
import com.sun.org.apache.bcel.internal.generic.INEG;

import java.util.List;
import java.util.Map;

public interface AssessPublicMapper {
    /*获取没有用过的评估体系前四个*/
    List<Map<String,Object>> getYearAssess(Integer schoolId);
    /*判断今年的评估体系是否创建*/
    List<Map<String,Object>> getNewYearAssess(Integer year);
    /*查询评估体系年度*/
    Map<String,Object> getAssessTargetYear(Integer targetId);
    /*创建评估体系模板*/
    int addNewAssessModel(TargetModel targetModel);
    /*判断此模板是否在用*/
    Map<String,Object> isUserTarget(Integer targetId);
    /*删除评估模板*/
    int delTarger(Integer targetId,Integer userId);
    /*删除评估模板的类型*/
    int deltargetTypes(Integer targetId);
    /*删除具体指标*/
    int deltargetTypedetail(Integer targetId);
    /*根据评估体系id获取所有的指标类型*/
    List<Map<String,Object>> getTargetAllType(Integer targetId);
    /*创建评估体系类型*/
    int addNewAssessTypeModel(TargetType targetType);
    /*开启评估添加数据*/
    int addNewAssess(StartAssess startAssess);
    /*查询目前已评估最大评估的年度*/
    Map<String,Object> getAssessMaxYear(Integer schoolId);
    /*将开启评估时选择的评估体系修改为1已用状态*/
    int updateStartTarget(Integer targetId,Integer targetYear,Integer stateId);
    /*添加评估专业*/
    int addAssessMajor(Integer assessId,Integer[] majorIds);

    /*查询是否提示*/
    Map<String,Object> getAssessHindState(Integer userId);
    /*评估开始提示信息*/
    Map<String,Object> getAssessHindInfo(Integer targetId);
    /*设置不再提示信息*/
    int setAssessInfo(Integer userId,Integer hindId,Integer schoolId);
    /*查询所有专业*/
    List<Map<String, Object>> getAllCollegeMajor(Integer schoolId);
    /*查询所有的院系*/
    List<Map<String,Object>> getAllCollege(Integer schoolId);
    /*查询当前评估体系的指标序号是否已存在*/
    Map<String,Object> getIndexNumIsExist(TargetDetail targetDetail);
    /*创建当前评估类型下的指标内容*/
    int addTargetDetail(TargetDetail targetDetail);
    /*修改指标内容*/
    int updateTargetDetail(TargetDetail targetDetail);
    /*删除指标类型*/
    int delTargetType(Integer TargetTypeId);
    /*删除指标*/
    int delTargetDetail2(Integer TargetTypeId);

    /*删除指标*/
    int delTargetDetail(Integer targetDetailId);
    /*查询所有算法*/
    List<Map<String,Object>> getAllArithmetic();
    /*根据评估体系类型id查询此类型所有的指标*/
    List<Map<String,Object>> getAllTargetDetail(Integer targetTypeId);
    /*根据评估体系id查询此体系所有的类型*/
    List<Map<String,Object>> getAllTargetType(Integer targetId);

    /*根据评估体系id查询此体系所有的类型*/
    List<Map<String,Object>> getAllTargetType1(Integer targetId);
    /*统计每个评估体系里有几个类型和指标*/
    List<Map<String,Object>> getAllTarget(Integer schoolId);
    /*统计这个评估体系各专业综合得分*/
    List<Map<String,Object>> getTargetAllMajorScore(Integer targetId);
    /*统计得分添加进第一次公示表里*/
    int addFirstTargetMajorScore(Integer targetId);
    /*第一次公示各专业得分*/
    List<Map<String,Object>> firstPublicMajorScore(Integer targetId,Integer stateId);
    /*教务处领导审核第一次公示得分，通过之后再公示,改变审核状态*/
    int updateCheckFirstState(Integer assessId,Integer stateId,Integer userId);
    /*根据评估体系类型查询此类型所有指标的数据信息*/
    List<Map<String,Object>> findFirstInfo(Integer targetTypeId,Integer majorId,Integer publicState);
    /*查看此专业第一次公示得分*/
    Map<String,Object> getFirstMajorScore(Integer publicId);
    /*第一次公示专业申诉添加申诉理由*/
    int addPublicMajorAppeal(MajorAppel majorAppel);
    /*申诉更改第一次公示表状态*/
    int updateFirstPublicState(Integer publicId,Integer stateId);
    /*教务处审核专业申诉，通过或驳回*/
    int updatePublicMajorAppeal(MajorAppel majorAppel);
    /*查询第一次公示申诉专业申诉理由*/
    Map<String,Object> getMajorAppelReason(Integer publicId,Integer publicCount);
    /*教务处负责人查询第一次各专业申诉列表*/
    List<Map<String,Object>> getMajorFirstAppelList(Integer targetId,Integer stateId);
    /*教务处负责人查询第二次各专业申诉列表*/
    List<Map<String,Object>> getMajorSecondAppelList(Integer targetId,Integer stateId);
    /*查询发起评估的已提交或未提交材料的专业成绩及排名*/
    List<Map<String,Object>> getMajorSummitList(Integer targetId,Integer stateId);
    /*第一次申诉通过改动专业提交的数据，须新增一条改动后的数据*/
    int addMajorDataWriteMa(MajorData majorData);
    /*第二次公示统计数据*/
    int addMajorSecondPublic(Integer assessId,Integer targetId);
    /*查询第二次公示数据教务处领导审核*/
    List<Map<String,Object>> getCheckSecondPublic(Integer targetId);
    /*教务处领导审核第二次公示得分，通过之后再公示*/
    int updateCheckSecondPublic(Integer targetId,Integer stateId,Integer userId);
    /*第二次公示*/
    List<Map<String,Object>> getSecondPublic(Integer targetId);
    /*第二次公示申诉，修改此专业公示状态改为申诉状态*/
    int updateSecondPublicState(Integer publicId,Integer stateId);
    /*查看此专业第二次公示得分*/
    Map<String,Object> getSecondMajorScore(Integer publicId);
    /*获取此专业第一次公示时申诉的理由及材料*/
    Map<String,Object> getMajorFirstAPPel(Integer targetId,Integer majorId);
    /*最终公示统计排名*/
    int addMajorFinalPublic(Integer assessId,Integer targetId);
    /*查询本次评估列表*/
    List<Map<String,Object>> getFinalPublicMajor(Integer targetId);
    /*查询此专业是否有排名后十的评估*/
    Map<String,Object> getMajorLastRanking(Integer majorId,Integer ranking,Integer assessId);
    /*统计星级的专业并出现次数*/
    Map<String,Object> getMajorStarLevel(Integer majorId,Integer assessId);
    /*查询此专业排名后十之后的评估星级数*/
    Map<String,Object> getMajorStarLevelNum(Integer majorId,Integer assessId);
    /*查询此专业是否连续两次评估为黄牌*/
    Map<String,Object> getMajorTwice(Integer majorId,Integer assessId);
    /*获取评估体系发起评估的id*/
    Map<String,Object> getAssessId(Integer targetId);
    /*修改此次评估的星级，首次、累计2次、3次前五名分别3星4星5星*/
    int updateMajorStarLevel(Integer targetId,Integer majorId,Integer starLevel);
    /*统计黄牌的专业并出现次数*/
    Map<String,Object> getMajorYellowCode(Integer majorId);
    /*统计橙牌的专业并出现次数*/
    Map<String,Object> getMajorOrangeCode(Integer majorId);
    /*统计红牌的专业并出现次数*/
    Map<String,Object> getMajorRedCode(Integer majorId);
    /*评为黄牌*/
    int updateMajorYellowCode(Integer targetId,Integer majorId,Integer yellowCode);
    /*评为橙牌*/
    int updateMajorOrangeCode(Integer targetId,Integer majorId,Integer orangeCode);
    /*评为红牌*/
    int updateMajorRedCode(Integer targetId,Integer majorId,Integer redCode);
    /*查看此专业第二次公示得分*/
    Map<String,Object> getFinalMajorScore(Integer publicId);
    /*查询最终公示数据教务处领导审核*/
    List<Map<String,Object>> getCheckFinalPublicList(Integer targetId);
    /*教务处领导审核最终公示得分，通过之后再公示*/
    int updateCheckFinalPublic(Integer targetId,Integer stateId,Integer userId);
    /*评估结束修改状态*/
    int updataStartAssess(Integer targetId);
    /*查询最终公示列表*/ /*专业通览历年评估榜单*/
    List<Map<String,Object>> getFinalPublic(Integer targetId);
    /*专业通览历年评估本专业排名及分数*/
    List<Map<String,Object>> getMajorFinalPublic(Integer majorId,Integer schoolId);
    /*评估档案教务处通览历年评估*/
    List<Map<String,Object>> getAllAssessPublic(Integer schoolId);
    /*判断最终榜单是否已生成*/
    Map<String,Object> isExistPublic(Integer targetId,String table);
    /*教务处领导需要审核的列表*/
    List<Map<String,Object>> getAllAssessCheck(Integer schoolId);
    /*更改审核第几次状态*/
    int updateCheckAssess(Integer targetId,Double stateId);
    /*查询数字化评估进行到哪个阶段*/
    Map<String,Object> findTargetStage(Integer schoolId);
    /*第一次公示专业申诉状态反馈*/
    Map<String,Object> getMajorAppelFirstState(Integer targetId,Integer majorId);
    /*第二次公示专业申诉状态反馈*/
    Map<String,Object> getMajorAppelSecondState(Integer targetId,Integer majorId);
    /*星级榜单列表*/
    List<Map<String,Object>> getStarLevelList(Integer targetId,Integer starLevel,Integer stateId);
    /*黄榜列表*/
    List<Map<String,Object>> getYellowList(Integer targetId,Integer stateId);
    /*橙榜列表*/
    List<Map<String,Object>> getOrangeList(Integer targetId,Integer stateId);
    /*红榜列表*/
    List<Map<String,Object>> getRedList(Integer targetId,Integer stateId);
    /*导入评估模板*/

    /*添加指标类型*/
    int addTargetType(AssessDemo assessDemo);
    /*添加指标明细*/
    int addTargetDetils(AssessDemo assessDemo);
    /*删除同一个评估模板指标编号重复的指标明细*/
    int deleRepetIndex(AssessDemo assessDemo);
    /*查询同一个评估模板中指标类型相同类型Id*/
    Map<String,Object> getRepetTargetType(AssessDemo assessDemo);
    /*统计评估体系指标数量*/
    Map<String,Object> getAssessIndexNum(Integer targetId);
    /*查询参与评估的专业*/
    List<Map<String,Object>> getAssessMajor(Integer targetId);
    /*查询评估体系的所有指标信息*/
    List<Map<String,Object>> getAssessIndexInfo(Integer targetId);
    /*删除发起评估*/
    int deleAssess(Integer targetId);
    /*删除发起评估的专业*/
    int deleAssessMajor(Integer assessId);
    /*删除评估数据*/
    int deleAssessData(Integer targetId);
    /*删除第一次公式表数据*/
    int deleFirstPublic(Integer targetId);
    /*删除第二次公式表数据*/
    int deleSecondPublic(Integer targetId);
    /*删除第三次公式表数据*/
    int deleFinalPublic(Integer targetId);
    /*根据targetId获取学校*/
    Map<String,Object> getSchool(Integer targetId);
    /*查询星级的数量*/
    List<Map<String,Object>> getStarLevelNum(Integer targetId,Integer level);
    /*查询黄红橙榜专业*/
    List<Map<String,Object>> getYellowRedOrange(Integer targetId,Integer yellow,Integer red,Integer orange);
}
