package com.assess.Controller;

import com.alibaba.fastjson.JSONArray;
import com.assess.Annotation.PassToken;
import com.assess.Annotation.UserLoginToken;
import com.assess.entity.*;
import com.assess.mapper.AssessPublicMapper;
import com.assess.util.ReturnStateUtil;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.Undefined;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: digitization-assess
 * @description: 评估模板建立及数据更改
 * @author: Gjm
 * @create: 2020-03-25 16:00
 **/
@Slf4j
@RestController
@RequestMapping("/buildModel")
@Api(tags = "评估模板建立及数据更改接口")
public class AssessModelBuildController {
    @Autowired
    AssessPublicMapper assessPublicMapper;

    /**
     * @param targetModel
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     * @author Gejm
     * @description: 创建评估模板名称，返回前端id
     * @date 2020/3/26 12:55
     */
    @ApiOperation(value = "创建评估模板名称接口", notes = "创建评估模板名称")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "targetName",value = "模板名称")
        }
    )
    @PassToken
    @GetMapping("/addAssessTarget")
    public Map<Object, Object> addAssessTarget(TargetModel targetModel){
        int i = assessPublicMapper.addNewAssessModel(targetModel);
        Map<Object, Object> hashMap = new HashMap();

        if (i!=0){
            //添加成功
            hashMap.put("targetId",targetModel.getTargetId());
            hashMap.put("targetName",targetModel.getTargetName());
            hashMap.put("state",104);
        }else {
            hashMap.put("state",105);
        }
        return hashMap;
    }
    /*
     * @param targetId
     * @return java.util.Map
     * @author Gejm
     * @description: 删除评估体系模板
     * @date 2020/3/31 11:27
     */
    @ApiOperation(value = "删除评估体系模板接口", notes = "创建评估模板名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "targetId",value = "模板id"),
            @ApiImplicitParam(name = "userId",value = "用户id")
    }
    )
    @Transactional
    @PostMapping("/delTarget")
    public Map delTarget(Integer targetId,Integer userId){

        HashMap hashMap = new HashMap();
        Map<String,Object> stateMap=assessPublicMapper.isUserTarget(targetId);
        //判断模板是否在用
        if (stateMap.get("state_id").equals(0)){
            //删除指标类型
            assessPublicMapper.deltargetTypes(targetId);
            //删除指标
            assessPublicMapper.deltargetTypedetail(targetId);
            //删除模板
           int i = assessPublicMapper.delTarger(targetId,userId);
            int state = ReturnStateUtil.returnDelState(i);
            hashMap.put("state",state);
            return hashMap;
        }else {
            hashMap.put("state",121);
            return hashMap;
        }


    }
    /**
     * @param targetId 
     * @return java.util.List
     * @author Gejm
     * @description: 根据评估体系id获取所有指标类型
     * @date 2020/3/27 17:52
     */
    @GetMapping("/getTargetAllType")
    public List getTargetAllType(Integer targetId){
        List<Map<String, Object>> targetAllType = assessPublicMapper.getTargetAllType(targetId);
        return targetAllType;
    }
    /**
     * @param targetType
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     * @author Gejm
     * @description: 创建指标体系类型
     * @date 2020/3/26 13:27
     */
    @PostMapping("/addAssessTargetType")
    public Map<Object,Object> addAssessType(TargetType targetType){

        int i = assessPublicMapper.addNewAssessTypeModel(targetType);
        Map<Object, Object> hashMap = new HashMap();
        if (i!=0){
            //添加成功
            hashMap.put("targetTypeId",targetType.getTargetTypeId());
            hashMap.put("targetTypeName",targetType.getTargetTypeName());
            hashMap.put("state",104);
        }else {
            hashMap.put("state", 105);
        };
        return hashMap;
    }
    /*
     * @param targetDetail
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     * @author Gejm
     * @description: 创建指标及添加数据
     * @date 2020/3/26 15:43
     */
    @PostMapping("/addTargetDetilModel")
    @ResponseBody
    public Map addTargetDetilModel(@RequestBody TargetDetail targetDetail){

        Map<Object, Object> hashMap = new HashMap();
        int state=0;
        Double  indexNum=new Double(targetDetail.getIndexNum());
        Double score = new Double(targetDetail.getScore());
        if (indexNum ==null||targetDetail.getTargetTypeId()==null ||targetDetail.getArithmeticId()==null ||score==null ||targetDetail.getTargetId()==null){
            state=105;
        }else {
            //将json数据转为string
            if (targetDetail.getArithmeticFourTitleo()!=null) {
                String fourTitleString = ReturnStateUtil.returnStringData(targetDetail.getArithmeticFourTitleo());
                //重新赋值给实体类
                targetDetail.setArithmeticFourTitle(fourTitleString);
            }
            if (targetDetail.getArithmeticThreeo()!=null) {
                String threeString = ReturnStateUtil.returnStringData(targetDetail.getArithmeticThreeo());
                //重新赋值给实体类
                targetDetail.setArithmeticThree(threeString);
            }
            //判断指标序号是否重复
            state = ReturnStateUtil.returnIsExist(assessPublicMapper.getIndexNumIsExist(targetDetail));
            //不重添加
            if (state == 120) {
                //保存数据
                int i = assessPublicMapper.addTargetDetail(targetDetail);
                //返回状态
                state = ReturnStateUtil.returnState(i);
            }
        }
        hashMap.put("targetDetail",targetDetail);
        hashMap.put("state",state);
        return hashMap;
    }
    /*
     * @param targetDetail
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     * @author Gejm
     * @description: 修改指标模板内容
     * @date 2020/3/26 16:10
     */
    @PostMapping("/updateTargetDetilModel")
    @ResponseBody
    public Map<Object,Object> updateTargetDetilModel(@RequestBody  TargetDetail targetDetail){
        //将json数据转为string
        if (targetDetail.getArithmeticFourTitleo()!=null) {
            String fourTitleString = ReturnStateUtil.returnStringData(targetDetail.getArithmeticFourTitleo());
            //重新赋值给实体类
            targetDetail.setArithmeticFourTitle(fourTitleString);
        }
        if (targetDetail.getArithmeticThreeo()!=null) {
            String threeString = ReturnStateUtil.returnStringData(targetDetail.getArithmeticThreeo());
            //重新赋值给实体类
            targetDetail.setArithmeticThree(threeString);
        }
        int i = assessPublicMapper.updateTargetDetail(targetDetail);
        Map<Object, Object> Map = new HashMap();
        Integer state=ReturnStateUtil.returnUpdateState(i);
        Map.put("targetDetail",targetDetail);
        Map.put("state",state);

        return Map;
    }
    /*
     * @param TargetTypeId
     * @return java.util.Map
     * @author Gejm
     * @description: 删除指标类型
     * @date 2020/4/21 10:14
     */
    @GetMapping("/delTargetType")
    public Map delTargetType(Integer TargetTypeId){
        //先删除指标类型
        int delTargetType = assessPublicMapper.delTargetType(TargetTypeId);
        if (delTargetType>0){
            //删除指标内容
            int i = assessPublicMapper.delTargetDetail2(TargetTypeId);

        }
        int state = ReturnStateUtil.returnDelState(delTargetType);
        HashMap  result = new HashMap();
        result.put("state",state);
        return result;

    }
    /*
     * @param targetDetailId
     * @return java.util.Map
     * @author Gejm
     * @description: 删除指标
     * @date 2020/3/31 11:32
     */
    @GetMapping("/delTargetDetail")
    public Map delTargetDetail(Integer targetDetailId){

        int i = assessPublicMapper.delTargetDetail(targetDetailId);
        int state = ReturnStateUtil.returnDelState(i);
        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        return hashMap;
    }
    /*
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 查询所有算法
     * @date 2020/3/26 16:45
     */
    @GetMapping("/getAllArithmetic")
    public List getAllArithmetic(){
        List<Map<String, Object>> allArithmetic = assessPublicMapper.getAllArithmetic();
        return allArithmetic;
    }
    /*
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 根据评估体系类型id查询此类型所有的指标
     * @date 2020/3/26 17:01
     */
    @GetMapping("/getAllTargetDetail")
    public List getAllTargetDetail(TargetDetail targetDetail){

        List<Map<String, Object>> allTargetDetail = assessPublicMapper.getAllTargetDetail(targetDetail.getTargetTypeId());
        for (int i=0;i<allTargetDetail.size();i++){
            Map  allTargetDetailMap= allTargetDetail.get(i);
            String fourTitle= (String) allTargetDetailMap.get("arithmetic_four_title");
            if (fourTitle!=null){
                allTargetDetailMap.put("arithmetic_four_title",JSONArray.parse(fourTitle));
            }
            String arithmeticThree= (String) allTargetDetailMap.get("arithmetic_three");
            if (arithmeticThree!=null){
                allTargetDetailMap.put("arithmetic_three",JSONArray.parse(arithmeticThree));
            }



        };
        return allTargetDetail;
    }
    /*
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 根据评估体id查询所有类型
     * @date 2020/3/26 17:01
     */
    @GetMapping("/getAllTargetType")
    public List getAllTargetType(TargetType targetType){
        List<Map<String, Object>> getAllTargetType = assessPublicMapper.getAllTargetType(targetType.getTargetId());
        return getAllTargetType;
    }
    /*
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 统计每个评估体系模板里有几个类型和指标
     * @date 2020/3/26 17:01
     */
    @GetMapping("/getAllTarget")
    public List getAllTarget(Integer schoolId){
        List<Map<String, Object>> getAllTarget = assessPublicMapper.getAllTarget(schoolId);
        for (int i=0;i<getAllTarget.size();i++){
            Map<String, Object> targetMap = getAllTarget.get(i);
            Object create_date = targetMap.get("create_date");
            if (create_date!=null){
                String formatDate = ReturnStateUtil.returnFormatDate(create_date);
                targetMap.put("create_date", formatDate);
            }

        }
        return getAllTarget;
    }

    /*
     * @param targetModel
     * @return java.util.Map
     * @author Gejm
     * @description: 复制模板
     * @date 2020/5/6 9:58
     */
    @Transactional
    @GetMapping("/copyTarget")
    public Map copyTarget(TargetModel targetModel){
        HashMap result = new HashMap();
        //查询出复制原始模板类型列表
        List<Map<String, Object>> targetTypeList = assessPublicMapper.getAllTargetType1(targetModel.getTargetId());
        int addTargetState =assessPublicMapper.addNewAssessModel(targetModel);

        if (addTargetState>0){
            //循环原始评估模板下的所有类型
            for (int i=0;i<targetTypeList.size();i++){
                System.out.println("============以下查询及添加================");
                Map targetTypeMap = targetTypeList.get(i);
                System.out.println("===========++++++++++++==============");
                //查询出复制原始模板类型的指标指标列表
                List<Map<String, Object>> TargetDetailList = assessPublicMapper.getAllTargetDetail(ReturnStateUtil.returnInteger(targetTypeMap.get("target_type_id")));
                TargetType targetType = new TargetType();
                targetType.setTargetId(targetModel.getTargetId());
                targetType.setTargetTypeName(targetTypeMap.get("target_type_name").toString());
                //添加指标类型
                int addtargetTypeState = assessPublicMapper.addNewAssessTypeModel(targetType);
                if (addtargetTypeState>0){
                    //循环原始指标类型下的所有指标

                    for (int j=0;j<TargetDetailList.size();j++){
                        Map<String,Object> TargetDetailMap = TargetDetailList.get(j);
                        TargetDetail targetDetail = new TargetDetail();
                        targetDetail.setTargetId(targetModel.getTargetId());
                        targetDetail.setTargetTypeId(targetType.getTargetTypeId());
                        targetDetail.setTargetContent(TargetDetailMap.get("target_content")+"");
                        targetDetail.setScore(ReturnStateUtil.returnDouble(TargetDetailMap.get("score")));
                        targetDetail.setScoreUnit(TargetDetailMap.get("score_unit")+"");
                        targetDetail.setTargetExplain(TargetDetailMap.get("target_explain")+"");
                        targetDetail.setArithmeticId(ReturnStateUtil.returnInteger(TargetDetailMap.get("arithmetic_id")));
                        targetDetail.setItemA(TargetDetailMap.get("item_a")+"");
                        targetDetail.setIndexNum(ReturnStateUtil.returnDouble(TargetDetailMap.get("index_num")));
                        //算法1与3填写项
                        if (ReturnStateUtil.returnInteger(TargetDetailMap.get("arithmetic_id"))==3 || ReturnStateUtil.returnInteger(TargetDetailMap.get("arithmetic_id"))==1) {
                            targetDetail.setItemB(TargetDetailMap.get("item_b").toString());
                        }
                        if (ReturnStateUtil.returnInteger(TargetDetailMap.get("arithmetic_id"))==3) {
                            targetDetail.setArithmeticThree(TargetDetailMap.get("arithmetic_three").toString());
                        }
                        if (ReturnStateUtil.returnInteger(TargetDetailMap.get("arithmetic_id"))==4) {
                            targetDetail.setArithmeticFourTitle(TargetDetailMap.get("arithmetic_four_title").toString());
                        }
                        int addTargetDetailState = assessPublicMapper.addTargetDetail(targetDetail);
                        //复制失败
                        if (addTargetDetailState==0){
                            result.put("state",123);
                            //复制失败，可能复制的不完整删除评估模板
                            assessPublicMapper.delTarger(targetModel.getTargetId(),0);
                            return result;
                        }
                    }
                }
            }
            //复制成功
            result.put("state",124);
        }else {
            //复制失败
            result.put("state",123);
        }

        return result;
    }
    /*
     * @param assessDemos
     * @return java.util.Map
     * @author Gejm
     * @description:评估模板导入
     * @date 2020/5/15 15:45
     */
    @PassToken
    @Transactional
    @PostMapping("/importAssessDemo")
    public Map importAssessDemo(@RequestBody List<AssessDemo> assessDemos){
        HashMap result = new HashMap();
        String targetTypeNames="";
        int targetTypeId=0;
        int state=0;
        for (int i=0;i<assessDemos.size();i++){
            AssessDemo assessDemo = assessDemos.get(i);
             String targetTypeName = assessDemo.getTargetTypeName();

             if (targetTypeNames.equals(targetTypeName)){
                 assessDemo.setTargetTypeId(targetTypeId);
                 int delState=assessPublicMapper.deleRepetIndex(assessDemo);
                 if (delState>0){
                       log.info("评估体系id："+assessDemo.getTargetId()+"中，指标编号"+assessDemo.getIndexNum()+"重复，已删除，按导入添加！");
                    }
                 //添加指标明细
                 int i2 = assessPublicMapper.addTargetDetils(assessDemo);
                 if (i2>0){
                     log.info("添加指标明细成功！");
                      state=104;
                 }else {
                      state=105;
                     log.info("添加指标明细失败");
                 }
             }else {
                 //查询已存在指标类型相同的名称
                 Map<String, Object> repetTargetType = assessPublicMapper.getRepetTargetType(assessDemo);
                 int addTypeState;
                 if (repetTargetType!=null){
                     assessDemo.setTargetTypeId(ReturnStateUtil.returnInteger(repetTargetType.get("target_type_id")));
                     addTypeState=2;
                     log.info("已存在此指标类型名称"+assessDemo.getTargetTypeName());
                 }else{
                     //添加指标类型
                     addTypeState = assessPublicMapper.addTargetType(assessDemo);
                     if (addTypeState>0){
                         log.info("添加指标类型成功");
                     }
                 }
                 /*if (delState>0){
                     log.info("评估体系id："+assessDemo.getTargetId()+"中有，指标类型"+assessDemo.getTargetTypeName()+"重复，已删除，按导入添加！");
                 }*/

                 //添加类型成功
                 if (addTypeState>0){

                     targetTypeId=assessDemo.getTargetTypeId();
                      int delState=assessPublicMapper.deleRepetIndex(assessDemo);
                     if (delState>0){
                         log.info("评估体系id："+assessDemo.getTargetId()+"中有指标编号"+assessDemo.getIndexNum()+"重复，已删除，按导入添加！");
                     }
                     //添加指标明细
                     int i2 = assessPublicMapper.addTargetDetils(assessDemo);
                     if (i2>0){
                          state=104;
                         log.info("添加指标明细成功！");
                     }else {
                         log.info("添加指标明细失败");
                          state=105;
                     }
                 }else {
                     log.info("添加指标类型失败");
                 }


             }


            targetTypeNames=targetTypeName;

        }
        result.put("state",state);
        return result;
    }

}
