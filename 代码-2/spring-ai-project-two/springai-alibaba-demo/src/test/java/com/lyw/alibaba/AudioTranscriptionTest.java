package com.lyw.alibaba;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionOptions;
import com.alibaba.cloud.ai.dashscope.audio.transcription.AudioTranscriptionModel;
import com.alibaba.dashscope.audio.asr.recognition.Recognition;
import com.alibaba.dashscope.audio.asr.recognition.RecognitionParam;
import com.alibaba.dashscope.audio.asr.transcription.Transcription;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionParam;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionQueryParam;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.Arrays;

@SpringBootTest
public class AudioTranscriptionTest {

    @Autowired
    private AudioTranscriptionModel audioTranscriptionModel;

    private static final String DEFAULT_MODEL = "paraformer-v2";

    @Test
    public void stt() {
        Resource resource = new DefaultResourceLoader()
                .getResource("https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_female2.wav");

        AudioTranscriptionResponse response = audioTranscriptionModel.call(
                new AudioTranscriptionPrompt(
                        resource,
                        DashScopeAudioTranscriptionOptions.builder()
                                .withModel(DEFAULT_MODEL)
                                .build()
                )
        );

        System.out.println(response.getResult().getOutput());
    }

    @Test
    void sttByDashscopeSdk(){
        // 创建转写请求参数
        TranscriptionParam param =
                TranscriptionParam.builder()
                        // 若没有将API Key配置到环境变量中，需将apiKey替换为自己的API Key
                        .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                        .model("paraformer-v2")
                        // “language_hints”只支持paraformer-v2模型
                        .parameter("language_hints", new String[]{"zh", "en"})
                        .fileUrls(
                                Arrays.asList(
                                        "https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_female2.wav",
                                        "https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_male2.wav"))
                        .build();
        try {
            Transcription transcription = new Transcription();
            // 提交转写请求
            TranscriptionResult result = transcription.asyncCall(param);
            System.out.println("RequestId: " + result.getRequestId());
            // 阻塞等待任务完成并获取结果
            result = transcription.wait(
                    TranscriptionQueryParam.FromTranscriptionParam(param, result.getTaskId()));

            // 打印结果
            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(result.getOutput()));
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
        System.exit(0);
    }

    @Test
    void testRecognition(){
        // 创建Recognition实例
        Recognition recognizer = new Recognition();
        // 创建RecognitionParam
        RecognitionParam param =
                RecognitionParam.builder()
                        // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将apiKey替换为自己的API Key
                        // .apiKey("yourApikey")
                        .model("paraformer-realtime-v2")
                        .format("wav")
                        .sampleRate(16000)
                        // “language_hints”只支持paraformer-realtime-v2模型
                        .parameter("language_hints", new String[]{"zh", "en"})
                        .build();

        try {
            String path = System.getProperty("user.dir") + "/hello_world_male_16k_16bit_mono.wav";
            String result = recognizer.call(param, new File(path));
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
            if (jsonObject.has("sentences")){
                jsonObject.get("sentences").getAsJsonArray().forEach(sent->{
                    JsonObject sentObject = sent.getAsJsonObject();
                    String text = sentObject.get("text").getAsString();
                    System.out.println(text);
                });
            }

//            System.out.println((result));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 任务结束后关闭 WebSocket 连接
            recognizer.getDuplexApi().close(1000, "bye");
        }
        System.out.println(
                "[Metric] requestId: "
                        + recognizer.getLastRequestId()
                        + ", first package delay ms: "
                        + recognizer.getFirstPackageDelay()
                        + ", last package delay ms: "
                        + recognizer.getLastPackageDelay());
        System.exit(0);
    }
}
