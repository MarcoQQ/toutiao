package com.marco.toutiao.service;


import com.marco.toutiao.dao.LoginTicketDAO;
import com.marco.toutiao.dao.UserDAO;
import com.marco.toutiao.model.LoginTicket;
import com.marco.toutiao.model.User;
import com.marco.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketdao;

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public Map<String, Object> register(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }

        if(StringUtils.isBlank(password)){
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg", "用户名已经被注册");
            return map;
        }
        //
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        Random random = new Random();
        user.setHeadUrl(String.format("http://images.toutiao.com/head/%dt.png",random.nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);



        return map;

    }

    public Map<String, Object> login(String username, String password){
        Map<String, Object>map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }

        if(StringUtils.isBlank(password)){
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg", "用户名未被注册");
            return map;
        }

        String Md5 = user.getPassword();


        String salt = user.getSalt();
        logger.info(Md5);
        logger.info(salt + password);
        logger.info(ToutiaoUtil.MD5(password + salt));
        if(Md5.equals(ToutiaoUtil.MD5(password + salt))){
            //ticket 登陆成功
            String ticket = addLoginTicket(user.getId());
            map.put("ticket", ticket);
        }else{
            map.put("msg", "密码不正确");
        }
        return map;

    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketdao.addLoginTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket){
        loginTicketdao.updateStatus(ticket, 1);

    }
}
