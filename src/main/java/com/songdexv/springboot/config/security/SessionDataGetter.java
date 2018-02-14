package com.songdexv.springboot.config.security;

/**
 * Created by songdexv on 2017/8/7.
 */
public interface SessionDataGetter {
    SessionDataResponse getSessionData(SessionDataRequest request);
}
