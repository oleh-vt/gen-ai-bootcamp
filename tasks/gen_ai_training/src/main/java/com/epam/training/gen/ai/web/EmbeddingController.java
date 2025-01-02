package com.epam.training.gen.ai.web;

import com.azure.ai.openai.models.Embeddings;
import com.epam.training.gen.ai.dto.EmbeddingItemDto;
import com.epam.training.gen.ai.dto.EmbeddingsDto;
import com.epam.training.gen.ai.exception.GenAiServiceException;
import com.epam.training.gen.ai.service.embeddings.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("embeddings")
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    EmbeddingsDto create(@RequestParam String text) {
        validate(text);
        return toDto(embeddingService.create(text));
    }

    private void validate(String text) {
        if (StringUtils.isBlank(text)) {
            throw new GenAiServiceException("Text cannot be null or empty");
        }
    }

    private EmbeddingsDto toDto(Embeddings embeddings) {
        if (embeddings == null) {
            return null;
        }
        return new EmbeddingsDto(
                embeddings.getData().stream()
                        .map(i -> new EmbeddingItemDto(i.getPromptIndex(), i.getEmbedding()))
                        .toList(),
                embeddings.getUsage()
        );
    }

}
