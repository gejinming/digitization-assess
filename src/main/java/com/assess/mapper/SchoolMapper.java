package com.assess.mapper;

import com.assess.entity.College;
import com.assess.entity.Major;
import com.assess.entity.School;
import com.assess.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SchoolMapper {
    /*查询指定学校*/
    Map<String, Object> getSchoolById(Integer schoolId);
    /*查询所有学校*/
    List<Map<String, Object>> getAllSchool();
    /*新增学校*/
    int addSchool(School school);
    /*修改学校*/
    int updateSchool(School school);
    /*删除学校*/
    int deleteSchool(Integer schoolId);
    /*新增院系*/
    int addCollege(College college);
    /*判断学院编号是否存在*/
    Map<String,Object> checkCollegeCode(String collegeCode,Integer schoolId);
    /*判断学院名称是否存在*/
    Map<String,Object> checkCollegeName(String collegeName,Integer schoolId);
    /*判断院系下是否有专业*/
    Map<String,Object> isExistCollegeMajor(Integer collegeId);
    /*删除院系*/
    int deleteCollege(Integer collegeId,Integer schoolId);
    /*修改院系*/
    int updateCollege(College college);
    /*查询某个院系*/
    Map<String,Object> getCollegeById(Integer collegeId);
    /*查询所有的院系*/
    List<Map<String,Object>> getAllCollege(Integer schoolId);
    /*新增专业*/
    int addMajor(Major major);
    /*判断专业编号是否存在*/
    Map<String, Object> checkMajorCode(String majorCode,Integer schoolId);
    /*判断专业名称是否存在*/
    Map<String, Object> checkMajorName(String majorName,Integer schoolId);

    /*专业列表*/
    List<Map<String,Object>> getAllMajor(Integer schoolId);
    /*修改专业*/
    int updateMajor(Major major);
    /*查询某个专业信息*/
    Map<String,Object> getMajorById(Integer majorId);
    /*删除专业之前判断是否有已经参与评估的专业*/
    Map<String,Object> isExistAssessMajor(Integer majorId);
    /*删除专业*/
    int deleteMajor(Integer majorId);
    /*批量添加学院*/
    int addColleges(@Param("college") List<College> colleges);
    /*批量添加专业*/
    int addMajors(@Param("major") List<Major> majors);
}
