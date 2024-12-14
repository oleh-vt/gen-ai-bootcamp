package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.Prompt;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Conversation {

    @Getter
    private final String id;
    @Getter
    private final String model;

    private final Kernel kernel;
    private final ChatHistory chatHistory;
    private final ChatCompletionService chatCompletionService;

    public String reply(Prompt prompt) {
        chatHistory.addUserMessage(prompt.input());
        String chatResponse = extractResponse(executeChatRequest(prompt.temperature()));
        chatHistory.addAssistantMessage(chatResponse);
        return chatResponse;
    }

    private List<ChatMessageContent<?>> executeChatRequest(Double temperature) {
        return chatCompletionService
                .getChatMessageContentsAsync(chatHistory, kernel, buildInvocationContext(temperature))
                .blockOptional()
                .orElseGet(Collections::emptyList);
    }

    private InvocationContext buildInvocationContext(Double temperature) {
        InvocationContext.Builder invocationContextbuilder = InvocationContext.builder()
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(Boolean.TRUE));
        if (temperature != null) {
            invocationContextbuilder.withPromptExecutionSettings(
                    PromptExecutionSettings.builder()
                            .withTemperature(temperature)
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
