package com.lyw.ai.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/sse")
public class SseController {

    //sse服务器持续输出数据到客户端
    @RequestMapping("/data")
    public void data(HttpServletResponse response) throws IOException, InterruptedException {
        response.setContentType("text/event-stream;charset=utf-8");

        PrintWriter writer = response.getWriter();

        for (int i = 0; i < 20; i++) {
            String s = "data: " + new Date() + "\n\n";
            writer.write(s);
            writer.flush();

            Thread.sleep(1000);
        }
    }

    //retry 服务器输出数据失败时, 会自动重试 retry设置重连时间
    @RequestMapping("/retry")
    public void retry(HttpServletResponse response) throws IOException {
        log.info("发起请求: retry");
        response.setContentType("text/event-stream;charset=utf-8");

        PrintWriter writer = response.getWriter();
        String s = "retry: 2000\n";
        s += "data: " + new Date() + "\n\n";

        writer.write(s);
        writer.flush();

    }
}
