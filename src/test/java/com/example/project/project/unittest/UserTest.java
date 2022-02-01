package com.example.project.project.unittest;

import com.example.project.project.exception.ExistingUsernameException;
import com.example.project.project.model.User;
import com.example.project.project.repository.UserRepository;
import com.example.project.project.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest {
    private static UserRepository userRepository;
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        userRepository = mock(UserRepository.class);
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void userDataTest(){
        User user = new User("",null,"patoLucas.com");

        List<String> messages = List.of("This field must not be null nor blank", "Email format not valid");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
        violations.stream().allMatch(violation -> messages.stream().anyMatch(message -> message == violation.getMessageTemplate()));
    }

    @Test
    public void checkExistingUserException(){
        User user = new User();
        UserService userService = new UserService(userRepository);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
        ExistingUsernameException exception = assertThrows(ExistingUsernameException.class, () -> userService.createNewAccount(user));
        assertTrue(exception.getMessage().contains("Existing username"));
    }
}