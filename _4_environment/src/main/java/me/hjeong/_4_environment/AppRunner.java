package me.hjeong._4_environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Autowired
    ClassForTest classForTest;

    @Autowired(required = false)
    ClassForRelease classForRelease;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}