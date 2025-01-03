package com.epam.training.gen.ai.service.embeddings.repository;

import com.azure.ai.openai.models.Embeddings;
import io.qdrant.client.grpc.Points.ScoredPoint;

import java.util.List;

public interface VectorStoreRepository {
    void save(String text, Embeddings embeddings);

    List<ScoredPoint> search(Embeddings embeddings, int limit);
}
