package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.plugin.CurrencyExchangePlugin;
import com.epam.training.gen.ai.plugin.ExchangeRate;
import com.epam.training.gen.ai.plugin.Rate;
import com.google.gson.Gson;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

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
    OpenAIChatCompletion.Builder chatCompletionService(OpenAIAsyncClient aiAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(aiAsyncClient);
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

    @Bean
    CurrencyExchangePlugin currencyExchangePlugin(@Value("classpath:data/rates.json") Resource ratesJson) throws IOException {
        try (var reader = new InputStreamReader(ratesJson.getInputStream())) {
            Gson gon = new Gson();
            Map<String, Rate> rates = gon.fromJson(reader, ExchangeRate.class).rates().stream()
                    .collect(Collectors.toMap(
                            r -> r.currency().getCurrencyCode(),
                            r -> r)
                    );
            return new CurrencyExchangePlugin(rates);
        }
    }

    @Bean
    QdrantClient qdrantClient(QdrantConfigProperties configProperties) {
        return new QdrantClient(
                QdrantGrpcClient.newBuilder(
                        configProperties.host(),
                        configProperties.port(),
                        configProperties.useTLS()
                ).build()
        );
    }

}
