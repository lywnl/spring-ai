package com.lyw.image.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Base64;

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

    @RequestMapping("/image2")
    public void image2(HttpServletResponse response){
//        ImageResponse imageResponse = openAiImageModel.call(
//                new ImagePrompt("一只戴着墨镜的猫在弹吉他")
//        );

//        ImageMessage imageMessage = new ImageMessage("太空站内部景观, 高科技感", 1.2f);
//        ImageResponse imageResponse = openAiImageModel.call(
//                new ImagePrompt(imageMessage, OpenAiImageOptions.builder().build())
//        );

        ImageResponse imageResponse = openAiImageModel.call(
                new ImagePrompt("孩子在海边玩耍", OpenAiImageOptions.builder()
                        .quality("standard")
                        .N(1)
                        .height(1024)
                        .width(1024)
//                        .responseFormat("b64_json")
                        .style("natural")
                        .build())
        );

        String imageUrl = imageResponse.getResult().getOutput().getUrl();
//        System.out.println(imageUrl);
        try {
            URL url = URI.create(imageUrl).toURL();
            InputStream in = url.openStream();

            //输出到浏览器
            response.setHeader("Content-Type", MediaType.IMAGE_JPEG_VALUE);
            response.getOutputStream().write(in.readAllBytes());
            response.getOutputStream().flush();

        }catch (IOException e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/image3")
    public void image3(HttpServletResponse response){
        ImageResponse imageResponse = openAiImageModel.call(
                new ImagePrompt("孩子在海边玩耍", OpenAiImageOptions.builder()
                        .quality("standard")
                        .N(1)
                        .height(1024)
                        .width(1024)
                        .responseFormat("b64_json")
                        .style("natural")
                        .build())
        );

        String b64Json = imageResponse.getResult().getOutput().getB64Json();

        try {
            //把base64字符串解码为字节数组
            byte[] decode = Base64.getDecoder().decode(b64Json);
            //输出到浏览器
            response.setHeader("Content-Type", MediaType.IMAGE_JPEG_VALUE);
            response.getOutputStream().write(decode);
            response.getOutputStream().flush();

        }catch (IOException e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
