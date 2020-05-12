package me.hjeong._6_application_event_publisher;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 이벤트 핸들러는 반드시 빈이어야 한다

// 스프링 4.2 이전에서는
// ApplicationListener를 구현해야 한다.
@Component
class BeforeEventHandler implements ApplicationListener<BeforeEvent> {
    @Override
    public void onApplicationEvent(BeforeEvent event) {
        System.out.println("받은 이벤트의 데이터: " + event.getData());
    }
}

// 스프링 4.2 이후는
// ApplicationListener를 구현할 필요 없다
// @EventListener를 메소드 핸들러 위에 붙여준다.
@Component
public class MyEventHandler {
    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void MyHandlerFunc(Event event) {
        System.out.println("My Event Handler");
        System.out.println("받은 이벤트의 데이터: " + event.getData());
    }
}
