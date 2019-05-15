package com.songdexv.springboot.dao.mapper.test;

import org.apache.ibatis.annotations.Param;

import com.songdexv.springboot.dao.mapper.CommonTestMapper;
import com.songdexv.springboot.dao.model.test.TUser;
import com.songdexv.springboot.mybatis.HashTableShardStrategy;
import com.songdexv.springboot.mybatis.TableShard;

@TableShard(tableName = "t_user", shardKey = "id", shardStrategy = HashTableShardStrategy.class)
public interface TUserMapper extends CommonTestMapper<TUser> {

    void updateUserEmail(@Param("id")long id, @Param("userEmail") String userEmail);
}