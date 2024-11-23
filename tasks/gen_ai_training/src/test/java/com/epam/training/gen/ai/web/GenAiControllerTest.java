package com.epam.training.gen.ai.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenAiController.class)
class GenAiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void processPromptTest() throws Exception {
        mockMvc.perform(
                        post("/conversations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"input\": \"hello there\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", equalTo("hi")));
    }
}