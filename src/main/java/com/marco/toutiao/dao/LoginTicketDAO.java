package com.marco.toutiao.dao;

import ch.qos.logback.classic.db.names.TableName;
import com.marco.toutiao.model.LoginTicket;
import com.marco.toutiao.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    //登录
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{userId}, #{ticket}, #{expired}, #{status})"})
    int addLoginTicket(LoginTicket loginTicket);

    //选择
    @Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME, "where ticket = #{ticket}"})
    LoginTicket selectByTicket(String ticket);

    //登出
    @Update({"update ", TABLE_NAME, "set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
