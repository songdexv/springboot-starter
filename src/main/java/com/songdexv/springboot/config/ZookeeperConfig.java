package com.songdexv.springboot.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by songdexv on 2017/8/3.
 */
@Configuration
@EnableConfigurationProperties(ZookeeperServerProperties.class)
public class ZookeeperConfig implements CommandLineRunner {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);
    @Autowired
    private ZookeeperServerProperties zookeeperServerProperties;

    @Override
    public void run(String... args) throws Exception {
//        logger.debug("zookeeper connectstring: " + zookeeperServerProperties.getConnectString());
//        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperServerProperties.getConnectString(),
//                retryPolicy);
//        client.start();
//
//        String supergwServerListPath = zookeeperServerProperties.getSupergwServerListPath();
//        String certmngServerListPath = zookeeperServerProperties.getCertmngServerListPath();
//
//        String supergwServerList = new String(client.getData().forPath(supergwServerListPath));
//        String certmngServerList = new String(client.getData().forPath(certmngServerListPath));
//
//        logger.info("supergwServerList: " + supergwServerList);
//        logger.info("certmngServerList: " + certmngServerList);
//
//        final NodeCache supergwNodeCache = new NodeCache(client, supergwServerListPath, false);
//        supergwNodeCache.start(true);
//        supergwNodeCache.getListenable().addListener(new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                logger.info("supergw server list changed, new list: " + new String(
//                        client.getData().forPath(supergwServerListPath)));
//            }
//        });
//
//        final NodeCache certmngNodeCache = new NodeCache(client, certmngServerListPath, false);
//        certmngNodeCache.start(true);
//        certmngNodeCache.getListenable().addListener(new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                logger.info("certmng server list changed, new list: " + new String(client.getData().forPath
//                        (certmngServerListPath)));
//            }
//        });
    }
}
