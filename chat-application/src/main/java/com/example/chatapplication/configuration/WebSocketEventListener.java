package com.example.chatapplication.configuration;

import com.example.chatapplication.chat.ChatMessage;
import com.example.chatapplication.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    //to notify that user disconnection we need this dependancy
    private final SimpMessageSendingOperations messageTemplate;
    @EventListener
    public void handleWebSocketDisconnectionListener(SessionDisconnectEvent event){

        //get the message from event to headerAccessor
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        //extract the username
        String username = (String)headerAccessor.getSessionAttributes().get("username");

        if(username != null){
            log.info("User Disconnected! {}", username);

            //build the chatMessage object
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();

            //now notify everyone in chat
            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
