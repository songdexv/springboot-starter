package com.songdexv;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.songdexv.springboot.dao.model.test.TUser;
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
    public void testAddOrder() {
        int result = orderService.saveOrder(3, "test order 3", 1000);
        Assert.assertEquals(1, result);
    }
}
