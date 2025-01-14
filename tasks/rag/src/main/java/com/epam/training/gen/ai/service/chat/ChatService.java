package com.epam.training.gen.ai.service.chat;

import com.epam.training.gen.ai.dto.Reply;

public interface ChatService {
    Reply reply(String ask);
}
