package dy.study.springrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dy.study.springrestapi.common.ApiType;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventEntityModel extends EntityModel<Event> {
  // json unwrapped 이미 적용되어있음
  public EventEntityModel(Event event, ApiType apiType, Link... links) {
    super(event, Arrays.asList(links));
    add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    add(linkTo(EventController.class).withRel("query-events"));
    switch (apiType) {
      case FINDALL:
        add(Link.of("/docs/index.html#resources-events-list", LinkRelation.of("profile")));
        break;
      case FIND:
        add(Link.of("/docs/index.html#resources-events-get", LinkRelation.of("profile")));
        break;
      case CREATE:
        add(Link.of("/docs/index.html#resources-events-create", LinkRelation.of("profile")));
        break;
      case UPDATE:
        add(Link.of("/docs/index.html#resources-events-update", LinkRelation.of("profile")));
        add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        break;
      case DELETE:
        break;
    }
  }
}
