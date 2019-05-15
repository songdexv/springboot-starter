package com.songdexv.springboot.dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import com.songdexv.springboot.dao.model.test2.TOrder;

/**
 * @author songdexu
 * @date 2019/5/14
 */
@Component
public class OrderDao extends SqlSessionDaoSupport {
    @Override
    @Resource(name="test2SqlSessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public int insert(TOrder order) {
        return getSqlSession().insert("com.songdexv.springboot.dao.mapper.test2.TOrderMapper.insert", order);
    }
}
