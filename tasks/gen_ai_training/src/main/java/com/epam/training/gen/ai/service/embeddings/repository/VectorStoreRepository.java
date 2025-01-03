package com.epam.training.gen.ai.service.embeddings.repository;

import com.azure.ai.openai.models.Embeddings;

public interface VectorStoreRepository {
    void save(String text, Embeddings embeddings);
    Object find();
}
