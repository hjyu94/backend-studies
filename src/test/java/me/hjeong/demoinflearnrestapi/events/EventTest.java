package me.hjeong.demoinflearnrestapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder()
    {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    public void testFree() {
        // Given
        Event event1 = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        // When
        event1.update();
        // Then
        assertThat(event1.isFree()).isTrue();

        // Given
        Event event2 = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        // When
        event2.update();
        // Then
        assertThat(event2.isFree()).isFalse();

        // Given
        Event event3 = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        // When
        event3.update();
        // Then
        assertThat(event3.isFree()).isFalse();
    }

    @Test
    public void isOffline() {
        // Given
        Event event1 = Event.builder()
                .location("강남")
                .build();
        // When
        event1.update();
        // Then
        assertThat(event1.isOffline()).isFalse();

       // Given
        Event event2 = Event.builder()
                .build();
        // When
        event2.update();
        // Then
        assertThat(event2.isOffline()).isTrue();


    }
}