package com.lyw.agents;

import org.springframework.ai.chat.client.ChatClient;

/**
 * 实现"提示链" (Prompt Chaining) 工作流模式：
 * 将复杂任务分解为多个连续的 LLM 调用, 每一步处理上一步的输出.
 *
 * <p>
 * 示例流程 (分析季度业绩中的关键指标) ：
 * <ol>
 *   <li>提取数值和对应指标</li>
 *   <li>统一转换为百分比格式</li>
 *   <li>按数值从高到低排序</li>
 *   <li>生成 Markdown 表格</li>
 * </ol>
 *
 * <p/>
 * 适用场景：当任务可清晰拆解为多个子步骤时使用. 通过分步降低单次推理难度, 提升准确率, 牺牲一点延迟换取更高质量输出.
 *
 * @author 改编自 Christian Tzolov (中文本地化)
 */
public class ChainWorkflow {

    /**
     * 四个阶段的系统提示语 (中文) , 定义每一步的数据转换规则
     */
    private static final String[] DEFAULT_SYSTEM_PROMPTS = {
            // 第一步：提取数值与指标
            """
                请从文本中提取所有数字及其对应的业务指标. 
                每行只写一个：'数值: 指标名称'
                示例格式：
                92: 客户满意度
                45%: 收入增长率
                """,

            // 第二步：标准化为百分比
            """
                将所有数值统一转换为百分比格式. 
                - 若是"点"或"分", 直接视为百分比 (如 87分 → 87%) 
                - 非百分比的小数也转为百分比 (如 0.78 → 78%) 
                - 金额类、用户数等非比例数据保留原值并标注[跳过]
                每行保持 '数值%: 指标名称' 或 '数值[跳过]: 指标名称'
                示例：
                92%: 客户满意度
                45%: 收入增长率
                $43[跳过]: 新客获取成本
                """,

            // 第三步：降序排列
            """
                将所有以百分比表示的指标按数值从高到低排序. 
                跳过的条目 (带[跳过]) 放在最后, 不参与排序. 
                格式保持不变：
                92%: 客户满意度
                78%: 产品使用率
                $43[跳过]: 新客获取成本
                """,

            // 第四步：生成表格
            """
                将排序后的数据整理成 Markdown 表格, 包含两列：
                | 指标 | 数值 |
                |:---|--:|
                示例行：
                | 客户满意度 | 92% |
                | 新客获取成本 | $43 [未转换] |
                注意：跳过的项目显示为原始值 + [未转换]
                """
    };

    private final ChatClient chatClient;
    private final String[] systemPrompts;

    public ChainWorkflow(ChatClient chatClient) {
        this(chatClient, DEFAULT_SYSTEM_PROMPTS);
    }

    public ChainWorkflow(ChatClient chatClient, String[] systemPrompts) {
        this.chatClient = chatClient;
        this.systemPrompts = systemPrompts;
    }

    /**
     * 执行提示链流程：输入 → 提取 → 标准化 → 排序 → 表格化
     *
     * @param userInput 用户输入的原始文本
     * @return 最终结构化输出 (Markdown 表格)
     */
    public String chain(String userInput) {
        int step = 0;
        String response = userInput;

        System.out.println("✅ 开始执行提示链工作流\n");
        System.out.println(String.format("STEP %d - 原始输入:\n%s", step++, response));

        for (String prompt : systemPrompts) {
            String input = String.format("%s\n--- 输入内容 ---\n%s", prompt, response);

            response = chatClient.prompt(input).call().content();

            System.out.println(String.format("\nSTEP %d - 处理结果:\n%s", step++, response));
        }

        return response;
    }
}
