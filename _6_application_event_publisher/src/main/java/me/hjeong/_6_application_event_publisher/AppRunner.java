package me.hjeong._6_application_event_publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    ApplicationEventPublisher publisher;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        publisher.publishEvent(new Event(this, 100));
    }
}
