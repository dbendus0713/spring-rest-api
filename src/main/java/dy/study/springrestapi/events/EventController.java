package dy.study.springrestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.hateoas.MediaTypes;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
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
  public ResponseEntity createEntity(@RequestBody @Valid EventDto eventDto, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(errors);
    }
    eventValidator.validate(eventDto, errors);
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(errors);
    }

    Event event = modelMapper.map(eventDto, Event.class);
    event.update();
    Event newEvent = this.eventRepository.save(event);
    WebMvcLinkBuilder selfLink = linkTo(EventController.class).slash(newEvent.getId());
    URI createURi = selfLink.toUri();
    EventEntityModel eventResource = new EventEntityModel(newEvent);
//    eventResource.add(linkTo(EventController.class).withRel("query-events"));
//    eventResource.add(selfLink.withRel("update-event"));
//    eventResource.add(selfLink.withSelfRel());
    return ResponseEntity.created(createURi).body(eventResource);
  }
}
