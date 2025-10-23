package com.netgroup.event_registration_backend.repository;

import com.netgroup.event_registration_backend.domain.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

  boolean existsByEventIdAndPersonId(Long eventId, Long personId);

  int countByEventId(Long eventId);
}
