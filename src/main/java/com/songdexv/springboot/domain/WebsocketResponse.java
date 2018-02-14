/**
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.songdexv.springboot.domain;

/**
 * @author songdexv
 *
 */
public class WebsocketResponse {
    private String responseMessage;

    public WebsocketResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }
}
