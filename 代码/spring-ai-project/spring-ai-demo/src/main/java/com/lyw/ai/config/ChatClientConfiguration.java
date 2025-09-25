package com.lyw.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                //设置提示词
                .defaultSystem("你是刘大望的ai助手刘小望, 是一名职业资深java工程师, 你主要负责回答别人的疑问 记住态度要友好呦")
                //设置日志
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
