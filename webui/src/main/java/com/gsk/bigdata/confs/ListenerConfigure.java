package com.gsk.bigdata.confs;


import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 任务调度监听
 * @author gsk
 */
@Configuration
public class ListenerConfigure {

    private ServletListenerRegistrationBean<QuartzInitializerListener> lisner = null;

    @Bean
    public ServletListenerRegistrationBean<QuartzInitializerListener> serssionListenerBean(){
        if (null == lisner){
            lisner = new ServletListenerRegistrationBean<>(new QuartzInitializerListener());
        }
        return lisner;
    }
}
