package me.hjeong._9_data_binding.ConverterFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    @GetMapping("/event/{event}")
    // "/event/1", "/event/2", ...
    // "1", "2", ... -> Event 객체 변환
    public String getEvent(@PathVariable Event event)
    {
        System.out.println(event);
        return event.getId().toString();
    }
}
