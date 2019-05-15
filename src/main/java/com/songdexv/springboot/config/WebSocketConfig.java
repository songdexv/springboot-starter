
package com.songdexv.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @author songdexv
 *
 */
//@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    /*
     * 
     * @see
     * org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer#registerStompEndpoints(org.
     * springframework.web.socket.config.annotation.StompEndpointRegistry)
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/endpoint").withSockJS();
        registry.addEndpoint("/endpointChat").withSockJS();
    }

    /*
     * 
     * @see
     * org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer#configureMessageBroker(
     * org.springframework.messaging.simp.config.MessageBrokerRegistry)
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");
    }

}
