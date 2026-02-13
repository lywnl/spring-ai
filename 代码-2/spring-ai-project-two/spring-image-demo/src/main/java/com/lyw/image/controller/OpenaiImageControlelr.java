package com.lyw.image.controller;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class OpenaiImageControlelr {

    @Autowired
    private OpenAiImageModel openAiImageModel;

    @RequestMapping("/image")
    public void image(){
        ImageResponse response = openAiImageModel.call(
                new ImagePrompt("A light cream colored mini golden doodle",
                        OpenAiImageOptions.builder()
                                .quality("hd")
                                .N(1)
                                .height(1024)
                                .width(1024).build())

        );
        String imageUrl = response.getResult().getOutput().getUrl();
        System.out.println(imageUrl);
    }

}
