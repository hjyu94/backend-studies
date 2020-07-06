package me.hjeong.springjpa.post;

import org.springframework.context.ApplicationListener;

public class PostListener implements ApplicationListener<PostPublishedEvent> {

    @Override
    public void onApplicationEvent(PostPublishedEvent postPublishedEvent) {
        System.out.println(postPublishedEvent.getPost().getTitle() + " is published()");
    }

}