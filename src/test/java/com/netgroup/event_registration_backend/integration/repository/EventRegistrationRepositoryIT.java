package com.netgroup.event_registration_backend.integration.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.netgroup.event_registration_backend.integration.BaseIntegrationTest;
import com.netgroup.event_registration_backend.integration.fixtures.domain.EventFixture;
import com.netgroup.event_registration_backend.integration.fixtures.domain.EventRegistrationFixture;
import com.netgroup.event_registration_backend.integration.fixtures.domain.PersonFixture;
import com.netgroup.event_registration_backend.repository.EventRegistrationRepository;
import com.netgroup.event_registration_backend.repository.EventRepository;
import com.netgroup.event_registration_backend.repository.PersonRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class EventRegistrationRepositoryIT extends BaseIntegrationTest {

  @Autowired
  EventRepository eventRepository;

  @Autowired
  PersonRepository personRepository;

  @Autowired
  EventRegistrationRepository eventRegistrationRepository;

  @DisplayName("Should return true when event exists")
  @Test
  void existsByEventIdAndPersonId_whenEventExists_returnsTrue() {
    var person = personRepository.save(PersonFixture.person());
    var event = eventRepository.save(EventFixture.event());

    var registration = eventRegistrationRepository.save(
        EventRegistrationFixture.withEventAndPerson(event, person)
    );

    assertAll("entity persistence",
        () -> assertThat(person.getId()).isNotNull(),
        () -> assertThat(event.getId()).isNotNull(),
        () -> assertThat(registration.getId()).isNotNull()
    );
    assertTrue(
        eventRegistrationRepository.existsByEventIdAndPersonId(event.getId(), person.getId()));
  }

  @DisplayName("Should return false when event not exists")
  @Test
  void existsByEventIdAndPersonId_whenEventNotExists_returnsFalse() {
    assertFalse(
        eventRegistrationRepository.existsByEventIdAndPersonId(Long.MAX_VALUE, Long.MAX_VALUE));
  }


  @DisplayName("Should return registrations count when event exists")
  @Test
  void shouldReturnRegistrationsCountWhenEventExists() {
    var event = eventRepository.save(EventFixture.event());

    var person1 = personRepository.save(PersonFixture.person());
    var person2 = personRepository.save(PersonFixture.withCode("39211270855"));
    var person3 = personRepository.save(PersonFixture.withCode("51802030829"));

    var registration1 = EventRegistrationFixture.withEventAndPerson(event, person1);
    var registration2 = EventRegistrationFixture.withEventAndPerson(event, person2);
    var registration3 = EventRegistrationFixture.withEventAndPerson(event, person3);
    var registrations = List.of(registration1, registration2, registration3);
    eventRegistrationRepository.saveAll(registrations);

    assertEquals(
        registrations.size(),
        eventRegistrationRepository.countByEventId(event.getId()));
  }

  @Test
  void shouldReturnZeroRegistrationsWhenEventNotExists() {
    assertEquals(
        0,
        eventRegistrationRepository.countByEventId(Long.MAX_VALUE));
  }

  @Test
  void shouldThrowWhenRegisteringSamePersonForSameEventTwice() {
    var person = personRepository.save(PersonFixture.person());
    var event = eventRepository.save(EventFixture.event());

    eventRegistrationRepository.save(
        EventRegistrationFixture.withEventAndPerson(event, person));
    var registration2 = EventRegistrationFixture.withEventAndPerson(event, person);

    assertThrows(DataIntegrityViolationException.class,
        () -> eventRegistrationRepository.saveAndFlush(registration2));
  }
}
