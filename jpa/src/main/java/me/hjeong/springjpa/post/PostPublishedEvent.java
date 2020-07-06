package me.hjeong.springjpa.post;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostPublishedEvent extends ApplicationEvent {

    private Post post;

    public PostPublishedEvent(Object source) {
        super(source);
        this.post = (Post) source;
    }

}
