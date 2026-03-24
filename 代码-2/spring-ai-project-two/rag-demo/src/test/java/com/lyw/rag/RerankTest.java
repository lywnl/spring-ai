package com.lyw.rag;

import com.alibaba.cloud.ai.advisor.RetrievalRerankAdvisor;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.pinecone.autoconfigure.PineconeVectorStoreAutoConfiguration;
import org.springframework.ai.vectorstore.redis.autoconfigure.RedisVectorStoreAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        PineconeVectorStoreAutoConfiguration.class,
        RedisVectorStoreAutoConfiguration.class
})
public class RerankTest {

    private SimpleVectorStore simpleVectorStore;

    @Autowired
    public RerankTest(EmbeddingModel embeddingModel) {
        this.simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    }

    @BeforeEach
    void testSimpleVectorStore(@Value("classpath:/file/rule.txt") Resource resource){
        // 读取文件
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();

        // 分割文档
        TokenTextSplitter splitter = new TokenTextSplitter(200, 50, 5, 1000, true);
        List<Document> apply = splitter.apply(documents);

        // 添加向量存储
        simpleVectorStore.add(apply);
        System.out.println("向量存储写入完成");
    }

    @Test
    void testRerank(@Autowired DashScopeRerankModel rerankModel,
                    @Autowired DashScopeChatModel chatModel){
        ChatClient client = ChatClient.builder(chatModel).build();
        //1. 定义一个advisor, 提供rerank模型
        RetrievalRerankAdvisor rerankAdvisor = new RetrievalRerankAdvisor(simpleVectorStore,
                rerankModel, SearchRequest.builder().topK(5).build());

        //2. 把这个advisor 绑定给chatclient
        String content = client.prompt()
                .user("金卡会员打几折")
                .advisors(rerankAdvisor)
                .call()
                .content();
        System.out.println(content);

    }
}
