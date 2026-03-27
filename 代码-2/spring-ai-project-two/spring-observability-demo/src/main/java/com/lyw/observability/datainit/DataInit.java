package com.lyw.observability.datainit;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInit {

    @Autowired
    private VectorStore vectorStore;

    @Value("classpath:/file/rule.txt")
    private Resource resource;
//    @PostConstruct
    void init() {
        //读取文档
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();

        //文档分割
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splitterDoc = splitter.apply(documents);

        //加载文档到redis向量存储
        vectorStore.add(splitterDoc);
        System.out.println("向量存储加载文档完成");
    }
}
