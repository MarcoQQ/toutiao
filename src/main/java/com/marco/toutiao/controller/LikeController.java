package com.marco.toutiao.controller;

import com.marco.toutiao.Async.EventModel;
import com.marco.toutiao.Async.EventProducer;
import com.marco.toutiao.Async.EventType;
import com.marco.toutiao.model.EntityType;
import com.marco.toutiao.model.HostHolder;
import com.marco.toutiao.model.News;
import com.marco.toutiao.service.LikeService;
import com.marco.toutiao.service.NewsService;
import com.marco.toutiao.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    private static Log logger = LogFactory.getLog(LikeController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @ResponseBody
    @RequestMapping(path = "/like", method = RequestMethod.GET)
    public String like(@RequestParam("newsId") int newsId){
//        System.out.println(hostHolder.getUser());
        try {
            int userId = hostHolder.getUser().getId();
            long likeCnt = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
            News news = newsService.getById(newsId);
            newsService.updateLikeCount(newsId, (int) likeCnt);
            eventProducer.fireEvent(new EventModel(EventType.LIKE).
                    setActorId(hostHolder.getUser().getId()).setEntityType(EntityType.ENTITY_NEWS)
                    .setEntityId(newsId).setEntityOwnerId(news.getUserId()));

            return ToutiaoUtil.getJSONString(0, String.valueOf(likeCnt));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ToutiaoUtil.getJSONString(1, "赞错误");
        }
    }

    @ResponseBody
    @RequestMapping(path = "/dislike", method = RequestMethod.GET)
    public String dislike(@RequestParam("newsId") int newsId){
        try {
            int userId = hostHolder.getUser().getId();
            long likeCnt = likeService.disLike(userId, EntityType.ENTITY_NEWS, newsId);
            newsService.updateLikeCount(newsId, (int) likeCnt);
            return ToutiaoUtil.getJSONString(0, String.valueOf(likeCnt));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ToutiaoUtil.getJSONString(1, "踩错误");
        }
    }


}
