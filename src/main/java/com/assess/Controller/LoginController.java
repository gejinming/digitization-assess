package com.assess.Controller;

import com.assess.Annotation.PassToken;
import com.assess.Common.JwtTokenUtil;
import com.assess.mapper.LoginMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: digitization-assess
 * @description: 登录验证及注销
 * @author: Gjm
 * @create: 2020-03-24 09:24
 **/
@Slf4j
@RestController
public class LoginController {
    @Autowired
    LoginMapper loginMapper;
    /*
     * @param userId
    	 * @param password
    	 * @param session
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     * @author Gejm
     * @description: 登录验证并查询用户的菜单
     * @date 2020/3/24 16:19
     */
    @PassToken
    @PostMapping("/checkUser")
    public Map checkUser(Integer jobNum, String password,Integer schoolId, HttpSession session){
        Map Map = new HashMap();
        Map hashMap = new HashMap();
        List<Map<String, Object>> lists = new ArrayList();
        int state=0;
        if (null==jobNum || null==password){
           state=101;
        }else {
            //判断是否是系统管理员
            Map<String, Object> stringObjectMap = loginMapper.checkUserAdmin(jobNum, password);
            Integer adminId=0;
            if (stringObjectMap!=null){
                adminId=1;
            }
            List<Map<String, Object>> list = loginMapper.checkUser(jobNum, password,schoolId,adminId);


            if (list.size() > 0) {
                //将用户信息返回
                Map.put("userId", list.get(0).get("user_id"));
                Map.put("userName", list.get(0).get("user_name"));
                Map.put("schoolId", list.get(0).get("school_id"));
                Map.put("roleId", list.get(0).get("role_id"));
                Map.put("roleName", list.get(0).get("role_name"));
                Map.put("majorName", list.get(0).get("major_name"));
                Map.put("majorId", list.get(0).get("major_id"));
                Map.put("tagId",list.get(0).get("tag_id"));
                String token=JwtTokenUtil.createToken(list.get(0).get("user_id").toString());
                Map.put("token",token);
                session.setAttribute("majorId", list.get(0).get("major_id"));
                session.setAttribute("userId", list.get(0).get("user_id"));
                session.setAttribute("schoolId", list.get(0).get("school_id"));
                log.info("用户Id:"+list.get(0).get("user_id")+"登录成功！");
                //判断此用户是否有菜单权限
                if (list.get(0).get("menu_name")==null || list.get(0).get("menu_name").equals("")){
                    state=300;
                    hashMap.put("state",state);
                    return hashMap;
                };
                List<Map<String, Object>> menuList = new ArrayList();

                for (int i = 0; i < list.size(); i++) {
                    Map userMenuLeve1 = list.get(i);

                    ArrayList subs = new ArrayList();

                    //一级菜单
                    if (userMenuLeve1.get("menu_level").equals(1)){
                        Map<String, Object> leve1 = new HashMap();
                        leve1.put("menuId", userMenuLeve1.get("menu_id"));
                        leve1.put("icon", userMenuLeve1.get("icon"));
                        leve1.put("index", userMenuLeve1.get("index"));
                        leve1.put("title", userMenuLeve1.get("menu_name"));
                        //二级菜单拼接
                        for (int j=i+1;j<list.size();j++){
                            Map userMenuLeve2 = list.get(j);
                            if (userMenuLeve2.get("menu_level").equals(2)) {
                                Map<String, Object> level2 = new HashMap();
                                if (userMenuLeve1.get("menu_id").equals(userMenuLeve2.get("parent_menu"))) {
                                    level2.put("menuId", userMenuLeve2.get("menu_id"));
                                    level2.put("index", userMenuLeve2.get("index"));
                                    level2.put("title", userMenuLeve2.get("menu_name"));
                                    subs.add(level2);
                                    leve1.put("subs", subs);
                                }
                            }

                        }
                        menuList.add(leve1);
                    }
                    /*else if (userMenu.get("menu_level").equals(2)){
                        level2.put("index", userMenu.get("index"));
                        level2.put("title", userMenu.get("menu_name"));
                        subs.add(level2);
                        leve1.put("subs",subs);
                    }*/


                }
                hashMap.put("items", menuList);
                hashMap.put("userInfo",lists);
                state=102;
            } else {

                state=101;

            }

        }
        lists.add(Map);

        hashMap.put("state",state);

        return hashMap;
    }
    /*
     * @param session
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author Gejm
     * @description: 注销
     * @date 2020/3/24 17:25
     */
    @GetMapping("/logout")
    public Map<String,Object> logout(HttpSession session){
        session.removeAttribute("userId");
        session.removeAttribute("majorId");
        HashMap hashMap = new HashMap();
        hashMap.put("state",0);
        return  hashMap;
    }

}
