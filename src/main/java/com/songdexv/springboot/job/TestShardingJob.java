package com.songdexv.springboot.job;

import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import lombok.extern.slf4j.Slf4j;

/**
 * @author songdexu
 * @date 2019/5/25
 */
@Component
@Slf4j
public class TestShardingJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        int shardingItem = shardingContext.getShardingItem();
        String shardingParameter = shardingContext.getShardingParameter();
        log.info("item {}, parameter {}", shardingItem, shardingParameter);
    }
}
