package com.marco.toutiao.controller;

import com.marco.toutiao.model.HostHolder;
import com.marco.toutiao.service.UserService;
import com.marco.toutiao.util.ToutiaoUtil;
import com.sun.deploy.net.HttpResponse;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @ResponseBody
    @RequestMapping(path = {"/regi"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String register(Model model, @RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @RequestParam(value = "remember", defaultValue = "0")int remenber){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = userService.register(username, password);
            if(map.size() == 0){
                return ToutiaoUtil.getJSONString(0, "register success");
            }else{
                return ToutiaoUtil.getJSONString(1, map);
            }
        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }
    }


    @ResponseBody
    @RequestMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "remember", defaultValue = "0") int remember,
                        HttpServletResponse response){
        if(hostHolder.getUser() != null){
            return ToutiaoUtil.getJSONString(1, "用户已经登录");
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map = userService.login(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(remember > 0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "登陆成功");
            }else{
                return ToutiaoUtil.getJSONString(1, map);
            }

        }catch (Exception e){
            logger.error("登录异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登录异常");
        }
    }


    @RequestMapping("/logout")
    public String logout(@CookieValue(value = "ticket", required = false) String ticket ){
        if(hostHolder.getUser() != null) {
            userService.logout(ticket);
        }
        return "redirect:/";
    }
}
