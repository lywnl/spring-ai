package com.lyw.rag;


import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class ReadTest {

    @Test
    void jsonReaderTest(@Value("classpath:/file/web.json") Resource resource) {
        JsonReader jsonReader = new JsonReader(resource);
        List<Document> documents = jsonReader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void jsonReaderTest2(@Value("classpath:/file/web.json") Resource resource) {
        JsonReader jsonReader = new JsonReader(resource, "sites"); //读取指定键sites
        List<Document> documents = jsonReader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void jsonReaderTest3(@Value("classpath:/file/webArray.json") Resource resource) {
        JsonReader jsonReader = new JsonReader(resource);
        List<Document> documents = jsonReader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void jsonReaderTest4(@Value("classpath:/file/webArray.json") Resource resource) {
        JsonReader jsonReader = new JsonReader(resource, "brand");
        List<Document> documents = jsonReader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }


    @Test
    void textReaderTest(@Value("classpath:/file/text.txt") Resource resource) {
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void textReaderTest2() {
        TextReader textReader = new TextReader("classpath:/file/text.txt");
        List<Document> documents = textReader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void textReaderTest3() {
        TextReader textReader = new TextReader("classpath:/file/text.txt");
        textReader.getCustomMetadata().put("test", "test content");
        List<Document> documents = textReader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void htmlReaderTest() {
        JsoupDocumentReader jsoupDocumentReader = new JsoupDocumentReader("classpath:/file/my-page.html");
        List<Document> documents = jsoupDocumentReader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void htmlReaderTest2() {
        JsoupDocumentReaderConfig config = JsoupDocumentReaderConfig.builder()
                .selector("article p") // 指定从中提取文本的元素 (默认为"body")
                .includeLinkUrls(true) // Include link URLs in metadata
                .metadataTags(List.of("author", "date")) // Extract author and date meta tags
                .additionalMetadata("source", "my-page.html") // Add custom metadata
                .build();

        JsoupDocumentReader jsoupDocumentReader = new JsoupDocumentReader("classpath:/file/my-page.html", config);
        List<Document> documents = jsoupDocumentReader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

}
