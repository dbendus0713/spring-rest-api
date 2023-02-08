package dy.study.springrestapi.events;

import dy.study.springrestapi.common.ApiType;
import dy.study.springrestapi.common.ErrorsEntityModel;
import org.apache.coyote.Response;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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

  @GetMapping("/{id}")
  public ResponseEntity selectEvent(@PathVariable Integer id) {
    Optional<Event> optionalEvent = this.eventRepository.findById(id);
    if (optionalEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Event event = optionalEvent.get();
    WebMvcLinkBuilder selfLink = linkTo(EventController.class).slash(event.getId());
    URI createURi = selfLink.toUri();
    EventEntityModel eventResource = new EventEntityModel(event, ApiType.FIND);
    return ResponseEntity.ok().body(eventResource);
  }

  @GetMapping
  public ResponseEntity selectEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {

    Page<Event> page = this.eventRepository.findAll(pageable);
    var pagedResources = assembler.toModel(page, e -> {
      return new EventResource(e);
    });
    pagedResources.add(Link.of("/docs/index.html#resources-events-list", LinkRelation.of("profile")));
    //spread
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
    EventEntityModel eventResource = new EventEntityModel(newEvent, ApiType.CREATE);
//    eventResource.add(linkTo(EventController.class).withRel("query-events"));
//    eventResource.add(selfLink.withRel("update-event"));
//    eventResource.add(selfLink.withSelfRel());
    return ResponseEntity.created(createURi).body(eventResource);
  }
  @PutMapping("/{id}")
  public ResponseEntity createEntity(@PathVariable Integer id,
                                     @RequestBody @Valid EventDto eventDto, Errors errors) {
    if (errors.hasErrors()) {
      return badRequest(errors);
    }
    eventValidator.validate(eventDto, errors);
    if (errors.hasErrors()) {
      return badRequest(errors);
    }
    Optional<Event> foundEvent = eventRepository.findById(id);
    if (foundEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    this.modelMapper.map(eventDto, foundEvent.get());

    Event updateEvent = this.eventRepository.save(foundEvent.get());
    WebMvcLinkBuilder selfLink = linkTo(EventController.class).slash(updateEvent.getId());
    URI createURi = selfLink.toUri();
    EventEntityModel eventResource = new EventEntityModel(updateEvent, ApiType.CREATE);
    return ResponseEntity.ok(eventResource);
  }

  private ResponseEntity badRequest(Errors errors) {
      return ResponseEntity.badRequest().body(ErrorsEntityModel.modelOf(errors));
  }
}
