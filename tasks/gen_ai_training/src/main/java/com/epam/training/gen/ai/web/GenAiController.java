package com.epam.training.gen.ai.web;

import com.epam.training.gen.ai.dto.ConversationDto;
import com.epam.training.gen.ai.model.Deployment;
import com.epam.training.gen.ai.model.Prompt;
import com.epam.training.gen.ai.model.Reply;
import com.epam.training.gen.ai.service.Conversation;
import com.epam.training.gen.ai.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("conversations")
@RequiredArgsConstructor
public class GenAiController {

    private final ConversationService conversationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationDto create(@RequestBody Deployment deployment) {
        Conversation conversation = conversationService.create(deployment);
        return new ConversationDto(conversation.getId(), conversation.getModel());
    }

    @GetMapping
    public Collection<ConversationDto> getAllConversations() {
        return conversationService.getAll().stream()
                .map(c -> new ConversationDto(c.getId(), c.getModel()))
                .toList();
    }

    @PostMapping("{id}")
    public Reply processPrompt(@PathVariable String id, @RequestBody Prompt prompt) {
        validateTemperature(prompt.temperature());
        return new Reply(conversationService.reply(id, prompt));
    }

    @GetMapping("{id}")
    public ConversationDto getConversation(@PathVariable String id) {
        var conversation = conversationService.get(id);
        return new ConversationDto(conversation.getId(), conversation.getModel());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeConversation(@PathVariable String id) {
        conversationService.delete(id);
    }

    private void validateTemperature(Double t) {
        if (t != null) {
            if (t < 0 || t > 1.0) {
                throw new IllegalArgumentException("Temperature must be between 0 and 1.0");
            }
        }
    }

}

