package com.epam.training.gen.ai.service.embeddings;

import com.epam.training.gen.ai.exception.GenAiServiceException;
import com.microsoft.semantickernel.services.textembedding.Embedding;
import com.microsoft.semantickernel.services.textembedding.TextEmbeddingGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SimpleEmbeddingGenerationService implements EmbeddingGenerationService {

    private final TextEmbeddingGenerationService embeddingGenerationService;

    public Mono<Embedding> generateEmbedding(String chunk) {
        return embeddingGenerationService.generateEmbeddingAsync(chunk)
                .retryWhen(RetryBackoffSpec.backoff(3, Duration.ofSeconds(2)))
                .onErrorMap(Exception.class, e -> new GenAiServiceException("Unable to generate embedding from the text chunk: '%s'\n%s".formatted(chunk, e.getMessage())));
    }
}

