package com.lyw.ollama.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ollama")
public class OllamaController {

    @Resource
    private OllamaChatModel chatModel;

    @RequestMapping("/chat")
    public String chat(String message) {
        return chatModel.call(message);
    }

    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String message) {
        return chatModel.stream(message);
    }


}
