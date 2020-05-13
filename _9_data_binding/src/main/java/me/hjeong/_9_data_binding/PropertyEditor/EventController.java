package me.hjeong._9_data_binding.PropertyEditor;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    @InitBinder
    public void init(WebDataBinder webDataBinder)
    {
        webDataBinder.registerCustomEditor(Event.class, new EventEditor());
        // Event class 를 다른 데이터 타입으로 변환하는 경우 EventEditor 를 사용하자
    }

    @GetMapping("/event/{event}")
    // "/event/1", "/event/2", ...
    // "1", "2", ... -> Event 객체 변환
    public String getEvent(@PathVariable Event event)
    {
        System.out.println(event);
        return event.getId().toString();
    }
}
