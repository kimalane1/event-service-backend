package com.netgroup.event_registration_backend.integration.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.netgroup.event_registration_backend.integration.BaseIntegrationTest;
import com.netgroup.event_registration_backend.integration.fixtures.PersonFixture;
import com.netgroup.event_registration_backend.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class PersonRepositoryIT extends BaseIntegrationTest {

  @Autowired
  PersonRepository personRepository;

  @Test
  void shouldFindPersonByPersonalCode() {
    var personalCode = "49303061234";
    var createdPerson = PersonFixture.withCode(personalCode);
    personRepository.save(createdPerson);

    var foundPerson = personRepository.findByPersonalCode(personalCode);
    var person = foundPerson.orElseThrow();
    assertEquals(personalCode, person.getPersonalCode());
    assertEquals(createdPerson.getFirstName(), person.getFirstName());
  }

  @Test
  void shouldReturnEmptyWhenNoPersonIsFound() {
    var foundPerson = personRepository.findByPersonalCode("NOPE");
    assertTrue(foundPerson.isEmpty());
  }

  @Test
  void shouldThrowDataIntegrityViolationExceptionWhenPersonAlreadyExists() {
    var personalCode = "49303061234";
    var createdPerson = PersonFixture.withCode(personalCode);
    personRepository.save(createdPerson);

    var duplicate = PersonFixture.withCode(personalCode);
    assertThrows(
        DataIntegrityViolationException.class,
        () -> personRepository.saveAndFlush(duplicate)
    );
  }
}
