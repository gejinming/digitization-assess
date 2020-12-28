package com.assess.mapper;

import com.assess.entity.College;
import com.assess.entity.User;
import com.assess.entity.UserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

//@Mapper
public interface UserMapper {
    /*根据userid获取用户信息*/
    List<Map<String, Object>> getUserByid(Integer userId);
    /*添加用户*/
    int insertUser(User user);
    /*密码修改*/
    int updatePassWord(User user);
    /*验证工号是否存在*/
    Map<String, Object> checkJobNum(Integer jobNum,Integer schoolId);
    /*模糊查询根据工号 用户名 角色 机构查询用户*/
    List<Map<String, Object>> getUser(Integer jobNum,String userName,Integer roleId,Integer majorId);
    /*获取所有用户*/
    List<Map<String, Object>> getAllUser(Integer schoolId,Integer tagId);
    /*删除用户*/
    int deleUser(Integer userId);
    /*编辑用户资料*/
    int updateUser(User user);
    /*查询所有角色*/
    List<Map<String, Object>> getAllRole(Integer schoolId,Integer tagId);
    /*查询最大的roleId*/
    Map<String, Object> getMaxRoleId();
    /*修改角色powerId*/
    int updatePowerId(UserRole userRole);
    /*查询所有角色*/
    List<Map<String, Object>> getAllRole1(Integer schoolId,Integer tagId);
    /*编辑角色*/
    Map<String, Object> getRoleInfo(Integer roleId);
    /*修改角色信息*/
    int updateRoleInfo(UserRole userRole);
    /*删除角色*/
    int deleRole(Integer roleId);
    /*新增角色*/
    int addRole(UserRole userRole);
    /*查询所有专业*/
    List<Map<String, Object>> getAllMajor(Integer schoolId);
    /*所有的菜单*/
    List<Map<String, Object>> getAllmenu();
    /*根据角色查询菜单*/
    List<Map<String, Object>> getRolePower(Integer roleId);
    /*修改角色权限*/
    /*先删除某个角色权限，再添加*/
    int delRolePower(Integer powerId);
    int addRolePower(Integer powerId,Integer[] menuIds);
    /*查询组织机构类别*/
    List<Map<String, Object>> getAllOrganization();
    /*获取职务列表*/
    List<Map<String, Object>> getAllJob();
    /*批量导入用户*/
    int addUsers(@Param("user") List<User> users);
    /*机构列表*/
    List<Map<String, Object>> organizationList(Integer schoolId);

}

