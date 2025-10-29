package com.netgroup.event_registration_backend.integration.fixtures.domain;

import com.netgroup.event_registration_backend.domain.Event;
import java.time.ZonedDateTime;

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
