package browser;

import java.util.List;

/**
 * Created by hug.
 */
public record NgordnetQuery(
        List<String> words,     // 用户要查询的单词表
        int startYear,          // 起始年份
        int endYear,            // 结束年份
        int k                   // 返回k个相关词
) {}
