package com.epam.training.gen.ai.repository;

import com.epam.training.gen.ai.model.EmbeddingItem;

import java.util.List;

public interface VectorStoreRepository {
    void save(List<EmbeddingItem> embeddingItems);
}
