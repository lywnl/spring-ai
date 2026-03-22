package com.lyw.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class RedisVectorStoreTest {

    @Autowired
    private RedisVectorStore redisVectorStore;

    @Test
    void testRedisVectorStore(@Value("classpath:/file/rule.txt") Resource resource) {
        //读取文档
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();

        //文档分割
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splitterDoc = splitter.apply(documents);

        //加载文档到redis向量存储
        redisVectorStore.add(splitterDoc);
        System.out.println("向量存储加载文档完成");

//        //相似性检索
//        SearchRequest searchRequest = SearchRequest.builder()
//                .query("大语言模型") //查询的文本
//                .topK(5) //匹配文档的前N个
//                .similarityThreshold(0.6) //相似度分数大于等于0.5
//                .build();
//        List<Document> searchResults = redisVectorStore.similaritySearch(searchRequest);
//        searchResults.forEach(System.out::println);
    }
}
