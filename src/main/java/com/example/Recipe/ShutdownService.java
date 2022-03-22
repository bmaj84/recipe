package com.example.Recipe;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ShutdownService implements ApplicationContextAware {

    private ApplicationContext context;

    @Async
    public void shutDown() {
        try {
            Thread.sleep(5_000);
            ((ConfigurableApplicationContext) context).close();
        } catch (InterruptedException e) {
            //
        }
    }
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;

    }
}