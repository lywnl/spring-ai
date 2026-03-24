package com.lyw.agents.config;

import com.lyw.agents.ChainWorkflow;
import com.lyw.agents.ParallelizationWorkflow;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

//    @Bean
//    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
//        return args -> {
//            new ChainWorkflow(chatClientBuilder.build()).chain(report);
//        };
//    }

    @Bean
    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {

        return args -> {
            List<String> parallelResponse = new ParallelizationWorkflow(chatClientBuilder.build())
                    .parallel(
                            "分析数字化转型对此部门的主要影响和应对建议, 每条不超过两句话. ",
                            List.of(
                                    "销售部：依赖人脉, 缺乏数据工具",
                                    "技术部：系统老旧, 人手紧张",
                                    "人力资源部：员工不适应, 培训不足",
                                    "财务部：控制成本, 担心安全"
                            ),
                            4
                    );

            // 输出结果
            System.out.println("=== 数字化转型简要分析 ===");
            List.of("销售部", "技术部", "人力资源部", "财务部")
                    .forEach(dept ->
                            System.out.println("\n " + dept + "：" + parallelResponse.get(List.of("销售部", "技术部", "人力资源部", "财务部").indexOf(dept)))
                    );
        };
    }

}
