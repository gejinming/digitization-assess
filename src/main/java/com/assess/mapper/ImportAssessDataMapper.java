package com.assess.mapper;

import com.assess.entity.Major;
import com.assess.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ImportAssessDataMapper {
    List<Map<String, Object>> getAllMajor();
    List<Map<String, Object>> getAllCollege();
    Integer addMajor(@Param("major") List<Major> majors);
    /*批量导入用户*/
    int addUsers(@Param("user") List<User> users);
}
