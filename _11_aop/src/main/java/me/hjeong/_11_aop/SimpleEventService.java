package me.hjeong._11_aop;

import org.springframework.stereotype.Component;

// real subject
@Component
public class SimpleEventService implements EventService {
    @Override
    public void createEvent() throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(1000l);
        System.out.println("create event");
        System.out.println(System.currentTimeMillis() - begin + "ms passed");
    }

    @Override
    public void publishEvent() throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(3000l);
        System.out.println("publish event");
        System.out.println(System.currentTimeMillis() - begin + "ms passed");
    }

    @Override
    public void deleteEvent() throws InterruptedException {
        Thread.sleep(1000l);
        System.out.println("delete event");
    }
}
