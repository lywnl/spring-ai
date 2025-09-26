package com.lyw.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAiApplication.class, args);
        System.setProperty("http.proxyHost", "127.0.0.1"); //代理服务器的ip
        System.setProperty("http.proxyPort", "7897"); // 建议同时设置端口

        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7897");
    }

}
