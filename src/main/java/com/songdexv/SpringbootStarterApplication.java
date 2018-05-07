package com.songdexv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableAutoConfiguration(exclude = {MongoDataAutoConfiguration.class, MongoRepositoriesAutoConfiguration.class})
public class SpringbootStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootStarterApplication.class, args);
    }
}
