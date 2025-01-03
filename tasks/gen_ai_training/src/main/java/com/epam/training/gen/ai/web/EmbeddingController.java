package com.epam.training.gen.ai.web;

import com.azure.ai.openai.models.Embeddings;
import com.epam.training.gen.ai.dto.EmbeddingItemDto;
import com.epam.training.gen.ai.dto.EmbeddingsDto;
import com.epam.training.gen.ai.exception.GenAiServiceException;
import com.epam.training.gen.ai.model.Prompt;
import com.epam.training.gen.ai.service.embeddings.EmbeddingsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("embeddings")
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingsService embeddingsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    EmbeddingsDto create(@RequestParam String text) {
        validate(text);
        return toDto(embeddingsService.create(text));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EmbeddingsDto create(@RequestBody Prompt prompt) {
        String input = prompt.input();
        validate(input);
        return toDto(embeddingsService.createAndSave(input));
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
