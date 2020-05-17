package me.hjeong.demoinflearnrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.LinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
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
        add(linkTo(EventController.class).slash(content.getId()).withSelfRel());
    }
}
