package com.songdexv.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by songdexv on 2017/4/27.
 */
@ConfigurationProperties(prefix = "spring.datasource.test", ignoreUnknownFields = true)
public class TestDataSourceProperties extends AbstractDataSourceProperties {

}
