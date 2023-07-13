package com.stage.spring;
import com.stage.spring.JwtAndAuthConf.LoginRequest;
import com.stage.spring.JwtAndAuthConf.UserDetailsImpl;
import com.stage.spring.controllers.UserRestController;
import com.stage.spring.entity.User;
import com.stage.spring.service.ServiceUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ServiceUserTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationManager authenticationManager;
    @MockBean
    private ServiceUser serviceUser;
    @InjectMocks
    private UserRestController userRestController;



    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void testRetrieveAllUsers() throws Exception {

        User user1 = new User();
        user1.setIdUser(1L);
        user1.setUserName("user1");
        User user2 = new User();
        user2.setIdUser(2L);
        user2.setUserName("user2");
        List<User> users = Arrays.asList(user1, user2);

        Mockito.when(serviceUser.getusers()).thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders.get("/User/getusers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idUser").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idUser").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userName").value("user2"));
    }


    @Test
    public void testDeleteUser() throws Exception {
        Long idUser = 28L;

        mockMvc.perform(MockMvcRequestBuilders.put("/User/deleteuser/{iduser}", idUser))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(serviceUser).deleteUser(idUser);

    }

  /*  @Test
    public void testAuthenticateUser() throws Exception {
        // Prepare test data
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpassword");

        // Mock the authentication manager
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);

        // Create an instance of your controller
        UserRestController userRestController = new UserRestController();

        // Set the authentication manager using the setter method
        userRestController.setAuthenticationManager(authenticationManager);

        // Perform the POST request
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userRestController).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/User/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idUser").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("testuser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("testuser@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").value(Arrays.asList("ROLE_ADMIN", "ROLE_RESPONSABLE")));

        // You can add additional assertions or verifications as needed

        // Verify that the authentication manager was called with the correct arguments
        Mockito.verify(authenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
    }*/


}
