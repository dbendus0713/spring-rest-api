package dy.study.springrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

//case2
public class EventResource extends EntityModel<Event> {
//  @JsonUnwrapped
//  private Event event;

  public EventResource(Event event, Link... links) {
    super(event, Arrays.asList(links));
    add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
  }

//  public Event getEvent() {
//    return this.event;
//  }
}
