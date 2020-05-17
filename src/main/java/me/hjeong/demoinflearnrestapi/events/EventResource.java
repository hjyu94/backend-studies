package me.hjeong.demoinflearnrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
//
//public class EventResource extends RepresentationModel {
//    @JsonUnwrapped
//    private Event event;
//
//    public EventResource(Event event) {
//        this.event = event;
//    }
//
//    public Event getEvent() {
//        return event;
//    }
//}

public class EventResource extends EntityModel<Event> {
    public EventResource(Event content, Link... links) {
        super(content, links);
    }
}
