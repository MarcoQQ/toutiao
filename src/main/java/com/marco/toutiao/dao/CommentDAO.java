package com.marco.toutiao.dao;

import ch.qos.logback.classic.db.names.TableName;
import com.marco.toutiao.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"select", INSERT_FIELDS, "from", TABLE_NAME, "where entity_id = #{entityId} and entity_type = #{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(*) from", TABLE_NAME, "where entity_id = #{entityId} and entity_type = #{entityType}"})
    int getNum(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update", TABLE_NAME, "set status = #{status} where entityId = #{entityId} and entityType = {entityType}"})
    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);

}
