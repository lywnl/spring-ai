package com.lyw.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class EvaluatorOptimizer {

    //生成器提示语：用于撰写沟通文案
    public static final String DEFAULT_GENERATOR_PROMPT = """
                  你的任务是帮助用户撰写一段合适的沟通文字, 用于与老师或辅导员交流某个学习相关请求. 
                  如果有之前的尝试和反馈, 请认真反思并进行改进. 
          
                  要求：
                  - 使用礼貌、尊重、诚恳的语气
                  - 理由充分, 体现责任感和规划能力
                  - 避免借口式表达, 突出主动性和成长意愿
          
                  输出必须为单行 JSON 格式, 严格遵循如下模板：
          
                  {"thoughts":"简要说明你的写作思路","response":"具体的沟通内容文本"}
          
                  规则：
                  1. 所有换行用 \\n 表示
                  2. 所有双引号写成 \\\"
                  3. 整个响应在一行内完成, 无真实回车
   
                  示例：
                  {"thoughts":"强调实习对职业发展的价值","response":"老师您好, \\n我想和你沟通一下...\\n感谢您的理解和支持！"}
          
                  请只返回这个 JSON 结构. 
            """;

    //评估器提示语：评估沟通文案质量
    public static final String DEFAULT_EVALUATOR_PROMPT = """
                     请评估以下沟通文案是否适合用于与高校教师或辅导员对话. 
                     从以下几个维度打分：
                     - 礼貌性：是否使用敬语, 态度是否谦逊
                     - 逻辑性：理由是否充分、真实可信
                     - 清晰度：目标是否明确, 结构是否清晰
                     - 合规性：是否符合学校规定, 不鼓吹逃课
                     - 改进建议：指出具体可优化之处
             
                     回复必须是单行 JSON, 格式如下：
             
                     {"evaluation":"PASS, NEEDS_IMPROVEMENT, 或 FAIL", "feedback":"具体评价和建议"}
             
                     evaluation 字段只能取值："PASS" (完全合格) 、"NEEDS_IMPROVEMENT" (基本可行但需调整) 、"FAIL" (不得体或违规) 
                     只有当文案非常得体且无需修改时才可标记为 PASS. 
            """;

    /**
     * 每次生成的结果
     */
    public static record Generation(String thoughts, String response) {
    }

    /**
     * 评估结果
     */
    public static record EvaluationResponse(Evaluation evaluation, String feedback) {

        public enum Evaluation {
            PASS, NEEDS_IMPROVEMENT, FAIL
        }
    }

    /**
     * 最终优化后的响应
     */
    public static record RefinedResponse(String solution, List<Generation> chainOfThought) {}

    private final ChatClient chatClient;

    private final String generatorPrompt;

    private final String evaluatorPrompt;

    public EvaluatorOptimizer(ChatClient chatClient) {
        this(chatClient, DEFAULT_GENERATOR_PROMPT, DEFAULT_EVALUATOR_PROMPT);
    }

    public EvaluatorOptimizer(ChatClient chatClient, String generatorPrompt, String evaluatorPrompt) {
        Assert.notNull(chatClient, "ChatClient 不能为空");
        Assert.hasText(generatorPrompt, "生成器提示不能为空");
        Assert.hasText(evaluatorPrompt, "评估器提示不能为空");

        this.chatClient = chatClient;
        this.generatorPrompt = generatorPrompt;
        this.evaluatorPrompt = evaluatorPrompt;
    }

    /**
     * 启动优化循环
     */
    public RefinedResponse loop(String task) {
        List<String> memory = new ArrayList<>();
        List<Generation> chainOfThought = new ArrayList<>();

        return loop(task, "", memory, chainOfThought);
    }

    private RefinedResponse loop(String task, String context, List<String> memory,
                                 List<Generation> chainOfThought) {

        Generation generation = generate(task, context);
        memory.add(generation.response());
        chainOfThought.add(generation);

        EvaluationResponse evaluationResponse = evalute(generation.response(), task);

        if (evaluationResponse.evaluation().equals(EvaluationResponse.Evaluation.PASS)) {
            // Solution is accepted!
            return new RefinedResponse(generation.response(), chainOfThought);
        }

        // Accumulated new context including the last and the previous attempts and
        // feedbacks.
        StringBuilder newContext = new StringBuilder();
        newContext.append("之前的尝试:");
        for (String m : memory) {
            newContext.append("\n- ").append(m);
        }
        newContext.append("\n最新反馈: ").append(evaluationResponse.feedback());

        return loop(task, newContext.toString(), memory, chainOfThought);
    }

    private Generation generate(String task, String context) {
        Generation generationResponse = chatClient.prompt()
                .user(u -> u.text("{prompt}\n{context}\n当前请求: {task}")
                        .param("prompt", this.generatorPrompt)
                        .param("context", context)
                        .param("task", task))
                .call()
                .entity(Generation.class);

        System.out.println(String.format("\n=== 生成器输出 ===\n思考: %s\n文案: %s\n",
                generationResponse.thoughts(), generationResponse.response()));
        return generationResponse;
    }

    private EvaluationResponse evalute(String content, String task) {

        EvaluationResponse evaluationResponse = chatClient.prompt()
                .user(u -> u.text("{prompt}\n原始请求: {task}\n待评估文案: {content}")
                        .param("prompt", this.evaluatorPrompt)
                        .param("task", task)
                        .param("content", content))
                .call()
                .entity(EvaluationResponse.class);

        System.out.println(String.format("\n=== 评估器输出 ===\n结果: %s\n\n反馈: %s\n",
                evaluationResponse.evaluation(), evaluationResponse.feedback()));
        return evaluationResponse;
    }
}
