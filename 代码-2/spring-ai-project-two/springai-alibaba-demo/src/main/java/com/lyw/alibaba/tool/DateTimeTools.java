package com.lyw.alibaba.tool;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;

public class DateTimeTools {

    @Tool(description = "A tool to get the current date and time")
    public String getCurrentDateTime(ToolContext context) {
        System.out.println(context.getContext().get("userID"));
        return LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toString();
    }


}
