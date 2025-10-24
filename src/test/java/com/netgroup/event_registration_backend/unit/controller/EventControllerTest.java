package com.netgroup.event_registration_backend.unit.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.netgroup.event_registration_backend.controller.EventController;
import com.netgroup.event_registration_backend.service.EventService;
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

  @Test
  void create_whenAllFieldsFilled_shouldReturnCreated() throws Exception {
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

  @Test
  void create_whenNameisBlank_shouldReturnBadRequest() throws Exception {
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
        .andExpect(status().isBadRequest());
  }

  @Test
  void create_whenEventTimeIsPast_shouldReturnBadRequest() throws Exception {
    String json = """
        {
            "name": "",
            "eventTime": "2020-01-01T10:00:00Z",
            "maxPeople": "1"
        }
        """;

    mockMvc.perform(post("/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void create_whenEventMaxPeopleIsNegative_shouldReturnBadRequest() throws Exception {
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
        .andExpect(status().isBadRequest());
  }


}
