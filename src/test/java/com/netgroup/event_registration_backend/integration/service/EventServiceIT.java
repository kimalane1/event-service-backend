package com.netgroup.event_registration_backend.integration.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.netgroup.event_registration_backend.domain.Event;
import com.netgroup.event_registration_backend.dto.event.EventResponse;
import com.netgroup.event_registration_backend.exception.DuplicateEventException;
import com.netgroup.event_registration_backend.integration.BaseIntegrationTest;
import com.netgroup.event_registration_backend.integration.fixtures.dto.EventRequestFixture;
import com.netgroup.event_registration_backend.repository.EventRepository;
import com.netgroup.event_registration_backend.service.EventService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EventServiceIT extends BaseIntegrationTest {

  @Autowired
  EventRepository repository;

  @Autowired
  EventService service;

  @DisplayName("Should create event when preconditions are met")
  @Test
  void create_whenPreconditionsMet_createsRegistration() {
    var request = EventRequestFixture.request();
    service.create(request);

    var saved = repository.existsByNameAndEventTime(request.name(), request.eventTime());
    assertTrue(saved);

    var createdEvent = repository.findAll().stream().findFirst().orElseThrow();

    assertThat(createdEvent)
        .extracting(
            Event::getName,
            Event::getEventTime,
            Event::getMaxPeople)
        .containsExactly(
            request.name(),
            request.eventTime(),
            request.maxPeople());
  }

  @DisplayName("Should throw exception when event already exists")
  @Test
  void create_whenEventExists_throwsDuplicateEventException() {
    var eventTime = ZonedDateTime.of(2030, 10, 10, 12, 0, 0, 0, ZoneId.of("UTC"));
    var request = EventRequestFixture.withTimeAndCode(eventTime, 10);

    //when
    service.create(request);

    //then
    assertThrows(DuplicateEventException.class, () -> service.create(request));
  }

  @Test
  void findAll_whenEventsExist_returnsAllEvents() {
    var request = EventRequestFixture.request();
    service.create(request);

    var events = service.findAll();
    assertNotNull(events);
    assertEquals(1, events.size());

    var event = events.getFirst();

    assertThat(event)
        .extracting(
            EventResponse::name,
            EventResponse::eventTime,
            EventResponse::maxPeople)
        .containsExactly(
            request.name(),
            request.eventTime(),
            request.maxPeople());
  }

  @DisplayName("Should return empty list when nothing exists")
  @Test
  void findAll_whenNothingExists_returnsEmptyList() {
    assertTrue(service.findAll().isEmpty());
  }

}
