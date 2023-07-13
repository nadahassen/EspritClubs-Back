package com.stage.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.spring.controllers.EventRestController;
import com.stage.spring.controllers.VoteRestController;
import com.stage.spring.entity.Election;
import com.stage.spring.entity.Event;
import com.stage.spring.entity.PostulerVote;
import com.stage.spring.entity.Vote;
import com.stage.spring.repository.PostulerVoteRepository;
import com.stage.spring.service.ServiceEvent;
import com.stage.spring.service.ServiceVote;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.*;

import com.stage.spring.service.ServiceElection;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;


@RunWith(SpringRunner.class)

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ServiceEventTest {

    private MockMvc mockMvc;

    @Mock
    private ServiceEvent serviceEvent;

    @InjectMocks
    private EventRestController eventRestController;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventRestController).build();
    }

    @Test
    public void testGetAllEvents() throws Exception {
        List<Event> events = Arrays.asList(
                createEvent(1L, "Event 1"),
                createEvent(2L, "Event 2")
        );

        Mockito.when(serviceEvent.getAllEvents()).thenReturn(events);

        mockMvc.perform(MockMvcRequestBuilders.get("/Event/getAllEvents"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idEvent").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].titre").value("Event 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idEvent").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].titre").value("Event 2"));

        Mockito.verify(serviceEvent).getAllEvents();
    }

    @Test
    public void testGetEventById() throws Exception {
        Event event = createEvent(1L, "Event 1");

        Mockito.when(serviceEvent.getEventById(Mockito.anyLong())).thenReturn(event);

        mockMvc.perform(MockMvcRequestBuilders.get("/Event/getEvent/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idEvent").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titre").value("Event 1"));

        Mockito.verify(serviceEvent).getEventById(Mockito.anyLong());
    }





    @Test
    public void testDeleteEvent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/Event/deleteEvent/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(serviceEvent).deleteEvent(Mockito.anyLong());
    }

    @Test
    public void testGetEventsByClubId() throws Exception {
        List<Event> events = Arrays.asList(
                createEvent(1L, "Event 1"),
                createEvent(2L, "Event 2")
        );

        Mockito.when(serviceEvent.getEventsByClubId(Mockito.anyLong())).thenReturn(events);

        mockMvc.perform(MockMvcRequestBuilders.get("/Event/getEventsByClubId/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idEvent").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].titre").value("Event 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idEvent").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].titre").value("Event 2"));

        Mockito.verify(serviceEvent).getEventsByClubId(Mockito.anyLong());
    }

    private Event createEvent(Long id, String titre) {
        Event event = new Event();
        event.setIdEvent(id);
        event.setTitre(titre);
        return event;
    }

    private String asJsonString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
