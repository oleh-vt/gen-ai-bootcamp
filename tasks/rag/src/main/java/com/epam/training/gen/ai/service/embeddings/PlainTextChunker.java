package com.epam.training.gen.ai.service.embeddings;

import java.util.List;

public interface PlainTextChunker {
    List<String> split(String text);
}
