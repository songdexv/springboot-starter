package com.songdexv.springboot.mybatis;

/**
 * @author songdexu
 * @date 2019/5/14
 */
public interface TableShardStrategy {
    String tableShard(String tableName, Object shardValue);
}
