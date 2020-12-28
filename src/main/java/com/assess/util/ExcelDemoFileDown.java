package com.assess.util;

import com.alibaba.fastjson.JSONArray;
import com.assess.Annotation.PassToken;
import com.assess.mapper.AssessPublicMapper;
import com.assess.mapper.SchoolMapper;
import com.assess.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;


/**
 * @program: digitization-assess
 * @description: 模板下载
 * @author: Gjm
 * @create: 2020-05-12 15:54
 **/
@Slf4j
@RestController
@RequestMapping("/excelDemoDown")
public class ExcelDemoFileDown {
    @Autowired
    UserMapper userMapper;
    @Autowired
    SchoolMapper schoolMapper;
    @Autowired
    AssessPublicMapper assessPublicMapper;
    /*
     * @param response1
     * @return java.lang.String
     * @author Gejm
     * @description: 用户模板文件下载
     * @date 2020/4/8 10:46
     */

    @PassToken
    @GetMapping("/userDemoFileDown")
    public String excelUserExceport(HttpServletResponse response, String fileName,Integer schoolId,Integer tagId){
        ClassPathResource filePath = new ClassPathResource("/excelDemo/"+fileName+".xlsx");
        try {
            InputStream is = filePath.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            //读取配置表
            XSSFSheet sheet=workbook.getSheetAt(1);
            sheet.setForceFormulaRecalculation(true);
            //角色
            List<Map<String, Object>> allRole = userMapper.getAllRole1(schoolId,3);
            //机构类别
            List<Map<String, Object>> allOrganization = userMapper.getAllOrganization();
            //学院
            List<Map<String, Object>> allCollege = schoolMapper.getAllCollege(schoolId);
            List<Map<String, Object>> allMajor = userMapper.organizationList(schoolId);
            //管理机构职务
            List<Map<String, Object>> allJob = userMapper.getAllJob();
            List<Map<String, Object>> allMajorList = schoolMapper.getAllMajor(schoolId);


            //专业行数最多
            for(int d=0 ; d<allMajor.size(); d++){
                Map<String, Object> majorMap = allMajor.get(d);
                //创建行
                XSSFRow  row = sheet.createRow(d + 1);

               if (d<allRole.size()){
                   Map<String, Object> roleMap = allRole.get(d);
                    //第一列角色
                    XSSFCell cell0 = row.createCell(0);
                    cell0.setCellValue(roleMap.get("role_name")+"");
               }
               if (d<allOrganization.size()){
                   Map<String, Object> organMap = allOrganization.get(d);
                   //第二列机构类别
                   XSSFCell cell1 = row.createCell(1);
                   cell1.setCellValue(organMap.get("organ_name")+"");
               }//第三列学院
               if (d<allCollege.size()){
                   Map<String, Object> collegeMap = allCollege.get(d);
                   XSSFCell cell2 = row.createCell(2);
                   cell2.setCellValue(collegeMap.get("college_name")+"");
               }
                //第四列专业
                XSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(majorMap.get("major_name")+"");
                //第五列
                if (d<allJob.size()){
                    Map<String, Object> jobMap = allJob.get(d);
                    XSSFCell cell4 = row.createCell(4);
                    cell4.setCellValue(jobMap.get("job_name")+"");
                }
                //第六列
                if (d<allMajorList.size()){
                    Map<String, Object> majorMaplist = allMajorList.get(d);
                    XSSFCell cell5 = row.createCell(5);
                    cell5.setCellValue(majorMaplist.get("major_name")+"");
                }

            }
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            workbook.write(baos);

            ExcelDemoFileDown.demoFileDown( response,"用户导入模板.xlsx",workbook);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return  null;
    }

    /*
     * @param response1
     * @return java.lang.String
     * @author Gejm
     * @description: 专业模板文件下载
     * @date 2020/4/8 10:46
     */

    @PassToken
    @GetMapping("/majorDemoFileDown")
    public String majorDemoFileDown(HttpServletResponse response, String fileName,Integer schoolId){
        ClassPathResource filePath = new ClassPathResource("/excelDemo/"+fileName+".xlsx");
        try {
            InputStream is = filePath.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            //读取配置表
            XSSFSheet sheet=workbook.getSheetAt(1);
            sheet.setForceFormulaRecalculation(true);
            List<Map<String, Object>> allCollege = schoolMapper.getAllCollege(schoolId);
            for(int d=0 ; d<allCollege.size(); d++){
                Map<String, Object> collegeMap = allCollege.get(d);
                XSSFRow  row = sheet.createRow(d + 1);

                    //第一列
                    XSSFCell cell0 = row.createCell(0);
                    cell0.setCellValue(collegeMap.get("college_name")+"");

            }
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            workbook.write(baos);

            ExcelDemoFileDown.demoFileDown( response,"专业导入模板.xlsx",workbook);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
    /*
     * @param response1
     * @return java.lang.String
     * @author Gejm
     * @description: 院系模板文件下载
     * @date 2020/4/8 10:46
     */

    @PassToken
    @GetMapping("/collegeDemoFileDown")
    public String collegeDemoFileDown(HttpServletResponse response, String fileName){
        ClassPathResource filePath = new ClassPathResource("/excelDemo/"+fileName+".xlsx");
        try {
            InputStream is = filePath.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(is);


            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            workbook.write(baos);

            ExcelDemoFileDown.demoFileDown( response,"院系导入模板.xlsx",workbook);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
    /*
     * @param response
    	 * @param fileName
     * @return java.lang.String
     * @author Gejm
     * @description: 评估模板导出
     * @date 2020/5/13 18:46
     */
    @PassToken
    @GetMapping("/assessDemoFileDown")
    public  String assessDemoFileDown(HttpServletResponse response, String fileName){
        ClassPathResource filePath = new ClassPathResource("/excelDemo/"+fileName+".xlsx");


        try {
            InputStream is = filePath.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet=workbook.getSheetAt(2);
            //读取配置表
            sheet.setForceFormulaRecalculation(true);
            List<Map<String, Object>> allArithmetic = assessPublicMapper.getAllArithmetic();
            for(int d=0 ; d<allArithmetic.size(); d++){
                Map<String, Object> arithmeticMap = allArithmetic.get(d);
                XSSFRow  row = sheet.createRow(d + 1);

                //第一列
                XSSFCell cell0 = row.createCell(0);
                cell0.setCellValue("算法"+arithmeticMap.get("arithmetic_id")+":"+arithmeticMap.get("arithmetic_center")+"");

            }



            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            workbook.write(baos);

            ExcelDemoFileDown.demoFileDown( response,"评估模板-导入模板.xlsx",workbook);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
    /*
     * @param response
    	 * @param targetId
     * @return java.lang.String
     * @author Gejm
     * @description: 评估数据填报模板
     * @date 2020/5/21 10:12
     */
    @PassToken
    @GetMapping("/assessImportDemo")
    public  String write(HttpServletResponse response,Integer targetId) throws IOException, ClassNotFoundException {
        // 初始一个workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle style = workbook.createCellStyle();
        //excel首行格式
        //style.setFillForegroundColor(IndexedColors.RED1.getIndex());
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFFont font=workbook.createFont();
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
       // style.setWrapText(true);
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setBorderBottom(BorderStyle.THIN); // 下边框
        style2.setBorderLeft(BorderStyle.THIN);//左边框    
        style2.setBorderTop(BorderStyle.THIN);//上边框    
        style2.setBorderRight(BorderStyle.THIN);//右边框  
        //参与评估的专业列表
        List<Map<String, Object>> assessMajor = assessPublicMapper.getAssessMajor(targetId);
        //指标信息
        List<Map<String, Object>> assessIndexInfo = assessPublicMapper.getAssessIndexInfo(targetId);


        for (int i=0;i<assessIndexInfo.size();i++){
            Map<String, Object> indexInfoMap = assessIndexInfo.get(i);
            //创建sheet
            String sheetName = new BigDecimal(String.valueOf(indexInfoMap.get("index_num"))).stripTrailingZeros().toPlainString();
            HSSFSheet  sheet= workbook.createSheet(sheetName);
            HSSFRow r0 = sheet.createRow(0);//创建第一行
            //冻结首行
            sheet.createFreezePane( 0, 1, 0, 1 );
            String arithmeticId = indexInfoMap.get("arithmetic_id").toString();


            if (arithmeticId.equals("1")){

                HSSFCell c0 = r0.createCell(0);// 创建列
                HSSFCell c1 = r0.createCell(1);// 创建列
                HSSFCell c2 = r0.createCell(2);// 创建列
                c0.setCellValue("专业名称");
                c1.setCellValue(indexInfoMap.get("item_a")+"");
                c2.setCellValue(indexInfoMap.get("item_b")+"");
                c0.setCellStyle(style);
                c1.setCellStyle(style);
                c2.setCellStyle(style);
                //设置列宽
                sheet.setColumnWidth(1,(indexInfoMap.get("item_a")+"").getBytes().length*256);
                sheet.setColumnWidth(2,(indexInfoMap.get("item_b")+"").getBytes().length*256);
            }else if (arithmeticId.equals("2")){
                HSSFCell c0 = r0.createCell(0);// 创建列
                HSSFCell c1 = r0.createCell(1);// 创建列
                c0.setCellValue("专业名称");
                c1.setCellValue(indexInfoMap.get("item_a")+"");
                c0.setCellStyle(style);
                c1.setCellStyle(style);
                //设置列宽
                sheet.setColumnWidth(1,(indexInfoMap.get("item_a")+"").getBytes().length*256);

            }else if (arithmeticId.equals("3")){
                HSSFCell c0 = r0.createCell(0);// 创建列
                HSSFCell c1 = r0.createCell(1);// 创建列
                HSSFCell c2 = r0.createCell(2);// 创建列
                HSSFCell c3 = r0.createCell(3);// 创建列
                HSSFCell c4 = r0.createCell(4);// 创建列
                // HSSFCell c4 = r0.createCell(4);// 创建列
                c0.setCellValue("专业名称");
                c1.setCellValue(indexInfoMap.get("item_a")+"");
                c2.setCellValue(indexInfoMap.get("item_b")+"");
                c3.setCellValue(indexInfoMap.get("item_a")+"/"+indexInfoMap.get("item_b"));
                c4.setCellValue("结果选择");
                c0.setCellStyle(style);
                c1.setCellStyle(style);
                c2.setCellStyle(style);
                c3.setCellStyle(style);
                c4.setCellStyle(style);
                //设置列宽
                sheet.setColumnWidth(1,(indexInfoMap.get("item_a")+"").getBytes().length*256);
                sheet.setColumnWidth(2,(indexInfoMap.get("item_b")+"").getBytes().length*256);
                sheet.setColumnWidth(3,(indexInfoMap.get("item_a")+"/"+indexInfoMap.get("item_b")).getBytes().length*256);
                sheet.setColumnWidth(4,24*256);
                log.info("-----------准备算法三选项开始------------");
                // 准备下拉列表数据
                String arithmeticThree = indexInfoMap.get("arithmetic_three") + "";
                if (arithmeticThree==null){
                    log.error("-----算法三选项为空---------");
                }
                JSONArray optionJson = JSONArray.parseArray(arithmeticThree);
                List<String> optionList1 = new ArrayList<String>();
                for (int u=0;u<optionJson.size();u++){
                    optionList1.add(optionJson.getJSONObject(u).getString("option"));
                }
                //optionList1.addAll(optionList0);

                String[] option = optionList1.toArray(new String[optionList1.size()]);
                log.info("-----------准备算法三选项结束------------");
                System.out.println("---------------------"+Arrays.toString(option));




                // 设置下拉列表     从第2行到第200行  从第5列到第5列
                CellRangeAddressList regions = new CellRangeAddressList(1, 200, 4, 4);
                // 创建下拉列表数据
                DVConstraint constraint = DVConstraint.createExplicitListConstraint(option);
                // 绑定
                HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);

                sheet.addValidationData(dataValidation);
            }else if (arithmeticId.equals("4")){
                HSSFCell c0 = r0.createCell(0);// 创建列
                c0.setCellValue("专业名称");
                c0.setCellStyle(style);
                //设置列宽
                sheet.setColumnWidth(0,  256*23+184);
                //算法4标题
                String arithmeticFourTitle = indexInfoMap.get("arithmetic_four_title") + "";
                JSONArray titleJson = JSONArray.parseArray(arithmeticFourTitle);
                for (int a=0;a<titleJson.size();a++){
                    HSSFCell c1 = r0.createCell(a+1);// 创建列
                    String title = titleJson.getJSONObject(a).getString("item4");
                    c1.setCellValue(title);
                    c1.setCellStyle(style);
                    sheet.setColumnWidth(a+1,  title.getBytes().length*256);
                }


            }

            for (int j=0;j<assessMajor.size();j++){
                Map<String, Object> majorMap = assessMajor.get(j);
                String majorName = majorMap.get("major_name")+"";
                HSSFRow row = sheet.createRow(j + 1);
                HSSFCell cell0 = row.createCell(0);
                cell0.setCellValue(majorName);
                cell0.setCellStyle(style2);
                //算法3计算公式
                if (arithmeticId.equals("3") ){
                    int rowse=j+2;
                    HSSFCell cell3 = row.createCell(3);
                    //IF(OR(B7="",C7=""),"",B7/C7)
                    //System.err.println("IF(OR(B"+rowse+"=\"\",C"+rowse+"=\"\"),\"\",B"+rowse+"/C"+rowse+")");
                    cell3.setCellFormula("IF(OR(B"+rowse+"=\"\",C"+rowse+"=\"\"),\"\",B"+rowse+"/C"+rowse+")");
                    sheet.setForceFormulaRecalculation(true);
                }
            }
            //专业名称列宽
            sheet.setColumnWidth(0,  256*23+184);



        }
        //评估年度
        Map<String, Object> assessTargetYear = assessPublicMapper.getAssessTargetYear(targetId);
        Integer targetYear = ReturnStateUtil.returnInteger(assessTargetYear.get("target_year"));
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        workbook.write(baos);

        ExcelDemoFileDown.demoFileDown( response,targetYear+"年度数字化评估数据导入模板.xls",workbook);

        return null;
    }
    /*
     * @param response
    	 * @param targetId
     * @return java.lang.String
     * @author Gejm
     * @description: 导出评估结果
     * @date 2020/5/26 18:36
     */
    @PassToken
    @GetMapping("/downAssessResult")
    public String downAssessResult(HttpServletResponse response,Integer targetId){
        //ClassPathResource filePath = new ClassPathResource("/excelDemo/assessResultDemo.xlsx");
        try {
            //InputStream is = filePath.getInputStream();
           // XSSFWorkbook workbook = new XSSFWorkbook(is);

            XSSFWorkbook workbook = new XSSFWorkbook();
            //标题格式
            XSSFCellStyle majorTilestyle = workbook.createCellStyle();
            majorTilestyle.setBorderBottom(BorderStyle.THIN); // 下边框
            majorTilestyle.setBorderLeft(BorderStyle.THIN);//左边框    
            majorTilestyle.setBorderTop(BorderStyle.THIN);//上边框    
            majorTilestyle.setBorderRight(BorderStyle.THIN);//右边框  
            majorTilestyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
            majorTilestyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            XSSFSheet sheet1 = workbook.createSheet("评估榜单");
            XSSFRow sheet1r0 = sheet1.createRow(0);
            //冻结首行
            sheet1.createFreezePane( 0, 1, 0, 1 );
            //创建三列
            XSSFCell sheet1c2 = sheet1r0.createCell(0);
            sheet1c2.setCellValue("排名");
            sheet1c2.setCellStyle(majorTilestyle);
            XSSFCell sheet1c0 = sheet1r0.createCell(1);
            sheet1c0.setCellValue("专业名称");
            sheet1c0.setCellStyle(majorTilestyle);
            //设置列宽
            sheet1.setColumnWidth(1,  256*23+184);
            XSSFCell sheet1c1 = sheet1r0.createCell(2);
            sheet1c1.setCellValue("分数");
            sheet1c1.setCellStyle(majorTilestyle);

           /* //读取表
            XSSFSheet sheet=workbook.getSheetAt(0);

            XSSFCell row0Cell = sheet.getRow(0).getCell(8);
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);//左边框    
            cellStyle.setBorderTop(BorderStyle.THIN);//上边框    
            cellStyle.setBorderRight(BorderStyle.THIN);//右边框  
            //设置标题
            row0Cell.setCellValue(getFinalPublic.get(0).get("target_year")+"年度数字化评估");
            //int ceil = (int)Math.ceil(getFinalPublic.size() / 2);

            int i;*/
            //专业、成绩、排名
            List<Map<String, Object>> getFinalPublic = assessPublicMapper.getFinalPublic(targetId);
            for (int i=0;i<getFinalPublic.size();i++){
                Map<String, Object> finalPublicMap = getFinalPublic.get(i);
                XSSFRow row = sheet1.createRow(1 + i);
                XSSFCell cell5 = row.createCell(0);
                cell5.setCellValue(finalPublicMap.get("ranking")+"");
                cell5.setCellStyle(majorTilestyle);
                XSSFCell cell0 = row.createCell(1);
                cell0.setCellValue(finalPublicMap.get("major_name")+"");
                cell0.setCellStyle(majorTilestyle);
                XSSFCell cell4 = row.createCell(2);
                cell4.setCellValue(ReturnStateUtil.formatDouble(finalPublicMap.get("count_score")));
                cell4.setCellStyle(majorTilestyle);

            }
            log.info("--------星级榜开始---------");
            //创建星级榜表
            XSSFSheet sheet2 = workbook.createSheet("星级榜单");
            //创建首行
            XSSFRow sheet2r0 = sheet2.createRow(0);
            log.info("--------三星榜标题开始---------");
            XSSFCell sheet2c0 = sheet2r0.createCell(0);
            XSSFCell sheet2c10 = sheet2r0.createCell(1);
            sheet2c10.setCellStyle(majorTilestyle);
            //合并单元格起始行号，终止行号， 起始列号，终止列号
            sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
            sheet2c0.setCellValue("三星榜");
            sheet2c0.setCellStyle(majorTilestyle);
            //创建第二行标题
            XSSFRow sheet2r1 = sheet2.createRow(1);
            XSSFCell sheet2c1 = sheet2r1.createCell(0);
            sheet2c1.setCellValue("专业名称");
            sheet2c1.setCellStyle(majorTilestyle);

            //设置列宽
            sheet2.setColumnWidth(0,  256*23+185);
            XSSFCell sheet2c2 = sheet2r1.createCell(1);
            sheet2.setColumnWidth(1,  256*23+200);
            sheet2c2.setCellValue("学院名称");
            sheet2c2.setCellStyle(majorTilestyle);
            log.info("--------三星榜标题结束---------");

            log.info("--------四星榜标题开始---------");
            XSSFCell sheet2c3 = sheet2r0.createCell(3);
            XSSFCell sheet2c40 = sheet2r0.createCell(4);
            sheet2c40.setCellStyle(majorTilestyle);
            sheet2c3.setCellValue("四星榜");
            //合并单元格起始行号，终止行号， 起始列号，终止列号
            sheet2.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
            sheet2c3.setCellStyle(majorTilestyle);
            //创建第二行标题
            XSSFCell sheet2r2c3 = sheet2r1.createCell(3);
            sheet2r2c3.setCellValue("专业名称");
            sheet2r2c3.setCellStyle(majorTilestyle);

            //设置列宽
            sheet2.setColumnWidth(3,  256*23+185);
            XSSFCell sheet2r2c4 = sheet2r1.createCell(4);
            sheet2.setColumnWidth(4,  256*23+200);
            sheet2r2c4.setCellValue("学院名称");
            sheet2r2c4.setCellStyle(majorTilestyle);


            log.info("--------四星榜标题结束---------");

            log.info("--------五星榜标题开始---------");
            XSSFCell sheet2c6 = sheet2r0.createCell(6);
            XSSFCell sheet2c70 = sheet2r0.createCell(7);
            sheet2c70.setCellStyle(majorTilestyle);
            //合并单元格起始行号，终止行号， 起始列号，终止列号
            sheet2.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
            sheet2c6.setCellValue("五星榜");
            sheet2c6.setCellStyle(majorTilestyle);
            //创建第二行标题
            XSSFCell sheet2r2c6 = sheet2r1.createCell(6);
            sheet2r2c6.setCellValue("专业名称");
            sheet2r2c6.setCellStyle(majorTilestyle);
            //设置列宽
            sheet2.setColumnWidth(6,  256*23+185);
            XSSFCell sheet2r2c7 = sheet2r1.createCell(7);
            sheet2.setColumnWidth(7,  256*23+200);
            sheet2r2c7.setCellValue("学院名称");
            sheet2r2c7.setCellStyle(majorTilestyle);

            log.info("--------五星榜标题结束---------");
            //插入数据
            List<Map<String, Object>> starLevel3 = assessPublicMapper.getStarLevelNum(targetId, 3);
            List<Map<String, Object>> starLevel4 = assessPublicMapper.getStarLevelNum(targetId, 4);
            List<Map<String, Object>> starLevel5 = assessPublicMapper.getStarLevelNum(targetId, 5);

            //查询星级列表比较数量，谁的数量多，让谁创建行数
            //假定3星级数量比较多
            int careatStarMaxRow=starLevel3.size();
            //通过比较取出最大行数
            if (starLevel4.size() > careatStarMaxRow) {
                careatStarMaxRow = starLevel4.size();
            }
            if (starLevel5.size() > careatStarMaxRow) {
                careatStarMaxRow =starLevel5.size();
            }


            for (int j=0;j<careatStarMaxRow;j++){
                XSSFRow sheet2Row = sheet2.createRow(j + 2);
                //三星榜数据
                if (j<starLevel3.size()){
                    Map<String, Object> starLevel3Map = starLevel3.get(j);
                    XSSFCell sheet2RowCell0 = sheet2Row.createCell(0);
                    sheet2RowCell0.setCellValue(starLevel3Map.get("major_name")+"");
                    sheet2RowCell0.setCellStyle(majorTilestyle);
                    XSSFCell sheet2RowCell1 = sheet2Row.createCell(1);
                    sheet2RowCell1.setCellValue(starLevel3Map.get("college_name")+"");
                    sheet2RowCell1.setCellStyle(majorTilestyle);
                }
                //四星榜数据
                if (j<starLevel4.size()){
                    Map<String, Object> starLevel4Map = starLevel4.get(j);
                    XSSFCell sheet2RowCell3 = sheet2Row.createCell(3);
                    sheet2RowCell3.setCellValue(starLevel4Map.get("major_name")+"");
                    sheet2RowCell3.setCellStyle(majorTilestyle);
                    XSSFCell sheet2RowCell4 = sheet2Row.createCell(4);
                    sheet2RowCell4.setCellValue(starLevel4Map.get("college_name")+"");
                    sheet2RowCell4.setCellStyle(majorTilestyle);

                }
                //五星榜数据
                if (j<starLevel5.size()){
                    Map<String, Object> starLevel5Map = starLevel5.get(j);
                    XSSFCell sheet2RowCell6 = sheet2Row.createCell(6);
                    sheet2RowCell6.setCellValue(starLevel5Map.get("major_name")+"");
                    sheet2RowCell6.setCellStyle(majorTilestyle);
                    XSSFCell sheet2RowCell7 = sheet2Row.createCell(7);
                    sheet2RowCell7.setCellValue(starLevel5Map.get("college_name")+"");
                    sheet2RowCell7.setCellStyle(majorTilestyle);
                }
            }
            log.info("---星级榜结束----");
            log.info("---黄橙红榜开始----");
            XSSFSheet sheet3 = workbook.createSheet("黄橙红榜");
            XSSFRow sheet3r0 = sheet3.createRow(0);
            log.info("--------黄榜标题开始---------");
            XSSFCell sheet3c0 = sheet3r0.createCell(0);
            XSSFCell sheet3c10 = sheet3r0.createCell(1);
            sheet3c10.setCellStyle(majorTilestyle);
            //合并单元格起始行号，终止行号， 起始列号，终止列号
            sheet3.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
            sheet3c0.setCellValue("黄榜");
            sheet3c0.setCellStyle(majorTilestyle);
            //创建第二行标题
            XSSFRow sheet3r1 = sheet3.createRow(1);
            XSSFCell sheet3c1 = sheet3r1.createCell(0);
            sheet3c1.setCellValue("专业名称");
            sheet3c1.setCellStyle(majorTilestyle);
            //设置列宽
            sheet3.setColumnWidth(0,  256*23+185);
            XSSFCell sheet3c2 = sheet3r1.createCell(1);
            sheet3.setColumnWidth(1,  256*23+200);
            sheet3c2.setCellValue("学院名称");
            sheet3c2.setCellStyle(majorTilestyle);
            log.info("--------黄榜标题结束---------");

            log.info("--------橙榜标题开始---------");
            XSSFCell sheet3c3 = sheet3r0.createCell(3);
            XSSFCell sheet3c40 = sheet3r0.createCell(4);
            sheet3c40.setCellStyle(majorTilestyle);
            sheet3c3.setCellValue("橙榜");
            //合并单元格起始行号，终止行号， 起始列号，终止列号
            sheet3.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
            sheet3c3.setCellStyle(majorTilestyle);
            //创建第二行标题
            XSSFCell sheet3r3c3 = sheet3r1.createCell(3);
            sheet3r3c3.setCellValue("专业名称");
            sheet3r3c3.setCellStyle(majorTilestyle);

            //设置列宽
            sheet3.setColumnWidth(3,  256*23+185);
            XSSFCell sheet3r2c4 = sheet3r1.createCell(4);
            sheet3.setColumnWidth(4,  256*23+200);
            sheet3r2c4.setCellValue("学院名称");
            sheet3r2c4.setCellStyle(majorTilestyle);


            log.info("--------橙榜标题结束---------");

            log.info("--------红榜标题开始---------");
            XSSFCell sheet3c6 = sheet3r0.createCell(6);
            XSSFCell sheet3c70 = sheet3r0.createCell(7);
            sheet3c70.setCellStyle(majorTilestyle);
            //合并单元格起始行号，终止行号， 起始列号，终止列号
            sheet3.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
            sheet3c6.setCellValue("红榜");
            sheet3c6.setCellStyle(majorTilestyle);
            //创建第二行标题
            XSSFCell sheet3r2c6 = sheet3r1.createCell(6);
            sheet3r2c6.setCellValue("专业名称");
            sheet3r2c6.setCellStyle(majorTilestyle);
            //设置列宽
            sheet3.setColumnWidth(6,  256*23+185);
            XSSFCell sheet3r2c7 = sheet3r1.createCell(7);
            sheet3.setColumnWidth(7,  256*23+200);
            sheet3r2c7.setCellValue("学院名称");
            sheet3r2c7.setCellStyle(majorTilestyle);

            log.info("--------红榜标题结束---------");

            List<Map<String, Object>> yellowList = assessPublicMapper.getYellowRedOrange(targetId, 1, 0, 0);
            List<Map<String, Object>> redList = assessPublicMapper.getYellowRedOrange(targetId, 0, 1, 0);
            List<Map<String, Object>> orangeList = assessPublicMapper.getYellowRedOrange(targetId, 0, 0, 1);
            int createYroMaxRow=yellowList.size();
            //通过比较取出最大行数
            if (redList.size() > createYroMaxRow) {
                createYroMaxRow = redList.size();
            }
            if (orangeList.size() > createYroMaxRow) {
                createYroMaxRow =orangeList.size();
            }
            //插入数据
            for (int y=0;y<createYroMaxRow;y++){
                XSSFRow yroRows = sheet3.createRow(y+2);
                if (y<yellowList.size()){
                    Map<String, Object> yellowMap = yellowList.get(y);
                    XSSFCell yellowMajorName = yroRows.createCell(0);
                    yellowMajorName.setCellValue(yellowMap.get("major_name")+"");
                    yellowMajorName.setCellStyle(majorTilestyle);
                    XSSFCell yellowCollegeName = yroRows.createCell(1);
                    yellowCollegeName.setCellValue(yellowMap.get("college_name")+"");
                    yellowCollegeName.setCellStyle(majorTilestyle);
                }
                if (y<orangeList.size()){
                    Map<String, Object> orangeMap = orangeList.get(y);
                    XSSFCell orangeMajorName = yroRows.createCell(3);
                    orangeMajorName.setCellValue(orangeMap.get("major_name")+"");
                    orangeMajorName.setCellStyle(majorTilestyle);
                    XSSFCell orangeCollegeName = yroRows.createCell(4);
                    orangeCollegeName.setCellValue(orangeMap.get("college_name")+"");
                    orangeCollegeName.setCellStyle(majorTilestyle);
                }
                if (y<redList.size()){
                    Map<String, Object> redMap = redList.get(y);
                    XSSFCell redMajorName = yroRows.createCell(6);
                    redMajorName.setCellValue(redMap.get("major_name")+"");
                    redMajorName.setCellStyle(majorTilestyle);
                    XSSFCell redCollegeName = yroRows.createCell(7);
                    redCollegeName.setCellValue(redMap.get("college_name")+"");
                    redCollegeName.setCellStyle(majorTilestyle);
                }

            }




            //结束
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        workbook.write(baos);

        ExcelDemoFileDown.demoFileDown( response,getFinalPublic.get(0).get("target_year")+"年度数字化评估结果.xlsx",workbook);


    } catch (IOException e) {
        e.printStackTrace();
    }
        return null;
    }

    public static void  demoFileDown(HttpServletResponse response , String fileName, Workbook workbook)  {

       // ClassPathResource filePath = new ClassPathResource("/excelDemo/"+fileName+".xlsx");
        try {

            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }




    }


}
