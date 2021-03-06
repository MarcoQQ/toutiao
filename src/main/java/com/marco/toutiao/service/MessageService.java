package com.marco.toutiao.service;

import com.marco.toutiao.dao.MessageDAO;
import com.marco.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getUnreadNum(int userId, String conversationId){
        return messageDAO.getUnreadNum(userId, conversationId);
    }
}
