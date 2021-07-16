package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final CartRepository cartRepository = Mockito.mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock((BCryptPasswordEncoder.class));

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        User expectedUser = new User();
        expectedUser.setUsername("Mohamed");
        expectedUser.setPassword("Model88");

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(expectedUser);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(expectedUser));
    }

    @Test
    public void testCreateUser() {

        String username = "testuser";
        String password = "testPassword";

        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void testBadUserRequest() {

        String username = "user1";
        String password = "test1";

        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testFindByUsername() {

        String username = "user2";
        String password = "testPassword2";

        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(userResponseEntity.getBody());

        final ResponseEntity<User> response = userController.findByUserName(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User U = response.getBody();

        assertNotNull(U);
        assertEquals(userResponseEntity.getBody().getUsername(), U.getUsername());
        assertEquals(userResponseEntity.getBody().getPassword(), U.getPassword());
    }


    @Test
    public void testFindById() {
        String username = "user3";
        String password = "testPassword3";

        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);

        Mockito.when(userRepository.findById(0l)).thenReturn(Optional.of(userResponseEntity.getBody()));

        final ResponseEntity<User> response = userController.findById(0l);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(userResponseEntity.getBody().getId(), user.getId());
    }

}
