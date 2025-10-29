package com.netgroup.event_registration_backend.integration.fixtures.dto;

import com.netgroup.event_registration_backend.dto.registration.RegistrationEventRequest;

public class RegistrationEventRequestFixture {

  public static RegistrationEventRequest request() {
    return new RegistrationEventRequest("John",
        "Doe",
        "12345678901");
  }

  public static RegistrationEventRequest withPersonalCode(String personalCode) {
    return new RegistrationEventRequest("John", "Doe", personalCode);
  }

}
