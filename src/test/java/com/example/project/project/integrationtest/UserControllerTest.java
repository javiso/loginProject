package com.example.project.project.integrationtest;

import com.example.project.project.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integration test suite for user request and their operations")

public class UserControllerTest {

    private static String URL = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void creationUserSuccessfulResponseTest(){
        String url = URL + port + "/project/v1/user/signup";
        User userTest = new User("patodonald", "cuack!", "patodonald@gmail.com");

        ResponseEntity<User> response = restTemplate.postForEntity(url, userTest, User.class);
        User userResponse = response.getBody();

        assertNotNull(userResponse);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("patodonald", userResponse.getUsername());
        assertEquals("cuack!", userResponse.getPassword());
        assertEquals("patodonald@gmail.com", userResponse.getEmail());
    }

    @Test
    public void createUnsuccessfulUserTest() {
        String url = URL + port + "/project/v1/user/signup";
        User userTest = new User("patodonald", "cuack!", "patodonaldgmail.com");

        ResponseEntity<User> response = restTemplate.postForEntity(url, userTest, User.class);
        User userResponse = response.getBody();

        assertNotNull(userResponse);
        assertNull(userResponse.getUsername());
        assertNull(userResponse.getPassword());
        assertNull(userResponse.getEmail());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createUnsuccessfulUserDueToExistingUsernameTest() {
        String url = URL + port + "/project/v1/user/signup";
        User userTest = new User("patodonald", "cuack!", "patodonald@gmail.com");

        restTemplate.postForEntity(url, userTest, User.class);
        ResponseEntity<User> response =restTemplate.postForEntity(url, userTest, User.class);

        User userResponse = response.getBody();

        assertNotNull(userResponse);
        assertNull(userResponse.getUsername());
        assertNull(userResponse.getPassword());
        assertNull(userResponse.getEmail());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}