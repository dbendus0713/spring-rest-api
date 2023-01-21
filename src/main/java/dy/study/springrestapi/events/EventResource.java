package dy.study.springrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;

//case2
public class EventResource extends EntityModel<Event> {
  @JsonUnwrapped
  private Event event;

  public EventResource(Event event) {
    this.event = event;
  }

  public Event getEvent() {
    return this.event;
  }
}
