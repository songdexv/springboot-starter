/**
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.songdexv.springboot.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.songdexv.springboot.domain.WebsocketMessage;
import com.songdexv.springboot.domain.WebsocketResponse;

/**
 * @author songdexv
 *
 */
@Controller
public class WebSocketController {
    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public WebsocketResponse say(WebsocketMessage message) throws Exception {
        Thread.sleep(3000);
        return new WebsocketResponse("Welcome, " + message.getName() + "!");
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;// 1

    @MessageMapping("/chat")
    public void handleChat(Principal principal, String msg) { // 2
        if (principal.getName().equals("wyf")) {// 3
            messagingTemplate.convertAndSendToUser("wisely",
                    "/queue/notifications", principal.getName() + "-send:"
                            + msg);
        } else {
            messagingTemplate.convertAndSendToUser("wyf",
                    "/queue/notifications", principal.getName() + "-send:"
                            + msg);
        }
    }
}
