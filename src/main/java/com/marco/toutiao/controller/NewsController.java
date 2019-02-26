package com.marco.toutiao.controller;

import com.marco.toutiao.model.*;
import com.marco.toutiao.service.CommentService;
import com.marco.toutiao.service.LikeService;
import com.marco.toutiao.service.NewsService;
import com.marco.toutiao.service.UserService;
import com.marco.toutiao.util.ToutiaoUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Controller
public class NewsController {

    private static Log logger = LogFactory.getLog(NewsController.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;




    @ResponseBody
    @RequestMapping(path = {"/uploadImg"}, method = {RequestMethod.POST})
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try{
            String fileUrl = newsService.saveImage(file);
            if(fileUrl != null){
                return ToutiaoUtil.getJSONString(0, fileUrl);

            }else{
                return ToutiaoUtil.getJSONString(1, "上传失败");
            }
        }catch (Exception e){
            logger.error("上传文件失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImg(@RequestParam("name") String imgName, HttpServletResponse response){
        response.setContentType("image/jpeg");
        try {
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMG_DIR + imgName)), response.getOutputStream());

        }catch (Exception e){
            logger.error("获取图片异常" + e.getMessage());
        }
    }

    @RequestMapping(path = {"/addNews"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam(value = "image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try{
            News news = new News();
            if(hostHolder.getUser() != null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                //匿名用户
                news.setUserId(3);
            }
            news.setLink(link);
            news.setTitle(title);
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setLikeCount(0);
            news.setCommentCount(0);
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0, "发布新闻成功");
        }catch (Exception e){
            logger.error("发布新闻异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布新闻异常");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/news/{newsId}", method = RequestMethod.GET)
    public String newsDetail(@PathVariable("newsId") int newsId){
        Map<String, Object> map = new HashMap<>();
        News news = newsService.getById(newsId);
        if(news != null){
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            // if you like it
            if(localUserId != 0){
                map.put("like", likeService.getLikedStatus(localUserId, EntityType.ENTITY_NEWS, newsId));
            }else{
                map.put("like", 0);
            }
            List<Comment> commentList = commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_NEWS);
            System.out.println(commentList.size());
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
            for(Comment comment : commentList){
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("commentPublisher", userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }
            map.put("comments", commentVOs);

            int userId = news.getUserId();
            User user = userService.getUser(userId);
            map.put("publisher", user);
            map.put("news", news);
        }
        return ToutiaoUtil.getJSONString(0, map);
    }

    @RequestMapping(path = "/addComment", method = RequestMethod.POST)
    public String addComments(@RequestParam("newsId") int newsId, @RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setEntityId(newsId);
            comment.setUserId(hostHolder.getUser().getId());
            commentService.addComment(comment);
            int count = commentService.getNum(newsId, EntityType.ENTITY_NEWS);
            newsService.updateCommentCount(newsId, count);
        }catch (Exception e){
            logger.error("添加评论失败" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

}
