package com.songdexv.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by songdexv on 2017/8/3.
 */
@ConfigurationProperties(prefix = "zookeeper", ignoreInvalidFields = true)
public class ZookeeperServerProperties {
    private String connectString;
    private String serverRoot;
    private String supergwServerListPath;
    private String certmngServerListPath;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public String getServerRoot() {
        return serverRoot;
    }

    public void setServerRoot(String serverRoot) {
        this.serverRoot = serverRoot;
    }

    public String getSupergwServerListPath() {
        return supergwServerListPath;
    }

    public void setSupergwServerListPath(String supergwServerListPath) {
        this.supergwServerListPath = supergwServerListPath;
    }

    public String getCertmngServerListPath() {
        return certmngServerListPath;
    }

    public void setCertmngServerListPath(String certmngServerListPath) {
        this.certmngServerListPath = certmngServerListPath;
    }
}
