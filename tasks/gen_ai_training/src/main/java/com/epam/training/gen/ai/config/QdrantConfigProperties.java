package com.epam.training.gen.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "qdrant")
public record QdrantConfigProperties(String host, int port, boolean useTLS) {
    @ConstructorBinding
    public QdrantConfigProperties {
    }
}
