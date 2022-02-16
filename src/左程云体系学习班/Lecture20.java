package 左程云体系学习班;

public class Lecture20 {

  /*
   * 1. 最长回文子序列(leetcode 516题): 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
   * 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
   *
   * 2. 象棋马跳跃问题: 请同学们自行搜索或者想象一个象棋的棋盘，然后把整个棋盘放入第一象限，棋盘的最左下角是(0,0)位置
   * 那么整个棋盘就是横坐标上9条线、纵坐标上10条线的区域
   * 给你三个 参数 x，y，k
   * 返回“马”从(0,0)位置出发，必须走k步
   * 最后落在(x,y)上的方法数有多少种?
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
    if (N == 0) {
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

  // 2. 象棋马跳跃问题
  public static int jumpMethod(int x, int y, int k) {
    if (x < 0 || y < 0 || x > 9 || y > 10 || k < 0) {
      return 0;
    }
    return jumpProcess(0, 0, x, y, k);
  }

  private static int jumpProcess(int currX, int currY, int targetX, int targetY, int restK) {
    if (currX < 0 || currY < 0 || currX > 9 || currY > 10) {
      return 0;
    }
    if (restK == 0) {
      return currX == targetX && currY == targetY ? 1 : 0;
    }
    int ways = jumpProcess(currX + 1, currY + 2, targetX, targetY, restK - 1);
    ways += jumpProcess(currX - 1, currY + 2, targetX, targetY, restK - 1);
    ways += jumpProcess(currX + 1, currY - 2, targetX, targetY, restK - 1);
    ways += jumpProcess(currX - 1, currY - 2, targetX, targetY, restK - 1);
    ways += jumpProcess(currX + 2, currY + 1, targetX, targetY, restK - 1);
    ways += jumpProcess(currX - 2, currY + 1, targetX, targetY, restK - 1);
    ways += jumpProcess(currX + 2, currY - 1, targetX, targetY, restK - 1);
    ways += jumpProcess(currX - 2, currY - 1, targetX, targetY, restK - 1);
    return ways;
  }

  public static void main(String[] args) {
    System.out.println(longestPalindromeSubseqDp("bbbab"));
  }
}
