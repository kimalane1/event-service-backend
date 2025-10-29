package com.netgroup.event_registration_backend.integration.fixtures.domain;

import com.netgroup.event_registration_backend.domain.Person;

public class PersonFixture {

  public static Person person() {
    return Person.builder()
        .personalCode("12345678900")
        .firstName("John")
        .lastName("Doe")
        .build();
  }

  public static Person withCode(String code) {
    Person p = person();
    p.setPersonalCode(code);
    return p;
  }
}
