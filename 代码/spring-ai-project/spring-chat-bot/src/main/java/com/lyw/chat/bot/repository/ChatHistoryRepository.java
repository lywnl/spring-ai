package com.lyw.chat.bot.repository;


import com.lyw.chat.bot.model.ChatInfo;

import java.util.List;

public interface ChatHistoryRepository {

    void save(String chatId, String title);

    void clearByChatId(String chatId);

    List<ChatInfo> getChats();
}
