package com.netgroup.event_registration_backend.integration.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
import org.junit.jupiter.api.DisplayName;
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

  @DisplayName("Should create event registration when event and person exists")
  @Test
  void register_whenEventAndPersonExists_createsEventRegistration() {
    var event = eventRepository.save(EventFixture.event());
    var person = personRepository.save(PersonFixture.person());
    var request = RegistrationEventRequestFixture.withPersonalCode(person.getPersonalCode());
    eventRegistrationService.register(event.getId(), request);

    var registration = eventRegistrationRepository.findAll().getFirst();
    assertEquals(event.getId(), registration.getEvent().getId());
    assertEquals(person.getId(), registration.getPerson().getId());
  }


  @DisplayName("Should create person and event registration when person not exists")
  @Test
  void register_whenPersonNotExists_createsPersonAndEventRegistration() {
    var event = eventRepository.save(EventFixture.event());
    var request = RegistrationEventRequestFixture.request();
    eventRegistrationService.register(event.getId(), request);

    var person = personRepository.findByPersonalCode(request.personalCode()).orElseThrow();
    var registration = eventRegistrationRepository.findAll().getFirst();

    assertEquals(event.getId(), registration.getEvent().getId());
    assertThat(person)
        .usingRecursiveComparison()
        .isEqualTo(registration.getPerson());
  }

  @DisplayName("Should throw exception when event not exists")
  @Test
  void register_whenEventNotExists_throwsEventNotFoundException() {
    var request = RegistrationEventRequestFixture.request();
    assertThrows(EventNotFoundException.class,
        () -> eventRegistrationService.register(Long.MAX_VALUE, request));
  }

  @DisplayName("Should throw exception when registration already exists")
  @Test
  void register_whenRegistrationExists_throwsDuplicateRegistrationException() {
    var event = eventRepository.save(EventFixture.event());
    var request = RegistrationEventRequestFixture.request();
    eventRegistrationService.register(event.getId(), request);

    assertThrows(DuplicateRegistrationException.class,
        () -> eventRegistrationService.register(event.getId(), request));
  }

  @DisplayName("Should throw exception when event max people is exceeded")
  @Test
  void register_whenEventMaxPeopleIsRegistered_throwsMaxPeopleExceededExceptionW() {
    var event = eventRepository.save(EventFixture.withMaxPeople(1));
    var request1 = RegistrationEventRequestFixture.request();
    eventRegistrationService.register(event.getId(), request1);

    var request2 = RegistrationEventRequestFixture.withPersonalCode("98765432101");

    assertThrows(MaxPeopleExceededException.class,
        () -> eventRegistrationService.register(event.getId(), request2));
  }

}
