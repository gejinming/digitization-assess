package com.assess.entity;

/**
 * @program: digitization-assess
 * @description: 权限
 * @author: Gjm
 * @create: 2020-04-20 18:47
 **/

public class Power {
    private  Integer powerId;
    private  Integer[] menuIds;

    public Integer getPowerId() {
        return powerId;
    }

    public void setPowerId(Integer powerId) {
        this.powerId = powerId;
    }

    public Integer[] getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(Integer[] menuIds) {
        this.menuIds = menuIds;
    }
}
