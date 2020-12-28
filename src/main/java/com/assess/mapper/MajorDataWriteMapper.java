package com.assess.mapper;

import com.assess.entity.MajorData;

import java.util.List;
import java.util.Map;

public interface MajorDataWriteMapper {
    /*各专业按指标填写数据,并统计当前指标得分填入专业提交数据表*/
    int addMajorDataWrite(MajorData majorData);
    /*获取这个指标模板的信息*/
    Map<String,Object> getTargetDetail(Integer targetDatailId);
    /*修改填写的指标数据*/
    int updateMajorDataWrite(MajorData majorData);
    /*专业填写数据完成将此专业材料提交状态改为已提交*/
    int updateAssessMajor(Integer assessId,Integer majorId);
    /*导入评估数据与手动填写的数据重复时删除手动填写的*/
    int deleRepetitData(Integer targetId,Integer targetDetailId,Integer majorId);
}
