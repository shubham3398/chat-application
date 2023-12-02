package com.example.chatapplication.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    //one method -> any user connect then inform other that new user join the chat
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor){
        //add the username to websocket session
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

    //second method -> to send the message
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage message){
        return message;
    }
}
