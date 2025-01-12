package com.epam.training.gen.ai.service.embeddings;

import com.epam.training.gen.ai.model.EmbeddingItem;

import java.util.List;

public interface EmbeddingGenerationService {
    List<EmbeddingItem> create(String text);
}
