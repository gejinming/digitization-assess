package com.assess.entity;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @program: digitization-assess
 * @description: 专业申诉表
 * @author: Gjm
 * @create: 2020-03-30 18:08
 **/

public class MajorAppel {
    private Integer appelId;
    /*公示id*/
    private Integer publicId;
    private Integer appelState;
    private String  appelReason;
    private String  appelStuffurl;
    private String  appelTime;
    private Integer appelUser;
    /*第几次公示*/
    private Integer publicCount;
    private String  checkReason;
    private String rejectStuffurl;
    private Integer checkUserId;
    private Date   checkDate;
    private MultipartFile fileUrl;

    public MultipartFile getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(MultipartFile fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getAppelId() {
        return appelId;
    }

    public void setAppelId(Integer appelId) {
        this.appelId = appelId;
    }

    public Integer getPublicId() {
        return publicId;
    }

    public void setPublicId(Integer publicId) {
        this.publicId = publicId;
    }

    public Integer getAppelState() {
        return appelState;
    }

    public void setAppelState(Integer appelState) {
        this.appelState = appelState;
    }

    public String getAppelReason() {
        return appelReason;
    }

    public void setAppelReason(String appelReason) {
        this.appelReason = appelReason;
    }

    public String getAppelStuffurl() {
        return appelStuffurl;
    }

    public void setAppelStuffurl(String appelStuffurl) {
        this.appelStuffurl = appelStuffurl;
    }

    public String getAppelTime() {
        return appelTime;
    }

    public void setAppelTime(String appelTime) {
        this.appelTime = appelTime;
    }

    public Integer getAppelUser() {
        return appelUser;
    }

    public void setAppelUser(Integer appelUser) {
        this.appelUser = appelUser;
    }

    public Integer getPublicCount() {
        return publicCount;
    }

    public void setPublicCount(Integer publicCount) {
        this.publicCount = publicCount;
    }

    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
    }

    public String getRejectStuffurl() {
        return rejectStuffurl;
    }

    public void setRejectStuffurl(String rejectStuffurl) {
        this.rejectStuffurl = rejectStuffurl;
    }

    public Integer getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(Integer checkUserId) {
        this.checkUserId = checkUserId;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
}
