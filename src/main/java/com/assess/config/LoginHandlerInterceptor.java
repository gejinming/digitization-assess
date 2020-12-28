package com.assess.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @program: digitization-assess
 * @description: 登录拦截
 * @author: Gjm
 * @create: 2020-03-24 17:29
 **/

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userId = request.getSession().getAttribute("userId");
        if(userId == null){
            //未登陆，返回登陆页面
            request.setAttribute("msg","没有权限请先登陆");
            return false;
        }else{
            //已登陆，放行请求
            return true;
        }

    }

}
