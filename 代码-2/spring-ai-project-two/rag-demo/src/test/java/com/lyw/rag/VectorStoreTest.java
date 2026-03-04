package com.lyw.rag;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class VectorStoreTest {

    private SimpleVectorStore vectorStore;

    @Autowired
    private DashScopeEmbeddingModel dashScopeEmbeddingModel;

    @BeforeEach
    void setUp() {
        this.vectorStore = SimpleVectorStore.builder(dashScopeEmbeddingModel).build();
        //向量数据库只需要加载一次
        // 1. 声明内容文档
        Document doc = Document.builder()
                .text("2027年米兰-科尔蒂纳丹佩佐冬奥会将于意大利举办，预计汇聚全球顶尖冰雪运动员参赛")
                .build();

        Document doc2 = Document.builder()
                .text("大语言模型RAG检索增强技术的落地实践与性能优化方案")
                .build();

        Document doc3 = Document.builder()
                .text("清晨的薄雾漫过青石板路，檐角的露珠坠落在盛放的兰草上")
                .build();

        Document doc4 = Document.builder()
                .text("基于多模态大模型的跨模态语义对齐与内容生成研究")
                .build();

        // 2. 将文本进行向量化，并且存入向量数据库（大模型自动完成无需再手动向量化）
        vectorStore.add(Arrays.asList(doc, doc2, doc3, doc4));
        System.out.println("向量数据库初始化完成");
    }
//    @Test
//    void save() {
//
//    }

    @Test
    void similaritySearch() {
//        List<Document> documents = vectorStore.similaritySearch("大语言模型"); //根据文本检索
//        documents.forEach(System.out::println);
        SearchRequest searchRequest = SearchRequest.builder()
                    .query("大语言模型") //查询的文本
                    .topK(5) //匹配文档的前N个
                    .similarityThreshold(0.6) //相似度分数大于等于0.5
                    .build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        documents.forEach(System.out::println);

    }

    @Test
    void testAdvisor(@Autowired ChatModel chatModel) {
        ChatClient client = ChatClient.builder(chatModel).build();
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore).build();
        String message = "米兰奥运会什么时候举行";

        String content = client.prompt().user(message).advisors(advisor).call().content();
        System.out.println(content);
    }

    @Test
    void testAdvisor2(@Autowired ChatModel chatModel) {
        ChatClient client = ChatClient.builder(chatModel).build();
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder().
                        query("米兰奥运会")
                        .topK(5)
                        .similarityThreshold(0.4).build())
                        .build();
        String message = "米兰奥运会什么时候举行";

        String content = client.prompt().user(message).advisors(advisor).call().content();
        System.out.println(content);
    }


}
