package com.netgroup.event_registration_backend.repository;

import com.netgroup.event_registration_backend.domain.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

  Optional<Person> findByPersonalCode(String personalCode);
}
