# 实验要求 —— Proj2a: Ngordnet（词频分析器）

## 概述

本实验是 UC Berkeley CS 61B 的 Project 2 第一部分。目标是构建一个**基于 Web 的词频分析器**（类似 Google Ngram Viewer），处理 Google Ngram 数据集和 WordNet 语义网络数据。

**技术栈：**
- 后端：Java, Spark（Web 框架）, Gson（JSON）, XChart（图表绘制）
- 前端：HTML, CSS, JavaScript, jQuery
- 测试：JUnit 5, Google Truth

---

## 实验目录结构

```
proj2a/
├── src/
│   ├── browser/
│   │   ├── NgordnetQuery.java        # 浏览器查询参数记录
│   │   ├── NgordnetQueryHandler.java # 查询处理器的抽象基类
│   │   └── NgordnetServer.java       # Spark Web 服务器配置
│   ├── main/
│   │   ├── Main.java                 # 程序入口
│   │   ├── FileReadDemo.java         # 数据文件读取演示
│   │   ├── PlotDemo.java             # 绘图演示
│   │   ├── DummyHistoryHandler.java  # 占位处理器（需替换）
│   │   └── DummyHistoryTextHandler.java # 占位文本处理器（需替换）
│   ├── ngrams/
│   │   ├── TimeSeries.java           # ★ 待实现：时间序列类
│   │   └── NGramMap.java            # ★ 待实现：NGram 数据查询类
│   ├── plotting/
│   │   └── Plotter.java             # 图表绘制工具类
│   └── utils/
│       └── Utils.java               # 数据文件路径常量
├── static/
│   ├── ngordnet.html                # 完整版前端页面
│   ├── ngordnet_2a.html             # Proj2a 专用前端页面
│   ├── ngordnet.css                 # 样式
│   ├── ngordnet.js                  # 前端交互逻辑
│   ├── blank.png                    # 占位图片
│   └── jquery.min.js                # jQuery 库
└── tests/
    ├── TimeSeriesTest.java          # ★ 时间序列测试
    ├── NGramMapTest.java            # ★ NGramMap 测试
    └── HistoryTextHandlerTest.java  # 文本处理器测试（已注释）
```

---

## 需要实现的内容

### 1. TimeSeries（时间序列类）

继承自 `TreeMap<Integer, Double>`，将年份映射到数值数据。

| 方法 | 要求 |
|------|------|
| `TimeSeries(TimeSeries ts, int startYear, int endYear)` | 拷贝构造函数，仅保留 `[startYear, endYear]` 范围内的数据（含两端） |
| `years()` | 返回所有年份的 `List<Integer>` |
| `data()` | 返回所有数据的 `List<Double>`，顺序必须与 `years()` 一致 |
| `plus(TimeSeries ts)` | 按年求和：对每个年份，将两个 TimeSeries 的数据相加。若某年份仅在一个 TimeSeries 中存在，则直接使用该值。返回新 TimeSeries（不修改原数据） |
| `dividedBy(TimeSeries ts)` | 按年求商：用 `this` 的数据除以 `ts` 的数据。若 `ts` 缺少 `this` 中存在的年份，抛出 `IllegalArgumentException`；若 `ts` 有多余年份，忽略。返回新 TimeSeries |

### 2. NGramMap（NGram 数据查询类）

核心数据结构，加载并查询 Google Ngram 数据集。

| 方法 | 要求 |
|------|------|
| `NGramMap(String wordsFilename, String countsFilename)` | 构造函数，加载单词文件和总量文件 |
| `countHistory(String word)` | 返回该单词每年的原始出现次数（**防御性拷贝**） |
| `countHistory(String word, int startYear, int endYear)` | 返回指定年份范围内的原始出现次数（防御性拷贝） |
| `totalCountHistory()` | 返回每年所有单词的总出现次数（防御性拷贝） |
| `weightHistory(String word)` | 返回每年相对频率 = `count / totalCount` |
| `weightHistory(String word, int startYear, int endYear)` | 返回指定年份范围内的相对频率 |
| `summedWeightHistory(Collection<String> words)` | 返回多个单词每年相对频率之和 |
| `summedWeightHistory(Collection<String> words, int startYear, int endYear)` | 返回指定年份范围内多个单词相对频率之和 |

### 3. Web 处理器（替换占位处理器）

- **`/history` 端点**：接收 `NgordnetQuery`，查询 `NGramMap` 获取词频数据，用 `Plotter` 绘制折线图，返回 Base64 编码的 PNG 图片字符串
- **`/historytext` 端点**：接收 `NgordnetQuery`，查询 `NGramMap` 获取词频数据，返回格式化文本

### 4. Main.java 修改

- 创建 `NGramMap` 实例
- 将占位处理器替换为真实处理器

---

## 数据文件

通过 `Utils.java` 中的常量引用，所有文件位于 `./data/ngrams/` 目录下：

| 常量 | 文件名 | 说明 |
|------|--------|------|
| `SHORT_WORDS_FILE` | `very_short.csv` | 小规模测试数据 |
| `TOP_14337_WORDS_FILE` | `top_14377_words.csv` | 前 14337 个常见单词数据 |
| `TOTAL_COUNTS_FILE` | `total_counts.csv` | 每年单词总量数据 |

### 数据格式

**单词文件**（以 tab 分隔）：
```
word    year    count  volume_count
airport  2007   175     5
```

**总量文件**（以逗号分隔）：
```
year,total_words
2005,1000000000
```

---

## 测试

运行测试：
```bash
# 在项目根目录运行
# 使用 IDE（IntelliJ）直接运行 JUnit 测试
```

### TimeSeriesTest

| 测试 | 描述 |
|------|------|
| `testFromSpec()` | 测试 cat(1991:0, 1992:100, 1994:200) + dog(1994:400, 1995:500) = 1991:0, 1992:100, 1994:600, 1995:500 |
| `testEmptyBasic()` | 测试空的 TimeSeries 的 `years()` 和 `data()` 返回空列表；空+空=空 |

### NGramMapTest

| 测试 | 描述 |
|------|------|
| `testCountHistory()` | 使用 `SHORT_WORDS_FILE` 测试 `countHistory("request")` 返回 2005-2008 的数据，以及过滤年份范围 2006-2007 |
| `testOnLargeFile()` | 使用 `TOP_14337_WORDS_FILE` 测试大规模数据，验证 fish(1865)=136497, fish(1922)=444924, 总量文件数据, weight 计算, 多词求和 |

---

## 运行

```bash
# 运行 Web 服务器
# 在 IntelliJ 中运行 Main.java
# 访问 http://localhost:4567/ngordnet_2a.html
```

---

## 关键提示

1. **防御性拷贝**：`countHistory` 等方法应返回数据的副本，而非内部引用
2. **年份范围**：假设年份参数在 1400–2100 之间（`TimeSeries.MIN_YEAR` / `MAX_YEAR`）
3. **数据加载**：参考 `FileReadDemo.java` 了解如何读取数据文件
4. **绘图示例**：参考 `PlotDemo.java` 了解如何使用 `Plotter` 和 `NGramMap`
