package com.assess.Controller;

import com.assess.Annotation.PassToken;
import com.assess.entity.Major;
import com.assess.entity.RespBean;
import com.assess.entity.User;
import com.assess.mapper.UserMapper;
import com.assess.service.ImportAssessDataService;
import com.assess.util.ImportOrExportExcel;
import com.assess.util.ReturnStateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.assess.entity.importAssessData;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: digitization-assess
 * @description: 导入评估excel数据
 * @author: Gjm
 * @create: 2020-03-25 09:54
 **/

@RestController
@RequestMapping("/system/importAssess")
public class ImportAssessDataController {
    @Autowired
    ImportAssessDataService importAssessDataService;
    @Autowired
    UserMapper userMapper;


    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");

    @PassToken
    @GetMapping("/import")
    public RespBean importData(@RequestParam(value="file",required=false) MultipartFile file, HttpServletRequest req) throws IOException {
        // 获取原始名字
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        List<Major> majors=null;
        if (suffixName.equals(".xls")){

            majors= ImportOrExportExcel.parseFile2List(file);
        }else if (suffixName.equals(".xlsx")){
             majors = ImportOrExportExcel.parseFile1List(file);
        }
        for (Major major : majors) {
            System.out.println(major);
        }

        //根据上传的专业信息文件，批量添加数据
        if (importAssessDataService.addMajors(majors)>0) {
            return RespBean.ok("批量导入成功！");
        } else {
            return RespBean.error("批量导入失败！");
        }
    }

    @PassToken
    @GetMapping("/importUser")
    public RespBean importUser(@RequestParam(value="file",required=false) MultipartFile file, HttpServletRequest req) throws IOException {
       /* // 获取原始名字
        String fileName = file.getOriginalFilename();


        //List<User> users = ImportOrExportExcel.importUser(file);
        List<User> users=new ArrayList<>();


        InputStream inputStream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();//获取表单所有的行
        *//*List<Map<String, Object>> allRole = userMapper.userMapperBean.getAllRole1();
        List<Map<String, Object>> organList = userMapper.userMapperBean.getAllOrganization();
        List<Map<String, Object>> allMajorList = userMapper.userMapperBean.getAllMajor();*//*
        String jobNums="";
        for (int i = 1; i < physicalNumberOfRows; i++) {
            XSSFRow row = sheet.getRow(i);
            User user=new User();

            XSSFCell c0 = row.getCell(0);
            //工号
            int jobNum = (int) c0.getNumericCellValue();
            user.setJobNum(jobNum );
            //工号验证
            int isExist = ReturnStateUtil.returnIsExist(userMapper.checkJobNum(jobNum));
            //工号存在
            if (isExist==121){
                jobNums=jobNums+jobNum+"";
            }else {
                //用户名
                XSSFCell c1 = row.getCell(1);
                user.setUserName(c1.getStringCellValue());
                //性别
                XSSFCell c2 = row.getCell(2);
                user.setSex(c2.getStringCellValue());
                //角色
                XSSFCell c3 = row.getCell(3);
                String roleString = c3.getStringCellValue();
                String roleIds = roleString.substring(roleString.indexOf("(")+1, roleString.indexOf(")"));
                user.setRoleId(Integer.parseInt(roleIds));

                //联系方式
                XSSFCell c4 = row.getCell(4);
                //String stringCellValue = c4.getStringCellValue();
                user.setPhone(c4.getStringCellValue()+"");
                //机构类型
                XSSFCell c5 = row.getCell(5);
                String organTypes = c5.getStringCellValue();
                String organType = organTypes.substring(organTypes.indexOf("(")+1, organTypes.indexOf(")"));
                user.setOrganType(Integer.parseInt(organType));
                //机构名称
                XSSFCell c6 = row.getCell(6);
                String organString = c6.getStringCellValue();
                String organs = organString.substring(organString.indexOf("(")+1, organString.indexOf(")"));
                if (organType.equals("1")){
                    user.setJobId(Integer.parseInt(organs));
                }else {
                    user.setMajorId(Integer.parseInt(organs));
                }


                users.add(user);
            }

        }

        for (User user : users) {
            System.out.println(user);
        }

        //批量添加数据
        if (importAssessDataService.addUsers(users)>0) {
            return RespBean.ok("批量导入成功！");
        } else {
            return RespBean.error("批量导入失败！");
        }*/
        return RespBean.error("批量导入失败！");
    }

    @PassToken
    @GetMapping("/export")
    //ResponseEntity里面装了所有响应的数据
    public ResponseEntity<byte[]> exportExcel() throws IOException {
        return ImportOrExportExcel.exportMajorExcel(importAssessDataService.getAllJobLevels(),importAssessDataService.getAllCollege());
    }

}
