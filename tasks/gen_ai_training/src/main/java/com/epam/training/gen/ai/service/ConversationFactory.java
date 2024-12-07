package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConversationFactory {

    private final OpenAIChatCompletion.Builder chatCompletionBuilder;

    public ConversationFactory(OpenAIChatCompletion.Builder chatCompletionBuilder) {
        this.chatCompletionBuilder = chatCompletionBuilder;
    }

    public Conversation create(String deploymentName) {
        ChatCompletionService chatCompletionService = chatCompletionBuilder
                .withModelId(deploymentName)
                .build();
        Kernel kernel = kernel(chatCompletionService);
        return new Conversation(
                UUID.randomUUID().toString(),
                deploymentName,
                kernel,
                new ChatHistory(),
                chatCompletionService
        );
    }

    private Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }
}
