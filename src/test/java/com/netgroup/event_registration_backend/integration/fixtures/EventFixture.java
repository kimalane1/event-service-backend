package com.netgroup.event_registration_backend.integration.fixtures;

import com.netgroup.event_registration_backend.domain.Event;
import com.netgroup.event_registration_backend.domain.Person;
import java.time.ZonedDateTime;
import java.util.UUID;

public class EventFixture {

  public static Event event() {
    return Event.builder()
        .name("B-day")
        .eventTime(ZonedDateTime.now())
        .maxPeople(10)
        .build();
  }

  public static Event withNameAndEventTime(String name, ZonedDateTime eventTime) {
    Event event = event();
    event.setName(name);
    event.setEventTime(eventTime);
    return event;
  }
}
