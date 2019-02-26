package com.marco.toutiao.service;

import com.marco.toutiao.dao.CommentDAO;
import com.marco.toutiao.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;

    public List<Comment> getCommentByEntity(int entityId, int entityType){
        return commentDAO.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    public int getNum(int entityId, int entityType){
        return commentDAO.getNum(entityId, entityType);
    }

    public void delete(int entityId, int entityType, int status){
        commentDAO.updateStatus(entityId, entityType, status);
    }
}
