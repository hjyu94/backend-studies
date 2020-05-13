package me.hjeong._12_null_safety;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    public String createEvent(@NonNull String name) {
        return "hello " + name;
    }
}
