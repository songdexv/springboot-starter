package com.songdexv.springboot.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * Created by songdexv on 2017/4/28.
 */
@Configuration
@AutoConfigureAfter(TestDataSourceConfig.class)
public class TestMapperScannerConfig {
    @Bean(name = "testMapperScanner")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("testSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.baifubao.springboot.dao.mapper.test");
        return mapperScannerConfigurer;
    }
}
