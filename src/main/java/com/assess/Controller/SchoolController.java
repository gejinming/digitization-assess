package com.assess.Controller;

import com.assess.Annotation.PassToken;
import com.assess.Common.Result;
import com.assess.entity.College;
import com.assess.entity.Major;
import com.assess.entity.School;
import com.assess.mapper.SchoolMapper;
import com.assess.util.ReturnStateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/school")
public class SchoolController {
    @Autowired
    SchoolMapper schoolMapper;
    /*
     * @param schoolId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author Gejm
     * @description: 查找某个学校信息
     * @date 2020/4/16 17:06
     */
    @GetMapping("/getSchoolById")
    public Map getSchoolById(Integer schoolId){
        return schoolMapper.getSchoolById(schoolId);
    }
    /*
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 查询学校列表
     * @date 2020/4/16 17:06
     */
    @PassToken
    @GetMapping("/getAllSchool")
    public List getAllSchool(){
        List<Map<String, Object>> list =schoolMapper.getAllSchool();
        return list;
    }
    /*
     * @param school
     * @return java.util.Map
     * @author Gejm
     * @description: 新增学校
     * @date 2020/4/16 17:12
     */
    @PostMapping("/addSchool")
    public Map addSchool(School school){
        int i = schoolMapper.addSchool(school);
        int state = ReturnStateUtil.returnState(i);
        HashMap<Object, Object> result = new HashMap();
        result.put("state",state);
        return result;

    }
    /*
     * @param school
     * @return java.util.Map
     * @author Gejm
     * @description: 修改学校信息
     * @date 2020/4/16 17:16
     */
    @PostMapping("/updateSchool")
    public Map updateSchool(School school){
        int i = schoolMapper.updateSchool(school);
        int state =ReturnStateUtil.returnUpdateState(i);
        HashMap<Object, Object> result = new HashMap();
        result.put("state",state);
        return result;
    }
    /*
     * @param schoolId
     * @return java.util.Map
     * @author Gejm
     * @description: 删除学校
     * @date 2020/4/16 17:18
     */
    @GetMapping("/deleteSchool")
    public Map deleteSchool(Integer schoolId){
        int state =ReturnStateUtil.returnUpdateState(schoolMapper.deleteSchool(schoolId));
        HashMap<Object, Object> result = new HashMap();
        result.put("state",state);
        return result;
    }
    /*
     * @param college
     * @return java.util.Map
     * @author Gejm
     * @description: 新增院系
     * @date 2020/4/16 17:24
     */
    @PostMapping("/addCollege")
    public Map addCollege(College college){
        //判断院系编号是否存在
        int state = ReturnStateUtil.returnIsExist(
                schoolMapper.checkCollegeCode(college.getCollegeCode(),college.getSchoolId())
                );
        if (state==120){
            //验证学院名称
            state=ReturnStateUtil.returnNameIsExist(schoolMapper.checkCollegeName(college.getCollegeName(),college.getSchoolId()));
        }
        if (state==125){
            int i = schoolMapper.addCollege(college);
             state = ReturnStateUtil.returnState(i);
        }

        HashMap<Object, Object> result = new HashMap();
        result.put("state",state);
        return result;
    }
    /*
     * @param collegeId
     * @return java.util.Map
     * @author Gejm
     * @description: 删除学院之前判断该学院下是否存在专业
     * @date 2020/4/21 10:33
     */
    @GetMapping("/isExistCollegeMajor")
    public Map isExistCollegeMajor(Integer collegeId){
        //判断是否存在
        int state = ReturnStateUtil.returnIsExist(schoolMapper.isExistCollegeMajor(collegeId));
        HashMap<Object, Object> result = new HashMap();
        result.put("state",state);
        return result;
    }
    /*
     * @param collegeId
     * @return java.util.Map
     * @author Gejm
     * @description: 删除学院
     * @date 2020/4/16 17:28
     */
    @GetMapping("/deleteCollege")
    public Map deleteCollege(Integer collegeId,Integer schoolId){
        //删除学院之前判断该学院下是否存在专业
        HashMap<Object, Object> result = new HashMap();
        int delstate = ReturnStateUtil.returnIsExist(schoolMapper.isExistCollegeMajor(collegeId));
        //没有专业可以删除
        if (delstate==120) {
            int i = schoolMapper.deleteCollege(collegeId,schoolId);
            int state = ReturnStateUtil.returnDelState(i);
            result.put("state", state);
            return result;
        }else {
            result.put("state", delstate);
            return result;
        }

    }
    /*
     * @param college
     * @return java.util.Map
     * @author Gejm
     * @description: 修改院系
     * @date 2020/4/16 17:32
     */
    @PostMapping("/updateCollege")
    public Map updateCollege(College college){
        int state;
        Map<String, Object> collegeInfo = schoolMapper.getCollegeById(college.getCollegeId());
        String collegeCode0 = collegeInfo.get("college_code").toString();
        String collegeName0=collegeInfo.get("college_name").toString();
        //判断是否与原始编号一致，一致就不判断是否存在
        if(collegeCode0.equals(college.getCollegeCode())){
            state=120;

        }else {
            state = ReturnStateUtil.returnIsExist(schoolMapper.checkCollegeCode(college.getCollegeCode(),college.getSchoolId()));
        }
        if (collegeName0.equals(college.getCollegeName())){
            //不用判断名称是否存在，可以添加

        }else {
            //验证学院名称是否存在
           int collegeNamestate=ReturnStateUtil.returnNameIsExist(schoolMapper.checkCollegeName(college.getCollegeName(),college.getSchoolId()));
            if (collegeNamestate==126){
                state=126;
            }
        }

        if (state==120) {
            int i = schoolMapper.updateCollege(college);
             state = ReturnStateUtil.returnUpdateState(i);
        }
        HashMap<Object, Object> result = new HashMap();
        result.put("state",state);
        return result;
    }
    /*
     * @param collegeId
     * @return java.util.Map
     * @author Gejm
     * @description: 查询院系信息
     * @date 2020/4/16 17:35
     */
    @GetMapping("/getCollegeById")
    public Map getCollegeById(Integer collegeId){
        Map<String, Object> collegeInfo = schoolMapper.getCollegeById(collegeId);
        return collegeInfo;
    }
    /*
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 查询所有院系
     * @date 2020/4/16 17:36
     */
    @GetMapping("/getAllCollege")
    public List getAllCollege(Integer schoolId){
        List<Map<String, Object>> allCollege = schoolMapper.getAllCollege(schoolId);
        return allCollege;
    }
    /*
     * @param major
     * @return java.util.Map
     * @author Gejm
     * @description: 专业添加
     * @date 2020/4/16 17:39
     */
    @PostMapping("/addMajor")
    public Map addMajor(Major major,Integer schoolId){

        HashMap<Object, Object> result = new HashMap();
        //验证专业编号是否存在
        Map<String, Object> majorCodeMap = schoolMapper.checkMajorCode(major.getMajorCode(),schoolId);
        int isExist = ReturnStateUtil.returnIsExist(majorCodeMap);
        if (isExist==120){
            //验证专业名称
            isExist=ReturnStateUtil.returnNameIsExist(schoolMapper.checkMajorName(major.getMajorName(),schoolId));
        }

        if (isExist==125){
            // 不存在 可以添加
            int i = schoolMapper.addMajor(major);
            int state = ReturnStateUtil.returnState(i);
            result.put("state",state);
        }else {
            result.put("state",isExist);
        }

        return result;
    }
    /*
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 查询所有专业
     * @date 2020/4/16 17:41
     */

    @GetMapping("/getAllMajor")
    public List getAllMajor(Integer schoolId){
        List<Map<String, Object>> allMajor = schoolMapper.getAllMajor(schoolId);
        return allMajor;
    }
    /*
     * @param major
     * @return java.util.Map
     * @author Gejm
     * @description: 修改专业
     * @date 2020/4/16 17:44
     */
    @PostMapping("/updateMajor")
    public Map updateMajor(Major major,Integer schoolId){

        int state;
        HashMap<Object, Object> result = new HashMap();
        Map<String, Object> majorInfo = schoolMapper.getMajorById(major.getMajorId());
        String majorCode0 = majorInfo.get("major_code").toString();
        String majorName0=majorInfo.get("major_name")+"";
        //判断是否与原始编号一致，一致就不判断是否存在
        if(majorCode0.equals(major.getMajorCode())){
            state=120;
        }else {
            Map<String, Object> majorCodeMap = schoolMapper.checkMajorCode(major.getMajorCode(),schoolId);
            state = ReturnStateUtil.returnIsExist(majorCodeMap);
        }
        if (majorName0.equals(major.getMajorName())){
            //state=125;
        }else{
            //验证专业名称
            int majorNameState=ReturnStateUtil.returnNameIsExist(schoolMapper.checkMajorName(major.getMajorName(),schoolId));
            if (majorNameState==126){
                state=126;
            }
        }

        if (state==120){
            int i = schoolMapper.updateMajor(major);
            state = ReturnStateUtil.returnUpdateState(i);
            result.put("state",state);
        }
        else {
            result.put("state",state);
        }


        return result;
    }
    /*
     * @param majorId
     * @return java.util.Map
     * @author Gejm
     * @description: 查询专业信息
     * @date 2020/4/16 17:47
     */
    @GetMapping("/getMajorById")
    public Map getMajorById(Integer majorId){
        Map<String, Object> majorInfo = schoolMapper.getMajorById(majorId);
        return majorInfo;
    }
    /*
     * @param  
     * @return java.util.Map
     * @author Gejm
     * @description: 删除专业
     * @date 2020/4/16 17:49
     */
    @GetMapping("/deleteMajor")
    public Map deleteMajor(Integer majorId){
        HashMap result = new HashMap();
        //判断是此专业是否已经参与评估，否则不能删除
        int state=ReturnStateUtil.returnIsExist(schoolMapper.isExistAssessMajor(majorId));
        if (state==120){
            int i = schoolMapper.deleteMajor(majorId);
            int delstate = ReturnStateUtil.returnDelState(i);
            result.put("state",delstate);
            return result;
        }else {
            result.put("state",state);
            return result;
        }

    }
    /*
     * @param colleges
     * @return java.util.Map
     * @author Gejm
     * @description: 批量添加院系
     * @date 2020/5/13 14:14
     */
    @PassToken
    @Transactional
    @PostMapping("/addColleges")
    public Map addColleges(@RequestBody  List<College> colleges){
        HashMap result = new HashMap();
        //批量添加数据
        int state = ReturnStateUtil.returnState(schoolMapper.addColleges(colleges));
        result.put("state",state);

        return result;
    }
    /*
     * @param majors
     * @return java.util.Map
     * @author Gejm
     * @description: 批量添加专业
     * @date 2020/5/13 14:14
     */
    @PassToken
    @Transactional
    @PostMapping("/addMajors")
    public Map addMajors(@RequestBody  List<Major> majors){
        HashMap result = new HashMap();
        int state=127;
        String message="";
        String majorCods="";
        String majorNames="";
        for (int i=0;i<majors.size();i++){
           Major major= majors.get(i);

            //验证专业编号是否存在
            Map<String, Object> majorCodeMap = schoolMapper.checkMajorCode(major.getMajorCode(),major.getSchoolId());
            int isExist = ReturnStateUtil.returnIsExist(majorCodeMap);
            if (isExist==120){
                //验证专业名称是否存在
                isExist=ReturnStateUtil.returnNameIsExist(schoolMapper.checkMajorName(major.getMajorName(),major.getSchoolId()));
                if (isExist==126){
                    majorNames+=major.getMajorName()+"、";
                }
            }else {
                //重复的编号信息
                majorCods+=major.getMajorCode()+"、";
            }
        }
        if (majorCods=="" && majorNames==""){
            //批量添加数据
             state = ReturnStateUtil.returnState(schoolMapper.addMajors(majors));
        }else{
            if (majorCods!=""){
                state=121;
                message="专业编号："+majorCods+"在此学院已存在，请检查！";
            }
            if (majorNames!=""){
                state=126;
                message="专业名称："+majorNames+"在此学院已存在，请检查！";
            }
            if (majorNames!="" && majorCods!=""){
                state=127;
                message="专业编号："+majorCods+"专业名称"+majorNames+"在此学院已存在，请检查!";
            }
        }

        log.error(message);

        result.put("state",state);
        result.put("massage",message);
        return result;
    }


}
