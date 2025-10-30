package com.netgroup.event_registration_backend.integration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.netgroup.event_registration_backend.exception.DuplicateEventException;
import com.netgroup.event_registration_backend.integration.BaseIntegrationTest;
import com.netgroup.event_registration_backend.integration.fixtures.dto.EventRequestFixture;
import com.netgroup.event_registration_backend.repository.EventRepository;
import com.netgroup.event_registration_backend.service.EventService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EventServiceIT extends BaseIntegrationTest {

  @Autowired
  EventRepository repository;

  @Autowired
  EventService service;

  @Test
  void shouldCreateEventWhenEverythingIsOK() {
    var request = EventRequestFixture.request();
    service.create(request);

    var saved = repository.existsByNameAndEventTime(request.name(), request.eventTime());
    assertTrue(saved);

    var createdEvent = repository.findAll().stream().findFirst().orElseThrow();

    assertEquals(request.name(), createdEvent.getName());
    assertEquals(request.eventTime(),
        createdEvent.getEventTime());
    assertEquals(request.maxPeople(), createdEvent.getMaxPeople());
  }

  @Test
  void shouldThrowExceptionWhenEventAlreadyExists() {
    var eventTime = ZonedDateTime.of(2030, 10, 10, 12, 0, 0, 0, ZoneId.of("UTC"));
    var request = EventRequestFixture.withTimeAndCode(eventTime, 10);

    //when
    service.create(request);

    //then
    assertThrows(DuplicateEventException.class, () -> service.create(request));
  }

  @Test
  void shouldReturnAllEventsWhenEventsExist() {
    var request = EventRequestFixture.request();
    service.create(request);

    var events = service.findAll();
    assertNotNull(events);
    assertEquals(1, events.size());

    var event = events.getFirst();
    assertEquals(request.name(), event.name());
    assertEquals(request.eventTime(), event.eventTime());
    assertEquals(request.maxPeople(), event.maxPeople());
  }

  @Test
  void shouldReturnEmptyListWhenNothingExists() {
    assertTrue(service.findAll().isEmpty());
  }

}
