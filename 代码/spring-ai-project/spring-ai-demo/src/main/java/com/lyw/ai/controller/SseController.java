package com.lyw.ai.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
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

    @RequestMapping("/event")
    public void event(HttpServletResponse response) throws IOException, InterruptedException {
        log.info("发起请求: event");
        response.setContentType("text/event-stream;charset=utf-8");

        PrintWriter writer = response.getWriter();
        for (int i = 0; i < 10; i++) {
            String s = "event: foo\n";
            s += "data: " + new Date() + "\n\n";
            writer.write(s);
            writer.flush();

            Thread.sleep(1000);
        }
    }

    @RequestMapping("/end")
    public void end(HttpServletResponse response) throws IOException, InterruptedException {
        log.info("发起请求: end");
        response.setContentType("text/event-stream;charset=utf-8");

        PrintWriter writer = response.getWriter();
        for (int i = 0; i < 10; i++) {
            String s = "event: foo\n";
            s += "data: " + new Date() + "\n\n";
            writer.write(s);
            writer.flush();

            Thread.sleep(1000);
        }

        //定义end事件
        writer.write("event: end\n : EOF\n\n");
        writer.flush();
    }

    @RequestMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream() {
        return Flux.interval(Duration.ofSeconds(1)).map(s -> new Date().toString());
    }


}
