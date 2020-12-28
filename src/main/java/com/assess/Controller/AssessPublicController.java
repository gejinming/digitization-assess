package com.assess.Controller;

import com.alibaba.fastjson.JSONArray;
import com.assess.Annotation.PassToken;
import com.assess.entity.MajorAppel;
import com.assess.entity.MajorData;
import com.assess.entity.StartAssess;
import com.assess.mapper.AssessPublicMapper;
import com.assess.mapper.MajorDataWriteMapper;
import com.assess.util.ReturnStateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: digitization-assess
 * @description: 发起评估  评估结果公示
 * @author: Gjm
 * @create: 2020-03-24 17:48
 **/
@RestController
public class AssessPublicController {
    @Autowired
    AssessPublicMapper assessPublicMapper;
    @Autowired
    MajorDataWriteMapper majorDataWriteMapper;
    /*
     * @param schoolId
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     * @author Gejm
     * @description: 开启评估
     * @date 2020/5/20 10:15
     */
    @GetMapping("/getAssessAndMajor")
    public Map getAssessAndMajor(Integer schoolId){
        Map<String, Object> result = new HashMap();
        /*Calendar date = Calendar.getInstance();
        String year1 = String.valueOf(date.get(Calendar.YEAR));
        Integer year=Integer.parseInt(year1);
        //判断今年的评估体系是否创建
        List<Map<String, Object>> newYearAssess = assessPublicMapper.getNewYearAssess(year);
        if (newYearAssess.size()==0){
            //没有，先创建
            int i = assessPublicMapper.addNewYear(year);
        }*/
        //获取目前已评估最大评估的年度
        Map<String, Object> assessMaxYear = assessPublicMapper.getAssessMaxYear(schoolId);
        Integer targetMaxYear=0;
        if (assessMaxYear!=null){
            targetMaxYear= ReturnStateUtil.returnInteger(assessMaxYear.get("targetyear"));
        }
        //获取没有用过的评估模板
        List<Map<String, Object>> yearAssess = assessPublicMapper.getYearAssess(schoolId);
        //获取学院专业信息
        List<Map<String, Object>> allMajor = assessPublicMapper.getAllCollegeMajor(schoolId);
        List<Map<String, Object>> allCollege = assessPublicMapper.getAllCollege(schoolId);


        /*lists.add(yearAssess);*/
        result.put("yearAssess",yearAssess);
        result.put("allCollege",allCollege);
        result.put("allMajor",allMajor);
        result.put("targetMaxYear",targetMaxYear);
        return result;
    }
    /*
     * @param startAssess
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author Gejm
     * @description: 添加开启评估时的数据
     * @date 2020/3/27 16:12
     */
    @PostMapping("/startAssess")
    public Map<String, Object> startAssess(@RequestBody  StartAssess startAssess){
        //System.out.println(startAssess.getMajorIds().length);
        //添加开启评估时的数据
        int sta = assessPublicMapper.addNewAssess(startAssess);
        //将选择的评估体系模板改为已用状态
        int i1 = assessPublicMapper.updateStartTarget(startAssess.getTargetId(),startAssess.getTargetYear(),1);
        int state;
        if (sta==1 && i1==1){
            //添加评估专业
            int i = assessPublicMapper.addAssessMajor(startAssess.getAssessId(),startAssess.getMajorIds());
            if (i!=0){
                //发起评估成功
                state=110;
            }else {
                state=111;
            }

        }else {
            state=111;
        }
        Map<String, Object> Map = new HashMap();
        Map.put("state",state);
        return Map;
    }
    /*
     * @param targetId
     * @return java.util.Map
     * @author Gejm
     * @description: 生成结果统计数据加入第一次公示表
     * @date 2020/4/3 15:38
     */
    @GetMapping("/addFirstPublic")
    public Map addFirstPublic(Integer targetId){
        Map<String, Object> Map = new HashMap();
        //判断榜单是否已生成
        int isExist = ReturnStateUtil.returnIsExist(assessPublicMapper.isExistPublic(targetId,"pg_firstpublic"));
        if (isExist==120){


            int i = assessPublicMapper.addFirstTargetMajorScore(targetId);
            int stateId = ReturnStateUtil.returnState(i);
            //结果已生成
            if (stateId==104){
                //更改进度参数
                int i1 = assessPublicMapper.updateCheckAssess(targetId, 0.5);
                 stateId=ReturnStateUtil.returnUpdateState(i1);

            }
            Map.put("state",stateId);
        }else {
            Map.put("state",isExist);
        }


        return Map;
    }
    /*
     * @param
     * @return java.util.Map
     * @author Gejm
     * @description:各阶段的生成结果和提交审核，将发起评估表评估状态改变
     * @date 2020/4/23 9:28
     */
    @PostMapping("/summitCheckList")
    public Map summitCheckList(Integer targetId,double stateId){
        int updateState = assessPublicMapper.updateCheckAssess(targetId, stateId);
        int state = ReturnStateUtil.returnUpdateState(updateState);
        Map<String, Object> Map = new HashMap();
        Map.put("state",state);
        return Map;
    }
    /*
     * @param targetId
     * @return java.util.Map
     * @author Gejm
     * @description: 领导审核各专业得分排名列表
     * @date 2020/3/27 19:26
     */
    @GetMapping("/firstCheckPublic")
    public Map firstPublic(Integer targetId){
        HashMap hashMap = new HashMap();
        //查询统计公示数据
        List<Map<String, Object>> list = assessPublicMapper.firstPublicMajorScore(targetId,0);
        hashMap.put("list",list);
        return hashMap;
    }
    /*
     * @param assessId
     * @param stateId
     * @return java.util.Map
     * @author Gejm
     * @description: 领导审核第一次公示各专业得分及排名，审核过之后公示
     * @date 2020/3/31 10:51
     */
    @PostMapping("/checkFirstPublic")
        public Map checkPublic(Integer assessId,Integer stateId,Integer userId,Integer targetId){
        int i = assessPublicMapper.updateCheckFirstState(assessId, stateId,userId);
        int i1=0;
        if (i>0){
            //领导第一次审核通过，将发起评估表评估状态改为2
            i1 = assessPublicMapper.updateCheckAssess(targetId, ReturnStateUtil.returnDouble(2));
        }
        int state = ReturnStateUtil.returnUpdateState(i1);
        HashMap hashMap = new HashMap();
        hashMap.put("state",state);

        return  hashMap;

    }
    /*
     * @param assessId
     * @return java.util.Map
     * @author Gejm
     * @description: 领导审核后第一次公示
     * @date 2020/3/27 19:30
     */
    @GetMapping("/selectFirstPublic")
    public Map selectfirstPublic(Integer targetId){
        List<Map<String, Object>> list = assessPublicMapper.firstPublicMajorScore(targetId,1);
        int state=0;
        if (list.size()!=0){
            state=104;
        }else {
            state=105;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        hashMap.put("list",list);
        return hashMap;
    }

    /*
     * @param assessId
    	 * @param targetId
    	 * @param majorId
    	 * @param publicId
     * @return java.util.List
     * @author Gejm
     * @description: 第一次公示点击专业进入详情页面 查询此体系的所有类型及此专业总得分
     * @date 2020/3/30 10:58
     */
    @GetMapping("/findFirstInfo")
    public Map findFirstInfo(Integer assessId,Integer targetId,Integer majorId,Integer publicId){
        HashMap hashMap = new HashMap();
        //查询这个评估体系所有的类型
        List<Map<String, Object>> allTargetType = assessPublicMapper.getAllTargetType(targetId);
        hashMap.put("targetType",allTargetType);
        //查询这个专业的成绩
        Map<String, Object> majorScore = assessPublicMapper.getFirstMajorScore(publicId);
        Object first_endtime = majorScore.get("first_endtime");
        hashMap.put("majorScore",majorScore);
        hashMap.put("first_endtime",first_endtime);
        return hashMap;
    }
    /*
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 查询类型对应所有指标的数据
     * @date 2020/3/30 15:40
     */
    @GetMapping("/getTargetData")
    public List getAllTargetDetaildata(Integer targetTypeId,Integer publicState,Integer majorId){
        //由体系类型查询对应所有指标
        List<Map<String, Object>> firstdataInfo = assessPublicMapper.findFirstInfo(targetTypeId, majorId,publicState);
        //将从数据库中查的string类型的json转为json
        for (int i=0;i<firstdataInfo.size();i++){
            Map  firstdataInfoMap= firstdataInfo.get(i);
            String fourTitle= (String) firstdataInfoMap.get("arithmetic_four_title");

            if (fourTitle!=null){
                firstdataInfoMap.put("arithmetic_four_title", JSONArray.parse(fourTitle));
            }

            String arithmeticThree= (String) firstdataInfoMap.get("arithmetic_three");
            if (arithmeticThree!=null){
                firstdataInfoMap.put("arithmetic_three",JSONArray.parse(arithmeticThree));
            }
            String arithmeticFourData= (String) firstdataInfoMap.get("arithmetic_four_data");
            if (arithmeticFourData!=null){
                firstdataInfoMap.put("arithmetic_four_data",JSONArray.parse(arithmeticFourData));
            }



        }
        return firstdataInfo;
    }
    /*
     * @param majorAppel
     * @return java.util.Map
     * @author Gejm
     * @description: 公示时间节点判断
     * @date 2020/5/7 16:49
     */
    @GetMapping("/checkPublicDate")
    public Map checkAppelDate(MajorAppel majorAppel,Integer schoolId){
        HashMap result = new HashMap();
        //当前服务器时间
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate=new Date();
        String newDateformat = dateFormat.format(newDate);

        //公示序号
        Integer publicCount=majorAppel.getPublicCount();
        Map<String, Object> targetStage = assessPublicMapper.findTargetStage(schoolId);
        //第一次公示
        if (publicCount==1){
            //第一次公示开始与结束时间
            String first_publictimes = targetStage.get("first_publictime")+" 00:00:00";
            String first_endtimes = targetStage.get("first_endtime")+" 23:59:59";
            try {
                //当前时间
                Date newDates = dateFormat.parse(newDateformat);
                //第一次公示开始时间
                Date firstPublicDate = dateFormat.parse(first_publictimes);
                //第一次公示结束时间
                Date firstEndDate = dateFormat.parse(first_endtimes);
                //时间节点比较
                //116 在公示时间范围内！ 117 不在公示时间范围内！
                if (firstPublicDate.getTime()<= newDates.getTime()&& newDates.getTime()<= firstEndDate.getTime()){
                    result.put("state",116);
                }else {
                    result.put("state",117);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            //第二次公示开始与结束
        }else if (publicCount==2){
            System.out.println("========+++++++++++222222222222=====");
            //第二次公示开始与结束时间
            String second_publictimes = targetStage.get("second_publictime")+" 00:00:00";
            String second_endtimes = targetStage.get("second_endtime")+" 23:59:59";
            try {
                //当前时间
                Date newDates = dateFormat.parse(newDateformat);
                //第二次公示开始时间
                Date secondPublicDate = dateFormat.parse(second_publictimes);
                //第二次公示结束时间
                Date secondEndDate = dateFormat.parse(second_endtimes);
                //时间节点比较
                //116 在公示时间范围内！ 117 不在公示时间范围内！
                if (secondPublicDate.getTime()<= newDates.getTime()&& newDates.getTime()<= secondEndDate.getTime()){
                    result.put("state",116);
                }else {
                    System.out.println("========+++++++++++=====");
                    result.put("state",117);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    /*
     * @param majorAppel
     * @return java.util.Map
     * @author Gejm
     * @description: 专业添加申诉理由
     * @date 2020/3/31 14:04
     */
    @PostMapping("/addPublicMajorAppeal")
    public Map addPublicMajorAppeal(MajorAppel majorAppel,@RequestParam(value="file",required=false) MultipartFile file){

        Integer publicCount=majorAppel.getPublicCount();

        if (file !=null ) {
            //文件上传
            String fileUrl = FileUploadOrDownController.uploadFile(file);
            //将生成的文件名传来
            majorAppel.setAppelStuffurl(fileUrl);
        }
        //第一次公示申诉为1
        //majorAppel.setPublicCount(1);
        //添加申诉信息
        int i = assessPublicMapper.addPublicMajorAppeal(majorAppel);
        int state = ReturnStateUtil.returnState(i);
        //添加成功再修改
        if (state==104 && publicCount==1){
            //申诉更改第一次公示表状态改为申诉状态
            int i1 = assessPublicMapper.updateFirstPublicState(majorAppel.getPublicId(),1);
            state = ReturnStateUtil.returnState(i1);
        }
        else if(state==104 && publicCount==2){
            //申诉更改第二次公示表状态
            int i1 = assessPublicMapper.updateSecondPublicState(majorAppel.getPublicId(), 1);
            state = ReturnStateUtil.returnState(i1);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        return hashMap;
    }
    /*
     * @param targetId
    	 * @param majorId
     * @return java.util.Map
     * @author Gejm
     * @description: 查询申诉信息
     * @date 2020/4/14 10:53
     */
    @GetMapping("/findMajorAppelState")
    public Map findMajorAppelState(Integer targetId,Integer majorId,Integer schoolId){
        Map<String, Object> targetStage = assessPublicMapper.findTargetStage(schoolId);
        Object check_ido = targetStage.get("check_id");
        double check_id = ReturnStateUtil.returnDouble(check_ido);
        Map<String, Object> result;
        //判断这个评估体系进行到哪个阶段
        if (check_id<=3){
            //进行到第一次公示，与第一次公示表连查申诉表数据
            result = assessPublicMapper.getMajorAppelFirstState(targetId, majorId);
        }else{
            //进行到第二次公示，与第二次公示表连查申诉表数据
            result = assessPublicMapper.getMajorAppelSecondState(targetId,majorId);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("result",result);

        return hashMap;
    }
    /*
     * @param assessId
     * @param targetId
     * @param majorId
     * @param publicId
     * @return java.util.List
     * @author Gejm
     * @description: s公示审核申诉查询专业详细信息
     * @date 2020/3/30 10:58
     */
    @GetMapping("/CheckAppel")
    public Map firstCheckAppel(Integer assessId,Integer targetId,Integer majorId,Integer publicId,Integer publicCount){
        HashMap hashMap = new HashMap();
        //查询这个评估体系所有的类型
        List<Map<String, Object>> allTargetType = assessPublicMapper.getAllTargetType(targetId);
        hashMap.put("targetType",allTargetType);
        //查询这个专业的总成绩
        Map<String, Object> majorScore;
        Object endtime;
        //第一次申诉
        if (publicCount==1){
             majorScore = assessPublicMapper.getFirstMajorScore(publicId);
             endtime = majorScore.get("first_endtime");
        }else {
            //第二次申诉
             majorScore = assessPublicMapper.getSecondMajorScore(publicId);
            endtime = majorScore.get("second_endtime");
        }


        //查询公示申诉理由及材料地址
        Map<String, Object> majorAppelReason = assessPublicMapper.getMajorAppelReason(publicId, publicCount);


        hashMap.put("majorScore",majorScore);
        hashMap.put("firstEndtime",endtime);
        hashMap.put("majorAppelReason",majorAppelReason);
        return hashMap;
    }
    /*
     * @param targetId
    	 * @param stateId
     * @return java.util.List
     * @author Gejm
     * @description: 查询公示申诉专业列表
     * @date 2020/3/31 14:02
     */
    @GetMapping("/getMajorAppelList")
    public List getMajorAppelList(Integer targetId,Integer stateId,Integer publicState){
        List<Map<String, Object>> majorAppelList;

        if (publicState==1) {
            //第一次公示申诉列表
            majorAppelList = assessPublicMapper.getMajorFirstAppelList(targetId, stateId);
        }else {
            //第二次公示申诉列表
            majorAppelList = assessPublicMapper.getMajorSecondAppelList(targetId, stateId);
        }
        return majorAppelList;
    }
    /*
     * @param publicId
    	 * @param stateId
     * @return java.util.Map
     * @author Gejm
     * @description: 申诉审核同意或驳回
     * @date 2020/3/31 14:11
     */
    @PostMapping("/checkAppelState")
    public  Map checkAppelState(MajorAppel majorAppel,@RequestParam(value="file",required=false) MultipartFile file){
        if ( file !=null){
            //文件上传
            String fileUrl = FileUploadOrDownController.uploadFile(file);

            //将生成的文件名传来
            majorAppel.setRejectStuffurl(fileUrl);
        }

        //1同意2驳回
        Integer appelState = majorAppel.getAppelState();
        //1是第一次公示 2是第二次公公示
        Integer publicCount = majorAppel.getPublicCount();
        int i=0;
        if (publicCount==1) {
            //第一次公示表中申诉状态2同意3驳回
            i = assessPublicMapper.updateFirstPublicState(majorAppel.getPublicId(), appelState + 1);
        }else if (publicCount==2){
            //第二次公示表中申诉状态2同意3驳回
            i =assessPublicMapper.updateSecondPublicState(majorAppel.getPublicId(), appelState + 1);
        }
        //修改申诉表的申诉状态,添加审核人审核时间 驳回理由 材料
        int state = 109;
        if (i!=0){
            int i1 = assessPublicMapper.updatePublicMajorAppeal(majorAppel);
            state = ReturnStateUtil.returnUpdateState(i1);
        }

        HashMap hashMap = new HashMap();
        hashMap.put("state",1);
        return hashMap;
    }
    /*
     * @param publicId
    	 * @param publicCount
     * @return java.util.Map
     * @author Gejm
     * @description: 查询专业申诉信息，驳回信息
     * @date 2020/4/13 15:38
     */
    @GetMapping("/getMajorAppelReason")
    public Map getMajorAppelReason(Integer publicId, Integer publicCount){
        Map<String, Object> majorAppelReason = assessPublicMapper.getMajorAppelReason(publicId, publicCount);
        HashMap hashMap = new HashMap();
        hashMap.put("majorAppelReason",majorAppelReason);
        return hashMap;
    }
    /*
     * @param majorData
     * @return java.util.Map
     * @author Gejm
     * @description: 专业申诉审核同意，并修改专业提交数据，修改的数据作为新数据添加到数据表pg_target_type_date
     * @date 2020/4/1 9:19
     */
    @PostMapping("/addMajorDataWriteMa")
    @ResponseBody
    public  Map addMajorDataWriteMa(@RequestBody MajorData majorData){
        //算法4填写的JSON转string
        if (majorData.getArithmeticFourDataJson()!=null){
            JSONArray arithmeticFourData = (JSONArray) JSONArray.toJSON(majorData.getArithmeticFourDataJson());
            //将转的string类型Json数据存入string类型的算法4数据实体类
            majorData.setArithmeticFourData(arithmeticFourData.toString());
        }
        //dataId为null或0新增，否则修改
        int i;
        if (majorData.getDataId()==null || majorData.getDataId().equals(0)){

             i = assessPublicMapper.addMajorDataWriteMa(majorData);

        }else{
             i = majorDataWriteMapper.updateMajorDataWrite(majorData);

        }
        int state = ReturnStateUtil.returnState(i);
        HashMap map = new HashMap();
        map.put("state",state);
        map.put("dataId",majorData.getDataId());
        return map;
    }
    /*
     * @param targetId
    	 * @param stateId
     * @return java.util.Map
     * @author Gejm
     * @description: 获取提交提交材料专业的成绩及排名，或获取未提交材料专业的信息
     * @date 2020/3/31 16:58
     */
    @GetMapping("/getMajorSummitList")
    public  List getMajorSummitList(Integer targetId, Integer stateId){
        List<Map<String, Object>> majorSummitList = assessPublicMapper.getMajorSummitList(targetId, stateId);
        return majorSummitList;
    }
    /*
     * @param targetId
     * @param majorId
     * @return java.util.List
     * @author Gejm
     * @description: 点击提交材料专业查询指标类型，再点击类型调用/getTargetData方法出现类型对应数据
     * @date 2020/3/30 10:58
     */
    @GetMapping("/getMajorSummitInfo")
    public Map getMajorSummitInfo(Integer targetId,Integer majorId,double score){
        HashMap hashMap = new HashMap();
        //查询这个评估体系所有的类型
        List<Map<String, Object>> allTargetType = assessPublicMapper.getAllTargetType(targetId);
        hashMap.put("targetType",allTargetType);
        hashMap.put("sumScore",score);
        return hashMap;
    }
    /*
     * @param assessId
    	 * @param targetId
     * @return java.util.Map
     * @author Gejm
     * @description: 第二次生成结果统计数据
     * @date 2020/4/8 16:05
     */
    @PostMapping("/addSecondPublicData")
    public Map countSecondPublicData(Integer assessId,Integer targetId){
        HashMap hashMap = new HashMap();
        int isExist = ReturnStateUtil.returnIsExist(assessPublicMapper.isExistPublic(targetId, "pg_secondpublic"));
        if (isExist==120) {
            int i = assessPublicMapper.addMajorSecondPublic(assessId, targetId);
            int stateId = ReturnStateUtil.returnState(i);
            //结果已生成
            if (stateId == 104) {
                //更改进度参数
                int i1 = assessPublicMapper.updateCheckAssess(targetId, 2.5);
                stateId = ReturnStateUtil.returnUpdateState(i1);
            }
            hashMap.put("state",stateId);
        }else {
            hashMap.put("state",isExist);
        }


        return hashMap;
    }
    /*
     * @param targetId
     * @return java.util.List
     * @author Gejm
     * @description: 教务处领导审核第二次公示列表
     * @date 2020/4/1 17:45
     */
    @GetMapping("/getCheckSecondPublic")
    public Map getCheckSecondPublic(Integer targetId){
        List<Map<String, Object>> checkSecondPublic = assessPublicMapper.getCheckSecondPublic(targetId);
        HashMap hashMap = new HashMap();
        //获取评估体系名称
        hashMap.put("targetYear",checkSecondPublic.get(0).get("target_year"));
        hashMap.put("targetName",checkSecondPublic.get(0).get("target_name"));
        //获取第二次公示截至时间
        hashMap.put("secondEndtime",checkSecondPublic.get(0).get("second_endtime"));
        hashMap.put("checkSecondPublic",checkSecondPublic);
        return  hashMap;
    }
    /*
     * @param assessId
    	 * @param stateId
    	 * @param userId
     * @return java.util.Map
     * @author Gejm
     * @description: 教务处领导审核第二次公示，通过后公示
     * @date 2020/4/1 15:11
     */
    @PostMapping("/checkSecondPublic")
    public Map checkSecondPublic(Integer targetId,Integer stateId,Integer userId){
        int i = assessPublicMapper.updateCheckSecondPublic(targetId, stateId, userId);
        int i1=0;
        if (i>0){
            i1 = assessPublicMapper.updateCheckAssess(targetId, ReturnStateUtil.returnDouble(4));
        }
        int state = ReturnStateUtil.returnUpdateState(i1);
        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        return hashMap;
    }
    /*
     * @param targetId
     * @return java.util.List
     * @author Gejm
     * @description: 第二次公示
     * @date 2020/4/1 17:46
     */
    @GetMapping("/getSecondPublic")
    public Map getSecondPublic(Integer targetId){
        HashMap hashMap = new HashMap();
        List<Map<String, Object>> secondPublic = assessPublicMapper.getSecondPublic(targetId);
        //获取评估体系名称
        hashMap.put("targetYear",secondPublic.get(0).get("target_year"));
        hashMap.put("targetName",secondPublic.get(0).get("target_name"));
        //获取第二次公示截至时间
        hashMap.put("secondEndtime",secondPublic.get(0).get("second_endtime"));
        hashMap.put("secondPublic",secondPublic);
        return  hashMap;
    }
    /*
     * @param assessId
    	 * @param targetId
    	 * @param majorId
    	 * @param publicId
     * @return java.util.Map
     * @author Gejm
     * @description: 第二次，查看专业详情
     * @date 2020/4/2 9:59
     */
    @GetMapping("/findSecondMajorInfo")
    public Map findSecondMajorInfo(Integer assessId,Integer targetId,Integer majorId,Integer publicId){
        HashMap hashMap = new HashMap();
        //查询这个评估体系所有的类型
        List<Map<String, Object>> allTargetType = assessPublicMapper.getAllTargetType(targetId);
        hashMap.put("targetType",allTargetType);
        //查询这个专业的成绩
        Map<String, Object> majorScore = assessPublicMapper.getSecondMajorScore(publicId);
        Object second_endtime = majorScore.get("second_endtime");
        hashMap.put("majorScore",majorScore);
        hashMap.put("secondEndtime",second_endtime);
        //查询第一次申诉理由
        Map<String, Object> majorFirstAPPel = assessPublicMapper.getMajorFirstAPPel(targetId, majorId);
        //判断此专业是否有申诉
        int state=0;
        if (majorFirstAPPel ==null){
            state=0;

        }
        else if (majorFirstAPPel != null){
            state=1;
            hashMap.put("majorFirstAPPel",majorFirstAPPel);
        }
        hashMap.put("state",state);
        return hashMap;
    }
    /*
     * @param assessId
    	 * @param targetId
     * @return java.util.Map
     * @author Gejm
     * @description: 统计最后公示列表并设计星级榜、红橙榜
     * 目前星级榜、红橙榜设计规则
        星级榜规则
            排名前五评为星级规则
                首次专业排名前五，评为3星
                如果后面评估出现一次排名后10重新累计次数
                累计两次前五评为4星，累计3次前五评为5星。
            排名6-后10之间的专业星级规则
               上次评估有星级的专业，继承上次的星级
        黄橙红榜规则
            首次排名后五，评为黄牌
            连续两次排名后五，评为橙牌
            累计三次排名后五，评为红牌
     * @date 2020/4/2 14:42
     */
    @Transactional
    @PostMapping("/countFinalPublicData")
    public Map countFinalPublicData(Integer assessId,Integer targetId){
        HashMap hashMap = new HashMap();
        Map<String, Object> assessMap = assessPublicMapper.getAssessId(targetId);
        if (assessMap!=null){
            assessId=ReturnStateUtil.returnInteger(assessMap.get("assess_id"));
        }

        //判断榜单是否重复生成
        int isExist = ReturnStateUtil.returnIsExist(assessPublicMapper.isExistPublic(targetId,"pg_finalpublic"));
        //isExist120 不存在，可以生成  121存在，不能生成
        if (isExist==120) {
            //统计数据添加到最终公示表中
            int addState = assessPublicMapper.addMajorFinalPublic(assessId, targetId);
            Map<String, Object> schoolMap = assessPublicMapper.getSchool(targetId);
            String schoolName = schoolMap.get("school_name").toString();
            Integer targetYear = ReturnStateUtil.returnInteger(schoolMap.get("target_year"));
            //针对“浙江科技学院”2018年开始生成星级榜和黄橙红榜 18年之前只生成黄橙红榜
            if (schoolName.equals("浙江科技学院") && targetYear<2018){
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
                        int sumMajor = finalPublicList.size();

                        if (ranking > sumMajor - 5) {
                            //判断优先级 ，先判断是否存在有红牌，有则此次评红，无则继续判断是否有橙牌，
                            // 有则此次评为红，无则继续判断上次评估此专业是否为黄牌，如果是则评为橙，否则评为黄
                            int exist = 0;
                            // 查询此专业此前评估的是否为红牌
                            Map<String, Object> majorRedCode = assessPublicMapper.getMajorRedCode(major_id);
                            if (majorRedCode!=null){
                                //上次红牌本次一样红牌
                                assessPublicMapper.updateMajorRedCode(targetId, major_id, redCode);
                            }else {
                                // 查询此前的评估的是否为橙牌
                                Map<String, Object> majorOrangeCode = assessPublicMapper.getMajorOrangeCode(major_id);
                                if (majorOrangeCode != null) {
                                    //因为此次排名后五，只要有橙牌就评为红牌
                                    assessPublicMapper.updateMajorRedCode(targetId, major_id, redCode);
                                }else{
                                    //查询此专业是否连续两次评估为黄牌
                                    Map<String, Object> majorTwice = assessPublicMapper.getMajorTwice(major_id, assessId);
                                    Integer yellowCards = 0;
                                    if (majorTwice != null) {
                                        yellowCards = ReturnStateUtil.returnInteger(majorTwice.get("yellow_card"));
                                    }
                                    if (yellowCards != 0) {
                                        //如果上次评估为黄牌，这次又是后五，此次评为橙牌
                                        assessPublicMapper.updateMajorOrangeCode(targetId, major_id, orangeCode);
                                    } else {
                                        //如果上次评估不是黄牌，则这次是后五，评为黄牌
                                        assessPublicMapper.updateMajorYellowCode(targetId, major_id, yellowCode);
                                    }
                                }

                            }
                        }

                    }
                }
            }else {
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
                        //查询此专业是否存在有排名后十的评估，如果有，从下次评估重新累计星级
                        Map<String, Object> majorLastRanking = assessPublicMapper.getMajorLastRanking(major_id, sumMajor - 10,assessId);
                        int assessIds=0;
                        if (majorLastRanking!=null){
                            assessIds=ReturnStateUtil.returnInteger(majorLastRanking.get("assess_id"));
                        }



                        //排名前五 评星级
                        if (ranking<=5) {
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
                        //18   6-13

                        //排名中间的继承上次星级sumMajor - 5
                        else if(ranking>5 && ranking<=sumMajor - 5){
                            Map<String, Object> majorStarLevelNum = assessPublicMapper.getMajorStarLevelNum(major_id, assessIds);
                            if (majorStarLevelNum!=null){
                                Integer star_level = ReturnStateUtil.returnInteger(majorStarLevelNum.get("star_level"));
                                int updateStarLevelState = assessPublicMapper.updateMajorStarLevel(targetId, major_id, star_level);
                            }
                        }


                        //后5排名的专业 亮黄牌  2>    9

                        if (ranking > sumMajor - 5) {
                            //判断优先级 ，先判断是否存在有红牌，有则此次评红，无则继续判断是否有橙牌，
                            // 有则此次评为红，无则继续判断上次评估此专业是否为黄牌，如果是则评为橙，否则评为黄
                            int exist = 0;
                            // 查询此专业此前评估的是否为红牌
                            Map<String, Object> majorRedCode = assessPublicMapper.getMajorRedCode(major_id);
                            if (majorRedCode!=null){
                                //上次红牌本次一样红牌
                                assessPublicMapper.updateMajorRedCode(targetId, major_id, redCode);
                            }else {
                                // 查询此前的评估的是否为橙牌
                                Map<String, Object> majorOrangeCode = assessPublicMapper.getMajorOrangeCode(major_id);
                                if (majorOrangeCode != null) {
                                    //因为此次排名后五，只要有橙牌就评为红牌
                                    assessPublicMapper.updateMajorRedCode(targetId, major_id, redCode);
                                }else{
                                    //查询此专业是否连续两次评估为黄牌
                                    Map<String, Object> majorTwice = assessPublicMapper.getMajorTwice(major_id, assessId);
                                    Integer yellowCards = 0;
                                    if (majorTwice != null) {
                                        yellowCards = ReturnStateUtil.returnInteger(majorTwice.get("yellow_card"));
                                    }
                                    if (yellowCards != 0) {
                                        //如果上次评估为黄牌，这次又是后五，此次评为橙牌
                                        assessPublicMapper.updateMajorOrangeCode(targetId, major_id, orangeCode);
                                    } else {
                                        //如果上次评估不是黄牌，则这次是后五，评为黄牌
                                        assessPublicMapper.updateMajorYellowCode(targetId, major_id, yellowCode);
                                    }
                                }






                            }



                        }


                    }

                }
            }

            int state = ReturnStateUtil.returnState(addState);
            //结果已生成
            if (state == 104) {
                //更改进度参数
                int i = assessPublicMapper.updateCheckAssess(targetId, 4.5);
                state = ReturnStateUtil.returnUpdateState(i);
            }
            hashMap.put("state",state);
        }

        else {
            hashMap.put("state",isExist);
        }


        return hashMap;
    }

    /*
     * @param targetId
     * @return java.util.Map
     * @author Gejm
     * @description: 教务处领导查询最终公示列表
     * @date 2020/4/3 9:51
     */
    @GetMapping("/getCheckFinalPublicList")
    public Map getCheckFinalPublicList(Integer targetId){

        List<Map<String, Object>> checkFinalPublic = assessPublicMapper.getCheckFinalPublicList(targetId);

        HashMap hashMap = new HashMap();
        //获取评估体系年度
        hashMap.put("targetYear",checkFinalPublic.get(0).get("target_year"));
        //获取评估体系名称
        hashMap.put("targetName",checkFinalPublic.get(0).get("target_name"));
        //获取第二次公示截至时间
        hashMap.put("secondEndtime",checkFinalPublic.get(0).get("second_endtime"));
        //公示列表
        hashMap.put("checkFinalPublic",checkFinalPublic);
        //3星级榜
        List<Map<String, Object>> starLevelThreeList = assessPublicMapper.getStarLevelList(targetId, 3, 0);
        hashMap.put("starLevelThreeList",starLevelThreeList);
        //4星级榜
        List<Map<String, Object>> starLevelFourList = assessPublicMapper.getStarLevelList(targetId, 4, 0);
        hashMap.put("starLevelFourList",starLevelFourList);
        //5星级榜
        List<Map<String, Object>> starLevelFiveList = assessPublicMapper.getStarLevelList(targetId, 5, 0);
        hashMap.put("starLevelFiveList",starLevelFiveList);
        //黄榜
        List<Map<String, Object>> yellowList = assessPublicMapper.getYellowList(targetId, 0);
        hashMap.put("yellowList",yellowList);
        //橙榜
        List<Map<String, Object>> orangeList = assessPublicMapper.getOrangeList(targetId, 0);
        hashMap.put("orangeList",orangeList);
        List<Map<String, Object>> redList = assessPublicMapper.getRedList(targetId, 0);
        hashMap.put("redList",redList);
        return  hashMap;
    }
    /*
     * @param targetId
    	 * @param stateId
    	 * @param userId
     * @return java.util.Map
     * @author Gejm
     * @description: 教务处领导审核最终公示
     * @date 2020/4/3 9:40
     */
    @PostMapping("/checkFinalPublic")
    public Map checkFinalPublic(Integer targetId,Integer stateId,Integer userId){
        int i = assessPublicMapper.updateCheckFinalPublic(targetId, stateId, userId);
        int i1=0;
        if (i>0){
            //教务处领导已审批第三次公示 状态改为6
            i1 = assessPublicMapper.updateCheckAssess(targetId, ReturnStateUtil.returnDouble(6));
        }

        int state = ReturnStateUtil.returnUpdateState(i1);
        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        return hashMap;
    }
    /*
     * @param assessId
     * @param targetId
     * @param majorId
     * @param publicId
     * @return java.util.Map
     * @author Gejm
     * @description: 最终公示，查看专业详情
     * @date 2020/4/2 9:59
     */
    @GetMapping("/findFinalMajorInfo")
    public Map findFinalMajorInfo(Integer assessId,Integer targetId,Integer majorId,Integer publicId) {
        HashMap hashMap = new HashMap();
        //查询这个评估体系所有的类型
        List<Map<String, Object>> allTargetType = assessPublicMapper.getAllTargetType(targetId);
        hashMap.put("targetType", allTargetType);
        //查询这个专业的成绩
        Map<String, Object> majorScore = assessPublicMapper.getFinalMajorScore(publicId);
        Object second_endtime = majorScore.get("second_endtime");
        hashMap.put("majorScore", majorScore);
        hashMap.put("secondEndtime", second_endtime);
        //查询第一次申诉理由
        Map<String, Object> majorFirstAPPel = assessPublicMapper.getMajorFirstAPPel(targetId, majorId);
        //查询第二次申诉理由
        Map<String, Object> majorSecondAPPel = assessPublicMapper.getMajorAppelSecondState(targetId, majorId);
        //判断此专业是否有申诉
        int state = 0;
        if (majorFirstAPPel == null && majorSecondAPPel == null) {
            state = 0;
            //两次申诉
        } else if (majorFirstAPPel != null && majorSecondAPPel != null) {
            state = 3;
            hashMap.put("majorFirstAPPel", majorFirstAPPel);

            hashMap.put("majorSecondAPPel", majorSecondAPPel);
            //第一次申诉
        } else if (majorFirstAPPel != null) {
            state = 1;
            hashMap.put("majorFirstAPPel", majorFirstAPPel);
            //第二次申诉
        } else if (majorSecondAPPel != null) {
            state = 2;
            hashMap.put("majorSecondAPPel", majorSecondAPPel);
        }
        hashMap.put("state", state);
        return hashMap;
    }
        /*
     * @param targetId
     * @return java.util.Map
     * @author Gejm
     * @description: 查询最终公示列表榜单
     * @date 2020/4/3 9:51
     */

    @GetMapping("/getFinalPublic")
    public Map getFinalPublic(Integer targetId){
        List<Map<String, Object>> getFinalPublic = assessPublicMapper.getFinalPublic(targetId);
        HashMap hashMap = new HashMap();
        //获取评估体系年度
        hashMap.put("targetYear",getFinalPublic.get(0).get("target_year"));
        //获取评估体系名称
        hashMap.put("targetName",getFinalPublic.get(0).get("target_name"));
        //获取第二次公示截至时间
        hashMap.put("secondEndtime",getFinalPublic.get(0).get("second_endtime"));
        hashMap.put("getFinalPublic",getFinalPublic);
        //3星级榜
        List<Map<String, Object>> starLevelThreeList = assessPublicMapper.getStarLevelList(targetId, 3, 1);
        hashMap.put("starLevelThreeList",starLevelThreeList);
        //4星级榜
        List<Map<String, Object>> starLevelFourList = assessPublicMapper.getStarLevelList(targetId, 4, 1);
        hashMap.put("starLevelFourList",starLevelFourList);
        //5星级榜
        List<Map<String, Object>> starLevelFiveList = assessPublicMapper.getStarLevelList(targetId, 5, 1);
        hashMap.put("starLevelFiveList",starLevelFiveList);
        //黄榜
        List<Map<String, Object>> yellowList = assessPublicMapper.getYellowList(targetId, 1);
        hashMap.put("yellowList",yellowList);
        //橙榜
        List<Map<String, Object>> orangeList = assessPublicMapper.getOrangeList(targetId, 1);
        hashMap.put("orangeList",orangeList);
        List<Map<String, Object>> redList = assessPublicMapper.getRedList(targetId, 1);
        hashMap.put("redList",redList);
        return  hashMap;
    }
    /*
     * @param targetId
     * @return java.util.Map
     * @author Gejm
     * @description: 评估结束
     * @date 2020/4/8 16:14
     */
    @GetMapping("/endTargetAssess")
    public Map endTargetAssess(Integer targetId,Integer schoolId){
            //此评估结束
        int i1 = assessPublicMapper.updataStartAssess(targetId);
        if (i1>0){
            //所有用户提示信息设置重置
            assessPublicMapper.setAssessInfo(0, 0,schoolId);
        }
        int state = ReturnStateUtil.returnUpdateState(i1);
        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        return  hashMap;
    }
    /*
     * @param majorId
     * @return java.util.List
     * @author Gejm
     * @description: 专业通览评估排名及分值
     * @date 2020/4/3 11:56
     */
    @GetMapping("/getMajorFinalPublic")
    public List getMajorFinalPublic(Integer majorId,Integer schoolId){
        List<Map<String, Object>> majorFinalPublic = assessPublicMapper.getMajorFinalPublic(majorId,schoolId);
        return majorFinalPublic;
    }
    /*
     * @param majorId
     * @return java.util.List
     * @author Gejm
     * @description: 评估档案 查询历年已结束的评估
     * @date 2020/4/3 14:33
     */
    @GetMapping("/getAllAssessPublic")
    public List getAllAssessPublic(Integer schoolId){
        List<Map<String, Object>> majorFinalPublic = assessPublicMapper.getAllAssessPublic(schoolId);
        return majorFinalPublic;
    }
    /*
     * @param
     * @return java.util.Map
     * @author Gejm
     * @description: 教务处领导审核列表
     * @date 2020/4/3 18:58
     */
    @GetMapping("/getAllCheckPublicList")
    public Map getAllCheckPublicList(Integer schoolId){
        List<Map<String, Object>> allAssessCheck = assessPublicMapper.getAllAssessCheck(schoolId);
        HashMap hashMap = new HashMap();
        if (allAssessCheck.size()==0){
            hashMap.put("state",0);
        }else {
            hashMap.put("state",1);
            hashMap.put("checkList",allAssessCheck);
        }
        return hashMap;
    }
    /*
     * @param targetId
    	 * @param checkId
     * @return java.util.Map
     * @author Gejm
     * @description: 所有阶段教务处领导审核（总）
     * @date 2020/4/3 19:24
     */
    @GetMapping("/checkPublic")
    public Map checkPublic(Integer targetId,Integer checkId){
        HashMap hashMap = new HashMap();
        List<Map<String, Object>> list = new ArrayList();
        if(checkId==1){
            //查询需要第一次审核公示的列表
            list = assessPublicMapper.firstPublicMajorScore(targetId,0);
            if (list.size()!=0){
                //结束时间
                hashMap.put("endTime",list.get(0).get("first_endtime"));
            }
        }else if(checkId==3){
            //查询需要第二次审核公示的列表
            list = assessPublicMapper.getCheckSecondPublic(targetId);
            if (list.size()!=0) {
                hashMap.put("endTime", list.get(0).get("second_endtime"));
            }
        }else if(checkId==5){
            //查询需要第三次审核公示的列表
            list = assessPublicMapper.getCheckFinalPublicList(targetId);
            if (list.size()!=0) {
                hashMap.put("endTime", list.get(0).get("second_endtime"));
            }
        }

        //获取评估体系名称
        hashMap.put("targetYear",list.get(0).get("target_year"));
        hashMap.put("targetName",list.get(0).get("target_name"));
        hashMap.put("checkList",list);
        return  hashMap;
    }
    /*
     * @param
     * @return java.util.Map
     * @author Gejm
     * @description: 因为每个学校一次只能进行一个评估体系，只有这个结束才能进行下一个
     *              所以查询未结束的评估状态，应该也只有一条数据
     * @date 2020/4/8 16:15
     */
    @GetMapping("/findTargetStage")
    public Map findTargetStage(Integer schoolId){
        HashMap hashMap = new HashMap();
        Map<String, Object> targetStage = assessPublicMapper.findTargetStage(schoolId);
        hashMap.put("targetStage",targetStage);
        return  hashMap;
    }
    /*
     * @param
     * @return java.util.Map
     * @author Gejm
     * @description: 获取评估开始提示信息
     * @date 2020/4/29 17:59
     */
    @GetMapping("/getAssessHindInfo")
    public Map getAssessHindInfo(Integer targetId,Integer userId){
        HashMap hashMap = new HashMap();
        if (userId==null || targetId==null){
            hashMap.put("state",117);
            return  hashMap;
        }else{

        Map<String, Object> assessHindState = assessPublicMapper.getAssessHindState(userId);
        //hand_id为null提示，否则不提示
            if (ReturnStateUtil.returnInteger(assessHindState.get("hind_id"))==0){
                Map<String, Object> assessHindInfo = assessPublicMapper.getAssessHindInfo(targetId);
                hashMap.put("state",116);
                hashMap.put("hindInfo",assessHindInfo);
            }else {
                hashMap.put("state",117);
            }
            return  hashMap;
        }
    }
    /*
     * @param assessId
    	 * @param userId
     * @return java.util.Map
     * @author Gejm
     * @description: 设置提示信息不显示
     * @date 2020/4/29 18:10
     */
    @GetMapping("/setAssessInfo")
    public Map setAssessInfo(Integer userId,Integer schoolId){
        HashMap hashMap = new HashMap();
        int state = ReturnStateUtil.returnUpdateState(assessPublicMapper.setAssessInfo(userId,1,schoolId));
        hashMap.put("state",state);
        return  hashMap;
    }
    /*
     * @param targetId
     * @return java.util.Map
     * @author Gejm
     * @description: 统计指标个数
     * @date 2020/5/14 11:50
     */
    @PassToken
    @GetMapping("/getAssessIndexNum")
    public Map getAssessIndexNum (Integer targetId){
        HashMap hashMap = new HashMap();
        Map<String, Object> indexNum = assessPublicMapper.getAssessIndexNum(targetId);
        Object num = indexNum.get("num");
        hashMap.put("num",num);
        return  hashMap;
    }
    /*
     * @param null
     * @return
     * @author Gejm
     * @description: 评估作废
     * @date 2020/5/21 11:26
     */
    @Transactional
    @GetMapping("/passAssess")
    public Map passAssess(Integer targetId,Integer schoolId){
        HashMap hashMap = new HashMap();
        Integer state=131;
        Map<String, Object> assessIds = assessPublicMapper.getAssessId(targetId);
        if (assessIds==null || targetId==null){
            state=131;
        }else {
            //获取assessId
            Integer assessId = ReturnStateUtil.returnInteger(assessIds.get("assess_id"));
            //删除开启的本次评估专业
            int deleAssessMajor = assessPublicMapper.deleAssessMajor(assessId);

            if (deleAssessMajor>0){
                //删除发起评估的数据
                assessPublicMapper.deleAssess(targetId);
                assessPublicMapper.deleAssessData(targetId);
                assessPublicMapper.deleFirstPublic(targetId);
                assessPublicMapper.deleSecondPublic(targetId);
                assessPublicMapper.deleFinalPublic(targetId);
                //将评估模板改为未启用状态
                int i = assessPublicMapper.updateStartTarget(targetId, null, 0);
                //学校所有用户提示信息设置重置
                assessPublicMapper.setAssessInfo(0, 0,schoolId);
                if (i>0){
                    state=130;
                }
            }
        }
        hashMap.put("state",state);
        return  hashMap;
    }
}
