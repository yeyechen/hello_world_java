package 左程云体系学习班;

public class Lecture20 {

  /*
   * 1. 最长回文子序列(leetcode 516题): 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
   * 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
   *
   * */

  // 1. 最长回文子序列(leetcode 516题)
  public static int longestPalindromeSubseq(String s) {
    int N = s.length();
    if (N == 0) {
      return 0;
    }
    return palindromeSubseqProcess(s.toCharArray(), 0, N - 1);
  }

  // 给定一个字符串, 在[L .. R]区间内最长的回文子序列长度
  private static int palindromeSubseqProcess(char[] s, int L, int R) {
    if (L == R) {
      return 1;
    }
    if (L == R - 1) {
      return s[L] == s[R] ? 2 : 1;
    }
    int p1 = palindromeSubseqProcess(s, L + 1, R);
    int p2 = palindromeSubseqProcess(s, L, R - 1);
    int p3 = s[L] == s[R] ? 2 + palindromeSubseqProcess(s, L + 1, R - 1) : 0;
    return Math.max(p1, Math.max(p2, p3));
  }

  // 1. 最长回文子序列(leetcode 516题): 改动态规划(自己尝试)
  public static int longestPalindromeSubseqDp(String s) {
    int N = s.length();
    char[] str = s.toCharArray();
    if (N== 0) {
      return 0;
    }
    int[][] dp = new int[N][N];
    for (int i = 0; i < N; i++) {
      dp[i][i] = 1;
    }
    for (int i = 1; i < N; i++) {
      dp[i - 1][i] = str[i - 1] == str[i] ? 2 : 1;
    }
    for (int L = N - 3; L >= 0; L--) {
      for (int R = L + 2; R < N; R++) {
        int p1 = dp[L + 1][R];
        int p2 = dp[L][R - 1];
        int p3 = str[L] == str[R] ? 2 + dp[L + 1][R - 1] : 0;
        dp[L][R] = Math.max(p1, Math.max(p2, p3));
      }
    }
    return dp[0][N - 1];
  }

  public static void main(String[] args) {
    System.out.println(longestPalindromeSubseqDp("bbbab"));
  }
}
