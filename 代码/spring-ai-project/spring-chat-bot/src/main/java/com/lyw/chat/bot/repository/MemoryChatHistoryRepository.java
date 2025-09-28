package com.lyw.chat.bot.repository;

import com.lyw.chat.bot.model.ChatInfo;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemoryChatHistoryRepository implements ChatHistoryRepository{

    private Map<String, String> chatInfos = new LinkedHashMap<>();

    /**
     * 保存会话记录
     * 新会话 则新增 已存在则更新
     * @param chatId
     * @param title
     */
    @Override
    public void save(String chatId, String title) {
        chatInfos.put(chatId, title);
    }

    @Override
    public void clearByChatId(String chatId) {
        chatInfos.remove(chatId);
    }

    @Override
    public List<ChatInfo> getChats() {
        return chatInfos.entrySet().stream()
                .map(e -> new ChatInfo(e.getKey(), e.getValue())).toList();
    }
}
