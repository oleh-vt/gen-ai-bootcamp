package com.epam.training.gen.ai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(GenAiServiceClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleServiceClientException(GenAiServiceClientException ex) {
        return createResponse(ex.getMessage());
    }

    @ExceptionHandler(GenAiServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleConversationNotFound(GenAiServiceException ex) {
        return createResponse(ex.getMessage());
    }

    private static Map<String, String> createResponse(String ex) {
        return Map.of("error", ex);
    }

}
