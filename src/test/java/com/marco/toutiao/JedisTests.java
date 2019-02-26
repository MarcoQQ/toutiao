package com.marco.toutiao;

import com.marco.toutiao.model.User;
import com.marco.toutiao.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToutiaoApplication.class)
public class JedisTests {
    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void testObj() {
//        User user = new User();
//        user.setSalt("salt");
//        user.setName("haha");
//        user.setHeadUrl("headurl");
//        user.setPassword("123456");
//        jedisAdapter.setObject("user12", user);
//        System.out.println(ToStringBuilder.reflectionToString(jedisAdapter.getObject("user12", User.class)));


    }
}
