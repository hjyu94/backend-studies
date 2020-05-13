package me.hjeong._11_aop;

// subject
public interface EventService {
    void createEvent() throws InterruptedException;
    void publishEvent() throws InterruptedException;
    void deleteEvent() throws InterruptedException;
}
