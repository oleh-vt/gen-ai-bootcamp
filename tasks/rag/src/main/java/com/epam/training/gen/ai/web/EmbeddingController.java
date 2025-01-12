package com.epam.training.gen.ai.web;

import com.epam.training.gen.ai.exception.GenAiServiceClientException;
import com.epam.training.gen.ai.service.embeddings.EmbeddingsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequestMapping("embeddings")
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingsService embeddingsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void create(@RequestParam MultipartFile file) {
        validate(file);
        embeddingsService.create(readContent(file.getResource()));
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new GenAiServiceClientException("File is null or empty");
        }
        if (!isTxtFile(file)) {
            throw new GenAiServiceClientException("Only 'txt' files allowed");
        }
    }

    @SneakyThrows
    private String readContent(Resource resource) {
        return resource.getContentAsString(StandardCharsets.UTF_8);
    }

    private boolean isTxtFile(MultipartFile file) {
        return Optional.of(file)
                .map(MultipartFile::getOriginalFilename)
                .map(filename -> filename.endsWith(".txt"))
                .orElse(Boolean.FALSE);
    }


}
