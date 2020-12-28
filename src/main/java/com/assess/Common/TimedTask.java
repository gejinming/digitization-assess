package com.assess.Common;

import com.assess.mapper.AssessPublicMapper;
import com.assess.util.ReturnStateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: digitization-assess
 * @description: 定时任务生成结果
 * @author: Gjm
 * @create: 2020-05-07 17:36
 **/
/*@Component
@Configurable
@EnableScheduling*/
public class TimedTask {

    private final static Logger logger = LoggerFactory.getLogger(TimedTask.class);

    @Autowired
    AssessPublicMapper assessPublicMapper;




    /*
     * @param
     * @return void
     * @author Gejm
     * @description: 每日凌晨1点执行
     * @date 2020/5/13 11:44
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    public void publicResult(Integer schoolId){
        logger.info("=============定时生成榜单任务已进入=========="+new Date());

        //当前服务器时间
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate=new Date();
        String newDateformat = dateFormat.format(newDate);
        //查询正在进行的评估
        Map<String, Object> targetStage = assessPublicMapper.findTargetStage(schoolId);
        Integer targetId = ReturnStateUtil.returnInteger(targetStage.get("target_id"));
        //第一次公示开始与结束时间
        String first_publictime = targetStage.get("first_publictime")+" 00:00:00";
        String first_endtime = targetStage.get("first_endtime")+" 23:59:59";
        //第二次公示开始与结束时间
        String second_publictime = targetStage.get("second_publictime")+" 00:00:00";
        String second_endtime = targetStage.get("second_endtime")+" 23:59:59";
        try {
            //当前时间
            Date newDates = dateFormat.parse(newDateformat);
            //第一次公示开始时间
            Date firstPublicDate = dateFormat.parse(first_publictime);
            //第一次公示结束时间
            Date firstEndDate = dateFormat.parse(first_endtime);
            //第二次公示开始时间
            Date secondPublicDate = dateFormat.parse(second_publictime);
            //第二次公示结束时间
            Date secondEndDate = dateFormat.parse(second_endtime);
            //当前时间大于等于第一次公示开始时间，并且小于结束时间执行生成第一次公示榜单
            if (firstPublicDate.getTime()<= newDates.getTime()&& newDates.getTime()<firstEndDate.getTime()){
                logger.info("==================开始生成第一次公示榜单信息=================");
                //判断榜单是否已生成
                int isExist = ReturnStateUtil.returnIsExist(assessPublicMapper.isExistPublic(targetId,"pg_firstpublic"));
                if (isExist==120){
                    int i = assessPublicMapper.addFirstTargetMajorScore(targetId);
                    int stateId = ReturnStateUtil.returnState(i);
                    //结果已生成
                    if (stateId==104){
                        logger.info("==========第一次公示榜单生成成功！=======");
                        //更改进度参数
                        int i1 = assessPublicMapper.updateCheckAssess(targetId, 0.5);
                        stateId=ReturnStateUtil.returnUpdateState(i1);
                        if (stateId==108){
                            logger.info("==========更新发起评估状态成功！=======");
                        }else {
                            logger.error("==========更新发起评估状态失败！=======");
                        }

                    }else {
                        logger.error("==========第一次公示榜单生成失败！=======");
                    }

                }else{
                    logger.error("============第一次公示榜单已生成，无需再次生成！========");
                }
            }
            //当前时间大于等于第二次公示开始时间，并且小于结束时间执行生成第二次公示榜单
            else if (secondPublicDate.getTime()<= newDates.getTime()&& newDates.getTime()<secondEndDate.getTime()){
                logger.info("==================开始生成第二次公示榜单信息=================");
                int isExist = ReturnStateUtil.returnIsExist(assessPublicMapper.isExistPublic(targetId, "pg_secondpublic"));
                if (isExist==120) {
                    int i = assessPublicMapper.addMajorSecondPublic(0, targetId);
                    int stateId = ReturnStateUtil.returnState(i);
                    //结果已生成
                    if (stateId == 104) {
                        logger.info("==========第二次公示榜单生成成功！=======");
                        //更改进度参数
                        int i1 = assessPublicMapper.updateCheckAssess(targetId, 2.5);
                        stateId = ReturnStateUtil.returnUpdateState(i1);
                        if (stateId==108){
                            logger.info("==========更新发起评估状态成功！=======");
                        }else {
                            logger.error("==========更新发起评估状态失败！=======");
                        }
                    }else {
                        logger.error("==========第一次公示榜单生成失败！=======");
                    }

                }else {
                    logger.error("============第二次公示榜单已生成，无需再次生成！========");
                }
            }
            //当前时间大于等于第二次公示结束时间执行生成第三次公示榜单
            else if (newDates.getTime()>=secondEndDate.getTime()){
                //判断榜单是否重复生成
                int isExist = ReturnStateUtil.returnIsExist(assessPublicMapper.isExistPublic(targetId,"pg_finalpublic"));
                //isExist120 不存在，可以生成  121存在，不能生成
                if (isExist==120) {
                    //统计数据添加到最终公示表中
                    int addState = assessPublicMapper.addMajorFinalPublic(0, targetId);
                    int i1 = 0;
                    //以下统计星级、红橙黄榜
                    if (addState > 0) {
                        //是否红榜
                        int redCode = 1;
                        //是否黄榜
                        int yellowCode = 1;
                        //是否橙榜
                        int orangeCode = 1;
                        //本次最终评估结果列表
                        List<Map<String, Object>> finalPublicList = assessPublicMapper.getFinalPublicMajor(targetId);
                        //统计历年所获星级榜的专业id及评为星级次数
                        for (int i = 0; i < finalPublicList.size(); i++) {
                            Map<String, Object> finalPublicMap = finalPublicList.get(i);


                            //本次评估专业排名
                            Integer ranking = ReturnStateUtil.returnInteger(finalPublicMap.get("ranking"));
                            //专业id
                            Integer major_id = ReturnStateUtil.returnInteger(finalPublicMap.get("major_id"));
                            //评估专业数量
                            int sumMajor=finalPublicList.size();
                            //排名前五 评星级
                            if (ranking <= 3) {
                                //查询此专业是否存在有排名后十的评估，如果有，从下次评估重新累计星级
                                Map<String, Object> majorLastRanking = assessPublicMapper.getMajorLastRanking(major_id, sumMajor - 10,0);
                                int assessIds=0;
                                if (majorLastRanking!=null){
                                    assessIds=ReturnStateUtil.returnInteger(majorLastRanking.get("assess_id"));
                                }

                                Map<String, Object> majorStarLevel = assessPublicMapper.getMajorStarLevel(major_id,assessIds);
                                if (majorStarLevel!=null){
                                    Integer startNum = ReturnStateUtil.returnInteger(majorStarLevel.get("num"));
                                    startNum = startNum + 1;
                                    //累计2次 4星
                                    if (startNum == 2) {

                                        //修改此专业的星级数
                                        assessPublicMapper.updateMajorStarLevel(targetId, major_id, 4);
                                    }
                                    //累计大于或等于3次 5星
                                    else if (startNum >= 3) {

                                        int updateStarLevelState = assessPublicMapper.updateMajorStarLevel(targetId, major_id, 5);
                                    }
                                }else {
                                    //首次排名
                                    assessPublicMapper.updateMajorStarLevel(targetId, major_id, 3);
                                }

                            }

                            //排名中间的待处理
                            //后10取消星级称号

                            //后5排名的专业 亮黄牌  2>    9
                            System.out.println((finalPublicList.size() - 3)+"-----------888888888888888888888");
                            if (ranking > (finalPublicList.size() - 3)) {
                                int exist = 0;
                                //查询此专业此前评估的黄牌
                                Map<String, Object> majorYellowCode = assessPublicMapper.getMajorYellowCode(major_id);
                                if (majorYellowCode!=null){
                                    Integer yellowNum = ReturnStateUtil.returnInteger(majorYellowCode.get("num"));
                                    if (yellowNum == 1) {
                                        //加上此次排位后五，即为黄牌两次，评为橙牌
                                        assessPublicMapper.updateMajorOrangeCode(targetId, major_id, orangeCode);
                                    } else if (yellowNum >= 2) {
                                        //加上此次排位后五，即最少黄牌三次，评为红牌
                                        assessPublicMapper.updateMajorRedCode(targetId, major_id, redCode);

                                    }
                                }else {
                                    //即为首次排位后五，评为黄牌
                                    assessPublicMapper.updateMajorYellowCode(targetId, major_id, yellowCode);
                                }


                                // 查询此前评估的橙牌列表
                                Map<String, Object> majorOrangeCode = assessPublicMapper.getMajorOrangeCode(major_id);
                                if (majorOrangeCode!=null){
                                    //因为此次排名后五，只要有橙牌就评为红牌
                                    assessPublicMapper.updateMajorRedCode(targetId, major_id, redCode);
                                }

                                // 查询此前评估的红牌列表
                                Map<String, Object> majorRedCode = assessPublicMapper.getMajorRedCode(major_id);
                                if (majorRedCode!=null){
                                    //上次红牌本次一样红牌
                                    assessPublicMapper.updateMajorRedCode(targetId, major_id, redCode);
                                }

                            }


                        }

                    }
                    int state = ReturnStateUtil.returnState(addState);
                    //结果已生成
                    if (state == 104) {
                        logger.info("==========最终公示榜单生成成功！=======");
                        //更改进度参数
                        int i = assessPublicMapper.updateCheckAssess(targetId, 4.5);
                        int stateId = ReturnStateUtil.returnUpdateState(i);
                        if (stateId==108){
                            logger.info("==========更新发起评估状态成功！=======");
                        }else {
                            logger.error("==========更新发起评估状态失败！=======");
                        }
                    }else {
                        logger.info("==========最终公示榜单生成成功！=======");
                    }

                }else {
                    logger.error("============最终公示榜单已生成，无需再次生成！========");
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

}
