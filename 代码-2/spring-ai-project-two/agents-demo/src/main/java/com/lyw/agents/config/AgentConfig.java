package com.lyw.agents.config;

import com.lyw.agents.ChainWorkflow;
import com.lyw.agents.OrchestratorWorkers;
import com.lyw.agents.ParallelizationWorkflow;
import com.lyw.agents.RoutingWorkflow;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

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

//    @Bean
//    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
//
//        return args -> {
//            List<String> parallelResponse = new ParallelizationWorkflow(chatClientBuilder.build())
//                    .parallel(
//                            "分析数字化转型对此部门的主要影响和应对建议, 每条不超过两句话. ",
//                            List.of(
//                                    "销售部：依赖人脉, 缺乏数据工具",
//                                    "技术部：系统老旧, 人手紧张",
//                                    "人力资源部：员工不适应, 培训不足",
//                                    "财务部：控制成本, 担心安全"
//                            ),
//                            4
//                    );
//
//            // 输出结果
//            System.out.println("=== 数字化转型简要分析 ===");
//            List.of("销售部", "技术部", "人力资源部", "财务部")
//                    .forEach(dept ->
//                            System.out.println("\n " + dept + "：" + parallelResponse.get(List.of("销售部", "技术部", "人力资源部", "财务部").indexOf(dept)))
//                    );
//        };
//    }


//    @Bean
//    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
//
//        return args -> {
//            // 定义四个简单明了的处理路线 (更短的提示词)
//            Map<String, String> simpleRoutes = Map.of(
//                    "财务",
//                    "你是财务助手, 请回答账单、扣费、退款等问题. 开头写【财务】. ",
//
//                    "技术",
//                    "你是技术支持, 请回答登录、功能使用、系统错误等问题. 开头写【技术】. ",
//
//                    "账户",
//                    "你是账户助手, 请处理账号找回、密码重置、安全验证等问题. 开头写【账户】. ",
//
//                    "产品",
//                    "你是产品顾问, 请回答功能咨询、如何使用、推荐操作等问题. 开头写【产品】. "
//            );
//
//            // 模拟三个非常简短的用户提问 (贴近日常对话)
//            List<String> simpleTickets = List.of(
//                    "我忘记密码了, 登不进去账号",           // → 应路由到"账户"
//                    "上个月怎么多扣了50块钱? ",            // → 应路由到"财务"
//                    "导出数据的功能在哪? 怎么用? "         // → 应路由到"产品"
//            );
//
//            var routerWorkflow = new RoutingWorkflow(chatClientBuilder.build());
//
//            int i = 1;
//            for (String ticket : simpleTickets) {
//                System.out.println("\n💬 问题 " + i++);
//                System.out.println("------------------------------");
//                System.out.println(ticket);
//                System.out.println("------------------------------");
//
//                String response = routerWorkflow.route(ticket, simpleRoutes);
//                System.out.println(response);
//            }
//        };
//    }


    @Bean
    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
        var chatClient = chatClientBuilder.build();
        return args -> {

            OrchestratorWorkers.FinalResponse response = new OrchestratorWorkers(chatClient)
                    .process("为一款新型环保可重复使用的保温杯撰写产品介绍文案");
            // 打印最终结果
            System.out.println("\n\n最终聚合结果：\n");
            for (int i = 0; i < response.workerResponses().size(); i++) {
                String style = response.workerResponses().get(i).startsWith("【专业版】") ? "专业" : "亲民";
                System.out.println(" [" + style + "风格] 输出：\n" + response.workerResponses().get(i) + "\n");
            }
        };
    }


}
