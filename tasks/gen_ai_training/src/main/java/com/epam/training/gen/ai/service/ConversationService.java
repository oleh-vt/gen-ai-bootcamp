package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.PromptSettings;

public interface ConversationService {
    String reply(String prompt , PromptSettings promptSettings);
}
