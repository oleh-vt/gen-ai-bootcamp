package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.exception.ConversationNotFoundException;
import com.epam.training.gen.ai.exception.GenAiServiceException;
import com.epam.training.gen.ai.model.Deployment;
import com.epam.training.gen.ai.model.Prompt;
import com.epam.training.gen.ai.service.dial.DialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConcurrentMap<String, Conversation> conversations = new ConcurrentHashMap<>();
    private final ConversationFactory conversationFactory;
    private final DialService dialService;

    @Override
    public Conversation get(String id) {
        return Optional.ofNullable(conversations.get(id))
                .orElseThrow(() -> new ConversationNotFoundException(id));
    }

    @Override
    public Collection<Conversation> getAll() {
        return conversations.values();
    }

    @Override
    public Conversation create(Deployment deployment) {
        String model = deployment.model();
        if (!dialService.exists(model)) {
            throw new GenAiServiceException("The model is not valid: " + model);
        }
        Conversation conversation = conversationFactory.create(model);
        String id = conversation.getId();
        conversations.put(id, conversation);
        return conversation;
    }

    @Override
    public String reply(String conversationId, Prompt prompt) {
        return get(conversationId).reply(prompt);
    }

    @Override
    public void delete(String conversationId) {
        Conversation removed = conversations.remove(conversationId);
        if (removed == null) {
            throw new ConversationNotFoundException(conversationId);
        }
    }
}
