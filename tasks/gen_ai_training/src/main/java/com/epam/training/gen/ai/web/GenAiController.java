package com.epam.training.gen.ai.web;

import com.epam.training.gen.ai.model.Prompt;
import com.epam.training.gen.ai.model.Reply;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenAiController {

    @PostMapping("/conversations")
    public Reply processPrompt(@RequestBody Prompt prompt) {
        return new Reply("hi");
    }

}
