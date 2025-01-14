package com.epam.training.gen.ai.service.embeddings;

import com.microsoft.semantickernel.services.textembedding.Embedding;
import reactor.core.publisher.Mono;

public interface EmbeddingGenerationService {
    Mono<Embedding> generateEmbedding(String text);
}
