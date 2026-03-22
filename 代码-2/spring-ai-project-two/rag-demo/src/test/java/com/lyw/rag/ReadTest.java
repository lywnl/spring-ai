package com.lyw.rag;


import com.alibaba.cloud.ai.reader.mysql.MySQLDocumentReader;
import com.alibaba.cloud.ai.reader.mysql.MySQLResource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
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

    @Test
    void markdownReaderTest() {
        MarkdownDocumentReaderConfig config =
                MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", "code.md")
                        .build();

        MarkdownDocumentReader reader = new MarkdownDocumentReader("classpath:/file/code.md", config);
        List<Document> documents = reader.get();
        System.out.printf("documents: %d\n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void pdfReaderTest() {
        PagePdfDocumentReader reader = new PagePdfDocumentReader("classpath:/file/sample1.pdf",
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        List<Document> documents = reader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void pdfReaderTest2() {
        ParagraphPdfDocumentReader reader = new ParagraphPdfDocumentReader("classpath:/file/sample1.pdf",
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        List<Document> documents = reader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void tikaReaderTest(@Value("classpath:/file/tika.docx")Resource resource) {
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        List<Document> documents = reader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void tikaReaderTest2(@Value("classpath:/file/ppt-sample.pptx")Resource resource) {
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        List<Document> documents = reader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void tikaReaderTest3(@Value("classpath:/file/sample1.pdf")Resource resource) {
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        List<Document> documents = reader.get();
        System.out.println("documents size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void testReadMysql() {
        String query = "SELECT * FROM user LIMIT 10;";
        List<String> columns = List.of("Host", "User", "Select_priv");
        MySQLResource mysqlResource = new MySQLResource("127.0.0.1", 3306,"mysql","root", "liuyvanwang", query, columns, columns);

        MySQLDocumentReader reader = new MySQLDocumentReader(mysqlResource);
        List<Document> documents = reader.get();
        System.out.printf("documents: %d\n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }



}
