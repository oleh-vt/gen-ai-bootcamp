package com.epam.training.gen.ai.config;

import com.google.common.util.concurrent.Futures;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections.CollectionOperationResponse;
import io.qdrant.client.grpc.Collections.Distance;
import io.qdrant.client.grpc.Collections.VectorParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.epam.training.gen.ai.service.embeddings.repository.QdrantRepository.COLLECTION_NAME;

@Slf4j
@Component
class QdrantInitializer implements InitializingBean {

    private final int vectorLength;
    private final QdrantClient qdrantClient;

    public QdrantInitializer(@Value("${openai-embeddings-deployment.vector-length}") int vectorLength,
                             QdrantClient qdrantClient) {
        this.vectorLength = vectorLength;
        this.qdrantClient = qdrantClient;
    }

    @Override
    public void afterPropertiesSet() {
        if (collectionExists()) {
            log.info("Qdrant collection '{}' already exists", COLLECTION_NAME);
            return;
        }
        createCollection(vectorLength);
    }

    private Boolean collectionExists() {
        return Futures.getUnchecked(qdrantClient.collectionExistsAsync(COLLECTION_NAME));
    }

    private void createCollection(int vectorLength) {
        var params = VectorParams.newBuilder()
                .setSize(vectorLength)
                .setDistance(Distance.Cosine)
                .build();
        CollectionOperationResponse response =
                Futures.getUnchecked(qdrantClient.createCollectionAsync(COLLECTION_NAME, params));
        if (!response.getResult()) {
            throw new IllegalStateException("Unable to create Qdrant collection");
        }
    }
}
