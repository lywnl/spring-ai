package com.lyw.alibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.lyw.alibaba.tool.DateTimeTools;
import com.lyw.alibaba.tool.WeatherTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient client;

    public ChatController(DashScopeChatModel dashScopeChatModel, ToolCallback toolCallback) {
        this.client = ChatClient.builder(dashScopeChatModel)
//                .defaultTools(new DateTimeTools()) //声明式工具
//                .defaultToolCallbacks(toolCallback) //编程式工具
                .build();
    }

    @RequestMapping("/call")
    public String chat(String message) {
        return client.prompt()
                .user(message)
                .tools(new DateTimeTools()) //添加工具
                .toolContext(Map.of("userID", "12345")) //上下文
                .call()
                .content();
    }


    @RequestMapping("/call2")
    public String chat2(String message) {
        //获取天气工具方法
        Method method = ReflectionUtils.findMethod(WeatherTools.class,
                "getCurrentWeatherByCityName", String.class);

        //创建工具回调
        ToolCallback toolCallback = MethodToolCallback.builder()
                .toolDefinition(ToolDefinitions.builder(method)
                        .description("Get current weather by city name")
                        .build())
                .toolMethod(method)
                .toolObject(new WeatherTools())
                .build();

        return client.prompt()
                .user(message)
                .toolCallbacks(toolCallback) //添加工具
                .call()
                .content();
    }

}
