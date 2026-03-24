package com.lyw.agents.config;

import com.lyw.agents.ChainWorkflow;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfig {

    String report = """
                     Q3 季度业务总结：
                     本季度客户满意度提升至92分.\s
                     收入同比增长45%.\s
                     主要市场的市场份额达到23%.\s
                     客户流失率从8%下降至5%.\s
                     新客户获取成本为每名用户43元.\s
                     产品使用率达到78%.\s
                     员工满意度评分为87分.\s
                     运营利润率提升至34%.\s
            """;

    @Bean
    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
        return args -> {
            new ChainWorkflow(chatClientBuilder.build()).chain(report);
        };
    }

}
