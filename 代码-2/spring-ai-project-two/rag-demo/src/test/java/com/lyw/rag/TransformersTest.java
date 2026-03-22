package com.lyw.rag;


import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher.SummaryType;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;




import java.util.List;

@SpringBootTest
public class TransformersTest {

    @Test
    void testSplitter(@Value("classpath:/file/rule.txt") Resource resource) {
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();
        System.out.println("加载文档的大小: " + documents.size());

//        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(500, 300, 5, 5000, true);//全参构造
        List<Document> documentsSplit = tokenTextSplitter.apply(documents);
        System.out.println("分段后的文档大小: " + documentsSplit.size());
        documentsSplit.forEach(document -> {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
                }
        );
    }


    @Test
    void testEnricher(@Autowired ChatModel chatModel, @Value("classpath:/file/rule.txt") Resource resource) {
        //加载文档
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();
        System.out.println("加载文档的大小: " + documents.size());

        //文档分块
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(500, 300, 5, 5000, true);//全参构造
        List<Document> documentsSplit = tokenTextSplitter.apply(documents);
        System.out.println("分段后的文档大小: " + documentsSplit.size());

        //关键词生成
//        KeywordMetadataEnricher keywordMetadataEnricher = KeywordMetadataEnricher.builder(chatModel)
//                .keywordCount(2).build();
        KeywordMetadataEnricher keywordMetadataEnricher = KeywordMetadataEnricher.builder(chatModel)
                .keywordsTemplate(new PromptTemplate("根据给定的文本：{context_str}，生成关键字，只允许以下关键字" +
                        " [会员宗旨，会员类型，会员注册，积分制度，会员权益，会员行为，会员服务，公告，隐私保护] " +
                        "只返回关键字，其他信息不返回")).build();
        List<Document> enricher = keywordMetadataEnricher.apply(documentsSplit);
        System.out.println("关键词生成后的文档大小: " + enricher.size());

        enricher.forEach(document -> {
                    System.out.println(document.getText());
                    System.out.println(document.getMetadata());
                }
        );
    }


    @Test
    void testSummary(@Autowired ChatModel chatModel, @Value("classpath:/file/rule.txt") Resource resource) {
        //加载文档
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();

        //文档分块
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(500, 300, 5, 5000, true);//全参构造
        List<Document> documentsSplit = tokenTextSplitter.apply(documents);

//        SummaryMetadataEnricher summaryMetadataEnricher = new  SummaryMetadataEnricher(chatModel,
//                List.of(SummaryType.PREVIOUS, SummaryType.CURRENT, SummaryType.NEXT));
        String summaryTemplate = """
        根据给定的文本：
        {context_str}
        生成摘要信息，限制在50字以内。只返回摘要信息，其他信息不返回
        """;
        SummaryMetadataEnricher summaryMetadataEnricher = new SummaryMetadataEnricher(chatModel,
                List.of(SummaryType.PREVIOUS, SummaryType.CURRENT, SummaryType.NEXT),
                summaryTemplate, MetadataMode.NONE);

        List<Document> enricher = summaryMetadataEnricher.apply(documentsSplit);
        enricher.forEach(document -> {
                    System.out.println(document.getText());
                    System.out.println(document.getMetadata());
                }
        );
    }
}
