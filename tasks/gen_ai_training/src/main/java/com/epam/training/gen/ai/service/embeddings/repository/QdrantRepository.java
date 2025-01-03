package com.epam.training.gen.ai.service.embeddings.repository;

import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.google.common.util.concurrent.Futures;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points;
import io.qdrant.client.grpc.Points.PointStruct;
import io.qdrant.client.grpc.Points.ScoredPoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;
import static io.qdrant.client.WithPayloadSelectorFactory.enable;

@Component
@RequiredArgsConstructor
public class QdrantRepository implements VectorStoreRepository {

    public static final String COLLECTION_NAME = "democollection";

    private final QdrantClient qdrantClient;

    @Override
    public void save(String text, Embeddings embeddings) {
        Futures.getUnchecked(qdrantClient.upsertAsync(COLLECTION_NAME, convert(text, embeddings)));
    }

    @SneakyThrows
    @Override
    public List<ScoredPoint> search(Embeddings embeddings, int limit) {
        return Futures.getUnchecked(
                qdrantClient.searchAsync(
                        Points.SearchPoints.newBuilder()
                                .setCollectionName(COLLECTION_NAME)
                                .setWithPayload(enable(Boolean.TRUE))
                                .setLimit(limit)
                                .addAllVector(
                                        embeddings.getData().stream()
                                                .map(EmbeddingItem::getEmbedding)
                                                .reduce(new ArrayList<>(), (partial, chunk) -> {
                                                    partial.addAll(chunk);
                                                    return partial;
                                                })
                                ).build()
                ));
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
