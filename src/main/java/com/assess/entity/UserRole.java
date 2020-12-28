package com.assess.entity;

/**
 * @program: digitization-assess
 * @description: 用户权限实体类
 * @author: Gjm
 * @create: 2020-03-24 15:17
 **/

public class UserRole {
    private  Integer roleId;
    private  String  roleName;
    private  Integer powerId;
    private  String  roleDetail;
    private  Integer schoolId;

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getRoleDetail() {
        return roleDetail;
    }

    public void setRoleDetail(String roleDetail) {
        this.roleDetail = roleDetail;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getPowerId() {
        return powerId;
    }

    public void setPowerId(Integer powerId) {
        this.powerId = powerId;
    }
}
