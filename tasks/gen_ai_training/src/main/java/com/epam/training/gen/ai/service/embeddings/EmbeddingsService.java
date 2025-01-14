package com.epam.training.gen.ai.service.embeddings;

import com.azure.ai.openai.models.Embeddings;
import io.qdrant.client.grpc.Points.ScoredPoint;

import java.util.List;

public interface EmbeddingsService {
    Embeddings create(String text);

    Embeddings createAndSave(String text);

    List<ScoredPoint> search(String text, int limit);
}