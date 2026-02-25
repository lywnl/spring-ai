package com.lyw.alibaba;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageModelTest {

    @Autowired
    private DashScopeImageModel dashScopeImageModel;

    @Test
    public void image() {
        //默认通义万象v1
        ImageResponse response = dashScopeImageModel.call(new ImagePrompt("A light cream colored mini golden doodle"));
        System.out.println(response.getResult().getOutput().getUrl());
    }

    @Test
    public void image2() {
        DashScopeImageOptions options = DashScopeImageOptions.builder()
                .withModel("qwen-image-plus")
                .withWidth(1024)
                .withHeight(1024)
                .withN(1) //固定为1
                .withResponseFormat("url")
                .build();
        ImageResponse response = dashScopeImageModel.call(new ImagePrompt("A light cream colored mini golden doodle", options));
        System.out.println(response.getResult().getOutput().getUrl());
    }

    @Test
    public void image3() {
        //通义万象v2
        DashScopeImageOptions options = DashScopeImageOptions.builder()
                .withModel("wan2.2-t2i-flash")
                .withWatermark(true) //添加水印
                .withHeight(1280)
                .withWidth(1280)
                .build();
        ImageResponse response = dashScopeImageModel.call(new ImagePrompt("A light cream colored mini golden doodle", options));
        System.out.println(response.getResult().getOutput().getUrl());
    }

    @Test
    public void image4() {
        //通义万象v2 生成多张图片
        DashScopeImageOptions options = DashScopeImageOptions.builder()
                .withModel("wan2.2-t2i-flash")
                .withN(3)
                .build();
        ImageResponse response = dashScopeImageModel.call(new ImagePrompt("A light cream colored mini golden doodle", options));
        response.getResults().forEach(result -> {
            System.out.println(result.getOutput().getUrl());
        });

    }
}
