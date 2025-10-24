package com.netgroup.event_registration_backend.mapper;

import com.netgroup.event_registration_backend.domain.Event;
import com.netgroup.event_registration_backend.dto.event.EventRequest;

public class EventMapper {
    public static Event toEntity(EventRequest dto) {
        return Event.builder()
                .name(dto.name())
                .eventTime(dto.eventTime())
                .maxPeople(dto.maxPeople())
                .build();
    }
}
