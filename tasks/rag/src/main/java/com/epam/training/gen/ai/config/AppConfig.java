package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    QdrantClient qdrantClient(QdrantConnectionProperties configProperties) {
        return new QdrantClient(
                QdrantGrpcClient.newBuilder(
                        configProperties.host(),
                        configProperties.port(),
                        configProperties.useTLS()
                ).build()
        );
    }

}
