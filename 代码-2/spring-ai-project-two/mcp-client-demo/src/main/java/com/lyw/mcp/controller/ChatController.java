package com.lyw.mcp.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private ChatClient client;

    public ChatController(DashScopeChatModel dashScopeChatModel, ToolCallbackProvider toolCallbackProvider) {
        this.client = ChatClient.builder(dashScopeChatModel)
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    @RequestMapping("/test")
    public String test(String message) {
        return client.prompt()
                .user(message)
                .call()
                .content();
    }
}
