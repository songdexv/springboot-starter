package com.songdexv.springboot.dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import com.songdexv.springboot.dao.model.test.TUser;

/**
 * @author songdexu
 * @date 2019/5/14
 */
@Component
public class UserDao extends SqlSessionDaoSupport {
    @Override
    @Resource(name="testSqlSessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public int insert(TUser user) {
       return getSqlSession().insert("com.songdexv.springboot.dao.mapper.test.TUserMapper.insert", user);
    }
}
