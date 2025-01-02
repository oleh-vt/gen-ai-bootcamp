package com.epam.training.gen.ai.service.embeddings;

import com.azure.ai.openai.models.Embeddings;

public interface EmbeddingService {
    Embeddings create(String text);
}
