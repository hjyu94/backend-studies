package me.hjeong._6_application_event_publisher;

import org.springframework.context.ApplicationEvent;

// 스프링 4.2 이전 버전에서의 경우
// ApplicationEvent 를 상속받아야 한다
class BeforeEvent extends ApplicationEvent {
    private int data;

    public BeforeEvent(Object source) {
        super(source);
        System.out.println("Event 객체 생성");
    }

    public BeforeEvent(Object source, int data) {
        super(source);
        this.data = data;
        System.out.println("Event 객체 생성");
    }

    public int getData() {
        return data;
    }
}

// 4.2 이후의 경우
// ApplicationEvent 를 상속받을 필요가 없다.
public class Event{
    private int data;

    public Event(Object source, int data) {
        this.data = data;
        System.out.println("Event 객체 생성");
    }

    public int getData() {
        return data;
    }
}
