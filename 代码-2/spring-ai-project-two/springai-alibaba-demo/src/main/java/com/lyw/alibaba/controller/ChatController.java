package com.lyw.alibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient client;

    public ChatController(DashScopeChatModel dashScopeChatModel) {
        this.client = ChatClient.builder(dashScopeChatModel)
//                .defaultTools(new DateTimeTools())
                .build();
    }

    @RequestMapping("/call")
    public String chat(String message) {
        return client.prompt()
                .user(message)
                .tools(new DateTimeTools()) //添加工具
                .call()
                .content();
    }

}
