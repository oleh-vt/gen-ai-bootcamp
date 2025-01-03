package com.epam.training.gen.ai.service.embeddings.repository;

import com.azure.ai.openai.models.Embeddings;
import com.google.common.util.concurrent.Futures;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points.PointStruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;

@Component
@RequiredArgsConstructor
public class QdrantRepository implements VectorStoreRepository {

    public static final String COLLECTION_NAME = "democollection";

    private final QdrantClient qdrantClient;

    @Override
    public void save(String text, Embeddings embeddings) {
        Futures.getUnchecked(qdrantClient.upsertAsync(COLLECTION_NAME, convert(text, embeddings)));
    }

    @Override
    public Object find() {
        return null;
    }

    private List<PointStruct> convert(String text, Embeddings embeddings) {
        return embeddings.getData().stream()
                .map(item -> PointStruct.newBuilder()
                        .setId(id(UUID.randomUUID()))
                        .setVectors(vectors(item.getEmbedding()))
                        .putPayload("text", value(text))
                        .build())
                .toList();
    }
}
