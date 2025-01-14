package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.textembedding.TextEmbeddingGenerationService;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class AppConfig {

    public static final String API_KEY = "Api-Key";

    @Bean
    OpenAIAsyncClient openAIAsyncClient(@Value("${client-openai-endpoint}") String clientEndpoint,
                                        @Value("${client-openai-key}") String clientKey) {
        return new OpenAIClientBuilder()
                .endpoint(clientEndpoint)
                .credential(new AzureKeyCredential(clientKey))
                .buildAsyncClient();
    }

    @Bean
    TextEmbeddingGenerationService textEmbeddingGenerationService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAITextEmbeddingGenerationService.builder()
                .withOpenAIAsyncClient(openAIAsyncClient)
                .withModelId("text-embedding-3-small-1")
                .withDimensions(OpenAITextEmbeddingGenerationService.EMBEDDING_DIMENSIONS_SMALL)
                .build();
    }

    @Bean
    QdrantClient qdrantClient(QdrantConnectionProperties configProperties) {
        return new QdrantClient(
                QdrantGrpcClient.newBuilder(
                        configProperties.host(),
                        configProperties.port(),
                        configProperties.useTLS()
                ).build()
        );
    }

    @Bean
    @SessionScope
    ChatHistory chatHistory() {
        return new ChatHistory();
    }

    @Bean
    ChatCompletionService chatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(openAIAsyncClient)
                .withModelId("gpt-35-turbo")
                .build();
    }

    @Bean
    Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

}
