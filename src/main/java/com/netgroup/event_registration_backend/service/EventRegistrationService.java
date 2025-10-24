package com.netgroup.event_registration_backend.service;

import com.netgroup.event_registration_backend.domain.EventRegistration;
import com.netgroup.event_registration_backend.domain.Person;
import com.netgroup.event_registration_backend.dto.registration.RegistrationEventRequest;
import com.netgroup.event_registration_backend.exception.DuplicateRegistrationException;
import com.netgroup.event_registration_backend.exception.EventNotFoundException;
import com.netgroup.event_registration_backend.exception.MaxPeopleExceededException;
import com.netgroup.event_registration_backend.repository.EventRegistrationRepository;
import com.netgroup.event_registration_backend.repository.EventRepository;
import com.netgroup.event_registration_backend.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EventRegistrationService {

  private final EventRepository eventRepository;
  private final PersonRepository personRepository;
  private final EventRegistrationRepository eventRegistrationRepository;

  @Transactional
  public void register(Long eventId, RegistrationEventRequest request) {
    var event = eventRepository.findById(eventId)
        .orElseThrow(() -> new EventNotFoundException(eventId));

    var person = personRepository.findByPersonalCode(request.personalCode())
        .orElseGet(() -> personRepository.save(
            Person.builder()
                .firstName(request.name())
                .lastName(request.lastname())
                .personalCode(request.personalCode())
                .build()
        ));

    if (eventRegistrationRepository.existsByEventIdAndPersonId(event.getId(), person.getId())) {
      throw new DuplicateRegistrationException();
    }

    var attendeesAmount = eventRegistrationRepository.countByEventId(eventId);
    if (attendeesAmount >= event.getMaxPeople()) {
      throw new MaxPeopleExceededException();
    }

    var eventRegistration = EventRegistration.builder()
        .event(event)
        .person(person)
        .build();

    eventRegistrationRepository.save(eventRegistration);
  }


}
