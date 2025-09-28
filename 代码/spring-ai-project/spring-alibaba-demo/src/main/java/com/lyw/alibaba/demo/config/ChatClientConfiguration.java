package com.lyw.alibaba.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem("你叫刘小望, 是刘大望的助手, 你的职责就是认真回答问题, 回答前面加上一个字{word}")
                .build();
    }

}
