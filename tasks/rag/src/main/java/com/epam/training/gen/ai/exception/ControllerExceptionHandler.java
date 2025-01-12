package com.epam.training.gen.ai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(GenAiServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleConversationNotFound(GenAiServiceException ex) {
        return Map.of("error", ex.getMessage());
    }

}
