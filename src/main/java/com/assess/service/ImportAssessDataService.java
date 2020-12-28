package com.assess.service;
import com.assess.entity.Major;
import com.assess.entity.User;
import com.assess.mapper.ImportAssessDataMapper;
import com.assess.util.ImportOrExportExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.assess.entity.importAssessData;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @program: digitization-assess
 * @description:
 * @author: Gjm
 * @create: 2020-03-25 10:32
 **/
@Service
public class ImportAssessDataService {
    @Autowired
    ImportAssessDataMapper importAssessDataMapper;
    /*批量导入专业*/
    @Transactional
    public Integer addMajors(List<Major> majors) {

        return importAssessDataMapper.addMajor(majors);
    }
    /*批量导入用户*/
    @Transactional
    public int addUsers(List<User> users){
        return importAssessDataMapper.addUsers(users);
    }


    public List getAllJobLevels() {
        return importAssessDataMapper.getAllMajor();
    }
    public  List getAllCollege(){
        return importAssessDataMapper.getAllCollege();
    }
}
