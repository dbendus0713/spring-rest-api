package dy.study.springrestapi.events;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

  @Test
  public void builder() {
    Event event = Event.builder().name("Spring test").build();
    assertThat(event).isNotNull();
  }

  @Test
  public void javaBean() {
    String name = "Event";
    String Desc = "Spring";
    Event event = new Event();
    event.setName(name);
    event.setDescription(Desc);

    assertThat(event.getName()).isEqualTo(name);
    assertThat(event.getDescription()).isEqualTo(Desc);
  }


  @org.junit.Test
  public void testFree() {
    Event event = Event.builder()
        .basePrice(0)
        .maxPrice(0)
        .build();
    //when
    event.update();
    //then
    AssertionsForClassTypes.assertThat(event.isFree()).isTrue();

    event = Event.builder()
        .basePrice(100)
        .maxPrice(0)
        .build();
    //when
    event.update();
    //then
    AssertionsForClassTypes.assertThat(event.isFree()).isFalse();

    event = Event.builder()
        .basePrice(0)
        .maxPrice(100)
        .build();
    //when
    event.update();
    //then
    AssertionsForClassTypes.assertThat(event.isFree()).isFalse();
  }

  @Test
  public void testOffline() {
    Event event = Event.builder()
        .location("Naver")
        .build();
    //when
    event.update();
    //then
    AssertionsForClassTypes.assertThat(event.isOffline()).isTrue();

  }
}