package com.lyw.rag;


import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TextEmbeddingTest {

    @Autowired
    private DashScopeEmbeddingModel embeddingModel;

    @Test
    public void testEmbedding() {
        float[] embed = embeddingModel.embed("Hello, World!");

        System.out.println(embed.length);
        System.out.println(Arrays.toString(embed));
    }

    @Test
    public void batchEmbedding() {
        List<String> texts = List.of("Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!"
                , "Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!"
        , "Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!"
        );

        List<float[]> embeds = embeddingModel.embed(texts);
        System.out.println(embeds.size());
    }

}
