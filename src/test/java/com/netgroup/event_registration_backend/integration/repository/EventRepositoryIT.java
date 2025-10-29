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
  EventRepository repository;

  @Test
  void shouldReturnTrueWhenEventExistsByNameAndEventTime() {
    var name = "Party";
    var eventTime = ZonedDateTime.of(
        2025, 3, 6,
        17, 45, 0,
        0,
        ZoneId.of("Europe/Tallinn"));
    var createdEvent = EventFixture.withNameAndEventTime(name, eventTime);
    repository.save(createdEvent);

    assertTrue(repository.existsByNameAndEventTime(name, eventTime));
  }

  @Test
  void shouldReturnFalseWhenEventExistsByNameAndEventTime() {
    assertFalse(repository.existsByNameAndEventTime("NONE", ZonedDateTime.now()));
  }

  @Test
  void shouldThrowWhenEventWithSameNameAndTimeExists() {
    var eventTime = ZonedDateTime.of(2030, 10, 10, 12, 0, 0, 0, ZoneId.of("UTC"));

    var event = EventFixture.withNameAndEventTime("B-day", eventTime);
    repository.saveAndFlush(event);

    var duplicate = EventFixture.withNameAndEventTime("B-day", eventTime);

    assertThrows(DataIntegrityViolationException.class,
        () -> repository.saveAndFlush(duplicate)
    );
  }

}
