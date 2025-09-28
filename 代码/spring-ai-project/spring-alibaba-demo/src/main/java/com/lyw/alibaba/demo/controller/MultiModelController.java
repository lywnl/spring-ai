package com.lyw.alibaba.demo.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/multi")
public class MultiModelController {

    @Resource
    private ChatClient chatClient;

    @RequestMapping("/image")
    public String image(String message) throws MalformedURLException, URISyntaxException {
        String url = "https://help-static-aliyun-doc.aliyuncs.com/file-manage-files/zh-CN/20241022/emyrja/dog_and_girl.jpeg";
        List<Media> mediaList = List.of(new Media(MimeTypeUtils.IMAGE_JPEG, new URL(url)));

        //创建用户提示词
        UserMessage userMessage = new UserMessage(message, mediaList);
        return chatClient.prompt(new Prompt(userMessage))
                .call()
                .content();
    }
}
