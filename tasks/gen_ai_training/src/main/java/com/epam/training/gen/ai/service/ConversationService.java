package com.epam.training.gen.ai.service;

public interface ConversationService {
    String reply(String prompt);
    String reply(String prompt , Integer temperature);
}
