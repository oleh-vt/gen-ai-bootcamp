package com.epam.training.gen.ai.exception;

public class ConversationNotFoundException extends GenAiServiceException {

    private String conversationId;

    public ConversationNotFoundException(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String getMessage() {
        return "The conversation is not found: id=" + conversationId;
    }
}
