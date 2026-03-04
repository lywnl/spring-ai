
import subprocess
import sys

# 自动安装 python-docx
subprocess.check_call([sys.executable, "-m", "pip", "install", "python-docx", "-q"])

from docx import Document
from docx.shared import Pt, RGBColor, Inches, Cm
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml.ns import qn
from docx.oxml import OxmlElement
import os
from datetime import datetime

doc = Document()

# ===== 页面设置 =====
section = doc.sections[0]
section.page_width = Inches(8.27)
section.page_height = Inches(11.69)
section.left_margin = Cm(2.5)
section.right_margin = Cm(2.5)
section.top_margin = Cm(2.5)
section.bottom_margin = Cm(2.5)

# ===== 标题 =====
title = doc.add_heading('2025年全球AI大模型排行榜', level=0)
title.alignment = WD_ALIGN_PARAGRAPH.CENTER
title_run = title.runs[0]
title_run.font.color.rgb = RGBColor(0x1A, 0x56, 0xDB)
title_run.font.size = Pt(22)

# 副标题
sub = doc.add_paragraph()
sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
sub_run = sub.add_run(f'数据更新时间：{datetime.now().strftime("%Y年%m月%d日")}  |  来源：综合公开评测数据')
sub_run.font.size = Pt(10)
sub_run.font.color.rgb = RGBColor(0x6B, 0x72, 0x80)

doc.add_paragraph()

# ===== 综合榜单表格 =====
doc.add_heading('全球综合排名TOP 10', level=1)

ranking_data = [
    ('排名', '模型名称', '开发机构', '智能指数', '亮点特性'),
    ('1', 'Gemini 2.5 Pro', 'Google', '92.5', '原生多模态、百万级上下文窗口'),
    ('2', 'GPT-4o / GPT-5', 'OpenAI', '91.8', '综合能力均衡、网络搜索集成'),
    ('3', 'Claude Opus 4.5', 'Anthropic', '90.9', '安全合规、91.2 HumanEval编程得分'),
    ('4', 'DeepSeek V3', '深度求索（中国）', '88.6', '性价比极高，0.32美元/百万token'),
    ('5', 'Kimi K2 Thinking', '月之暗面（中国）', '87.3', '长文档处理、法律条文分析'),
    ('6', 'Qwen2.5-Max', '阿里云（中国）', '86.9', '数学/编程单项榜首、多语言领先'),
    ('7', 'Gemma 3 27B', 'Google', '85.2', '轻量级开源、端侧部署优秀'),
    ('8', 'LLaMA 3.3 70B', 'Meta', '84.7', '全开源、插件生态庞大'),
    ('9', 'Doubao-1.5-Pro', '字节跳动（中国）', '83.9', '实时语音交互、稀疏MoE架构'),
    ('10', 'Command R+', 'Cohere', '82.5', '企业RAG优化、128K上下文'),
]

table = doc.add_table(rows=len(ranking_data), cols=5)
table.style = 'Table Grid'

for row_idx, row_data in enumerate(ranking_data):
    row = table.rows[row_idx]
    for col_idx, cell_text in enumerate(row_data):
        cell = row.cells[col_idx]
        cell.text = cell_text
        para = cell.paragraphs[0]
        run = para.runs[0] if para.runs else para.add_run(cell_text)
        if row_idx == 0:
            run.font.bold = True
            run.font.size = Pt(10)
            run.font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)
            tc = cell._tc
            tcPr = tc.get_or_add_tcPr()
            shd = OxmlElement('w:shd')
            shd.set(qn('w:val'), 'clear')
            shd.set(qn('w:color'), 'auto')
            shd.set(qn('w:fill'), '1A56DB')
            tcPr.append(shd)
        else:
            run.font.size = Pt(9.5)
            if row_idx % 2 == 0:
                tc = cell._tc
                tcPr = tc.get_or_add_tcPr()
                shd = OxmlElement('w:shd')
                shd.set(qn('w:val'), 'clear')
                shd.set(qn('w:color'), 'auto')
                shd.set(qn('w:fill'), 'EBF5FB')
                tcPr.append(shd)
        para.alignment = WD_ALIGN_PARAGRAPH.CENTER if col_idx in [0, 3] else WD_ALIGN_PARAGRAPH.LEFT

doc.add_paragraph()

# ===== 国产模型专题 =====
doc.add_heading('国产AI大模型专题排名', level=1)

china_data = [
    ('排名', '模型', '机构', '核心优势', '适用场景'),
    ('1', 'DeepSeek V3/R1', '深度求索', '超强性价比，推理速度提升3倍', '复杂推理、中文长文本'),
    ('2', 'Qwen2.5-Max/Qwen3', '阿里云', '数学/编程双料冠军，多语言领先', '数学推理、代码生成'),
    ('3', 'Kimi K2 Thinking', '月之暗面', '7.5万字长文本，法律条文分析', '长文档理解、法律金融'),
    ('4', '文心一言 4.0', '百度', 'MMLU中文第一，情感识别92%', '营销内容、政务问答'),
    ('5', '豆包 1.5 Pro', '字节跳动', '语音实时交互领先，成本低', '语音助手、实时交互'),
    ('6', 'GLM-4', '清华/智谱AI', '首个视频通话千亿参数，人机交互自然', '视频多模态、创意内容'),
    ('7', '讯飞星火', '科大讯飞', '30+语言交互，语音行业标杆', '语音识别/合成、教育'),
]

table2 = doc.add_table(rows=len(china_data), cols=5)
table2.style = 'Table Grid'

for row_idx, row_data in enumerate(china_data):
    row = table2.rows[row_idx]
    for col_idx, cell_text in enumerate(row_data):
        cell = row.cells[col_idx]
        cell.text = cell_text
        para = cell.paragraphs[0]
        run = para.runs[0] if para.runs else para.add_run(cell_text)
        if row_idx == 0:
            run.font.bold = True
            run.font.size = Pt(10)
            run.font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)
            tc = cell._tc
            tcPr = tc.get_or_add_tcPr()
            shd = OxmlElement('w:shd')
            shd.set(qn('w:val'), 'clear')
            shd.set(qn('w:color'), 'auto')
            shd.set(qn('w:fill'), 'C0392B')
            tcPr.append(shd)
        else:
            run.font.size = Pt(9.5)
            if row_idx % 2 == 0:
                tc = cell._tc
                tcPr = tc.get_or_add_tcPr()
                shd = OxmlElement('w:shd')
                shd.set(qn('w:val'), 'clear')
                shd.set(qn('w:color'), 'auto')
                shd.set(qn('w:fill'), 'FDEDEC')
                tcPr.append(shd)
        para.alignment = WD_ALIGN_PARAGRAPH.CENTER if col_idx == 0 else WD_ALIGN_PARAGRAPH.LEFT

doc.add_paragraph()

# ===== 基准测试说明 =====
doc.add_heading('主要评测基准说明', level=1)

benchmarks = [
    ('LMSYS Chatbot Arena', '通过众包用户盲测实时更新，最能反映真实用户体验的模型排名平台'),
    ('HumanEval', '代码生成能力测试，Claude 3.7 Sonnet以91.2分领先'),
    ('MMLU', '多学科综合理解，文心一言4.0在中文MMLU中排名第一'),
    ('TheoremQA', '数学定理证明推理，Qwen2.5-Max在数学单项中排名第一'),
    ('Hugging Face Open LLM Leaderboard', '开源模型专属评测平台，DeepSeek R1开源榜第一'),
]

for name, desc in benchmarks:
    p = doc.add_paragraph(style='List Bullet')
    run_name = p.add_run(name + '：')
    run_name.bold = True
    run_name.font.color.rgb = RGBColor(0x1A, 0x56, 0xDB)
    run_desc = p.add_run(desc)
    run_desc.font.size = Pt(10)

doc.add_paragraph()

# ===== 趋势总结 =====
doc.add_heading('2025年AI大模型发展趋势', level=1)

trends = [
    '技术突破加速：多模态能力成为标配，图像、音频、视频输入全面普及',
    '国产模型崛起：DeepSeek、Qwen、Kimi等在性价比和特定能力上超越国际竞品',
    '长上下文成标准：百万级Token上下文窗口逐渐普及，支持超长文档处理',
    '推理成本大幅下降：通过MoE架构等技术，推理成本降低数十倍',
    '开源生态繁荣：LLaMA、DeepSeek开源版本带动社区生态快速发展',
    '竞争维度转变：从参数规模竞争转向场景渗透力和生态共建能力',
]

for trend in trends:
    p = doc.add_paragraph(style='List Bullet')
    run = p.add_run(trend)
    run.font.size = Pt(10)

# ===== 页脚说明 =====
doc.add_paragraph()
footer_p = doc.add_paragraph()
footer_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
footer_run = footer_p.add_run('本文档由 AI 自动搜索整理生成，数据来源于公开评测平台及技术媒体，仅供参考。')
footer_run.font.size = Pt(9)
footer_run.font.color.rgb = RGBColor(0x9C, 0xA3, 0xAF)
footer_run.font.italic = True

# ===== 保存文件 =====
desktop = os.path.join(os.path.expanduser('~'), 'Desktop')
output_path = os.path.join(desktop, '2025年AI大模型排行榜.docx')
doc.save(output_path)
print(f"文档已成功保存到：{output_path}")
