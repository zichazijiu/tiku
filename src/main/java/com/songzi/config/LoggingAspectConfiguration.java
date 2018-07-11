package com.songzi.config;

import com.songzi.aop.logging.LogbackAspect;
import com.songzi.aop.logging.LoggingAspect;

import io.github.jhipster.config.JHipsterConstants;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }

    /**
     * 日志记录
     * @return
     */
    @Bean
    public LogbackAspect logbackAspect(){
        return new LogbackAspect();
    }
}
