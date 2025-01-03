package com.epam.training.gen.ai.service.embeddings;

import com.azure.ai.openai.models.Embeddings;
import com.epam.training.gen.ai.service.embeddings.repository.VectorStoreRepository;
import com.epam.training.gen.ai.service.embeddings.vector.EmbeddingVectorService;
import io.qdrant.client.grpc.Points.ScoredPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingsServiceImpl implements EmbeddingsService {

    private final EmbeddingVectorService embeddingVectorService;
    private final VectorStoreRepository vectorStoreRepository;

    @Override
    public Embeddings create(String text) {
        return embeddingVectorService.create(text);
    }

    @Override
    public Embeddings createAndSave(String text) {
        Embeddings embeddings = create(text);
        vectorStoreRepository.save(text, embeddings);
        return embeddings;
    }

    @Override
    public List<ScoredPoint> search(String text, int limit) {
        return vectorStoreRepository.search(create(text), limit);
    }
}
