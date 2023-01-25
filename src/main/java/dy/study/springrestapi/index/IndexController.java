package dy.study.springrestapi.index;

import dy.study.springrestapi.events.EventController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {

  @GetMapping("/api")
  public RepresentationModel index() {
    var index = new RepresentationModel();
    index.add(linkTo(EventController.class).withRel("events"));
    return index;
  }
}
