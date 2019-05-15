
package com.songdexv.springboot.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.songdexv.springboot.dao.mapper.test.TUserMapper;
import com.songdexv.springboot.dao.model.test.TUser;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * @author songdexv
 */
@Service
@CacheConfig(cacheNames = "users")
public class UserService {
    @Autowired
    private TUserMapper userMapper;

    public List<TUser> selectAll() {
        return userMapper.selectAll();
    }

    public List<TUser> selectByPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        Example example = new Example(TUser.class);
        example.setOrderByClause("order by id asc");
        return userMapper.selectAll();
    }

    @Cacheable(key = "#root.method.name + 'I' + #userName")
    public TUser selectByUserName(String userName) {
        Example example = new Example(TUser.class);
        example.createCriteria().andEqualTo("userName", userName);
        return userMapper.selectByExample(example).get(0);
    }

    @Transactional(value = "testTransactionManager")
    @CachePut(key = "'selectByUserName' + 'I' + #userName")
    public int saveUser(String userName, String userEmail, String userMobile) {
        TUser user = new TUser();
        user.setUserName(userName);
        user.setUserEmail(userEmail);
        user.setUserMobile(userMobile);
        user.setGmtCreate(new Date());
        user.setGmtModify(new Date());
        return userMapper.insert(user);
    }

    @Transactional(value = "testTransactionManager")
    public int saveUser(TUser user) {
        return userMapper.insert(user);
    }
    @CacheEvict(key = "'selectByUserName' + 'I' + #userName")
    public int deleteUser(String userName) {
        Example example = new Example(TUser.class);
        example.createCriteria().andEqualTo("userName", userName);
        return userMapper.deleteByExample(example);
    }
}
