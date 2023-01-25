package dy.study.springrestapi.common;

import dy.study.springrestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsEntityModel extends EntityModel<Errors> {
//  public ErrorsEntityModel(Errors content, Iterable<Link> links) {
//    super(content, links);
//    add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
//  }
  public static EntityModel<Errors> modelOf(Errors errors) {
    EntityModel<Errors> errorsModel = EntityModel.of(errors);
    errorsModel.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    return errorsModel;
  }
}
