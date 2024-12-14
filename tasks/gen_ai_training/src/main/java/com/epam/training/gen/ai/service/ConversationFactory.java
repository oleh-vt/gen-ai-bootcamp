package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.plugin.CurrencyExchangePlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConversationFactory {

    private final OpenAIChatCompletion.Builder chatCompletionBuilder;
    private final CurrencyExchangePlugin currencyExchangePlugin;

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
                .withPlugin(KernelPluginFactory.createFromObject(currencyExchangePlugin, CurrencyExchangePlugin.class.getSimpleName()))
                .build();
    }
}
