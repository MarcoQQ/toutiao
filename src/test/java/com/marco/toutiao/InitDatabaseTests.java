package com.marco.toutiao;


import com.marco.toutiao.dao.CommentDAO;
import com.marco.toutiao.dao.LoginTicketDAO;
import com.marco.toutiao.dao.NewsDAO;
import com.marco.toutiao.dao.UserDAO;
import com.marco.toutiao.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    CommentDAO commentDAO;

    @Test
    public void contextLoads() {
        Random random = new Random();
        for(int i = 0; i < 10; i++){
            User user = new User();
            user.setHeadUrl(String.format("http://images.toutiao.com/head/%dt.png",random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            //每个人隔5个小时
            date.setTime(date.getTime() + 1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.toutiao.com/head/%dm.png", random.nextInt(1000)));
            news.setUserId(i+1);
            news.setLikeCount(i+1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.toutiao.com/%d.html", i));
            newsDAO.addNews(news);

            for(int j = 0; j < 3; j++){
                Comment comment = new Comment();
                comment.setUserId(i+1);
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                comment.setStatus(0);
                comment.setCreatedDate(new Date());
                comment.setContent("Comment:" + String.valueOf(j));
                commentDAO.addComment(comment);
            }


            user.setPassword("newPW");
            userDAO.updatePassword(user);

//            LoginTicket loginTicket = new LoginTicket();
//            loginTicket.setExpired(date);
//            loginTicket.setUserId(i+1);
//            loginTicket.setStatus(0);
//            loginTicket.setTicket(String.format("TICKET%d", i+1));
//            loginTicketDAO.addLoginTicket(loginTicket);
        }
//        loginTicketDAO.updateStatus("TICKET2", 1);
//        Assert.assertEquals(loginTicketDAO.selectByTicket("TICKET2").getStatus(), 1);
        Assert.assertEquals("newPW", userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));
        Assert.assertEquals(commentDAO.selectByEntity(1, EntityType.ENTITY_NEWS).get(0).getUserId(), 1);
        Assert.assertEquals(commentDAO.getNum(1, EntityType.ENTITY_NEWS), 3);
    }
}
