package com.epam.training.gen.ai.service.embeddings.vector;

import com.azure.ai.openai.models.Embeddings;

public interface EmbeddingVectorService {
    Embeddings create(String text);
}
