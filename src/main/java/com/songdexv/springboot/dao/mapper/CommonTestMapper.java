package com.songdexv.springboot.dao.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by songdexv on 2017/4/28.
 */
public interface CommonTestMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
