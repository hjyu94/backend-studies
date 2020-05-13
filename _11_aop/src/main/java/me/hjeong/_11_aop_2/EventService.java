package me.hjeong._11_aop_2;

// subject
public interface EventService {
    void createEvent() throws InterruptedException;
    void publishEvent() throws InterruptedException;
    void deleteEvent() throws InterruptedException;
}
