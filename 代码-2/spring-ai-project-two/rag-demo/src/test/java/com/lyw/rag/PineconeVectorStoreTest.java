package com.lyw.rag;


import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pinecone.PineconeVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class PineconeVectorStoreTest {

    @Autowired
    private PineconeVectorStore pineconeVectorStore;

    @Test
    void testPineconeVectorStore(@Value("classpath:/file/rule.txt") Resource resource) {
        //读取文档
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();

        //文档分割
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splitterDoc = splitter.apply(documents);

        //加载文档到redis向量存储
        pineconeVectorStore.add(splitterDoc);
        System.out.println("向量存储加载文档完成");

        //相似性检索
        SearchRequest searchRequest = SearchRequest.builder()
                .query("会员") //查询的文本
                .topK(2) //匹配文档的前N个
                .build();
        List<Document> searchResults = pineconeVectorStore.similaritySearch(searchRequest);
        searchResults.forEach(System.out::println);
        System.out.println("相似性检索完成");
    }


}
