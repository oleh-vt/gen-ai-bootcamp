package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.Deployment;
import com.epam.training.gen.ai.model.Prompt;

import java.util.Collection;

public interface ConversationService {
    Conversation get(String id);
    Collection<Conversation> getAll();
    Conversation create(Deployment deployment);
    String reply(String conversationId, Prompt prompt);
    void delete(String conversationId);
}
