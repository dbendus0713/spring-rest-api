package dy.study.springrestapi.events;

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
}