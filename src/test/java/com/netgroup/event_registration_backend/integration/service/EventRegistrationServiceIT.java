package com.netgroup.event_registration_backend.integration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.netgroup.event_registration_backend.exception.DuplicateRegistrationException;
import com.netgroup.event_registration_backend.exception.EventNotFoundException;
import com.netgroup.event_registration_backend.exception.MaxPeopleExceededException;
import com.netgroup.event_registration_backend.integration.BaseIntegrationTest;
import com.netgroup.event_registration_backend.integration.fixtures.domain.EventFixture;
import com.netgroup.event_registration_backend.integration.fixtures.domain.PersonFixture;
import com.netgroup.event_registration_backend.integration.fixtures.dto.RegistrationEventRequestFixture;
import com.netgroup.event_registration_backend.repository.EventRegistrationRepository;
import com.netgroup.event_registration_backend.repository.EventRepository;
import com.netgroup.event_registration_backend.repository.PersonRepository;
import com.netgroup.event_registration_backend.service.EventRegistrationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
public class EventRegistrationServiceIT extends BaseIntegrationTest {

  @Autowired
  PersonRepository personRepository;

  @Autowired
  EventRepository eventRepository;

  @Autowired
  EventRegistrationRepository eventRegistrationRepository;

  @Autowired
  EventRegistrationService eventRegistrationService;

  @Test
  void shouldCreateEventRegistrationWhenEventAndPersonExists() {
    var event = eventRepository.save(EventFixture.event());
    var person = personRepository.save(PersonFixture.person());
    var request = RegistrationEventRequestFixture.withPersonalCode(person.getPersonalCode());
    eventRegistrationService.register(event.getId(), request);

    var registration = eventRegistrationRepository.findAll().getFirst();
    assertEquals(event.getId(), registration.getEvent().getId());
    assertEquals(person.getId(), registration.getPerson().getId());
  }


  @Test
  void shouldCreatePersonAndEventRegistrationWhenPersonNotExists() {
    var event = eventRepository.save(EventFixture.event());
    var request = RegistrationEventRequestFixture.request();
    eventRegistrationService.register(event.getId(), request);

    var person = personRepository.findByPersonalCode(request.personalCode()).orElseThrow();
    var registration = eventRegistrationRepository.findAll().getFirst();
    assertEquals(event.getId(), registration.getEvent().getId());
    assertEquals(person.getId(), registration.getPerson().getId());
    assertEquals(person.getFirstName(), registration.getPerson().getFirstName());
    assertEquals(person.getLastName(), registration.getPerson().getLastName());
    assertEquals(person.getPersonalCode(), registration.getPerson().getPersonalCode());
  }

  @Test
  void shouldThrowEventNotFoundExceptionWhenEventNotExists() {
    var request = RegistrationEventRequestFixture.request();
    assertThrows(EventNotFoundException.class,
        () -> eventRegistrationService.register(Long.MAX_VALUE, request));
  }

  @Test
  void shouldThrowDuplicateRegistrationExceptionWhenRegistrationExists() {
    var event = eventRepository.save(EventFixture.event());
    var request = RegistrationEventRequestFixture.request();
    eventRegistrationService.register(event.getId(), request);

    assertThrows(DuplicateRegistrationException.class,
        () -> eventRegistrationService.register(event.getId(), request));
  }

  @Test
  void shouldThrowMaxPeopleExceededExceptionWhenEventIsFull() {
    var event = eventRepository.save(EventFixture.withMaxPeople(1));
    var request1 = RegistrationEventRequestFixture.request();
    eventRegistrationService.register(event.getId(), request1);

    var request2 = RegistrationEventRequestFixture.withPersonalCode("98765432101");

    assertThrows(MaxPeopleExceededException.class,
        () -> eventRegistrationService.register(event.getId(), request2));
  }

}
