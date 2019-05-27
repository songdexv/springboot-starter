package com.songdexv.springboot.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import com.songdexv.springboot.mybatis.TableShardInterceptor;

/**
 * Created by songdexv on 2017/4/27.
 */
@Configuration
@EnableTransactionManagement
public class TestDataSourceConfig {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TestDataSourceConfig.class);
    @Autowired
    private TableShardInterceptor tableShardInterceptor;

    @Bean(name = "testDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.test")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "testSqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("com.songdexv.springboot.dao.model.test");
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mappers/test/*.xml"));
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageInterceptor.setProperties(properties);

        bean.setPlugins(new Interceptor[] {pageInterceptor, tableShardInterceptor});

        return bean.getObject();
    }

    @Bean(name = "testTransactionManager")
    @Primary
    public PlatformTransactionManager testTransactionManager(@Qualifier("testDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "testTransactionTemplate")
    public TransactionTemplate test2TransactionTemplate(@Qualifier("testTransactionManager")
                                                                PlatformTransactionManager transactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        return transactionTemplate;
    }

    @Bean(name = "testJdbcTemplate")
    public JdbcTemplate testJdbcTemplate(@Qualifier("testDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
