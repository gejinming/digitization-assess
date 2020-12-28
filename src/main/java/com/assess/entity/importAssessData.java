package com.assess.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: digitization-assess
 * @description: 评估上传评估数据
 * @author: Gjm
 * @create: 2020-03-25 09:51
 **/

public class importAssessData implements Serializable {
    private Integer id;

    private String name;

    private String titlelevel;

    private Date createdate;

    private Boolean enabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTitlelevel() {
        return titlelevel;
    }

    public void setTitlelevel(String titlelevel) {
        this.titlelevel = titlelevel == null ? null : titlelevel.trim();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "JObLevel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", titlelevel='" + titlelevel + '\'' +
                ", createdate=" + createdate +
                ", enabled=" + enabled +
                '}';
    }



}
