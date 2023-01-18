package dy.study.springrestapi.events;


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

  @PostMapping
  public ResponseEntity createEntity(@RequestBody Event event) {
    URI createURi = linkTo(EventController.class).slash("{id }").toUri();
    event.setId(10);
    return ResponseEntity.created(createURi).body(event);
  }
}
