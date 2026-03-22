package com.lyw.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class WriterTest {

    private SimpleVectorStore vectorStore;

    @Autowired
    public WriterTest(EmbeddingModel embeddingModel) {
        this.vectorStore = SimpleVectorStore.builder(embeddingModel).build();
    }

    //文件写入
    @Test
    void testFileWritter(@Value("classpath:/file/rule.txt") Resource resource){
        //读取文档
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        //文档分割
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splitterDoc = splitter.apply(documents);

        //文档写入
        FileDocumentWriter writer = new FileDocumentWriter("output.txt",
                true, MetadataMode.ALL, true);
        writer.accept(splitterDoc);
        System.out.println("文档写入完成");
    }

    //存入向量数据库
    @Test
    void testSimpleVectorWritter(@Value("classpath:/file/rule.txt") Resource resource){
        //读取文档
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        //文档分割
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splitterDoc = splitter.apply(documents);

        //文档写入向量数据库
        vectorStore.accept(splitterDoc);
        System.out.println("文档写入完成");
    }



}
