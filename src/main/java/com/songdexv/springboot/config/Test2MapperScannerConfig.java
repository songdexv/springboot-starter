package com.songdexv.springboot.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * Created by songdexv on 2017/4/28.
 */
@Configuration
@AutoConfigureAfter(Test2DataSourceConfig.class)
public class Test2MapperScannerConfig {
    @Bean(name = "test2MapperScanner")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("test2SqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.songdexv.springboot.dao.mapper.test2");
        return mapperScannerConfigurer;
    }
}
