package com.assess.util;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: digitization-assess
 * @description: 返回状态的工具类
 * @author: Gjm
 * @create: 2020-03-26 14:19
 **/

public class ReturnStateUtil {

    /*
     * @param i
     * @return int
     * @author Gejm
     * @description: 添加状态
     * @date 2020/3/31 11:25
     */
    public static  int returnState(int i){
        int state;
        if (i!=0){
            //添加成功
           state=104;
        }else {
            state=105;
        }
        return state;
    }
    /*
     * @param i
     * @return int
     * @author Gejm
     * @description: 删除状态
     * @date 2020/3/31 11:25
     */
    public static  int returnDelState(int i){
        int state;
        if (i!=0){
            //删除成功
            state=106;
        }else {
            state=107;
        }
        return state;
    }

    /*
     * @param i
     * @return int
     * @author Gejm
     * @description: 修改状态
     * @date 2020/3/31 11:25
     */
    public static  int returnUpdateState(int i){
        int state;
        if (i!=0){
            //成功
            state=108;
        }else {
            state=109;
        }
        return state;
    }
    /*
     * @param obj
     * @return java.lang.Integer
     * @author Gejm
     * @description: object类型数据转integer
     * @date 2020/3/27 10:46
     */
    public static  Integer returnInteger(Object obj){
        String objs = obj.toString();
        return Integer.parseInt(objs);
    }
    /*
     * @param obj
     * @return java.lang.Integer
     * @author Gejm
     * @description: object类型数据转double
     * @date 2020/3/27 10:46
     */
    public static  double returnDouble(Object obj){
        String objs = obj.toString();
        return Double.parseDouble(objs);
    }

    /*
     * @param rusultMap
     * @return int
     * @author Gejm
     * @description: 判断是编号否存在
     * @date 2020/4/21 10:39
     */
    public static int returnIsExist(Map rusultMap){
        int state;
        if (rusultMap==null){
            //不存在
            state=120;
        }else {
            //存在
            state=121;
        }
        return state;

    }
    /*
     * @param rusultMap
     * @return int
     * @author Gejm
     * @description: 判断名称是否存在
     * @date 2020/4/21 10:39
     */
    public static int returnNameIsExist(Map rusultMap){
        int state;
        if (rusultMap==null){
            //不存在
            state=125;
        }else {
            //存在
            state=126;
        }
        return state;

    }
    /*
     * @param rusultMap
     * @return int
     * @author Gejm
     * @description: 下载状态
     * @date 2020/4/21 10:39
     */
    public static int returnDownFileState(boolean state){
        int stateId;
        if (state){
            //下载成功
            stateId=114;
        }else {
            //下载失败！
            stateId=115;
        }
        return stateId;

    }
    /*
     * @param list
     * @return java.lang.String
     * @author Gejm
     * @description: json转spring
     * @date 2020/4/24 15:30
     */
    public static String returnStringData(List list){
        JSONArray fourTitleJson=  (JSONArray)JSONArray.toJSON(list);

        return fourTitleJson.toString();

    }
    public static String returnFormatDate(Object date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return  dateFormat.format(date);

    }
    /*
     * @param value
     * @return java.lang.String
     * @author Gejm
     * @description: 保留两位小数
     * @date 2020/5/26 18:42
     */
    public static String formatDouble(Object value) {
        BigDecimal score = new BigDecimal(String.valueOf(value));

        score = score.setScale(2, RoundingMode.HALF_UP);
        return score.toString();
    }
}
