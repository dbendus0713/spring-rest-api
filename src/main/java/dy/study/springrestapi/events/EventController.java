package dy.study.springrestapi.events;


import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.hateoas.MediaTypes;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

  private final EventRepository eventRepository;
  private final ModelMapper modelMapper;

  public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
    this.eventRepository = eventRepository;
    this.modelMapper = modelMapper;
  }

  @PostMapping
  public ResponseEntity createEntity(@RequestBody EventDto eventDto) {
    Event event = modelMapper.map(eventDto, Event.class);
    Event newEvent = this.eventRepository.save(event);
    URI createURi = linkTo(EventController.class).slash(newEvent.getId()).toUri();
    return ResponseEntity.created(createURi).body(newEvent);
  }
}
