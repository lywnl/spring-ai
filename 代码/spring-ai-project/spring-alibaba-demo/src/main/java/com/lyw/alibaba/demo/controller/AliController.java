package com.lyw.alibaba.demo.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alibaba")
public class AliController {

    @Resource
    private ChatModel chatModel;

    @RequestMapping("/chat")
    public String chat(String message) {
        return chatModel.call(message);
    }

}
