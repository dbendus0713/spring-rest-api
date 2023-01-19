package dy.study.springrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class  EventControllerTets {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  EventRepository eventRepository;

  @Test
  public void createEvent() throws Exception {
    Event event = Event.builder()
        .name("name")
        .description("description")
        .beginEnrollmentDateTime(LocalDateTime.of(2023, 1, 18, 13, 41))
        .closeEnrollmentDateTime(LocalDateTime.of(2023, 1, 19, 13, 41))
        .beginEventDateTime(LocalDateTime.of(2023, 1, 20, 13, 41))
        .endEventDateTime(LocalDateTime.of(2023, 1, 21, 13, 41))
        .basePrice(200)
        .maxPrice(300)
        .limitOfEnrollment(100)
        .location("gangnam")
        .build();

    event.setId(10);
    //repository에 save메소드가 수행될때.
    Mockito.when(eventRepository.save(event)).thenReturn(event);

    mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(event))
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").exists())
        .andExpect(header().exists(HttpHeaders.LOCATION))
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE));
  }

}
