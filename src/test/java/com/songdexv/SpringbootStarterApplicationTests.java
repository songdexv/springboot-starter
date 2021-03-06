package com.songdexv;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.songdexv.springboot.dao.mapper.test2.TOrderMapper;
import com.songdexv.springboot.dao.model.test.TUser;
import com.songdexv.springboot.dao.model.test2.TOrder;
//import com.songdexv.springboot.service.AsyncTaskService;
import com.songdexv.springboot.service.CompositeService;
import com.songdexv.springboot.service.OrderService;
import com.songdexv.springboot.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringbootStarterApplication.class})
@WebAppConfiguration
public class SpringbootStarterApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(SpringbootStarterApplicationTests.class);
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private CompositeService compositeService;

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private AsyncTaskService asyncTaskService;

    @Test
    public void selectAllUser() {
        List<TUser> userList = userService.selectAll();
        userList.stream().forEach(user -> {
            logger.info(ToStringBuilder.reflectionToString(user, ToStringStyle.SHORT_PREFIX_STYLE));
        });
    }

    @Test
    public void selectUserByPage() {
        List<TUser> userList = userService.selectByPage(2, 5);
        userList.stream().forEach(user -> {
            logger.info(ToStringBuilder.reflectionToString(user, ToStringStyle.SHORT_PREFIX_STYLE));
        });
    }

    @Test
    public void testAddUser() {
        int result = userService.saveUser("test16", "test16@test.com", "13612345682");
        Assert.assertEquals(1, result);
    }

    @Test
    public void testGetUserByUserName() {
        TUser user = userService.selectByUserName("test_1");
        logger.info("----------------------load again-----------------");
        TUser user2 = userService.selectByUserName("test_1");
        logger.info("-------user2: " + ToStringBuilder.reflectionToString(user2, ToStringStyle.SHORT_PREFIX_STYLE));
    }

    @Test
//    @Transactional
//    @Rollback
    public void testAddOrder() {
        int result = orderService.saveOrder(3, "test order 4", 1000);
        Assert.assertEquals(1, result);
    }

//    @Test
//    public void testRedis() {
//        stringRedisTemplate.opsForValue().set("aaa", "11111", 10, TimeUnit.SECONDS);
//        Assert.assertEquals(stringRedisTemplate.opsForValue().get("aaa"), "11111");
//        System.out.println("aaa 对应值：" + stringRedisTemplate.opsForValue().get("aaa"));
//    }
//
//    @Test
//    public void asynServiceTest() throws Exception {
//        asyncTaskService.doTaskOne();
//        asyncTaskService.doTaskTwo();
//        asyncTaskService.doTaskThree();
//        Thread.currentThread().join();
//    }

    @Test
    public void testCompositeSave() {
        Date now = new Date();
        TUser user = new TUser();
        user.setUserName("test");
        user.setUserMobile("123456789");
        user.setUserEmail("test@test.com");
        user.setGmtModify(now);
        user.setGmtCreate(now);

        TOrder order = new TOrder();
        order.setUserId(1l);
        order.setOrderName("test");
        order.setOrderAmount(100l);
        order.setGmtCreate(now);
        order.setGmtModify(now);

        compositeService.insert(user, order);

//        compositeService.insert2(user, order);
    }
}
