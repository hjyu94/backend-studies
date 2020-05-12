package me.hjeong._6_application_event_publisher;

import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AnotherEventHandler {
    @EventListener
    @Order(Ordered.LOWEST_PRECEDENCE)
    @Async
    public void EventHandlerMethod(Event event)
    {
        System.out.println("Another Event Handler");
        System.out.println("받은 이벤트의 데이터: " + event.getData());
    }
    // Event 가 생성되면
    // @EventListener 가 붙은 메소드중 매개변수로 Event 타입을 받는 메소드 핸들러가
    // 자동 실행된다.
}

// 같은 이벤트 타입을 처리하는 핸들러가 여러개 있을 경우
// 우선순위를 줄 수 있다.
// Ordered.HIGHEST_PRECEDENCE // -2147483648 낮을 수록 우선순위가 높다
// Ordered.LOWEST_PRECEDENCE
