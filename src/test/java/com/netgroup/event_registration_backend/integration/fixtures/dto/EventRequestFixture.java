package com.netgroup.event_registration_backend.integration.fixtures.dto;

import com.netgroup.event_registration_backend.dto.event.EventRequest;
import java.time.ZonedDateTime;

public class EventRequestFixture {

  public static EventRequest request() {
    return new EventRequest("John", ZonedDateTime.now(), 10);
  }

  public static EventRequest withTimeAndCode(ZonedDateTime time, int maxPeople) {
    return new EventRequest("John", time, maxPeople);
  }

}
