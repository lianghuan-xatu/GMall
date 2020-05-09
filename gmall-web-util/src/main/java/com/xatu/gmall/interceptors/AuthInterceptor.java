package com.xatu.gmall.interceptors;


import com.alibaba.fastjson.JSON;
import com.xatu.gmall.annotations.LoginRequired;
import com.xatu.gmall.util.CookieUtil;
import com.xatu.gmall.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 拦截代码

        //判断被拦截的请求的访问的方法的注解（是否是需要拦截的）
        HandlerMethod hm = (HandlerMethod)handler;
        LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);
        //是否拦截
        if(methodAnnotation==null){
            return true;
        }

        /**
         * 如果oldToken为null newToken为null 说明从未登lu过
         * newToken为null oldToken不为null之前登录过
         * newToken不为null oldToken为null刚刚登录过
         * newToken不为空   oldToken不为空  oldToken过期
         */
        String token = "";
        String oldToken = CookieUtil.getCookieValue(request,"oldToken",true);
        if(StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }
        String newToken = request.getParameter("token");
        if(StringUtils.isNotBlank(newToken)){
            token = newToken;
        }
        //是否必须登录
        boolean loginSuccess = methodAnnotation.loginSuccess();
        //调用认证中心进行验证
        String success = "fail";
        String ip = request.getHeader("x-forwarded-for");//通过nginx转发的获得的客户端ip
        if(StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();//从request中会的ip
            if(StringUtils.isBlank(ip)){
                ip = "192.168.157.1";
            }
        }
        String successJSON = HttpclientUtil.doGet("http://localhost:8085/verify?token="+token+"&currentIp="+ip);
        Map successMap = JSON.parseObject(successJSON, Map.class);
        if(successMap.get("status")!=null){
            success = successMap.get("status").toString();
        }
        if(loginSuccess==true){

            if(!success.equals("success")){
                //重定向到passport登录
                response.sendRedirect("http://localhost:8085/index?ReturnUrl="+request.getRequestURL());
                return false;

            }else{
                //验证通过，覆盖cookie中的tocken
                //需要将tockn携带的用户信息写入
                request.setAttribute("memberId",successMap.get("memberId"));
                request.setAttribute("nickname",successMap.get("nickname"));
                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }
                return true;
            }
        }else{
            //没有登良成功也能够使用功能，但是必须验证
            if(success.equals("success")){
                //需要将tockn携带的用户信息写入
                request.setAttribute("memberId",successMap.get("memberId"));
                request.setAttribute("nickname",successMap.get("nickname"));
                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }
            }
        }
        return true;
    }
}
