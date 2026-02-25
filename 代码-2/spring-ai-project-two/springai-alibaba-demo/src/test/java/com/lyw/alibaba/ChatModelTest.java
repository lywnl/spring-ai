package com.lyw.alibaba;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatModelTest {

    @Autowired
    DashScopeChatModel dashScopeChatModel;

    @Test
    public void testChatModel() {
        String message = dashScopeChatModel.call("你好");
        System.out.println(message);
    }

}
