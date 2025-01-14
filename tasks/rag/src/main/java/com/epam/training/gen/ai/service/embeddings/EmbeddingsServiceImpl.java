package com.epam.training.gen.ai.service.embeddings;


import com.epam.training.gen.ai.exception.GenAiServiceException;
import com.epam.training.gen.ai.model.EmbeddingItem;
import com.epam.training.gen.ai.repository.VectorStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingsServiceImpl implements EmbeddingsService {

    private final EmbeddingGenerationService embeddingGenerationService;
    private final VectorStoreRepository vectorStoreRepository;
    private final PlainTextChunker textChunker;

    @Override
    public void create(String text) {
        List<EmbeddingItem> embeddingItems = generateEmbeddings(text);
        vectorStoreRepository.save(embeddingItems);
    }

    @Override
    public List<String> search(String text) {
        return embeddingGenerationService.generateEmbedding(text)
                .map(vectorStoreRepository::search)
                .blockOptional().orElseThrow(GenAiServiceException::new);
    }

    private List<EmbeddingItem> generateEmbeddings(String text) {
        List<EmbeddingItem> embeddingItems = Flux.fromIterable(textChunker.split(text))
                .flatMap(this::generate)
                .collectList()
                .blockOptional().orElseThrow(GenAiServiceException::new);
        log.info("Embeddings created: {}", embeddingItems.size());
        return embeddingItems;
    }

    private Mono<EmbeddingItem> generate(String text) {
        return embeddingGenerationService.generateEmbedding(text)
                .map(e -> new EmbeddingItem(text, e));
    }

}
