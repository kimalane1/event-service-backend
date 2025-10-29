package com.netgroup.event_registration_backend.integration.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.netgroup.event_registration_backend.integration.BaseIntegrationTest;
import com.netgroup.event_registration_backend.integration.fixtures.domain.EventFixture;
import com.netgroup.event_registration_backend.repository.EventRepository;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class EventRepositoryIT extends BaseIntegrationTest {

  @Autowired
  EventRepository eventRepository;

  @Test
  void shouldReturnTrueWhenEventExistsByNameAndEventTime() {
    var name = "Party";
    var eventTime = ZonedDateTime.of(
        2025, 3, 6,
        17, 45, 0,
        0,
        ZoneId.of("Europe/Tallinn"));
    var createdEvent = EventFixture.withNameAndEventTime(name, eventTime);
    eventRepository.save(createdEvent);

    assertTrue(eventRepository.existsByNameAndEventTime(name, eventTime));
  }

  @Test
  void shouldReturnFalseWhenEventExistsByNameAndEventTime() {
    assertFalse(eventRepository.existsByNameAndEventTime("NONE", ZonedDateTime.now()));
  }

  @Test
  void shouldThrowWhenEventWithSameNameAndTimeExists() {
    var eventTime = ZonedDateTime.of(2030, 10, 10, 12, 0, 0, 0, ZoneId.of("UTC"));

    var event = EventFixture.withNameAndEventTime("B-day", eventTime);
    eventRepository.saveAndFlush(event);

    var duplicate = EventFixture.withNameAndEventTime("B-day", eventTime);

    assertThrows(DataIntegrityViolationException.class,
        () -> eventRepository.saveAndFlush(duplicate)
    );
  }

}
