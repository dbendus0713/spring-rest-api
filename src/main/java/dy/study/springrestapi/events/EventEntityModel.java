package dy.study.springrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventEntityModel extends EntityModel<Event> {
  // json unwrapped 이미 적용되어있음
  public EventEntityModel(Event event, Link... links) {
    super(event, Arrays.asList(links));
    add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
    add(linkTo(EventController.class).withRel("query-events"));
  }
}
