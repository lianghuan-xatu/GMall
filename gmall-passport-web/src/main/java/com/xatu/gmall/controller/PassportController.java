package com.xatu.gmall.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.xatu.gmall.entity.Member;
import com.xatu.gmall.service.UserService;
import com.xatu.gmall.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
public class PassportController {

    @Reference
    UserService userService;

    @RequestMapping("/index")
    public String index(String ReturnUrl, ModelMap map){
        map.put("ReturnUrl", ReturnUrl);
        return "index";
    }


    @ResponseBody
    @RequestMapping("/login")
    public String login(Member loginMember, HttpServletRequest request){
        String token = "";
        //调用用户服务，验证用户名和密码
        Member member = userService.login(loginMember);
        if(member!=null){
            //登陆成功
            //用jwt制作token
            Long memberId = member.getId();
            String nickName = member.getNickname();
            Map<String,Object> userMap = new HashMap<>();
            userMap.put("memberId",memberId);
            userMap.put("nickName",nickName);
            String ip = request.getHeader("x-forwarded-for");//通过nginx转发的获得的客户端ip
            if(StringUtils.isBlank(ip)){
                 ip = request.getRemoteAddr();//从request中会的ip
                 if(StringUtils.isBlank(ip)){
                     ip = "192.168.157.1";
            }
            }
            //按照设计的算法对参数进行加密，生成token
            token = JwtUtil.encode("2020gmall",userMap,ip);

            //将token存入redis一份
            userService.addToken(token,memberId);

        }else{
            //登陆失败
            token = "fail";

        }
        return token;
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verify(String token,String currentIp){
        //通过jwt校验token真假
        Map<String,String> map = new HashMap<>();
        Map<String, Object> decode = null;
        if(StringUtils.isNotBlank(token)&&StringUtils.isNotBlank(currentIp)){
         decode = JwtUtil.decode(token, "2020gmall", currentIp);
        }

        if(decode!=null){
            map.put("status","success");
            map.put("memberId",decode.get("memberId").toString());
            map.put("nickName",decode.get("nickName").toString());
        }else{
            map.put("status","fail");
        }
        return JSON.toJSONString(map);
    }




}
