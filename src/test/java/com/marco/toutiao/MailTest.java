package com.marco.toutiao;

import com.marco.toutiao.util.MailSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToutiaoApplication.class)
public class MailTest {

    @Autowired
    MailSender mailSender;

    @Test
    public void test(){
        mailSender.sendEmail("1007284166@qq.com", "welcome", "hello");

    }
}
