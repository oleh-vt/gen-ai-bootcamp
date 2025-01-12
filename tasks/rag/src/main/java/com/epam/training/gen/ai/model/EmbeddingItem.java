package com.epam.training.gen.ai.model;

import com.microsoft.semantickernel.services.textembedding.Embedding;

import java.util.List;

public record EmbeddingItem(String text, Embedding textEmbedding) {
}
