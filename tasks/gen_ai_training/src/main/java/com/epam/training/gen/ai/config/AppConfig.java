package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class AppConfig {

    @Bean
    ChatCompletionService chatCompletionService(@Value("${client-openai-endpoint}") String clientEndpoint,
                                                @Value("${client-openai-key}") String clientKey,
                                                @Value("${client-openai-deployment-name}") String deploymentName) {
        return OpenAIChatCompletion.builder()
                .withModelId(deploymentName)
                .withOpenAIAsyncClient(
                        new OpenAIClientBuilder()
                                .endpoint(clientEndpoint)
                                .credential(new AzureKeyCredential(clientKey))
                                .buildAsyncClient()
                )
                .build();
    }

    @Bean
    Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    @Bean
    @SessionScope
    ChatHistory chatHistory() {
        return new ChatHistory();
    }

    @Bean
    InvocationContext invocationContext() {
        return new InvocationContext.Builder()
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(Boolean.TRUE))
                .build();
    }

    @Bean
    RestClient restClient(
            @Value("${client-openai-endpoint}") String clientEndpoint,
            @Value("${client-openai-key}") String clientKey
    ) {
        return RestClient.builder()
                .baseUrl(clientEndpoint)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add("Api-Key", clientKey);
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                })
                .build();
    }

}
