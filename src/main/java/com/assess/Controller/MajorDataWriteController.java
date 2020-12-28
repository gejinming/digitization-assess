package com.assess.Controller;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.alibaba.fastjson.JSONArray;
import com.assess.Annotation.PassToken;
import com.assess.entity.MajorData;
import com.assess.mapper.MajorDataWriteMapper;
import com.assess.util.ReturnStateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: digitization-assess
 * @description: 各专业材料提交、申报
 * @author: Gjm
 * @create: 2020-03-27 09:30
 **/
@Slf4j
@RestController
public class MajorDataWriteController {
    @Autowired
    MajorDataWriteMapper majorDataWriteMapper;

    /*
     * @param majorData
     * @return java.util.Map
     * @author Gejm
     * @description: 专业提交数据
     * @date 2020/4/24 10:33
     */
    @PostMapping("/majorSummitData")
    @ResponseBody
    public Map majorSummitData(@RequestBody MajorData majorData){
        //算法4填写的JSON转string
        if (majorData.getArithmeticFourDataJson()!=null){
            JSONArray arithmeticFourData = (JSONArray) JSONArray.toJSON(majorData.getArithmeticFourDataJson());
            //将转的string类型Json数据存入string类型的算法4数据实体类
            majorData.setArithmeticFourData(arithmeticFourData.toString());
        }

 /*       System.out.println("aa");
        //获取这个指标的信息
        Map<String, Object> targetDetail = majorDataWriteMapper.getTargetDetail(majorData.getTargetDetailId());
        //获取指标体系id
        Object target_ido = targetDetail.get("target_id");
        Integer targetId = ReturnStateUtil.returnInteger(target_ido);
        majorData.setTargetId(targetId);
        //获取指标的算法id
        Object arithmetic_ido = targetDetail.get("arithmetic_id");
        Integer arithmenticId = ReturnStateUtil.returnInteger(arithmetic_ido);
        majorData.setArithmeticId(arithmenticId);
        //获取指标的分数
        String targetscores = targetDetail.get("score").toString();
        double targetscore = Double.parseDouble(targetscores);
        //算法1计算当前指标分数
        double sumScore=0;
        //数据计算前端做，现在后端测试用
        if (arithmenticId==1){
            Double itemAData = majorData.getItemAData();
            Double itemBData = majorData.getItemBData();
            sumScore=targetscore*itemAData/itemBData;

        }else if(arithmenticId==2 ||arithmenticId==4){
            Double itemAData = majorData.getItemAData();
            sumScore=targetscore*itemAData;
        }else if (arithmenticId==3){

        }
        System.out.println(sumScore);
        majorData.setSumScore(sumScore);*/

        HashMap hashMap = new HashMap();
        int i = majorDataWriteMapper.addMajorDataWrite(majorData);
        int state = ReturnStateUtil.returnState(i);
        hashMap.put("state",state);
        hashMap.put("dataId",majorData.getDataId());
        return hashMap;
    }
    /*
     * @param majorData
     * @return java.util.Map
     * @author Gejm
     * @description: 专业数据修改
     * @date 2020/4/1 17:02
     */
    @PostMapping("/updateMajorData")
    @ResponseBody
    public Map updateMajorData(@RequestBody  MajorData majorData){
        //算法4填写的JSON转string
        if (majorData.getArithmeticFourDataJson()!=null){
            JSONArray arithmeticFourData = (JSONArray) JSONArray.toJSON(majorData.getArithmeticFourDataJson());
            //将转的string类型Json数据存入string类型的算法4数据实体类
            majorData.setArithmeticFourData(arithmeticFourData.toString());
        }
        int i = majorDataWriteMapper.updateMajorDataWrite(majorData);
        int state = ReturnStateUtil.returnUpdateState(i);
        HashMap hashMap = new HashMap();
        hashMap.put("state",state); hashMap.put("dataId",majorData.getDataId());

        return  hashMap;
    }
    /*
     * @param assessId
    	 * @param majorId
     * @return java.util.Map
     * @author Gejm
     * @description: 专业提交数据完成，状态改为已提交状态
     * @date 2020/4/1 18:23
     */
    @PostMapping("/updateAssessMajor")
    public Map updateAssessMajor(Integer assessId,Integer majorId){
        int i = majorDataWriteMapper.updateAssessMajor(assessId, majorId);
        int state = ReturnStateUtil.returnUpdateState(i);
        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        return  hashMap;
    }

    @PassToken
    @Transactional
    @PostMapping("/importMajorData")
    public Map importMajorData(@RequestBody List<MajorData> majorData){
        //JSONArray listJson = JSONArray.parseArray(list);

        HashMap hashMap = new HashMap();
        int majorNum=0;
        int state=0;
      for (int i=0;i<majorData.size();i++){
          MajorData majorDataMap = majorData.get(i);
          if (majorDataMap.getMajorId()!=null &&
                  majorDataMap.getScore()!=null && majorDataMap.getTargetDetailId() !=null &&
                  majorDataMap.getArithmeticId()!=null){

             log.info("开始导入数据！");
             //删除此专业此类型此指标录入的数据重新添加
              int deleState=majorDataWriteMapper.deleRepetitData(majorDataMap.getTargetId(),majorDataMap.getTargetDetailId(),majorDataMap.getMajorId());
              if (deleState>0){
                  log.info("删除评估体系id:"+majorDataMap.getTargetId()+"指标类型id:"+majorDataMap.getTargetDetailId()+"专业id："+majorDataMap.getMajorId()+"重新添加");
              }
              int addState = majorDataWriteMapper.addMajorDataWrite(majorDataMap);
              if (addState>0){
                  log.info("数据导入成功！");
                  majorNum+=1;
              }else{
                  log.error("专业"+majorDataMap.getMajorId()+"导入数据失败！");
              }
          }else {
              log.error("数据不全，此条跳过！");
          }
      }
        if (majorNum>0){
            state=104;
        }
        log.info("共导入"+majorNum+"条数据");
        hashMap.put("state",state);
        hashMap.put("dataNum",majorNum);
        return  hashMap;
    }
}
