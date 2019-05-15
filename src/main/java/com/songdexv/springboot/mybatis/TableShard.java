package com.songdexv.springboot.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author songdexu
 * @date 2019/5/14
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableShard {
    String tableName();

    Class<? extends TableShardStrategy> shardStrategy();

    String shardKey();
}
