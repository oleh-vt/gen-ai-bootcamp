package com.epam.training.gen.ai.dto;

import java.util.List;

public record EmbeddingItemDto(int promptIndex, List<Float>embedding) {
}
