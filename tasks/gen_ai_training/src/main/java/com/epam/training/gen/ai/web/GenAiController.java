package com.epam.training.gen.ai.web;

import com.epam.training.gen.ai.model.Prompt;
import com.epam.training.gen.ai.model.Reply;
import com.epam.training.gen.ai.service.ConversationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenAiController {

    private final ConversationService conversationService;

    public GenAiController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    /**
     * LLM chat chandler method
     *
     * @param prompt      request body containing the user prompt
     *                    Example
     *                    {
     *                      "input": "tell me a joke"
     *                    }
     * @param temperature LLM temperature setting in range from 0 to 100
     * <p>
     *                    FINDINGS:
     *                    The temperature value affects the LLM to generate either more focused
     *                    or more creative response. The higher temperature value, the more 'creative' responses are,
     *                    although in case of DIAL it seems to work opposite: the higher value, the more concise response
     * @return LLM response
     */
    @PostMapping("/conversations")
    public Reply processPrompt(@RequestBody Prompt prompt, @RequestParam(required = false) Integer temperature) {
        validateTemperature(temperature);
        return new Reply(conversationService.reply(prompt.input(), temperature));
    }

    private void validateTemperature(Integer t) {
        if (t != null) {
            if (t < 0 || t > 100) {
                throw new IllegalArgumentException("Temperature must be in range [0 - 100]");
            }
        }
    }

}

