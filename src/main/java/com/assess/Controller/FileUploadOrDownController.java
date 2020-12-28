package com.assess.Controller;

import com.assess.Annotation.PassToken;
import com.assess.util.ReturnStateUtil;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: digitization-assess
 * @description: 文件上传和下载
 * @author: Gjm
 * @create: 2020-04-03 14:39
 **/
@RestController
@RequestMapping("/file")
public class FileUploadOrDownController {
    private final static Logger logger = LoggerFactory.getLogger(FileUploadOrDownController.class);


    public static String uploadFile(MultipartFile file) {

        //String realPath  = request.getSession().getServletContext().getRealPath("upload");
        if (file.isEmpty()) {
            return "文件为空";
        }

        // 获取原始名字
        String fileName = file.getOriginalFilename();
        //获取前缀
        String name = fileName.substring(0, fileName.indexOf("."));
        // 获取后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 文件保存路径
        String filePath = "c:/upload/";
        // 文件重命名，防止重复
        long totalMilliSeconds = System.currentTimeMillis();
        long totalSeconds = totalMilliSeconds / 1000;
        //求出现在的秒
        long currentSecond = totalSeconds % 60;
        String path = filePath + name + currentSecond + suffixName;
        String fileNames = name + currentSecond + suffixName;
        // 文件对象
        File dest = new File(path);
        // 判断路径是否存在，如果不存在则创建
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            // 保存到服务器中
            file.transferTo(dest);
            logger.info("上传路径", path);
            return fileNames;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    /*
     * @param fileUrl
     * @return java.util.Map
     * @author Gejm
     * @description: 判断文件是否存在
     * @date 2020/5/7 9:51
     */
    @GetMapping("/judgeIsExist")
    public Map judgeIsExist(String fileUrl) {
        HashMap result = new HashMap();
        String filePath = "c:/upload/";
        File file = new File(filePath + "/" + fileUrl);
        if (file.exists()) { //判断文件是否存在
            result.put("state", 114);
        } else {
            result.put("state", 115);
        }
        return result;
    }

    /*
     * @param response1
     * @return java.lang.String
     * @author Gejm
     * @description: 文件下载
     * @date 2020/4/8 10:46
     */
    @PassToken
    @GetMapping("/filedown")
    public String downLoad(HttpServletResponse response, String fileUrl) throws UnsupportedEncodingException {
        String filePath = "c:/upload/";
        File file = new File(filePath + "/" + fileUrl);
        //原始文件名
        String fileName0 = fileUrl.substring(0, fileUrl.indexOf(".") - 2);
        String fileFormat = fileUrl.substring(fileUrl.indexOf("."));
        String fileName = fileName0 + fileFormat;
        if (file.exists()) { //判断文件是否存在
            // 配置文件下载
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流


            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            logger.info("----------file download---" + fileUrl);
            System.out.println("----------file download---" + fileUrl);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
        return null;
    }

}
