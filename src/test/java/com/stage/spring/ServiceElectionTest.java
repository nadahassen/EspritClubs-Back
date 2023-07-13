package com.stage.spring;

import com.stage.spring.entity.Election;

import com.stage.spring.entity.Image;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.stage.spring.service.ServiceElection;


@RunWith(SpringRunner.class)

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ServiceElectionTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceElection serviceElection;

    @Test
    public void testGetAllElections() throws Exception {
        // Prepare test data
        List<Election> elections = Arrays.asList(
                createElection(1L, "Election 1", new Date(), new Date(), "Participant 1", "Type 1"),
                createElection(2L, "Election 2", new Date(), new Date(), "Participant 2", "Type 2")
        );

        // Mock the service method
        Mockito.when(serviceElection.retrieveAllElections()).thenReturn(elections);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/Election/get-all-elections"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idElection").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomElection").value("Election 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idElection").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nomElection").value("Election 2"));

        // Verify the service method was called
        Mockito.verify(serviceElection).retrieveAllElections();
    }

    @Test
    public void testGetElectionById() throws Exception {
        System.out.println("Testing get Election by id");
        Long electionId = 1L;
        Election election = createElection(electionId, "Election 1", new Date(), new Date(), "Participant 1", "Type 1");

        Mockito.when(serviceElection.retrieveElection(electionId)).thenReturn(election);

        mockMvc.perform(MockMvcRequestBuilders.get("/Election/getElectionById/{id}", electionId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idElection").value(electionId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomElection").value("Election 1"));

        Mockito.verify(serviceElection).retrieveElection(electionId);
    }


    // Utility method to create an Election object
    private Election createElection(Long id, String nomElection, Date dateElection, Date dateFinElection, String NomsParticipants, String typeElection) {
        Election election = new Election();
        election.setIdElection(id);
        election.setNomElection(nomElection);
        election.setDateElection(dateElection);
        election.setDateFinElection(dateFinElection);
        election.setNomsParticipants(NomsParticipants);
        election.setTypeElection(typeElection);
        return election;
    }

}
