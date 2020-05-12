package me.hjeong._6_application_event_publisher;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BasicEventHandler {
    @EventListener
    public void handle1(ContextRefreshedEvent evnet)
    {
        System.out.println(Thread.currentThread().toString());
    }

    @EventListener
    public void handle2(ContextClosedEvent evnet)
    {
        System.out.println(Thread.currentThread().toString());
    }

    @EventListener
    public void handle3(ContextStoppedEvent evnet)
    {
        System.out.println(Thread.currentThread().toString());
    }

    @EventListener
    public void handle4(ContextClosedEvent evnet)
    {
        System.out.println(Thread.currentThread().toString());
    }
}
