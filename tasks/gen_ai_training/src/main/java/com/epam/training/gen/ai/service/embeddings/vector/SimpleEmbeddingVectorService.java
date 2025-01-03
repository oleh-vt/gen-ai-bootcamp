package com.epam.training.gen.ai.service.embeddings.vector;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleEmbeddingVectorService implements EmbeddingVectorService {

    private final String modelName;
    private final OpenAIAsyncClient openAIAsyncClient;

    public SimpleEmbeddingVectorService(@Value("${openai-embeddings-deployment.name}") String modelName,
                                        OpenAIAsyncClient openAIAsyncClient) {
        this.modelName = modelName;
        this.openAIAsyncClient = openAIAsyncClient;
    }

    @Override
    public Embeddings create(String text) {
        return openAIAsyncClient.getEmbeddings(modelName, new EmbeddingsOptions(List.of(text))).block();
    }
}
