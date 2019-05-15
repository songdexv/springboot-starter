package com.songdexv.springboot.mybatis;

/**
 * @author songdexu
 * @date 2019/5/14
 */
public class HashTableShardStrategy implements TableShardStrategy {
    private static final int totalShards = 16;

    @Override
    public String tableShard(String tableName, Object shardValue) {
        int shardNum = ((String.valueOf(shardValue).hashCode() % 16) + 16) % 16;
        String suffix = "_";
        if (shardNum < 10) {
            suffix += "0" + shardNum;
        } else {
            suffix += shardNum;
        }
        return tableName + suffix;
    }
}
