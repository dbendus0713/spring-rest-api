package dy.study.springrestapi.events;

import dy.study.springrestapi.common.ErrorsEntityModel;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

  @GetMapping
  public ResponseEntity selectEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {

    Page<Event> page = this.eventRepository.findAll(pageable);
    var pagedResources = assembler.toModel(page, e -> {
      return new EventResource(e);
    });
    pagedResources.add(Link.of("/docs/index.html#resources-events-list", LinkRelation.of("profile")));

//    var pagedResources = assembler.toModel(page, new RepresentationModelAssembler<Event, RepresentationModel<?>>() {
//      @Override
//      public RepresentationModel<?> toModel(Event entity) {
//        return new EventResource(entity);
//      }
//    });

    return ResponseEntity.ok(pagedResources);
  }

  @PostMapping
  public ResponseEntity createEntity(@RequestBody @Valid EventDto eventDto, Errors errors) {
    if (errors.hasErrors()) {
//      return ResponseEntity.badRequest().body(errors);
      return badRequest(errors);
    }
    eventValidator.validate(eventDto, errors);
    if (errors.hasErrors()) {
//      return ResponseEntity.badRequest().body(errors);
      return badRequest(errors);
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

  private ResponseEntity badRequest(Errors errors) {
      return ResponseEntity.badRequest().body(ErrorsEntityModel.modelOf(errors));
  }
}
