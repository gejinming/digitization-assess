package com.assess.Interceptor;

import com.assess.Common.CustomerDefinedException;
import com.assess.Common.GloablExceptionHandler;
import com.assess.mapper.UserMapper;
import com.assess.Annotation.PassToken;
import com.assess.Annotation.UserLoginToken;
import com.assess.Common.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;


@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserMapper userService;

    //请求处理之前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object
                             ) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod) object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }else{
            if (token == null) {
                log.error("无token，请重新登录");
                //throw new RuntimeException("无token，请重新登录");
                throw new CustomerDefinedException(203,"无token，请重新登录！");
            }


            // 验证 token
            if(JwtTokenUtil.verifyToken(token)){

                return true;
            }else {
                log.error("==========token已过期！=========");
                throw new CustomerDefinedException(203,"token错误，或已失效,请重新登录！");


            }
        }
        return true;

    }
    //请求处理之后调用
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    //请求之后
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
