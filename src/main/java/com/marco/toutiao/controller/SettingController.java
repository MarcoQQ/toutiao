package com.marco.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SettingController {
    @ResponseBody
    @RequestMapping("/setting")
    public String setting(){
        return "";
    }
}
