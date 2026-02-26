package com.lyw.alibaba.controller;

import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;

public class DateTimeTools {

    @Tool(description = "A tool to get the current date and time")
    public String getCurrentDateTime() {
        return LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toString();
    }


}
