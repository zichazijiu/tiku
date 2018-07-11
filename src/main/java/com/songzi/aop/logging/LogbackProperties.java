package com.songzi.aop.logging;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "logbackup", ignoreUnknownFields = false)
public class LogbackProperties {

    private Map<String,Map<String,String>> needlogbakmethod;

    public Map<String, Map<String, String>> getNeedlogbakmethod() {
        return needlogbakmethod;
    }

    public void setNeedlogbakmethod(Map<String, Map<String, String>> needlogbakmethod) {
        this.needlogbakmethod = needlogbakmethod;
    }
}
