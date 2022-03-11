package LeetCodeHot100;

public class Code062 {

  // brute-force recursion, exceed time limit
  public static int uniquePaths(int m, int n) {
    if (m < 1 || n < 1) {
      return 0;
    }
    return pathProcess(m, n, 0, 0);
  }

  private static int pathProcess(int m, int n, int i, int j) {
    if (i == m - 1 && j == n - 1) {
      return 1;
    }
    if (i >= m || j >= n) {
      return 0;
    }
    return pathProcess(m, n, i + 1, j) + pathProcess(m, n, i, j + 1);
  }

  // change into dynamic programming
  public static int uniquePathsDp(int m, int n) {
    if (m < 1 || n < 1) {
      return 0;
    }
    int[][] dp = new int[m][n];
    dp[m - 1][n - 1] = 1;
    for (int i = m - 2; i >= 0; i--) {
      dp[i][n - 1] = dp[i + 1][n - 1];
    }
    for (int j = n - 2; j >= 0; j--) {
      dp[m - 1][j] = dp[m - 1][j + 1];
    }
    for (int i = m - 2; i >= 0; i--) {
      for (int j = n - 2; j >= 0; j--) {
        dp[i][j] = dp[i + 1][j] + dp[i][j + 1];
      }
    }
    return dp[0][0];
  }
}