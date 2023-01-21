package dy.study.springrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import dy.study.springrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.regex.Matcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTets {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

//  @MockBean
//  EventRepository eventRepository;

  @Test
  @TestDescription("정상적으로 이벤트 생성하는 테스트") //junit5에선 제공됨.
  public void createEvent() throws Exception {
    EventDto event = EventDto.builder()
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

//    event.setId(10);
    //repository에 save메소드가 수행될때.
    //spring boot test로 전환
//    Mockito.when(eventRepository.save(event)).thenReturn(event);

    mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(event))
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").exists())
        .andExpect(header().exists(HttpHeaders.LOCATION))
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
        .andExpect(jsonPath("id").value(Matchers.not(100)))
        .andExpect(jsonPath("free").value(Matchers.not(true)))
        .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name() ))
    ;
  }

  @Test
  @TestDescription("입력받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
  public void createEvent_Bad_Requset () throws Exception {
    Event event = Event.builder()
        .id(100)
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
        .free(true)
        .eventStatus(EventStatus.PUBLISHED)
        .build();

//    event.setId(10);
    //repository에 save메소드가 수행될때.
    //spring boot test로 전환
//    Mockito.when(eventRepository.save(event)).thenReturn(event);

    mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(event))
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
    ;
  }

  @Test
  @TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
  public void createEvent_Bad_Request_Empty_Input() throws Exception {
    EventDto eventDto = EventDto.builder().build();
    this.mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
        .andExpect(status().isBadRequest())
    ;
  }

  @Test
  @TestDescription("입력값 체크 에러가 발생하는 테스트")
  public void createEvent_Bad_Request_Wrong_Input() throws Exception {
    EventDto eventDto = EventDto.builder()
        .name("name")
        .description("description")
        .location("gangnam")
        .beginEnrollmentDateTime(LocalDateTime.of(2023, 1, 18, 13, 41))
        .closeEnrollmentDateTime(LocalDateTime.of(2023, 1, 19, 13, 41))
        .beginEventDateTime(LocalDateTime.of(2023, 1, 22, 13, 41))
        .endEventDateTime(LocalDateTime.of(2023, 1, 21, 13, 41))
        .basePrice(1200)
        .maxPrice(300)
        .limitOfEnrollment(100)
        .build();

    this.mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
        .andExpect(status().isBadRequest())
    ;
  }
}
