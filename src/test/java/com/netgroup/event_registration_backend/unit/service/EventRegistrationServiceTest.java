package com.netgroup.event_registration_backend.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.netgroup.event_registration_backend.domain.Event;
import com.netgroup.event_registration_backend.domain.EventRegistration;
import com.netgroup.event_registration_backend.domain.Person;
import com.netgroup.event_registration_backend.dto.registration.RegistrationEventRequest;
import com.netgroup.event_registration_backend.exception.DuplicateRegistrationException;
import com.netgroup.event_registration_backend.exception.EventNotFoundException;
import com.netgroup.event_registration_backend.exception.MaxPeopleExceededException;
import com.netgroup.event_registration_backend.repository.EventRegistrationRepository;
import com.netgroup.event_registration_backend.repository.EventRepository;
import com.netgroup.event_registration_backend.repository.PersonRepository;
import com.netgroup.event_registration_backend.service.EventRegistrationService;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventRegistrationServiceTest {

  @Mock
  private EventRepository eventRepository;

  @Mock
  private PersonRepository personRepository;

  @Mock
  private EventRegistrationRepository eventRegistrationRepository;

  @InjectMocks
  private EventRegistrationService eventRegistrationService;

  @Captor
  private ArgumentCaptor<EventRegistration> eventCaptor;

  @DisplayName("Should save event when event does not exist and person exists")
  @Test
  void register_whenEventDoesNotExistAndPersonExists_savesEvent() {
    var event = Event.builder().id(1L).name("B-day").eventTime(ZonedDateTime.now()).maxPeople(10)
        .build();
    var person = Person.builder().id(1L).firstName("John").lastName("Doe")
        .personalCode("49303061111").build();

    when(eventRepository.findById(any()))
        .thenReturn(Optional.of(event));
    when(personRepository.findByPersonalCode(any()))
        .thenReturn(Optional.of(person));
    when(eventRegistrationRepository.existsByEventIdAndPersonId(event.getId(), person.getId()))
        .thenReturn(false);
    when(eventRegistrationRepository.countByEventId(event.getId()))
        .thenReturn(1);

    eventRegistrationService.register(1L,
        new RegistrationEventRequest("John", "Doe", "49303061111"));
    verify(eventRegistrationRepository).save(eventCaptor.capture());
    EventRegistration saved = eventCaptor.getValue();
    assertEquals(event.getId(), saved.getEvent().getId());
    assertEquals(person.getId(), saved.getPerson().getId());
  }

  @DisplayName("Should register event when event and person does not exist")
  @Test
  void register_whenEventAndPersonDoesNotExist_savesEvent() {
    var event = Event.builder().id(1L).name("B-day").eventTime(ZonedDateTime.now()).maxPeople(10)
        .build();
    var createdPerson = Person.builder().id(1L).firstName("John").lastName("Doe")
        .personalCode("49303061111").build();

    when(eventRepository.findById(any()))
        .thenReturn(Optional.of(event));
    when(personRepository.findByPersonalCode(any()))
        .thenReturn(Optional.empty());
    when(personRepository.save(any()))
        .thenReturn(createdPerson);
    when(eventRegistrationRepository.existsByEventIdAndPersonId(event.getId(),
        createdPerson.getId()))
        .thenReturn(false);
    when(eventRegistrationRepository.countByEventId(event.getId()))
        .thenReturn(1);

    eventRegistrationService.register(1L,
        new RegistrationEventRequest("John", "Doe", "49303061111"));

    verify(eventRegistrationRepository).save(eventCaptor.capture());
    EventRegistration saved = eventCaptor.getValue();
    assertEquals(event.getId(), saved.getEvent().getId());
    assertEquals(createdPerson.getId(), saved.getPerson().getId());
  }

  @DisplayName("Should throw exception when event does not exist ")
  @Test
  void register_whenEventDoesNotExist_throwsException() {
    when(eventRepository.findById(any()))
        .thenReturn(Optional.empty());

    assertThrows(
        EventNotFoundException.class,
        () -> eventRegistrationService.register(1L,
            new RegistrationEventRequest("John", "Doe", "49303061111"))
    );

    verify(eventRegistrationRepository, never()).save(any());
  }

  @DisplayName("Should throw exception when event registration already exists")
  @Test
  void register_whenEventRegistrationExists_throwsException() {
    var event = Event.builder().id(1L).name("B-day").eventTime(ZonedDateTime.now()).maxPeople(10)
        .build();
    var person = Person.builder().id(1L).firstName("John").lastName("Doe")
        .personalCode("49303061111").build();

    when(eventRepository.findById(any()))
        .thenReturn(Optional.of(event));
    when(personRepository.findByPersonalCode(any()))
        .thenReturn(Optional.of(person));
    when(eventRegistrationRepository.existsByEventIdAndPersonId(event.getId(), person.getId()))
        .thenReturn(true);

    assertThrows(
        DuplicateRegistrationException.class,
        () -> eventRegistrationService.register(1L,
            new RegistrationEventRequest("John", "Doe", "49303061111"))
    );

    verify(eventRegistrationRepository, never()).save(any());
  }

  @DisplayName("Should throw exception when max people reached")
  @Test
  void register_whenMaxPeopleReached_throwsException() {
    var event = Event.builder().id(1L).name("B-day").eventTime(ZonedDateTime.now()).maxPeople(1)
        .build();
    var person = Person.builder().id(1L).firstName("John").lastName("Doe")
        .personalCode("49303061111").build();

    when(eventRepository.findById(any()))
        .thenReturn(Optional.of(event));
    when(personRepository.findByPersonalCode(any()))
        .thenReturn(Optional.of(person));
    when(eventRegistrationRepository.existsByEventIdAndPersonId(event.getId(), person.getId()))
        .thenReturn(false);
    when(eventRegistrationRepository.countByEventId(event.getId()))
        .thenReturn(1);

    assertThrows(
        MaxPeopleExceededException.class,
        () -> eventRegistrationService.register(1L,
            new RegistrationEventRequest("Sarah", "Doe", "49303061112"))
    );

    verify(eventRegistrationRepository, never()).save(any());
  }


}
