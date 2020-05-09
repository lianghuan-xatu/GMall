package com.xatu.gmall.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.xatu.gmall.entity.Member;
import com.xatu.gmall.service.UserService;
import com.xatu.gmall.util.HttpclientUtil;
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


    /**
     * 社交登录
     */


    @RequestMapping("vlogin")
    public String vlogin(String code,HttpServletRequest request){

        // 授权码换取access_token
        // 换取access_token
        // client_secret=f043fe09dcab7e9b90cdd7491e282a8f
        // client_id=2173054083
        String s3 = "https://api.weibo.com/oauth2/access_token?";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","2173054083");
        paramMap.put("client_secret","f043fe09dcab7e9b90cdd7491e282a8f");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://passport.gmall.com:8085/vlogin");
        paramMap.put("code",code);// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        String access_token_json = HttpclientUtil.doPost(s3, paramMap);

        Map<String,Object> access_map = JSON.parseObject(access_token_json,Map.class);

        // access_token换取用户信息
        String uid = (String)access_map.get("uid");
        String access_token = (String)access_map.get("access_token");
        String show_user_url = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
        String user_json = HttpclientUtil.doGet(show_user_url);
        Map<String,Object> user_map = JSON.parseObject(user_json,Map.class);

        // 将用户信息保存数据库，用户类型设置为微博用户
        Member member = new Member();
        member.setSourceType(2);
        member.setAccessCode(code);
        member.setAccessToken(access_token);
        member.setSourceUid((String)user_map.get("idstr"));
        member.setCity((String)user_map.get("location"));
        member.setNickname((String)user_map.get("screen_name"));
        String g = "0";
        String gender = (String)user_map.get("gender");
        if(gender.equals("m")){
            g = "1";
        }
        member.setGender(Integer.valueOf(g));

        Member umsCheck = new Member();
        umsCheck.setSourceUid(member.getSourceUid());
        Member MemberCheck = userService.checkOauthUser(umsCheck);

        if(MemberCheck==null){
            userService.addOauthUser(member);
        }else{
            member = MemberCheck;
        }

        // 生成jwt的token，并且重定向到首页，携带该token
        String token = null;
        Long memberId = member.getId();//rpc的主键返回策略失效
        String nickname = member.getNickname();
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("memberId",memberId);
        userMap.put("nickname",nickname);


        String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
        if(StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();// 从request中获取ip
            if(StringUtils.isBlank(ip)){
                ip = "127.0.0.1";
            }
        }

        // 按照设计的算法对参数进行加密后，生成token
        token = JwtUtil.encode("2020gmall", userMap, ip);

        // 将token存入redis一份
        userService.addToken(token,memberId);


        return "redirect:http://localhost:8083/index?token="+token;
    }




}
