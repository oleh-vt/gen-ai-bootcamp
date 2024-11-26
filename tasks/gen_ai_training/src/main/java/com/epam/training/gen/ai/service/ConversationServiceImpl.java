package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;
    private final ChatHistory chatHistory;

    public ConversationServiceImpl(ChatCompletionService chatCompletionService,
                                   Kernel kernel,
                                   ChatHistory chatHistory) {
        this.chatCompletionService = chatCompletionService;
        this.kernel = kernel;
        this.chatHistory = chatHistory;
    }

    @Override
    public String reply(String prompt) {
        return reply(prompt, null);
    }

    @Override
    public String reply(String prompt, Integer temperature) {
        chatHistory.addUserMessage(prompt);
        String chatResponse = extractResponse(executeChatRequest(temperature));
        chatHistory.addAssistantMessage(chatResponse);
        return chatResponse;
    }

    private List<ChatMessageContent<?>> executeChatRequest(Integer temperature) {
        return Optional.ofNullable(
                chatCompletionService
                        .getChatMessageContentsAsync(chatHistory, kernel, buildInvocationContext(temperature))
                        .block()
        ).orElseGet(Collections::emptyList);
    }

    private InvocationContext buildInvocationContext(Integer temperature) {
        InvocationContext.Builder invocationContextbuilder = InvocationContext.builder();
        if (temperature != null) {
            invocationContextbuilder.withPromptExecutionSettings(
                    PromptExecutionSettings.builder()
                            .withTemperature(temperature.doubleValue() / 100)
                            .build()
            );
        }
        return invocationContextbuilder.build();
    }

    private String extractResponse(Collection<ChatMessageContent<?>> responseContent) {
        return responseContent.stream()
                .map(ChatMessageContent::getContent)
                .collect(Collectors.joining());
    }
}

