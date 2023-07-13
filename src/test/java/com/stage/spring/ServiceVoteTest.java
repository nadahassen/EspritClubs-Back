package com.stage.spring;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.spring.controllers.VoteRestController;
import com.stage.spring.entity.Election;
import com.stage.spring.entity.PostulerVote;
import com.stage.spring.entity.Vote;
import com.stage.spring.repository.PostulerVoteRepository;
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


@RunWith(SpringRunner.class)

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ServiceVoteTest {
    @InjectMocks
    private VoteRestController voteRestController;

    @Mock
    private ServiceVote serviceVote;

    @Mock
    private PostulerVoteRepository postulerVoteRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(voteRestController).build();
    }

    @Test
    public void testGetAllVotes() throws Exception {
        List<Vote> votes = Arrays.asList(
                createVote(1L, "Vote 1", "Candidate 1"),
                createVote(2L, "Vote 2", "Candidate 2")
        );

        Mockito.when(serviceVote.retrieveAllVotes()).thenReturn(votes);

        mockMvc.perform(MockMvcRequestBuilders.get("/Vote/get-all-votes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idVote").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomElection").value("Vote 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomCandidat").value("Candidate 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idVote").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nomElection").value("Vote 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nomCandidat").value("Candidate 2"));

        Mockito.verify(serviceVote).retrieveAllVotes();
    }

    @Test
    public void testGetVoteById() throws Exception {
        Vote vote = createVote(1L, "Vote 1", "Candidate 1");

        Mockito.when(serviceVote.retrieveVote(Mockito.anyLong())).thenReturn(vote);

        mockMvc.perform(MockMvcRequestBuilders.get("/Vote/getVoteById/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idVote").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomElection").value("Vote 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomCandidat").value("Candidate 1"));

        Mockito.verify(serviceVote).retrieveVote(Mockito.anyLong());
    }



    @Test
    public void testUpdateVote() throws Exception {
        Vote vote = createVote(1L, "Updated Vote", "Updated Candidate");

        Mockito.when(serviceVote.checkIfExist(Mockito.anyLong())).thenReturn(true);
        Mockito.when(serviceVote.updateVote(Mockito.any(Vote.class))).thenReturn(vote);

        mockMvc.perform(MockMvcRequestBuilders.put("/Vote/updateVote/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vote)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idVote").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomElection").value("Updated Vote"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomCandidat").value("Updated Candidate"));

        Mockito.verify(serviceVote).checkIfExist(Mockito.anyLong());
        Mockito.verify(serviceVote).updateVote(Mockito.any(Vote.class));
    }

    @Test
    public void testDeleteVote() throws Exception {
        Mockito.when(serviceVote.checkIfExist(Mockito.anyLong())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/Vote/deleteVote/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(serviceVote).checkIfExist(Mockito.anyLong());
        Mockito.verify(serviceVote).deleteVote(Mockito.anyLong());
    }

    @Test
    public void testGetPagedVotes() throws Exception {
        List<Vote> votes = Arrays.asList(
                createVote(1L, "Vote 1", "Candidate 1"),
                createVote(2L, "Vote 2", "Candidate 2")
        );

        Page<Vote> votePage = new PageImpl<>(votes);

        Mockito.when(serviceVote.getVotesPaged(Mockito.any(Pageable.class))).thenReturn(votePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/Vote/paged"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].idVote").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nomElection").value("Vote 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nomCandidat").value("Candidate 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].idVote").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].nomElection").value("Vote 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].nomCandidat").value("Candidate 2"));

        Mockito.verify(serviceVote).getVotesPaged(Mockito.any(Pageable.class));
    }

    @Test
    public void testCountVote() throws Exception {
        // Prepare test data
        Long candidateId = 1L;

        // Mock the PostulerVote record
        PostulerVote postulerVote = new PostulerVote();
        postulerVote.setIdPsotuler(candidateId);
        postulerVote.setVoteCount(10);
        postulerVote.setElection(new Election());
        postulerVote.getElection().setPostulerVotes(Collections.singletonList(postulerVote));

        // Mock the postulerVoteRepository
        Mockito.when(postulerVoteRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(postulerVote));
        Mockito.when(postulerVoteRepository.save(Mockito.any(PostulerVote.class))).thenReturn(postulerVote);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/Vote/count")
                        .param("candidateId", String.valueOf(candidateId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.voteCount").value(11))
                .andExpect(MockMvcResultMatchers.jsonPath("$.percentage").value(100.0));

        // Verify the postulerVoteRepository method was called with the correct argument
        Mockito.verify(postulerVoteRepository).findById(Mockito.anyLong());
        Mockito.verify(postulerVoteRepository).save(Mockito.any(PostulerVote.class));
    }






    private Vote createVote(Long id, String nomElection, String nomCandidat) {
        Vote vote = new Vote();
        vote.setIdVote(id);
        vote.setNomElection(nomElection);
        vote.setNomCandidat(nomCandidat);
        return vote;
    }


    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
