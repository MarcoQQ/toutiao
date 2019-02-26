package com.marco.toutiao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;


@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);



    //RequestParam 默认 required=true
    @ResponseBody
    @RequestMapping("/profile/{groupId}/{userId}")
    public String Profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "marco") String key){
        return String.format("{%s}, {%d}, {%d}, {%s}", groupId, userId, type, key);
    }

    @ResponseBody
    @RequestMapping("/request")
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headers = request.getHeaderNames();
        while(headers.hasMoreElements()){
            String name = headers.nextElement();
            System.out.println("name" + request.getHeader(name) + "<br/>");
            sb.append(name + " : " + request.getHeader(name) + "<br/>");
        }

        for(Cookie cookie : request.getCookies()){
            sb.append(cookie.getName());
            sb.append(" : ");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }
        return sb.toString();



    }

    @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable int code,
                                HttpSession session ){
        RedirectView rv = new RedirectView("/", true);
        if(code == 301){
            //永久的跳转,浏览器直接记住
            rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        session.setAttribute("msg", "redirect");
        return rv;
    }

//    @ExceptionHandler()
//    @ResponseBody
//    public String error(Exception e){
//        return "error:" + e.getMessage();
//    }
}
