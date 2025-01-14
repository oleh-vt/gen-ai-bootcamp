package com.epam.training.gen.ai.repository;

import com.epam.training.gen.ai.model.EmbeddingItem;
import com.google.common.util.concurrent.Futures;
import com.microsoft.semantickernel.services.textembedding.Embedding;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.ValueFactory;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import io.qdrant.client.grpc.Points.PointStruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;
import static io.qdrant.client.WithPayloadSelectorFactory.enable;

@Slf4j
@Component
@RequiredArgsConstructor
public class QdrantRepository implements VectorStoreRepository {

    public static final String TEXT_FIELD = "text";
    public static final String COLLECTION_NAME = "rag_collection";
    public static final float THRESHOLD = 0.3f;

    private final QdrantClient qdrantClient;

    @Override
    public void save(List<EmbeddingItem> embeddingItems) {
        Futures.getUnchecked(qdrantClient.upsertAsync(COLLECTION_NAME, convert(embeddingItems)));
        log.info("Embeddings saved to the vector store");
    }

    @Override
    public List<String> search(Embedding embedding) {
        List<String> results = Futures.getUnchecked(
                        qdrantClient.searchAsync(
                                Points.SearchPoints.newBuilder()
                                        .setCollectionName(COLLECTION_NAME)
                                        .setWithPayload(enable(Boolean.TRUE))
                                        .setScoreThreshold(THRESHOLD)
                                        .setLimit(5)
                                        .addAllVector(embedding.getVector())
                                        .build()
                        )).stream()
                .peek(p -> log.info("Score: {}", p.getScore()))
                .map(p -> p.getPayloadOrDefault(TEXT_FIELD, ValueFactory.nullValue()))
                .map(JsonWithInt.Value::getStringValue)
                .toList();
        log.info("Results found: {}", results.size());
        return results;
    }

    private List<PointStruct> convert(Collection<EmbeddingItem> embeddingItems) {
        return embeddingItems.stream()
                .map(item -> PointStruct.newBuilder()
                        .setId(id(UUID.randomUUID()))
                        .setVectors(vectors(item.textEmbedding().getVector()))
                        .putPayload(TEXT_FIELD, value(item.text()))
                        .build())
                .toList();
    }
}
