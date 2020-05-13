package me.hjeong._11_aop;

import org.springframework.stereotype.Component;

// real subject
@Component
public class SimpleEventService implements EventService {
    @Override
    public void createEvent() throws InterruptedException {
        Thread.sleep(1000l);
        System.out.println("create event");
    }

    @Override
    public void publishEvent() throws InterruptedException {
        Thread.sleep(3000l);
        System.out.println("publish event");
    }

    @Override
    public void deleteEvent() throws InterruptedException {
        Thread.sleep(1000l);
        System.out.println("delete event");
    }
}
