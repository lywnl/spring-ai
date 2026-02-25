package com.lyw.image.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping
public class OpenaiSpeechControlelr {

    @Autowired
    private OpenAiAudioSpeechModel openAiAudioSpeechModel;

    @RequestMapping("/tts")
    public String tts() {
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .model("tts-1")
                .voice(OpenAiAudioApi.SpeechRequest.Voice.NOVA)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.OPUS)
                .speed(1.0f)
                .build();

        SpeechPrompt speechPrompt = new SpeechPrompt("小池, 泉眼无声惜细流, 树阴照水爱晴柔. 小荷才露尖尖角, 早有蜻蜓立上头", speechOptions);
        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);

        //输出到文件
        File file = new File(System.getProperty("user.dir") +"/output.opus");
        try(FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(response.getResult().getOutput());
            return "success";
        } catch (IOException e){
            log.error("文件写入失败, e:", e);
            return "failed";
        }
    }


}
