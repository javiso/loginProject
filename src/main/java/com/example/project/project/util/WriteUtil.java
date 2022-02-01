package com.example.project.project.util;

import com.example.project.project.exception.ErrorApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class WriteUtil {

    public static void writeErrorResponse(final HttpServletResponse response, final String message, final HttpStatus httpStatus) {
        log.error("Error: {}", message);
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        ErrorApi apiError = new ErrorApi(httpStatus, message);

        try (PrintWriter writer = response.getWriter()) {
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());

            String json = mapper.writeValueAsString(apiError);
            writer.write(json);
            writer.flush();
        } catch (IOException ie) {
            log.error("Problem writing output to response!", ie);
        }
    }

    public static void writeBodyResponse(final String token, final HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("access_token", token);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), tokenMap);
    }
}