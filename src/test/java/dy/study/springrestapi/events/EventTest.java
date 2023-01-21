package dy.study.springrestapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

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


  @Test
//  @Parameters({
//      "0, 0, true",
//      "100, 0, false",
//      "0, 100, false"
//  })
//  @Parameters(method = "paramsForTestFree")
  @Parameters
  public void testFree(int basePrice, int maxPrice, boolean isFree) {
    Event event = Event.builder()
        .basePrice(basePrice)
        .maxPrice(maxPrice)
        .build();
    //when
    event.update();
    //then
    assertThat(event.isFree()).isEqualTo(isFree);
  }

  //parameterFor => prefix
//  private Object[] paramsForTestFree() {
  private Object[] parametersForTestFree() {
    return new Object[] {
        new Object[] {0, 0, true},
        new Object[] {100, 0, false},
        new Object[] {0, 100, false }
    };
  }

  @Test
  @Parameters
  public void testOffline(String location, boolean isOff) {
    Event event = Event.builder()
        .location(location)
        .build();
    //when
    event.update();
    //then
    AssertionsForClassTypes.assertThat(event.isOffline()).isEqualTo(isOff);
  }

  private Object[] parametersForTestOffline() {
    return new Object[] {
        new Object[] {"강남", true},
        new Object[] {null, false},
        new Object[] {"부산", true},
        new Object[] {"     ", false},
        new Object[] {null, false }
    };
  }
}