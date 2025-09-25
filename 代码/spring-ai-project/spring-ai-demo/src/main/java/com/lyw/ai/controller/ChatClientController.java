package com.lyw.ai.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatClientController {

    @Resource(name = "chatClient")
    private ChatClient chatClient;

    /*public ChatClientController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                //设置系统提示词
                .defaultSystem("你是刘大望的ai助手刘小望, 是一名职业资深java工程师, 你主要负责回答别人的疑问 记住态度要友好呦")
                .build();

    }*/

    //chatclient
    @RequestMapping("/call")
    public String chat(String message) {
        return chatClient.prompt()
                //用户输入
                .user(message)
                //调用ai
                .call()
                //返回响应
                .content();
    }

    record Recipe(String dish, List<String> ingredients){}

    //返回实体类
    @RequestMapping("/entity")
    public String entity(String message) {
        Recipe entity = chatClient.prompt()
                .user(String.format("请帮我生成%s的食谱", message))
                .call()
                .entity(Recipe.class);

        return entity.toString();
    }

    //流式输出
    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String message) {
        return chatClient.prompt()
                //用户输入
                .user(message)
                //调用AI 流式输出
                .stream()
                //返回响应
                .content();
    }


}
