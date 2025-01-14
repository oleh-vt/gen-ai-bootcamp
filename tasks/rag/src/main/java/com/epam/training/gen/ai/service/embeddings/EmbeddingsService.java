package com.epam.training.gen.ai.service.embeddings;

import java.util.List;

public interface EmbeddingsService {
    void create(String text);
    List<String> search(String text);
}