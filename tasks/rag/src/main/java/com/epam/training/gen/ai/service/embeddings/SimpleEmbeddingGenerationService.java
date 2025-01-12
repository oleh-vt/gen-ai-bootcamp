package com.epam.training.gen.ai.service.embeddings;

import com.epam.training.gen.ai.exception.GenAiServiceException;
import com.epam.training.gen.ai.model.EmbeddingItem;
import com.microsoft.semantickernel.services.textembedding.TextEmbeddingGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleEmbeddingGenerationService implements EmbeddingGenerationService {

    private final PlainTextChunker textChunker;
    private final TextEmbeddingGenerationService embeddingGenerationService;

    @Override
    public List<EmbeddingItem> create(String text) {
        return Flux.fromIterable(textChunker.split(text))
                .flatMap(this::generateEmbedding)
                .collectList()
                .blockOptional().orElseThrow(GenAiServiceException::new);
    }

    private Mono<EmbeddingItem> generateEmbedding(String chunk) {
        return embeddingGenerationService.generateEmbeddingAsync(chunk)
                .map(embedding -> new EmbeddingItem(chunk, embedding))
                .retryWhen(RetryBackoffSpec.backoff(3, Duration.ofSeconds(2)))
                .onErrorMap(Exception.class, e -> new GenAiServiceException("Unable to generate embedding from the text chunk: '%s'\n%s".formatted(chunk, e.getMessage())));
    }

}

