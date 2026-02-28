package com.lyw.mcp.config;

import com.lyw.mcp.service.UserService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsConfig {

    @Bean
    public ToolCallbackProvider getToolCallbackProvider(UserService userService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(userService) //注入工具对象
                .build();
    }

}
