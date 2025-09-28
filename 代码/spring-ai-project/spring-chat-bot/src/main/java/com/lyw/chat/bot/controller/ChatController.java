package com.lyw.chat.bot.controller;

import com.lyw.chat.bot.model.ChatInfo;
import com.lyw.chat.bot.model.MessageVO;
import com.lyw.chat.bot.repository.ChatHistoryRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient chatClient;

    @Resource
    private ChatHistoryRepository memoryHistoryRepository;

    @Resource
    private ChatMemory chatMemory;

//    public ChatController(ChatClient ollamaChatClient) {
//        this.chatClient = ollamaChatClient;
//    }

    public ChatController(ChatClient deepseekChatClient) {
        this.chatClient = deepseekChatClient;
    }

    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String prompt, String chatId) {
        log.info("prompt: {}, chatId: {}", prompt, chatId);
        memoryHistoryRepository.save(chatId, prompt);

        return chatClient.prompt()
                .user(prompt)
                .advisors(spec -> spec.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }


    /**
     * 获取会话列表
     */
    @RequestMapping("/getChatIds")
    public List<ChatInfo> getChatIds() {
        return memoryHistoryRepository.getChats();
    }

    /**
     * 获取会话记录
     */
    @RequestMapping("/getChatHistory")
    public List<MessageVO> getChatHistory(String chatId) {
        log.info("获取会话记录, charID:{}", chatId);

        List<Message> messages = chatMemory.get(chatId, 20);

        return messages.stream().map(message -> new MessageVO(message)).collect(Collectors.toList());
    }

    /**
     * 删除会话
     * @param chatId
     * @return
     */
    @RequestMapping("/deleteChat")
    public Boolean deleteChat(String chatId){
        log.info("删除会话, chatId:{}", chatId);
        try {
            memoryHistoryRepository.clearByChatId(chatId);
            chatMemory.clear(chatId);
        }catch (Exception e){
            log.error("删除会话失败, chatId:{}", chatId);
            return false;
        }
        return true;
    }
}
