package com.epam.training.gen.ai.service.embeddings;


import com.epam.training.gen.ai.model.EmbeddingItem;
import com.epam.training.gen.ai.repository.VectorStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingsServiceImpl implements EmbeddingsService {

    private final EmbeddingGenerationService embeddingGenerationService;
    private final VectorStoreRepository vectorStoreRepository;

    @Override
    public void create(String text) {
        List<EmbeddingItem> embeddingItems = embeddingGenerationService.create(text);
        vectorStoreRepository.save(embeddingItems);
    }
}
