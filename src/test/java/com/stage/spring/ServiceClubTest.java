package com.stage.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.spring.controllers.ClubRestController;
import com.stage.spring.controllers.VoteRestController;
import com.stage.spring.entity.*;
import com.stage.spring.repository.PostulerVoteRepository;
import com.stage.spring.service.ServiceClub;
import com.stage.spring.service.ServiceMail;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.io.IOException;
import java.util.*;

import com.stage.spring.service.ServiceElection;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;


@RunWith(SpringRunner.class)

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ServiceClubTest {
    private MockMvc mockMvc;

    @Mock
    private ServiceClub serviceClub;

    @Mock
    private ServiceMail serviceMail;

    @InjectMocks
    private ClubRestController clubRestController;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(clubRestController).build();
    }
    @Test
    public void testParticiperClub() throws Exception {
        Club club = createClub(1L, "Club 1");

        // Mock the service method to return the updated club
        Mockito.when(serviceClub.participerClub(Mockito.anyLong(), Mockito.anyLong())).thenReturn(club);

        mockMvc.perform(MockMvcRequestBuilders.put("/Club/participerclub/1/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idClub").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomClub").value("Club 1"));

        Mockito.verify(serviceClub).participerClub(1L, 1L);
    }


    @Test
    public void testAnnulerParticipation() throws Exception {
        Mockito.when(serviceClub.annulerParticipation(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/Club/annulerparticipation/1/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));

        Mockito.verify(serviceClub).annulerParticipation(1L, 1L);
    }

    @Test
    public void testRetrieveClubParticipants() throws Exception {
        List<User> participants = Arrays.asList(
                createUser(1L, "User 1"),
                createUser(2L, "User 2")
        );

        Mockito.when(serviceClub.retrieveClubParticipants(Mockito.anyLong())).thenReturn(participants);

        mockMvc.perform(MockMvcRequestBuilders.get("/Club/retrieve-participants-club/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idUser").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName").value("User 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idUser").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userName").value("User 2"));

        Mockito.verify(serviceClub).retrieveClubParticipants(1L);
    }
    private Club createClub(Long id, String nomClub) {
        Club club = new Club();
        club.setIdClub(id);
        club.setNomClub(nomClub);
        return club;
    }

    private User createUser(Long idUser, String userName) {
        User user = new User();
        user.setIdUser(idUser);
        user.setUserName(userName);
        return user;
    }

    private File createFile(Long id, String name) {
        File file = new File();
        file.setIdFile(id);

        return file;
    }

    private String asJsonString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private MultipartFile createMultipartFile(String fieldName, String originalFilename) throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile(fieldName, originalFilename, MediaType.TEXT_PLAIN_VALUE, "test-file".getBytes());
        return multipartFile;
    }
}
