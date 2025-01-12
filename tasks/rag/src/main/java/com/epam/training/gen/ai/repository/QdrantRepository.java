package com.epam.training.gen.ai.repository;

import com.epam.training.gen.ai.model.EmbeddingItem;
import com.google.common.util.concurrent.Futures;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points.PointStruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;

@Component
@RequiredArgsConstructor
public class QdrantRepository implements VectorStoreRepository {

    public static final String COLLECTION_NAME = "rag_collection";

    private final QdrantClient qdrantClient;

    @Override
    public void save(List<EmbeddingItem> embeddingItems) {
        Futures.getUnchecked(qdrantClient.upsertAsync(COLLECTION_NAME, convert(embeddingItems)));
    }

    private List<PointStruct> convert(Collection<EmbeddingItem> embeddingItems) {
        return embeddingItems.stream()
                .map(item -> PointStruct.newBuilder()
                        .setId(id(UUID.randomUUID()))
                        .setVectors(vectors(item.textEmbedding().getVector()))
                        .putPayload("text", value(item.text()))
                        .build())
                .toList();
    }
}
