package 左程云体系学习班.Lecture47;

public class Code01_StrangePrinter {

  /*
   * leetcode 664: https://leetcode.com/problems/strange-printer/
   *
   * 有台奇怪的打印机有以下两个特殊要求：
   * 打印机每次只能打印由 同一个字符 组成的序列。
   * 每次可以在从起始到结束的任意位置打印新字符，并且会覆盖掉原来已有的字符。
   * 给你一个字符串 s ，你的任务是计算这个打印机打印它需要的最少打印次数。
   * */

  public static int strangePrinter(String s) {
    if (s == null || s.length() == 0) {
      return 0;
    }
    char[] str = s.toCharArray();
    return process(str, 0, s.length() - 1);
  }

  // 在 str[L..R]范围上，打印机需要的最少打印次数
  // 打印机肯定连续打印 str[L] 代表的char
  private static int process(char[] str, int L, int R) {
    if (L == R) {
      return 1;
    }
    int min = R - L + 1;
    // 尝试找到在哪个位置上停止打印，能得到最小打印次数
    // [L, L+1,...i..., R-1, R]
    // [L..i), [i..R]
    for (int i = L + 1; i <= R; i++) {
      int leftMin = process(str, L, i - 1);
      int rightMin = process(str, i, R);
      // 如果左侧打印的字符和右侧打印的字符是一样的，就连续打印，少一次打印次数
      int keepRolling = str[L] == str[i] ? 1 : 0;
      min = Math.min(min, leftMin + rightMin - keepRolling);
    }
    return min;
  }

  // 改动态规划
  public static int strangePrinterDp(String s) {
    if (s == null || s.length() == 0) {
      return 0;
    }

    char[] str = s.toCharArray();
    int N = s.length();
    int[][] dp = new int[N][N];
    for (int i = 0; i < N; i++) {
      dp[i][i] = 1;
    }

    for (int L = N - 2; L >= 0; L--) {
      for (int R = L + 1; R < N; R++) {
        int min = R - L + 1;
        for (int i = L + 1; i <= R; i++) {
          int leftMin = dp[L][i - 1];
          int rightMin = dp[i][R];
          int keepRolling = str[L] == str[i] ? 1 : 0;
          min = Math.min(min, leftMin + rightMin - keepRolling);
        }
        dp[L][R] = min;
      }
    }
    return dp[0][N - 1];
  }
}
