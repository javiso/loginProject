package com.example.project.project.integrationtest;

import com.example.project.project.cache.CacheToken;
import com.example.project.project.model.User;
import com.example.project.project.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;
;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integration test suite for login and logout processes")
public class AccountTest {

    private static String URL = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheToken cacheToken;

    @BeforeAll
    public void init(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = new User("patolucas",bCryptPasswordEncoder.encode("quack!"),"patolucas@gmail.com");
        userRepository.save(user);
    }

    @AfterEach
    public void empty(){
        cacheToken.cleanCache();
    }

    @Test
    public void successfulLoginTest(){
        HttpEntity entity = new HttpEntity<>(new HttpHeaders());
        ParameterizedTypeReference<Map<String, String>> parameterizedTypeReference = new ParameterizedTypeReference<>() {};
        String url = URL + port + "/project/api/login";
        String uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("username","patolucas")
                .queryParam("password","quack!")
                .encode()
                .toUriString();

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(uriComponentsBuilder, HttpMethod.POST, entity, parameterizedTypeReference);

        assertNotNull(response);
        String token = response.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        assertNotNull(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void unsuccesssfulLoginTest(){
        HttpEntity entity = new HttpEntity<>(new HttpHeaders());
        String url = URL + port + "/project/api/login";
        String uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("username","jorge")
                .queryParam("password","quack!")
                .encode()
                .toUriString();

        ResponseEntity<Object> response = restTemplate.exchange(uriComponentsBuilder, HttpMethod.POST, entity, Object.class);
        HashMap<String, String> body = (HashMap<String, String>) response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password",body.get("message"));
    }

    @Test
    public void successfulSum(){
        ParameterizedTypeReference<Map<String, String>> parameterizedTypeReference = new ParameterizedTypeReference<>() {};
        String url = URL + port + "/project/api/login";
        String uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("username","patolucas")
                .queryParam("password","quack!")
                .encode()
                .toUriString();

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(uriComponentsBuilder, HttpMethod.POST, new HttpEntity<>(new HttpHeaders()), parameterizedTypeReference);
        String token = response.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity("body", httpHeaders);

        url = URL + port + "/project/v1/user/sum";
        uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("value1",7)
                .queryParam("value2",3)
                .encode()
                .toUriString();

        ResponseEntity<Object> responseFinal = restTemplate.exchange(uriComponentsBuilder, HttpMethod.GET, entity, Object.class);
        assertEquals(10,responseFinal.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void unsuccessfulSum(){
        HttpEntity entity = new HttpEntity<>(new HttpHeaders());
        String url = URL + port + "/project/v1/user/sum";
        String uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("value1",7)
                .queryParam("value2",3)
                .encode()
                .toUriString();

        ResponseEntity<Object> response = restTemplate.exchange(uriComponentsBuilder, HttpMethod.POST, entity, Object.class);
        HashMap<String, String> body = (HashMap<String, String>) response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Denied Access.",body.get("message"));
    }

    @Test
    public void successLogout(){
        ParameterizedTypeReference<Map<String, String>> parameterizedTypeReference = new ParameterizedTypeReference<>() {};
        String url = URL + port + "/project/api/login";
        String uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("username","patolucas")
                .queryParam("password","quack!")
                .encode()
                .toUriString();

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(uriComponentsBuilder, HttpMethod.POST, new HttpEntity<>(new HttpHeaders()), parameterizedTypeReference);
        String token = response.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        url = URL + port + "/project/api/logout";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity("body", httpHeaders);

        ResponseEntity responseLogout = restTemplate.exchange(url, HttpMethod.POST, entity, ResponseEntity.class);
        assertNotNull(responseLogout);
        assertEquals(HttpStatus.OK,responseLogout.getStatusCode());
    }
}