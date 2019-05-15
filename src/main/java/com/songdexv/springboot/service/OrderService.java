package com.songdexv.springboot.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.songdexv.springboot.dao.mapper.test2.TOrderMapper;
import com.songdexv.springboot.dao.model.test2.TOrder;

/**
 * Created by songdexv on 2017/4/27.
 */
@Service
@CacheConfig(cacheNames = "orders")
public class OrderService {
    @Autowired
    private TOrderMapper orderMapper;

    @Transactional(value = "test2TransactionManager")
    public int saveOrder(long userId, String orderName, long orderAmount) {
        TOrder order = new TOrder();
        order.setUserId(userId);
        order.setOrderName(orderName);
        order.setOrderAmount(orderAmount);
        order.setGmtCreate(new Date());
        order.setGmtModify(new Date());
        int result = orderMapper.insert(order);
        if (result == 0) {
            throw new RuntimeException("runtime exception for rollback");
        }

        return result;
    }

    @Transactional(value = "test2TransactionManager")
    public int saveOrder(TOrder order) {
        return orderMapper.insert(order);
    }

    public List<TOrder> getOrderByPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return orderMapper.selectAll();
    }
}
