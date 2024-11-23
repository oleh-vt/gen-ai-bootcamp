package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;
    private final InvocationContext invocationContext;

    public ConversationServiceImpl(ChatCompletionService chatCompletionService,
                                   Kernel kernel,
                                   InvocationContext invocationContext) {
        this.chatCompletionService = chatCompletionService;
        this.kernel = kernel;
        this.invocationContext = invocationContext;
    }

    @Override
    public String reply(String prompt) {
        ChatHistory history = new ChatHistory() {{
            addUserMessage(prompt);
        }};
        return extractResponse(executeChatRequest(history));
    }

    private List<ChatMessageContent<?>> executeChatRequest(ChatHistory history) {
        return Optional.ofNullable(
                chatCompletionService
                        .getChatMessageContentsAsync(history, kernel, invocationContext)
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

