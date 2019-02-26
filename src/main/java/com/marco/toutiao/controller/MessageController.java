package com.marco.toutiao.controller;

import com.marco.toutiao.model.HostHolder;
import com.marco.toutiao.model.Message;
import com.marco.toutiao.model.User;
import com.marco.toutiao.model.ViewObject;
import com.marco.toutiao.service.MessageService;
import com.marco.toutiao.service.UserService;
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

import java.util.*;

@Controller
public class MessageController {
    private static Log logger= LogFactory.getLog(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @ResponseBody
    @RequestMapping(path="/addMessage", method=RequestMethod.POST)
    public String addMessage(@RequestParam("content") String content,
                            @RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId){
        try{
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            msg.setHasRead(0);
            msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :  String.format("%d_%d", toId, fromId));
            messageService.addMessage(msg);
            return ToutiaoUtil.getJSONString(0, "发信成功");
        }catch (Exception e){
            logger.error("发信失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发信失败");
        }

    }

    //two users
    @ResponseBody
    @RequestMapping(path = "/msg/detail", method = RequestMethod.GET)
    public String getConversationDetail(@Param("conversationId") String conversationId){
        try {
            Map<String, Object> map = new HashMap<>();
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messageVOs = new ArrayList<>();
            for(Message message : messageList){
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                //sender
                User user = userService.getUser(message.getFromId());

                vo.set("userName", user.getName());
                messageVOs.add(vo);
            }
            map.put("messages", messageVOs);
            return ToutiaoUtil.getJSONString(0, map);


        }catch (Exception e){
            logger.error("查看信箱错误" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "查看信箱错误");
        }
    }

    //all users
    @ResponseBody
    @RequestMapping(path = "/msg/list", method = RequestMethod.GET)
    public String getConversationList(){
        try {
            int userId = hostHolder.getUser().getId();
            Map<String, Object> map = new HashMap<>();
            List<Message> messageList = messageService.getConversationList(userId, 0, 10);
            List<ViewObject> messageVOs = new ArrayList<>();
            for(Message message : messageList){
                ViewObject vo = new ViewObject();
                int fromId = message.getFromId();
                int toId = message.getToId();
                int targetId = (fromId == userId) ? toId : fromId;
                User user = userService.getUser(targetId);
                vo.set("user", user);

                //id means total count in this situation
                vo.set("message", message);
                int unreadNum = messageService.getUnreadNum(userId, message.getConversationId());
                vo.set("unreadNum", unreadNum);
                messageVOs.add(vo);
            }
            map.put("messages", messageVOs);
            return ToutiaoUtil.getJSONString(0, map);


        }catch (Exception e){
            logger.error("查看信箱错误" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "查看信箱错误");
        }
    }



}
