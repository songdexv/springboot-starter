package com.songdexv.springboot.mybatis;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * @author songdexu
 * @date 2019/5/14
 */
@Intercepts(@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
))
public class TableShardInterceptor implements Interceptor {
    private static final ReflectorFactory reflectorFactory = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, reflectorFactory);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        //原始sql
        String originSql = (String) metaObject.getValue("delegate.boundSql.sql");
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String id = mappedStatement.getId();
        id = id.substring(0, id.lastIndexOf('.'));
        Class clazz = Class.forName(id);
        TableShard tableShard = (TableShard) clazz.getAnnotation(TableShard.class);
        if (tableShard != null) {
            String tableName = tableShard.tableName();
            String shardKey = tableShard.shardKey();
            Object shardValue = getParameterFromMappedStatement(mappedStatement, boundSql).get(shardKey);
            Class<? extends TableShardStrategy> shardStrategyClazz = tableShard.shardStrategy();
            TableShardStrategy tableShardStrategy = shardStrategyClazz.newInstance();
            String newTableName = tableShardStrategy.tableShard(tableName, shardValue);
            metaObject.setValue("delegate.boundSql.sql", originSql.replaceAll(tableName, newTableName));
        }
        return invocation.proceed();
    }

    private Map<String, Object> getParameterFromMappedStatement(MappedStatement ms, BoundSql boundSql) {
        Map<String, Object> paramMap;

        Object parameterObject = boundSql.getParameterObject();
        if (parameterObject == null) {
            paramMap = new HashMap<String, Object>();
        } else if (parameterObject instanceof Map) {
            paramMap = new HashMap<String, Object>();
            paramMap.putAll((Map) parameterObject);
        } else {
            paramMap = new HashMap<String, Object>();
            boolean hasTypeHandler = ms.getConfiguration().getTypeHandlerRegistry()
                    .hasTypeHandler(parameterObject.getClass());

            MetaObject metaObject = SystemMetaObject.forObject(parameterObject);
            if (!hasTypeHandler) {
                for (String name : metaObject.getGetterNames()) {
                    paramMap.put(name, metaObject.getValue(name));
                }
            }
            //下面这段方法，主要解决一个常见类型的参数时的问题
            if (boundSql.getParameterMappings() != null && boundSql.getParameterMappings().size() > 0) {
                for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                    String name = parameterMapping.getProperty();
                    if (paramMap.get(name) == null) {
                        if (hasTypeHandler || parameterMapping.getJavaType().equals(parameterObject.getClass())) {
                            paramMap.put(name, parameterObject);
                            break;
                        }
                    }
                }
            }
        }
        return paramMap;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return null;
    }

    @Override
    public void setProperties(Properties properties) {
        //do nothing
    }
}
