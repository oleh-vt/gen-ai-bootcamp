package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    public static final String API_KEY = "Api-Key";

    @Bean
    OpenAIChatCompletion.Builder chatCompletionService(@Value("${client-openai-endpoint}") String clientEndpoint,
                                                       @Value("${client-openai-key}") String clientKey) {
        return OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(
                        new OpenAIClientBuilder()
                                .endpoint(clientEndpoint)
                                .credential(new AzureKeyCredential(clientKey))
                                .buildAsyncClient()
                );
    }

    @Bean
    RestClient restClient(
            @Value("${client-openai-endpoint}") String clientEndpoint,
            @Value("${client-openai-key}") String clientKey
    ) {
        return RestClient.builder()
                .baseUrl(clientEndpoint)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(API_KEY, clientKey);
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                })
                .build();
    }

}
