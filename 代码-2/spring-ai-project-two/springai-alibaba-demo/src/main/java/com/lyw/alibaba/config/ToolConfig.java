package com.lyw.alibaba.config;

import com.lyw.alibaba.tool.WeatherTools;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Configuration
public class ToolConfig {

    @Bean
    public ToolCallback weatherToolCallback() {
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

        return toolCallback;
    }
}
