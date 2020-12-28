package com.assess.mapper;

import java.util.List;
import java.util.Map;

public interface LoginMapper {
    /*登录验证*/
    List<Map<String, Object>> checkUser(Integer jobNum,String password,Integer schoolId,Integer adminId);
    /*判断是否系统管理员*/
    Map<String, Object> checkUserAdmin(Integer jobNum,String password);
}
