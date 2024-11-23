package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    ChatCompletionService chatCompletionService(
            @Value("${client-openai-endpoint}") String clientEndpoint,
            @Value("${client-openai-key}") String clientKey,
            @Value("${client-openai-deployment-name}") String deploymentName

    ) {
        return OpenAIChatCompletion.builder()
                .withDeploymentName(deploymentName)
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
    InvocationContext invocationContext() {
        return new InvocationContext.Builder()
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(Boolean.TRUE))
                .build();
    }

}
