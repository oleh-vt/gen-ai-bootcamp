package com.epam.training.gen.ai.exception;

public class GenAiServiceException extends RuntimeException {
  public GenAiServiceException() {
  }
  public GenAiServiceException(String message) {
    super(message);
  }
}
