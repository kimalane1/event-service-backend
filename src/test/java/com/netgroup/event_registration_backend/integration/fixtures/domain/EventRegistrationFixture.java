package com.netgroup.event_registration_backend.integration.fixtures.domain;

import com.netgroup.event_registration_backend.domain.Event;
import com.netgroup.event_registration_backend.domain.EventRegistration;
import com.netgroup.event_registration_backend.domain.Person;
import java.time.ZonedDateTime;

public class EventRegistrationFixture {

  public static EventRegistration eventRegistration() {
    return EventRegistration.builder()
        .registeredAt(ZonedDateTime.now())
        .build();
  }

  public static EventRegistration withEventAndPerson(Event event, Person person) {
    EventRegistration eventRegistration = eventRegistration();
    eventRegistration.setEvent(event);
    eventRegistration.setPerson(person);
    return eventRegistration;
  }
}
