package com.lyw.ai.controller;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/deepseek")
public class DeepseekController {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @RequestMapping("/chat")
    public String chat(String message) {
        return openAiChatModel.call(message);
    }

    //结构化输入
    @RequestMapping("/chatByPrompt")
    public String chatByPrompt(String message) {
        Prompt prompt = new Prompt(message);
        ChatResponse response = openAiChatModel.call(prompt);

        return response.getResult().getOutput().getText();
    }

    @RequestMapping("/role")
    public String role(String message) {
        SystemMessage systemMessage = new SystemMessage("你是刘大望的ai助手刘小望, 是一名职业资深java工程师, 你主要负责回答别人的疑问 记住态度要友好呦");
        UserMessage userMessage = new UserMessage(message);

        Prompt prompt = new Prompt(systemMessage, userMessage);

        ChatResponse response = openAiChatModel.call(prompt);

        return response.getResult().getOutput().getText();
    }

    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String message) {
        Prompt prompt = new Prompt(message);
        Flux<ChatResponse> stream = openAiChatModel.stream(prompt);

        return stream.map(x -> x.getResult().getOutput().getText());
    }


}
