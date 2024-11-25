package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.PromptSettings;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
    public String reply(String prompt, PromptSettings promptSettings) {
        chatHistory.addUserMessage(prompt);
        return extractResponse(executeChatRequest(promptSettings));
    }

    private List<ChatMessageContent<?>> executeChatRequest(PromptSettings promptSettings) {
        InvocationContext invocationContext = InvocationContext.builder()
                .withPromptExecutionSettings(
                        PromptExecutionSettings.builder()
                                .withTemperature(BigDecimal.valueOf(promptSettings.temperature().longValue()).di)
                                .build()
                )
                .build();

        return Optional.ofNullable(
                chatCompletionService
                        .getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
                        .block()
        ).orElseGet(Collections::emptyList);
    }

    private String extractResponse(Collection<ChatMessageContent<?>> responseContent) {
        return responseContent.stream()
                .filter(r -> r.getAuthorRole() == AuthorRole.ASSISTANT)
                .map(ChatMessageContent::getContent)
                .filter(Objects::nonNull)
                .findFirst().orElseThrow(() -> new RuntimeException("Unexpected chat response"));
    }
}

