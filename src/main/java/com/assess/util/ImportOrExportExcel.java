package com.assess.util;
import com.assess.entity.College;
import com.assess.entity.Major;
import com.assess.entity.User;
import com.assess.entity.UserRole;
import com.assess.mapper.SchoolMapper;
import com.assess.mapper.UserMapper;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument;
import org.omg.CORBA.IRObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: digitization-assess
 * @description: 导入或导出excel
 * @author: Gjm
 * @create: 2020-03-25 10:15
 **/

public class ImportOrExportExcel {
    @Autowired
    private UserMapper userMapperBean;

    @Autowired
    private static ImportOrExportExcel userMapper ;


    Logger logger = LoggerFactory.getLogger(ImportOrExportExcel.class);


    @PostConstruct
    public void init(){
        userMapper = this;//工具类的实例赋值给fileUtils
        userMapper.userMapperBean=this.userMapperBean;//会激活Spring对Dao的管理并赋给此类
        System.out.println("工具类已经初始化了，被纳入spring管理");

        //我们在初始化之后调用一下静态方法



    }

    //这是把数据导出到本地保存为Excel文件的方法
    public static ResponseEntity<byte[]> exportMajorExcel(List<Major> majors,List<College> colleges) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();//创建一个Excel文件

        //创建Excel文档属性，必不可少。少了的话，getDocumentSummaryInformation()方法就会返回null
        workbook.createInformationProperties();

        DocumentSummaryInformation info = workbook.getDocumentSummaryInformation();
        info.setCompany("学院id");//设置表头信息
        info.setManager("专业名称");
        info.setCategory("专业表");

        //设置文件中的日期格式
        HSSFCellStyle datecellStyle = workbook.createCellStyle();
        datecellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));//这个文件的日期格式和平时的不一样

        //创建专业表单
        HSSFSheet sheet = workbook.createSheet("专业表");
        HSSFRow r0 = sheet.createRow(0);//创建第一行
        HSSFCell c0 = r0.createCell(0);// 创建列
        HSSFCell c1 = r0.createCell(1);// 创建列
        HSSFCell c2 = r0.createCell(2);// 创建列
        c0.setCellValue("学院id");
        c1.setCellValue("专业名称");
        c2.setCellValue("是否可用");

        for (int i = 0; i < majors.size(); i++) {
            Major major = majors.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(major.getCollegeId());
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(major.getMajorName());
            HSSFCell cell2 = row.createCell(2);
            if (major.getDeleId()==0){
                cell2.setCellValue("是");
            }else {
                cell2.setCellValue("否");
            }

        }

        //创建学院表单
        HSSFSheet collegeSheet = workbook.createSheet("学院表");
        HSSFRow s0 = collegeSheet.createRow(0);//创建第一行
        HSSFCell t0 = s0.createCell(0);// 创建列
        HSSFCell t1 = s0.createCell(1);// 创建列
        HSSFCell t2 = s0.createCell(2);// 创建列
        t0.setCellValue("学院id");
        t1.setCellValue("学院名称");
        t2.setCellValue("是否可用");
       for (int i = 0; i < colleges.size(); i++) {
            College college = colleges.get(i);
            HSSFRow row = collegeSheet.createRow(i + 1);
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(college.getCollegeId());
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(college.getCollegeName());
            HSSFCell cell2 = row.createCell(2);
            if (college.getDeleId()==0){
                cell2.setCellValue("是");
            }else {
                cell2.setCellValue("否");
            }

        }

        HttpHeaders headers = new HttpHeaders();

        headers.setContentDispositionFormData("attachment",
                new String("专业.xls".getBytes("UTF-8"),"iso-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        workbook.write(baos);

        ResponseEntity<byte[]> entity = new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);

        return entity;
    }

    /*
     * @param file
     * @return java.util.List<com.assess.entity.Major>
     * @author Gejm
     * @description: 这是解析上传的Excel文件为对象集合，从而批量添加数据的方法 xls格式
     * @date 2020/4/20 10:05
     */
    public static List<Major> parseFile2List(MultipartFile file) throws IOException {
        List<Major> majors=new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        // 获取原始名字
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();//获取表单所有的行
        for (int i = 1; i < physicalNumberOfRows; i++) {
            HSSFRow row = sheet.getRow(i);
            Major major=new Major();

            //专业名称
            HSSFCell c0 = row.getCell(0);
            major.setMajorName(c0.getStringCellValue());
            //专业编号
            HSSFCell c1 = row.getCell(1);
            major.setMajorCode(c1.getStringCellValue());

            HSSFCell c2 = row.getCell(2);
            //学院id
            major.setCollegeId( c2.getStringCellValue());


            /*HSSFCell c2 = row.getCell(2);
            major.setTitlelevel(c2.getStringCellValue());*/


            majors.add(major);
        }

        return majors;
    }
    /*
     * @param file
     * @return java.util.List<com.assess.entity.Major>
     * @author Gejm
     * @description: 这是解析上传的Excel文件为对象集合，从而批量添加数据的方法 xlsx格式
     * @date 2020/4/20 10:05
     */
    public static List<Major> parseFile1List(MultipartFile file) throws IOException {
        List<Major> majors=new ArrayList<>();


        InputStream inputStream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();//获取表单所有的行

        for (int i = 1; i < physicalNumberOfRows; i++) {
            XSSFRow row = sheet.getRow(i);
            Major major=new Major();

            XSSFCell c0 = row.getCell(0);
            //专业名称
            major.setMajorName( c0.getStringCellValue());
            //专业编号
            XSSFCell c1 = row.getCell(1);
            major.setMajorCode(c1.getStringCellValue());
            //院系编号
            XSSFCell c2 = row.getCell(2);
            major.setCollegeId(c2.getStringCellValue());


            majors.add(major);
        }

        return majors;

    }

    /*
     * @param file
     * @return java.util.List<com.assess.entity.Major>
     * @author Gejm
     * @description: 导入用户
     * @date 2020/4/20 10:05
     */
    public static List<User> importUser(MultipartFile file) throws IOException {
        List<User> users=new ArrayList<>();


        InputStream inputStream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();//获取表单所有的行
        /*List<Map<String, Object>> allRole = userMapper.userMapperBean.getAllRole1();
        List<Map<String, Object>> organList = userMapper.userMapperBean.getAllOrganization();
        List<Map<String, Object>> allMajorList = userMapper.userMapperBean.getAllMajor();*/

        for (int i = 1; i < physicalNumberOfRows; i++) {
            XSSFRow row = sheet.getRow(i);
            User user=new User();

            XSSFCell c0 = row.getCell(0);
            //工号
            user.setJobNum((int)c0.getNumericCellValue() );
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

        return users;

    }

}
