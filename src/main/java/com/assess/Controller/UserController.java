package com.assess.Controller;

import com.assess.Annotation.PassToken;
import com.assess.Annotation.UserLoginToken;
import com.assess.entity.*;
import com.assess.mapper.SchoolMapper;
import com.assess.mapper.UserMapper;
import com.assess.util.ReturnStateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 用户操作类
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserMapper usermapper;
    @Autowired
    SchoolMapper schoolMapper;

    /*
     *
     * @param userId
     * @return com.assess.entity.User
     * @author Gejm
     * @info: 根据user_id查找信息
     * @date 2020/3/23 18:41
     */
    @GetMapping("/getUserById")
    public Map getUserById( Integer userId,Integer schoolId){
        List<Map<String, Object>> user = usermapper.getUserByid(userId);
        List<Map<String, Object>> organizationList = usermapper.getAllOrganization();
        List<Map<String, Object>> jobList = usermapper.getAllJob();
        List<Map<String, Object>> collegeList = schoolMapper.getAllCollege(schoolId);
        List<Map<String, Object>> majorList = usermapper.getAllMajor(schoolId);

        Map<String, Object> result = new HashMap();
        //userId为空或没有找到信息
        if (userId==null ||user.size()==0){
            result.put("state",103);

            return result;
        }
        result.put("userInfo",user);
        result.put("organizationList",organizationList);
        result.put("jobList",jobList);
        result.put("collegeList",collegeList);
        result.put("majorList",majorList);
        return result;

    }

    /*
     * @param
     * @return java.util.Map
     * @author Gejm
     * @description: 添加用户之前获取的数据
     * @date 2020/5/6 15:01
     */
    @GetMapping("/getAddUserData")
    public Map getAddUserData(Integer schoolId){
        HashMap result = new HashMap();
        List<Map<String, Object>> organizationList = usermapper.getAllOrganization();
        List<Map<String, Object>> jobList = usermapper.getAllJob();
        List<Map<String, Object>> collegeList = schoolMapper.getAllCollege(schoolId);
        List<Map<String, Object>> majorList = usermapper.getAllMajor(schoolId);
        result.put("organizationList",organizationList);
        result.put("jobList",jobList);
        result.put("collegeList",collegeList);
        result.put("majorList",majorList);
        return result;
    }
    /*
     *
     * @param user
     * @return com.assess.entity.User
     * @author Gejm
     * @info: 添加用户
     * @date 2020/3/23 18:41
     */
    @PostMapping("/addUser")
    public Map addUser(User user){
        //返回1，添加成功！返回0添加失败！
        Map hashMap = new HashMap();
        //验证工号是否占用
        Integer jobNum = user.getJobNum();
        int state=0;

        Map<String, Object> jobNums = usermapper.checkJobNum(jobNum,user.getSchoolId());

        if (jobNums==null) {

            int star = usermapper.insertUser(user);
            state = ReturnStateUtil.returnState(star);
        }else {
            state=100;
        }



        hashMap.put("state",state);
        return hashMap;


    }
    /*
     * @param jobNum
    	 * @param userName
    	 * @param roleId
    	 * @param majorId
     * @return java.util.List
     * @author Gejm
     * @description: 模糊查询根据工号 用户名 角色 专业查询用户
     * @date 2020/4/26 14:24
     */
    @GetMapping("/getUser")
    public List getUser(Integer jobNum,String userName,Integer roleId,Integer majorId){
        List<Map<String, Object>> userList = usermapper.getUser(jobNum, userName, roleId, majorId);
        return userList;
    }
    /*
     * @param user
     * @return java.util.Map
     * @author Gejm
     * @description: 修改密码
     * @date 2020/5/21 15:23
     */
    @PostMapping("/updatePassword")
    public Map updatePassword(User user){
        HashMap result = new HashMap();
        int state;
        if (user.getUserId()==null || user.getPassword()==null ||user.getPasswords()==null){
           log.error("------密码修改数据填写不完整----");
            state=129;
        }else {
            List<Map<String, Object>> users = usermapper.getUserByid(user.getUserId());
            //原密码
            String password0 = users.get(0).get("password").toString();
            if (user.getPasswords().equals(password0)){
                int i = usermapper.updatePassWord(user);
                state = ReturnStateUtil.returnUpdateState(i);
            }else{
                state=132;
            }
        }

        result.put("state",state);
        return result;

    }
    /*
     *
     * @param
     * @return java.util.List
     * @author Gejm
     * @info: 查询所有用户
     * @date 2020/3/23 18:42
     */
    @GetMapping("/getAllUser")
    public List getAllUser(Integer schoolId,Integer tagId){
        //tagId=1 查 tagId=2角色的用户  tagId=2、3 查 tagId=3的角色用户
        if (tagId!=3){
            tagId+=1;
        }
        List<Map<String, Object>> allUser = usermapper.getAllUser(schoolId,tagId);
        return allUser;
    }
    /*
     *
     * @param userId
     * @return int
     * @author Gejm
     * @info: 删除用户修改state_id
     * @date 2020/3/23 18:42
     */
    @GetMapping("/deleUser")
    @ResponseBody
    public Map deleUser(Integer userId){
        HashMap hashMap = new HashMap();
        int sta = usermapper.deleUser(userId);
        int state = ReturnStateUtil.returnDelState(sta);
        hashMap.put("state",state);
        return hashMap;
    }

    /*
     * @param user
     * @return java.util.List
     * @author Gejm
     * @description: 修改成功后，查询所有用户state状态108添加成功，109失败
     * @date 2020/3/24 15:02
     */
    @PostMapping("/updateUser")
    @ResponseBody
    public Map updateUser(User user){
        //验证工号是否存在
        List<Map<String, Object>> userInfo = usermapper.getUserByid(user.getUserId());
        Integer jobNum0 = ReturnStateUtil.returnInteger(userInfo.get(0).get("job_num"));
        Integer jobNum = user.getJobNum();
        //判断原始工号与现在是否相同，相同则不验证是否存在
        int state;
        if (jobNum0.equals(jobNum)){
            state=120;
        }else{
            Map<String, Object> jobNums = usermapper.checkJobNum(jobNum,user.getSchoolId());
            state=ReturnStateUtil.returnIsExist(jobNums);
        }

        if (state==120) {
            int sta = usermapper.updateUser(user);
             state = ReturnStateUtil.returnUpdateState(sta);
        }
        Map<String, Object> result = new HashMap();
        result.put("state",state);
        return result;
    }
    /**
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 获取所有角色
     * @date 2020/3/24 15:25
     */

    @GetMapping("/getAllRole")
    public List getAllRole(Integer schoolId,Integer tagId){
        //tagId=1 查 tagId=2的角色  tagId=2 查 tagId=3的角色
        if (tagId!=3){
            tagId+=1;
        }

        List<Map<String, Object>> allRole = usermapper.getAllRole(schoolId,tagId);
        return allRole;
    }
    /*
     * @param roleId
     * @return java.util.Map
     * @author Gejm
     * @description: 获取某个角色信息
     * @date 2020/4/20 16:47
     */

    @GetMapping("/getRoleInfo")
    public Map getRoleInfo(Integer roleId){
        Map<String, Object> roleInfo = usermapper.getRoleInfo(roleId);
        return roleInfo;
    }
    /*
     * @param userRole
     * @return java.util.Map
     * @author Gejm
     * @description: 修改角色信息
     * @date 2020/4/20 16:50
     */
    @PostMapping("/updateRoleInfo")
    public Map updateRoleInfo(UserRole userRole){
        int i = usermapper.updateRoleInfo(userRole);
        int state = ReturnStateUtil.returnUpdateState(i);
        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        return  hashMap;
    }
    /*
     * @param userRole
     * @return java.util.Map
     * @author Gejm
     * @description: 添加角色
     * @date 2020/4/20 16:52
     */
    @PostMapping("/addRole")
    public Map addRole(UserRole userRole){
        //获取目前最大roleId
        //Map<String, Object> maxRoleId = usermapper.getMaxRoleId();
        int i = usermapper.addRole(userRole);
        int state=0;
        if (i>0){
            int i1=usermapper.updatePowerId(userRole);
             state = ReturnStateUtil.returnState(i1);
        }

        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        return  hashMap;
    }
    /*
     * @param
     * @return java.util.Map
     * @author Gejm
     * @description: 删除角色
     * @date 2020/4/21 11:06
     */
    @GetMapping("/deleRole")
    public  Map deleRole(Integer roleId,Integer powerId){
        //删除角色
        int delstate=usermapper.deleRole(roleId);
        if (delstate>0){
            //删除权限
            usermapper.delRolePower(powerId);
        }
        int state=ReturnStateUtil.returnDelState(delstate);
        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        return hashMap;
    }
    /**
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 获取所有专业
     * @date 2020/3/24 15:25
     */
    @GetMapping("/getAllMajor")
    public List getAllMajor(Integer schoolId){
        List<Map<String, Object>> allMajor = usermapper.getAllMajor(schoolId);
        return allMajor;
    }
    /*
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 查询所有菜单
     * @date 2020/3/24 16:22
     */
    @GetMapping("/getAllMenu")
    public Map getAllMenu(){
        List<Map<String, Object>> allmenu = usermapper.getAllmenu();
        List<Map<String, Object>> menuList = new ArrayList();
        for (int i=0;i<allmenu.size();i++){
            Map<String, Object> menuMap1 = allmenu.get(i);
            ArrayList menuLevel2 = new ArrayList();
            //一级菜单
            if (menuMap1.get("menu_level").equals(1)){
                Map<String, Object> level1Map = new HashMap();
                level1Map.put("menuId",menuMap1.get("menu_id"));
                level1Map.put("menuName",menuMap1.get("menu_name"));
                //二级菜单
                for (int j=i+1;j<allmenu.size();j++){
                    Map<String, Object> menuMap2 = allmenu.get(j);
                    if (menuMap2.get("menu_level").equals(2)){
                        Map<String, Object> level2 = new HashMap();
                        if (menuMap1.get("menu_id").equals(menuMap2.get("parent_menu"))){
                            level2.put("menuId", menuMap2.get("menu_id"));
                            level2.put("menuName", menuMap2.get("menu_name"));
                            menuLevel2.add(level2);
                            level1Map.put("menuLevel2",menuLevel2);
                        }
                    }

                }
                menuList.add(level1Map);
            }
        }
        Map<Object, Object> resultMap = new HashMap();
        resultMap.put("menuList",menuList);
        return resultMap;
    }
    /*
     * @param
     * @return java.util.List
     * @author Gejm
     * @description: 根据角色查询菜单权限
     * @date 2020/3/24 16:22
     */
    @GetMapping("/getRolePower")
    public List getRolePower(Integer roleId){
        List<Map<String, Object>> allRolePower = usermapper.getRolePower(roleId);
        return allRolePower;
    }
    /*
     * @param
     * @return java.util.Map
     * @author Gejm
     * @description: 修改角色权限
     * @date 2020/4/20 17:06
     */
    @PostMapping("/updateRolePower")
    public Map updateRolePower(@RequestBody Power power){
        int state;
       if ( power.getMenuIds()==null){
            state=109;
        }else {
            //先删除权限
            usermapper.delRolePower(power.getPowerId());
            //添加权限
            int i = usermapper.addRolePower(power.getPowerId(), power.getMenuIds());
            state = ReturnStateUtil.returnUpdateState(i);
        }

        HashMap hashMap = new HashMap();
        hashMap.put("state",state);
        return  hashMap;
    }
    /*
     * @param users
     * @return java.util.Map
     * @author Gejm
     * @description: 批量添加用户
     * @date 2020/5/13 16:07
     */
    @PassToken
    @Transactional
    @PostMapping("/addUsers")
    public Map addUsers(@RequestBody  List<User> users){
        HashMap result = new HashMap();
        int state=0;
        for (int i=0; i<users.size();i++){
            User user = users.get(i);
            Integer jobNum = user.getJobNum();
            if (jobNum==null){
                state=128;

            }

        }
        if (state==0){
            //批量添加数据
            state = ReturnStateUtil.returnState(usermapper.addUsers(users));
        }

        result.put("state",state);

        return result;
    }
    /*
     * @param schoolId
     * @return java.util.List
     * @author Gejm
     * @description: 所属机构包括专业及教务处
     * @date 2020/5/21 15:48
     */
    @GetMapping("/organizationList")
    public List organizationList(Integer schoolId){

        List<Map<String, Object>> list = usermapper.organizationList(schoolId);
        return  list;
    }
}
