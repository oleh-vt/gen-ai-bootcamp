package com.epam.training.gen.ai.dto;

import com.azure.ai.openai.models.EmbeddingsUsage;

import java.util.List;

public record EmbeddingsDto(List<EmbeddingItemDto> embeddings, EmbeddingsUsage embeddingsUsage) {
}
