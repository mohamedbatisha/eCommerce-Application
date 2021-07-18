package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.LoginRequest;
import com.example.demo.security.JWTAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    private UserController userController;
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final CartRepository cartRepository = Mockito.mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);

    private JWTAuthenticationFilter jwtAuthenticationFilter;
    private AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);


    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
        jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager);

    }


    @Test
    public void testLoginWithValidCredentials () throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        CreateUserRequest  validUser= new CreateUserRequest();
        validUser.setUsername("testuser");
        validUser.setPassword("testPassword");
        validUser.setConfirmPassword("testPassword");

        this.mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().is2xxSuccessful());

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("testPassword");
        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void testLoginWithInvalidCredentials () throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testPassword");

        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }
}
