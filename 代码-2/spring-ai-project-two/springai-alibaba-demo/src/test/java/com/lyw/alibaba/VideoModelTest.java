package com.lyw.alibaba;

import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesis;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisParam;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class VideoModelTest {

    @Test
    void image2Video() throws NoApiKeyException, InputRequiredException {
        String imgUrl = "https://n.sinaimg.cn/sinakd20230117ac/214/w1080h2334/20230117/e875-42e7995e77807e060ad58b38c0d6ee7a.jpg";
        VideoSynthesis vs = new VideoSynthesis();
        VideoSynthesisParam param =
                VideoSynthesisParam.builder()
                        .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                        .model("wan2.5-i2v-preview")
                        .prompt("一名女士端起桌子上的水杯喝水")
                        .audio(false)
                        .imgUrl(imgUrl)
                        .build();
        System.out.println("please wait...");
        VideoSynthesisResult result = vs.call(param);
        System.out.println(JsonUtils.toJson(result));
    }

    /**
     * 基于首帧生成视频
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    @Test
    void image2Video2() throws NoApiKeyException, InputRequiredException {
        String imgUrl = "https://p1.ssl.qhmsg.com/t01a4d2f84398894b71.jpg";
        VideoSynthesis vs = new VideoSynthesis();
        VideoSynthesisParam param =
                VideoSynthesisParam.builder()
                        .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                        .model("wan2.2-i2v-flash")
                        .prompt("一名男士笑了笑, 向我点点头")
                        .imgUrl(imgUrl)
                        .build();
        System.out.println("please wait...");
        VideoSynthesisResult result = vs.call(param);
        System.out.println(JsonUtils.toJson(result));
    }

    /**
     * 基于首尾帧生成视频
     */
    @Test
    void image2Video3(){

        String firstFrameUrl = "https://wanx.alicdn.com/material/20250318/first_frame.png";
        String lastFrameUrl = "https://wanx.alicdn.com/material/20250318/last_frame.png";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prompt_extend", true);
        parameters.put("resolution", "720P");

        VideoSynthesis videoSynthesis = new VideoSynthesis();
        VideoSynthesisParam param =
                VideoSynthesisParam.builder()
                        .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                        .model("wan2.2-kf2v-flash")
                        .prompt("写实风格，一只黑色小猫好奇地看向天空，镜头从平视逐渐上升，最后俯拍它的好奇的眼神。")
                        .firstFrameUrl(firstFrameUrl)
                        .lastFrameUrl(lastFrameUrl)
                        .parameters(parameters)
                        .build();
        VideoSynthesisResult result = null;
        try {
            System.out.println("---sync call, please wait a moment----");
            result = videoSynthesis.call(param);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    void image2Video4() throws NoApiKeyException, InputRequiredException {
        String imgUrl = "https://cdn.translate.alibaba.com/r/wanx-demo-1.png";
        VideoSynthesis vs = new VideoSynthesis();
        VideoSynthesisParam param =
                VideoSynthesisParam.builder()
                        .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                        .model("wan2.2-i2v-flash")
                        .template("flying")
                        .imgUrl(imgUrl)
                        .build();
        System.out.println("please wait...");
        VideoSynthesisResult result = vs.call(param);
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    void image2Video5() throws NoApiKeyException, InputRequiredException {
        String imgUrl = "https://help-static-aliyun-doc.aliyuncs.com/assets/img/zh-CN/8150385571/p1000087.png";
        VideoSynthesis vs = new VideoSynthesis();
        VideoSynthesisParam param =
                VideoSynthesisParam.builder()
                        .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                        .model("wanx2.1-i2v-plus")
                        .template("carousel")
                        .imgUrl(imgUrl)
                        .build();
        System.out.println("please wait...");
        VideoSynthesisResult result = vs.call(param);
        System.out.println(JsonUtils.toJson(result));
    }

}
