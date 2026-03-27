package com.lyw.observability.controller;


import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestController {

    @Autowired
    private ChatModel chatModel;


    @Autowired
    private VectorStore vectorStore;

    @RequestMapping("/chat")
    public String chat(String message) {
        return chatModel.call(message);
    }

    @RequestMapping("/search")
    public String search(String query){
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(5)
                .similarityThreshold(0.3)
                .build();
        List<Document> documents = vectorStore.similaritySearch(request);
        System.out.println(documents);
        return documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining(";"));
    }

}
