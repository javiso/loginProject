package com.example.project.project.handler;

import com.example.project.project.exception.ErrorApi;
import com.example.project.project.exception.ExistingUsernameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request){
        final List<String> validationsErrors = exception.getBindingResult().getFieldErrors().stream().map(
                violation -> violation.getField() + ":" + violation.getDefaultMessage()).collect(Collectors.toList());
        log.error("Excpetion: {}: ", exception);
        ErrorApi apiError = new ErrorApi(HttpStatus.BAD_REQUEST, "Parameters Not valid",validationsErrors);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ExistingUsernameException.class)
    public ResponseEntity<Object> handleMethodExistingUsername(final ExistingUsernameException existingUsernameException){
        log.error("Exception: {} ", existingUsernameException);
        ErrorApi apiError = new ErrorApi(HttpStatus.BAD_REQUEST, existingUsernameException.getMessage(), new ArrayList<>());

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}