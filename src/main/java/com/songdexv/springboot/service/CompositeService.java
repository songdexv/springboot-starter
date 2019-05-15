package com.songdexv.springboot.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.songdexv.springboot.dao.OrderDao;
import com.songdexv.springboot.dao.UserDao;
import com.songdexv.springboot.dao.model.test.TUser;
import com.songdexv.springboot.dao.model.test2.TOrder;

/**
 * @author songdexu
 * @date 2019/5/14
 */
@Component
public class CompositeService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Resource(name = "testTransactionTemplate")
    private TransactionTemplate testTransactionTemplate;
    @Resource(name = "test2TransactionTemplate")
    private TransactionTemplate test2TransactionTemplate;
    @Resource(name = "testJdbcTemplate")
    private JdbcTemplate testJdbcTemplate;

    public void insert(TUser user, TOrder order) {
        testTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                userDao.insert(user);

                test2TransactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        orderDao.insert(order);
                    }
                });
                test2TransactionTemplate.getTransactionManager().rollback(status);
            }
        });
    }

    public void insert2(TUser user, TOrder order) {

    }
}
