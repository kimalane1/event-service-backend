package com.netgroup.event_registration_backend.integration.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.netgroup.event_registration_backend.integration.BaseIntegrationTest;
import com.netgroup.event_registration_backend.integration.fixtures.domain.EventFixture;
import com.netgroup.event_registration_backend.repository.EventRepository;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class EventRepositoryIT extends BaseIntegrationTest {

  @Autowired
  EventRepository repository;

  @DisplayName("Should return true when event exists by name and event time")
  @Test
  void existsByNameAndEventTime_whenEventExists_returnsTrue() {
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

  @DisplayName("Should return false when event does not exist")
  @Test
  void existsByNameAndEventTime_whenEventExists_returnsFalse() {
    assertFalse(repository.existsByNameAndEventTime("NONE", ZonedDateTime.now()));
  }

  @DisplayName("Should throw exception when event with same name and time exists")
  @Test
  void save_whenEventExists_throwsDataIntegrityViolationException() {
    var eventTime = ZonedDateTime.of(2030, 10, 10, 12, 0, 0, 0, ZoneId.of("UTC"));

    var event = EventFixture.withNameAndEventTime("B-day", eventTime);
    repository.saveAndFlush(event);

    var duplicate = EventFixture.withNameAndEventTime("B-day", eventTime);

    assertThrows(DataIntegrityViolationException.class,
        () -> repository.saveAndFlush(duplicate)
    );
  }

}
