package me.hjeong.demoinflearnrestapi.events;

import me.hjeong.demoinflearnrestapi.accounts.AccountAdapter;
import me.hjeong.demoinflearnrestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Controller
@RequestMapping(
        value = "/api/events"
        , produces = MediaTypes.HAL_JSON_VALUE
)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);

        LinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();

        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event")); // put
        eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable
                                    , PagedResourcesAssembler<Event> assembler
                                    , @AuthenticationPrincipal User user
    ) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedModel<EventResource> pagedEntityModel = assembler.toModel(page, e -> new EventResource(e));
        pagedEntityModel.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));

        if(user!=null)
        {
            pagedEntityModel.add(linkTo(EventController.class).withRel("create-event"));
        }
        return ResponseEntity.ok(pagedEntityModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id
                                , @RequestBody @Valid EventDto eventDto
                                , Errors errors
                            )
    {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        this.eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        Event existingEvent = optionalEvent.get();
        this.modelMapper.map(eventDto, existingEvent);
        Event savedEvent = eventRepository.save(existingEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(new Link("docs/index.html#resources-events-update").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }

    private ResponseEntity<ErrorsResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
