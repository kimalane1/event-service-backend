package com.netgroup.event_registration_backend.dto.event;

import java.time.ZonedDateTime;

public record EventResponse(
    Long id,
    String name,
    ZonedDateTime eventTime,
    Integer maxPeople
) {

}