package com.epam.training.gen.ai.service.embeddings;

import com.azure.ai.openai.models.Embeddings;

public interface EmbeddingsService {
    Embeddings create(String text);
    Embeddings createAndSave(String text);
    Object search(Object o);
}