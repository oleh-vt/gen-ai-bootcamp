package com.epam.training.gen.ai.service.embeddings;

import com.microsoft.semantickernel.text.TextChunker;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlainTextLineChanker implements PlainTextChunker {

    public static final int MAX_TOKENS_PER_LINE = 2000;

    @Override
    public List<String> split(String text) {
        return TextChunker.splitPlainTextLines(text, MAX_TOKENS_PER_LINE);
    }
}
