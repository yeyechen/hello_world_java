package 左程云体系学习班;

import java.util.HashMap;
import java.util.Map;

public class Lecture19 {
  /*
   * 动态规划
   *
   * 1. 背包问题(knapsack problem)：给定两个长度都为N的数组weights和values，
   * weights[i]和values[i]分别代表 i号物品的重量和价值
   * 给定一个正数bag，表示一个载重bag的袋子，装的物品不能超过这个重量，返回能装下的最大价值。
   *
   * 2. 字母转换问题：规定1和A对应、2和B对应、3和C对应...26和Z对应
   * 那么一个数字字符串比如"111”就可以转化为:"AAA"、"KA"和"AK"
   * 给定一个只有数字字符组成的字符串str，返回有多少种转化结果
   *
   * 3. 贴纸拼词(leetcode 691)：我们有 n 种不同的贴纸。每个贴纸上都有一个小写的英文单词。
   * 您想要拼写出给定的字符串target，方法是从收集的贴纸中切割单个字母并重新排列它们。如果你愿意，你可以多次使用每个贴纸，每个贴纸的数量是无限的。
   * 返回你需要拼出target的最小贴纸数量。如果任务不可能，则返回-1。
   *
   * 4. 最长公共子序列(leetcode 1143)：给定两个字符串text1 和text2，返回这两个字符串的最长公共子序列的长度。如果不存在公共子序列 ，返回0。
   * 一个字符串的子序列是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串。
   * 例如，"ace" 是 "abcde" 的子序列，但 "aec" 不是 "abcde" 的子序列。
   * */

  // 1. 背包问题(knapsack problem)
  public static int maxVlaue(int[] w, int[] v, int bag) {
    if (w == null || v == null || w.length != v.length || w.length == 0) {
      return 0;
    }
    return knapsackProcess(w, v, 0, bag);
  }

  private static int knapsackProcess(int[] w, int[] v, int index, int restBagVol) {
    if (restBagVol < 0) {
      return -1;
    }
    if (index == w.length) {
      return 0;
    }
    // 情况1：没要当前index的物品
    int p1 = knapsackProcess(w, v, index + 1, restBagVol);
    // 情况2：要了当前index的物品，但是需要判断要了这个物品是否超重，如果是，则不能要
    int next = knapsackProcess(w, v, index + 1, restBagVol - w[index]);
    int p2 = next != -1 ? v[index] + next : 0;
    return Math.max(p1, p2);
  }

  // 动态规划
  public static int maxValueDp(int[] w, int[] v, int bag) {
    if (w == null || v == null || w.length != v.length || w.length == 0) {
      return 0;
    }
    int[][] dp = new int[w.length + 1][bag + 1];
    // 动态规划表最下面一行一定是0因为(index, restBagVol), index == w.length -> 0
    for (int index = w.length - 1; index >= 0; index--) {
      // 从倒数第二行开始，每一个数据只依赖自己下面行的数据
      for (int restBagVol = 0; restBagVol <= bag; restBagVol++) {
        int p1 = dp[index + 1][restBagVol];
        int p2 = restBagVol - w[index] >= 0 ? v[index] + dp[index + 1][restBagVol - w[index]] : 0;
        dp[index][restBagVol] = Math.max(p1, p2);
      }
    }
    return dp[0][bag];
  }

  // 2.字母转换问题
  public static int translate(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    return translateProcess(s, 0);
  }

  // 在index往后，有多少种转换方法
  private static int translateProcess(String s, int index) {
    // index到最后了，说明找到了一种方法
    if (index == s.length()) {
      return 1;
    }
    // index还没有到最后
    // 情况1：只考虑单独index位置的字符转换，但不能是'0'，因为'0'不对应任何字符
    if (s.charAt(index) == '0') {
      return 0;
    }
    int ways = translateProcess(s, index + 1);
    // 情况2：考虑index位置和index+1位置组成的两位数，不能超过26，而且index+1不能越界
    if (index + 1 < s.length() && (s.charAt(index) - '0') * 10 + s.charAt(index + 1) - '0' < 27) {
      ways += translateProcess(s, index + 2);
    }
    return ways;
  }

  // 转动态规划，因为可变参数只有一个，所以是个一维数组
  public static int translateDp(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    int[] dp = new int[s.length() + 1];
    dp[s.length()] = 1;
    for (int index = s.length() - 1; index >= 0; index--) {
      // 因为初始化已经是0了，不需要在s.charAt(index) == '0'时单独设置0
      if (s.charAt(index) != '0') {
        int ways = dp[index + 1];
        if (index + 1 < s.length()
                && (s.charAt(index) - '0') * 10 + s.charAt(index + 1) - '0' < 27) {
          ways += dp[index + 2];
        }
        dp[index] = ways;
      }
    }
    return dp[0];
  }

  // 3. 贴纸拼词(leetcode 691)：会超时
  public static int minStickers(String[] stickers, String target) {
    if (stickers.length == 0 || target.length() == 0) {
      return -1;
    }
    int minStickers = minStickersProcess(stickers, target);
    return minStickers == Integer.MAX_VALUE ? -1 : minStickers;
  }

  private static int minStickersProcess(String[] stickers, String target) {
    if (target.length() == 0) {
      return 0;
    }
    int minStickers = Integer.MAX_VALUE;
    for (String sticker : stickers) {
      String rest = minus(target, sticker);
      if (rest.length() != target.length()) {
        minStickers = Math.min(minStickers, minStickersProcess(stickers, rest));
      }
    }
    return minStickers + (minStickers == Integer.MAX_VALUE ? 0 : 1);
  }

  private static String minus(String target, String sticker) {
    int[] count = new int[26];
    // 记录每个英文字母在target中出现的频率
    for (char c : target.toCharArray()) {
      count[c - 'a']++;
    }
    // 每当sticker中含有target里的字母，减去这个字母的频率
    for (char c : sticker.toCharArray()) {
      count[c - 'a']--;
    }
    // 最后组合起来
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < 26; i++) {
      if (count[i] > 0) {
        result.append(String.valueOf((char) (i + 'a')).repeat(Math.max(0, count[i])));
      }
    }
    return result.toString();
  }

  // 3. 贴纸拼词(leetcode 691)：优化版，加入傻缓存
  public static int minStickersDp(String[] stickers, String target) {
    int N = stickers.length;
    // 关键优化(用词频表替代贴纸数组)
    int[][] counts = new int[N][26];
    for (int i = 0; i < N; i++) {
      char[] str = stickers[i].toCharArray();
      for (char cha : str) {
        counts[i][cha - 'a']++;
      }
    }
    Map<String, Integer> dp = new HashMap<>();
    dp.put("", 0);
    int ans = minStickersDpProcess(counts, target, dp);
    return ans == Integer.MAX_VALUE ? -1 : ans;
  }

  private static int minStickersDpProcess(int[][] stickers, String target, Map<String, Integer> dp) {
    if (dp.containsKey(target)) {
      return dp.get(target);
    }
    // target做出词频统计
    int[] tcounts = new int[26];
    for (char cha : target.toCharArray()) {
      tcounts[cha - 'a']++;
    }
    int N = stickers.length;
    int min = Integer.MAX_VALUE;
    for (int[] sticker : stickers) {
      // 尝试第一张贴纸是谁
      // 最关键的优化(重要的剪枝!这一步也是贪心!)
      // 让有target起始字母的sticker先作为第一个尝试
      if (sticker[target.toCharArray()[0] - 'a'] > 0) {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < 26; j++) {
          if (tcounts[j] > 0) {
            int nums = tcounts[j] - sticker[j];
            builder.append(String.valueOf((char) (j + 'a')).repeat(Math.max(0, nums)));
          }
        }
        String rest = builder.toString();
        min = Math.min(min, minStickersDpProcess(stickers, rest, dp));
      }
    }
    int ans = min + (min == Integer.MAX_VALUE ? 0 : 1);
    dp.put(target, ans);
    return ans;
  }

  // 4. 最长公共子序列(leetcode 1143)
  public static int longestCommonSubsequence(String s1, String s2) {
    if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
      return 0;
    }

    return commonSubProcess(s1.toCharArray(), s2.toCharArray(), s1.length() - 1, s2.length() - 1);
  }

  // s1在[0..i]和s2在[0..j]中最长公共子序列的长度
  // str1[0...i]和str2[0...j]，这个范围上最长公共子序列长度是多少？
  // 可能性分类:
  // a) 最长公共子序列，一定不以str1[i]字符结尾、也一定不以str2[j]字符结尾
  // b) 最长公共子序列，可能以str1[i]字符结尾、但是一定不以str2[j]字符结尾
  // c) 最长公共子序列，一定不以str1[i]字符结尾、但是可能以str2[j]字符结尾
  // d) 最长公共子序列，必须以str1[i]字符结尾、也必须以str2[j]字符结尾
  // 注意：a)、b)、c)、d)并不是完全互斥的，他们可能会有重叠的情况
  // 但是可以肯定，答案不会超过这四种可能性的范围
  // 那么我们分别来看一下，这几种可能性怎么调用后续的递归。
  // a) 最长公共子序列，一定不以str1[i]字符结尾、也一定不以str2[j]字符结尾
  //    如果是这种情况，那么有没有str1[i]和str2[j]就根本不重要了，因为这两个字符一定没用啊
  //    所以砍掉这两个字符，最长公共子序列 = str1[0...i-1]与str2[0...j-1]的最长公共子序列长度(后续递归)
  // b) 最长公共子序列，可能以str1[i]字符结尾、但是一定不以str2[j]字符结尾
  //    如果是这种情况，那么我们可以确定str2[j]一定没有用，要砍掉；但是str1[i]可能有用，所以要保留
  //    所以，最长公共子序列 = str1[0...i]与str2[0...j-1]的最长公共子序列长度(后续递归)
  // c) 最长公共子序列，一定不以str1[i]字符结尾、但是可能以str2[j]字符结尾
  //    跟上面分析过程类似，最长公共子序列 = str1[0...i-1]与str2[0...j]的最长公共子序列长度(后续递归)
  // d) 最长公共子序列，必须以str1[i]字符结尾、也必须以str2[j]字符结尾
  //    同时可以看到，可能性d)存在的条件，一定是在str1[i] == str2[j]的情况下，才成立的
  //    所以，最长公共子序列总长度 = str1[0...i-1]与str2[0...j-1]的最长公共子序列长度(后续递归) + 1(共同的结尾)
  // 综上，四种情况已经穷尽了所有可能性。四种情况中取最大即可
  // 其中b)、c)一定参与最大值的比较，
  // 当str1[i] == str2[j]时，a)一定比d)小，所以d)参与
  // 当str1[i] != str2[j]时，d)压根不存在，所以a)参与
  // 但是再次注意了！
  // a)是：str1[0...i-1]与str2[0...j-1]的最长公共子序列长度
  // b)是：str1[0...i]与str2[0...j-1]的最长公共子序列长度
  // c)是：str1[0...i-1]与str2[0...j]的最长公共子序列长度
  // a)中str1的范围 < b)中str1的范围，a)中str2的范围 == b)中str2的范围
  // 所以a)不用求也知道，它比不过b)啊，因为有一个样本的范围比b)小啊！
  // a)中str1的范围 == c)中str1的范围，a)中str2的范围 < c)中str2的范围
  // 所以a)不用求也知道，它比不过c)啊，因为有一个样本的范围比c)小啊！
  // 至此，可以知道，a)就是个垃圾，有它没它，都不影响最大值的决策
  // 所以，当str1[i] == str2[j]时，b)、c)、d)中选出最大值
  // 当str1[i] != str2[j]时，b)、c)中选出最大值
  private static int commonSubProcess(char[] s1, char[] s2, int i, int j) {
    if (i == 0 && j == 0) {
      return s1[i] == s2[j] ? 1 : 0;
    } else if (i == 0) {
      return s1[i] == s2[j] ? 1 : commonSubProcess(s1, s2, i, j - 1);
    } else if (j == 0) {
      return s1[i] == s2[j] ? 1 : commonSubProcess(s1, s2, i - 1, j);
    } else { // s1 和 s2都不只最后一个index
      int p1 = commonSubProcess(s1, s2, i - 1, j);
      int p2 = commonSubProcess(s1, s2, i, j - 1);
      int p3 = s1[i] == s2[j] ? (1 + commonSubProcess(s1, s2, i - 1, j - 1)) : 0;
      return Math.max(p1, Math.max(p2, p3));
    }
  }

  // 改动态规划
  public static int longestCommonSubsequenceDp(String s1, String s2) {
    if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
      return 0;
    }
    char[] str1 = s1.toCharArray();
    char[] str2 = s2.toCharArray();
    int N = str1.length;
    int M = str2.length;
    int[][] dp = new int[N][M];
    dp[0][0] = str1[0] == str2[0] ? 1 : 0;
    for (int j = 1; j < M; j++) {
      dp[0][j] = str1[0] == str2[j] ? 1 : dp[0][j - 1];
    }
    for (int i = 1; i < N; i++) {
      dp[i][0] = str1[i] == str2[0] ? 1 : dp[i - 1][0];
    }
    for (int i = 1; i < N; i++) {
      for (int j = 1; j < M; j++) {
        int p1 = dp[i - 1][j];
        int p2 = dp[i][j - 1];
        int p3 = str1[i] == str2[j] ? (1 + dp[i - 1][j - 1]) : 0;
        dp[i][j] = Math.max(p1, Math.max(p2, p3));
      }
    }
    return dp[N - 1][M - 1];
  }

  public static void main(String[] args) {
    int[] weights = {3, 2, 4, 7, 3, 1, 7};
    int[] values = {5, 6, 3, 19, 12, 4, 2};
    int bag = 15;
    System.out.println(maxVlaue(weights, values, bag));
    System.out.println(maxValueDp(weights, values, bag));

    String s = "111";
    System.out.println(translate(s));
    System.out.println(translateDp(s));

    System.out.println(minus("haha", "h"));
  }
}
