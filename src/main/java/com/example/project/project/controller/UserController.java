package com.example.project.project.controller;

import com.example.project.project.exception.ExistingUsernameException;
import com.example.project.project.model.User;
import com.example.project.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("project/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/sum")
    public ResponseEntity<Integer> calculateSum(@RequestParam(name = "value1") final int value1, @RequestParam(name = "value2") final int value2 ){
        return new ResponseEntity(userService.sum(value1, value2), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@Valid @RequestBody final User user) throws ExistingUsernameException {
        return new ResponseEntity(userService.createNewAccount(user), HttpStatus.CREATED);
    }
}