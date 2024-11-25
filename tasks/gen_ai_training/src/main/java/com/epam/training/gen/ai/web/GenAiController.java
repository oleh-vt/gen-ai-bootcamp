package com.epam.training.gen.ai.web;

import com.epam.training.gen.ai.model.Prompt;
import com.epam.training.gen.ai.model.PromptSettings;
import com.epam.training.gen.ai.model.Reply;
import com.epam.training.gen.ai.service.ConversationService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenAiController {

    private final ConversationService conversationService;

    public GenAiController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/conversations")
    public Reply processPrompt(@RequestBody Prompt prompt, @ModelAttribute PromptSettings promptSettings) {
        return new Reply(conversationService.reply(prompt.input(), promptSettings));
    }

}

