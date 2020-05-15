package me.hjeong._1_introduction_of_spring_mvc;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class EventService {
    public List<Event> getEvents() {
        Event event1 = Event.builder()
                .name("이벤트1")
                .limitOfEnrollment(10)
                .startDateTime(LocalDateTime.of(2020, 10, 26, 0, 0))
                .endDateTime(LocalDateTime.of(2021, 10, 26, 0, 0))
                .build();

        Event event2 = Event.builder()
                .name("이벤트2")
                .limitOfEnrollment(20)
                .startDateTime(LocalDateTime.of(2020, 10, 26, 0, 0))
                .endDateTime(LocalDateTime.of(2021, 10, 26, 0, 0))
                .build();

        return Arrays.asList(event1, event2);
    }
}
