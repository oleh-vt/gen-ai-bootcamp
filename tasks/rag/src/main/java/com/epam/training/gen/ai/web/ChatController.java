package com.epam.training.gen.ai.web;

import com.epam.training.gen.ai.dto.Ask;
import com.epam.training.gen.ai.dto.Reply;
import com.epam.training.gen.ai.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("chat")
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    Reply replyChat(@RequestBody Ask ask) {
        return chatService.reply(ask.text());
    }
}
