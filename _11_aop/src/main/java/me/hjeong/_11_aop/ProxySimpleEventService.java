package me.hjeong._11_aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class ProxySimpleEventService implements EventService{
/*
    @Autowired
    EventService eventService;
    // 원래 인터페이스 주입받을 땐 인터페이스 타입으로 주입 받지만
    // 여러 EventService 구현체가 있으므로 명시적으로 적어주는게 좋다

    @Autowired
    EventService simpleEventService;
    // 필드명이 빈의 이름과 같을 경우에는 자동으로 SimpleEventService 타입으로 빈을 주입한다.
*/
    @Autowired
    SimpleEventService simpleEventService;

    @Override
    public void createEvent() throws InterruptedException {
        simpleEventService.createEvent();
    }

    @Override
    public void publishEvent() throws InterruptedException {
        simpleEventService.publishEvent();
    }

    @Override
    public void deleteEvent() throws InterruptedException {
        simpleEventService.deleteEvent();
    }
}
