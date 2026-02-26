package com.lyw.alibaba.controller;

import com.lyw.alibaba.tool.DateTimeTools;
import com.lyw.alibaba.tool.WeatherTools;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@RestController
@RequestMapping("/chatModel")
public class ChatModelController {

    private ChatModel chatModel;

    public ChatModelController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 声明式调用工具
     * @param message
     * @return
     */
    @RequestMapping("/callByTool")
    public String callByTool(String message){
        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(dateTimeTools)
                .build();
        Prompt prompt = new Prompt(message, chatOptions);

        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    /**
     * 编程式调用工具
     * @param message
     * @return
     */
    @RequestMapping("/callByTool2")
    public String callByTool2(String message){
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

        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(toolCallback)
                .build();
        Prompt prompt = new Prompt(message, chatOptions);

        return chatModel.call(prompt).getResult().getOutput().getText();
    }

}
