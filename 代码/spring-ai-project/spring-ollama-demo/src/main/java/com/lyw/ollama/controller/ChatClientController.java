package com.lyw.ollama.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatClientController {

    @Resource
    private ChatClient chatClient;

    @RequestMapping("/call")
    public String call(String message) {
        return chatClient.prompt(message)
                .call()
                .content();
    }

    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String message) {
        return chatClient.prompt(message)
                .stream()
                .content();
    }
}
