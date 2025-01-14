package com.epam.training.gen.ai.service.chat;

import com.epam.training.gen.ai.dto.Reply;
import com.epam.training.gen.ai.service.embeddings.EmbeddingsService;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final EmbeddingsService embeddingsService;
    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;

    private ChatHistory chatHistory;

    private static final String PROMPT_TEMPLATE = """
            Respond to the user's input based on the provided context.
            Context:
            %s
            
            Input:
            %s""";

    @Override
    public Reply reply(String ask) {
        List<String> embeddingSearchResults = embeddingsService.search(ask);
        String prompt = createPrompt(ask, embeddingSearchResults);
        log.debug(prompt);
        chatHistory.addUserMessage(prompt);
        String response = extractResponse(executeChatRequest());
        return new Reply(ask, response);
    }

    @Autowired
    public void setChatHistory(ChatHistory chatHistory) {
        this.chatHistory = chatHistory;
    }

    private String createPrompt(String question, List<String> searchResults) {
        if (searchResults.isEmpty()) {
            return question;
        }
        return PROMPT_TEMPLATE.formatted(formContext(searchResults), question);
    }

    private String formContext(List<String> searchResults) {
        var template = "\n\"%s\"\n";
        var sb = new StringBuilder();
        for (String searchResult : searchResults) {
            sb.append(template.formatted(searchResult));
        }
        return sb.toString();
    }

    private List<ChatMessageContent<?>> executeChatRequest() {
        return chatCompletionService
                .getChatMessageContentsAsync(chatHistory, kernel, buildInvocationContext())
                .blockOptional()
                .orElseGet(Collections::emptyList);
    }

    private InvocationContext buildInvocationContext() {
        return InvocationContext.builder()
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withPromptExecutionSettings(
                        PromptExecutionSettings.builder().build()
                ).build();
    }

    private String extractResponse(Collection<ChatMessageContent<?>> responseContent) {
        return responseContent.stream()
                .map(ChatMessageContent::getContent)
                .collect(Collectors.joining());
    }
}
