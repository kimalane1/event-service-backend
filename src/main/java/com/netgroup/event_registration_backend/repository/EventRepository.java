package com.netgroup.event_registration_backend.repository;

import com.netgroup.event_registration_backend.domain.Event;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  boolean existsByNameAndEventTime(String name, ZonedDateTime eventTime);
}
