package com.marco.toutiao.controller;

import com.marco.toutiao.model.EntityType;
import com.marco.toutiao.model.HostHolder;
import com.marco.toutiao.model.News;
import com.marco.toutiao.model.ViewObject;
import com.marco.toutiao.service.LikeService;
import com.marco.toutiao.service.NewsService;
import com.marco.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    public List<ViewObject> getNews(int userId, int offset, int limit){
        List<News> newsList = newsService.getLatestNews(userId, offset, limit );
        int localUserId = (hostHolder.getUser() != null) ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for(News news : newsList){
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            // if you like it
            if(localUserId != 0){
                vo.set("like", likeService.getLikedStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            }else{
                vo.set("like", 0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/userId/{userId}/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(@PathVariable("userId") int userId, Model model){
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }

    @RequestMapping(path ={ "/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop){
        model.addAttribute("vos", getNews(0, 0, 10));
        //弹出登录框
        model.addAttribute("pop", pop);
        return "home";
    }
}
