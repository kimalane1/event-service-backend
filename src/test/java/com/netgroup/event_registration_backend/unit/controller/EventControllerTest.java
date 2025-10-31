package com.netgroup.event_registration_backend.unit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.netgroup.event_registration_backend.controller.EventController;
import com.netgroup.event_registration_backend.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EventControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  EventService eventService;

  @DisplayName("Should return HTTP 201 Created when event is created successfully")
  @Test
  void create_whenAllFieldsFilled_returnsCreated() throws Exception {
    String json = """
        {
            "name": "Party",
            "eventTime": "2026-01-01T10:00:00Z",
            "maxPeople": "1"
        }
        """;

    mockMvc.perform(post("/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated());
  }

  @DisplayName("Should return HTTP 400 Bad Request when name is blank")
  @Test
  void create_whenNameIsBlank_returnsBadRequest() throws Exception {
    String json = """
        {
            "name": "",
            "eventTime": "2026-01-01T10:00:00Z",
            "maxPeople": "1"
        }
        """;

    mockMvc.perform(post("/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Name should not be blank")));
  }

  @DisplayName("Should return HTTP 400 Bad Request when event time is in the past")
  @Test
  void create_whenEventTimeIsPast_returnsBadRequest() throws Exception {
    String json = """
        {
            "name": "John",
            "eventTime": "2020-01-01T10:00:00Z",
            "maxPeople": "1"
        }
        """;

    mockMvc.perform(post("/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Event time should be in the future")));
  }

  @DisplayName("Should return HTTP 400 Bad Request when max people is negative")
  @Test
  void create_whenEventMaxPeopleIsNegative_returnsBadRequest() throws Exception {
    String json = """
        {
            "name": "John",
            "eventTime": "2020-01-01T10:00:00Z",
            "maxPeople": "-1"
        }
        """;

    mockMvc.perform(post("/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Amount of people should be positive")));
  }


}
