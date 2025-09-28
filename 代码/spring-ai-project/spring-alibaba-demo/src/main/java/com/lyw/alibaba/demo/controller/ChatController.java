package com.lyw.alibaba.demo.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private ChatClient chatClient;

//    public ChatController(ChatClient.Builder chatClientBuilder) {
//        this.chatClient = chatClientBuilder.build();
//    }

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

    record ActorFilms(String name, List<String> films) {
    }

    @RequestMapping("/entity")
    public String entity(String message) {
        ActorFilms entity = chatClient.prompt(message)
                .call()
                .entity(ActorFilms.class);

        return entity.toString();

    }

    @RequestMapping("/word")
    public String word(String message, String word) {
         return chatClient.prompt(message)
                .system(sp -> sp.param("word", word))
                .call()
                .content();
    }

}
