package dy.study.springrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import dy.study.springrestapi.common.RestDocsConfiguration;
import dy.study.springrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTets {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

//  @MockBean
  @Autowired
  EventRepository eventRepository;

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
//        .andExpect(jsonPath("id").value(Matchers.not(100)))
        .andExpect(jsonPath("free").value(false))
        .andExpect(jsonPath("offline").value(true))
        .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name() ))
//        .andExpect(jsonPath("_links.self").exists())
//        .andExpect(jsonPath("_links.query-events").exists())
//        .andExpect(jsonPath("_links.update-event").exists())
        .andDo(document("create-event",
            links(
                linkWithRel("self").description("link to self"),
                linkWithRel("query-events").description("link to query events"),
                linkWithRel("update-event").description("link to update an existing"),
                linkWithRel("profile").description("link to profile")
            ),
            requestHeaders(
                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
            ),
            requestFields(
                fieldWithPath("name").description("Name of new event"),
                fieldWithPath("description").description("description of new event"),
                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                fieldWithPath("beginEventDateTime").description("date time of begin of event"),
                fieldWithPath("endEventDateTime").description("date time of end of event"),
                fieldWithPath("basePrice").description("basePrice"),
                fieldWithPath("maxPrice").description("maxPrice"),
                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment"),
                fieldWithPath("location").description("location")
            ),
            // relaxed는 정확한 문서를 만들 수 없다.
            // relaxed는 문서 일부분만 테스트 가능하다.
            responseFields(
//            relaxedResponseFields(
                fieldWithPath("id").description("od of new event"),
                fieldWithPath("name").description("Name of new event"),
                fieldWithPath("description").description("description of new event"),
                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                fieldWithPath("beginEventDateTime").description("date time of begin of event"),
                fieldWithPath("endEventDateTime").description("date time of end of event"),
                fieldWithPath("basePrice").description("basePrice"),
                fieldWithPath("maxPrice").description("maxPrice"),
                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment"),
                fieldWithPath("location").description("location"),
                fieldWithPath("free").description("it tells if this event is free or not"),
                fieldWithPath("offline").description("it tells if event is offline or not"),
                fieldWithPath("eventStatus").description("event status"),
                //not relaxedRes~R
                fieldWithPath("_links.self.href").description("event status"),
                fieldWithPath("_links.query-events.href").description("event status"),
                fieldWithPath("_links.update-event.href").description("event status"),
                fieldWithPath("_links.profile.href").description("link to profile")
                )
        ))
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
  @TestDescription("입력값이 잘못된 경우 에러가 발생하는 테스트")
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
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errors[0].objectName").exists())
        .andExpect(jsonPath("errors[0].defaultMessage").exists())
        .andExpect(jsonPath("errors[0].code").exists())
        .andExpect(jsonPath("_links.index").exists())
    ;
  }

  @Test
  @TestDescription("30개 이벤트를 10개 두번째 페이지 조회")
  public void QueryEvents() throws Exception {
    //Given
    IntStream.range(0, 30).forEach(this::generateEvent);
    this.mockMvc.perform(get("/api/events")
            .param("page", "1")
            .param("size", "10")
            .param("sort", "name,DESC")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("page").exists())
        .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
        .andExpect(jsonPath("_links.self").exists())
        .andExpect(jsonPath("_links.profile").exists())
        .andDo(document("query-events",
//            requestHeaders(
//                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
//                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
//            ),
            requestParameters(
                parameterWithName("page").description("starts with 0"),
                parameterWithName("size").description("page size"),
                parameterWithName("sort").description("field name with comma and ASC/DESC")
            ),
            responseFields(
              fieldWithPath("_embedded.eventList").description("list of events"),
              fieldWithPath("_embedded.eventList[0]id").description("od of new event"),
              fieldWithPath("_embedded.eventList[0].name").description("list of events"),
              fieldWithPath("_embedded.eventList[0].description").description("description of new event"),
              fieldWithPath("_embedded.eventList[0].beginEnrollmentDateTime").description("date time of begin of new event"),
              fieldWithPath("_embedded.eventList[0].closeEnrollmentDateTime").description("date time of close of new event"),
              fieldWithPath("_embedded.eventList[0].beginEventDateTime").description("date time of begin of event"),
              fieldWithPath("_embedded.eventList[0].endEventDateTime").description("date time of end of event"),
              fieldWithPath("_embedded.eventList[0].basePrice").description("basePrice"),
              fieldWithPath("_embedded.eventList[0].maxPrice").description("maxPrice"),
              fieldWithPath("_embedded.eventList[0].limitOfEnrollment").description("limitOfEnrollment"),
              fieldWithPath("_embedded.eventList[0].location").description("location"),
              fieldWithPath("_embedded.eventList[0].free").description("it tells if this event is free or not"),
              fieldWithPath("_embedded.eventList[0].offline").description("it tells if event is offline or not"),
              fieldWithPath("_embedded.eventList[0].eventStatus").description("event status"),



              fieldWithPath("_embedded.eventList[0]._links").description("list of events"),
              fieldWithPath("_embedded.eventList[0]._links.self.href").description("list of events"),
              fieldWithPath("_embedded.eventList").description("list of events"),
              fieldWithPath("_links.profile.href").description("first page"),
              fieldWithPath("_links.first.href").description("first page"),
              fieldWithPath("_links.prev.href").description("pre page"),
              fieldWithPath("_links.self.href").description("slef page"),
              fieldWithPath("_links.next.href").description("next page"),
              fieldWithPath("_links.last.href").description("last page"),
              fieldWithPath("page.size").description(" page"),
              fieldWithPath("page.totalElements").description(" page"),
              fieldWithPath("page.totalPages").description(" page"),
              fieldWithPath("page.number").description(" page")
            )
        ))

//    "page":{
//      "size":10,
//          "totalElements":30,
//          "totalPages":3,
//          "number":1
//    }
    ;
  }

  @Test
  @TestDescription("event 조회시 없을때")
  public void getEventException404() throws Exception {
    //create
    //when then
    this.mockMvc.perform(get("/api/events/{id}", 99))
        .andExpect(status().isNotFound())
    ;
  }
  @Test
  @TestDescription("event 조회")
  public void getEvent() throws Exception {
    //create
    Event event = this.generateEvent(100);
    //when then
    this.mockMvc.perform(get("/api/events/{id}", event.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").exists())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("_links.self").exists())
        .andExpect(jsonPath("_links.profile").exists())
        .andDo(document("get-an-event"))
    ;
  }

  private Event generateEvent(int index) {
    Event event = Event.builder()
        .name("evet " + index)
        .description("test event " + index)
        .build();
    event.update();
    this.eventRepository.save(event);
    return event;
  }
}
