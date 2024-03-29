package dy.study.springrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {
  public void validate(EventDto eventDto, Errors errors) {
    if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
      errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
      errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");
    }

    LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
    if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime())) {
      errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong");
    }
  }
}
