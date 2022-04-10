package LeetCode.LeetCodeHot100;

public class Code064 {

  // brute-force recursion
  public static int minPathSum(int[][] grid) {
    if (grid.length < 1 || grid[0].length < 1) {
      return 0;
    }
    return minPathProcess(grid, 0, 0);
  }

  private static int minPathProcess(int[][] grid, int i, int j) {
    if (i == grid.length - 1 && j == grid[0].length - 1) {
      return grid[i][j];
    }
    if (i >= grid.length || j >= grid[0].length) {
      return Integer.MAX_VALUE;
    }
    return grid[i][j] + Math.min(minPathProcess(grid, i + 1, j), minPathProcess(grid, i, j + 1));
  }

  // dynamic programming
  public static int minPathSumDp(int[][] grid) {
    int m = grid.length;
    int n = grid[0].length;
    if (m < 1 || n < 1) {
      return 0;
    }
    int[][] dp = new int[m][n];
    dp[m - 1][n - 1] = grid[m - 1][n - 1];

    for (int i = m - 2; i >= 0; i--) {
      dp[i][n - 1] = grid[i][n - 1] + dp[i + 1][n - 1];
    }
    for (int j = n - 2; j >= 0; j--) {
      dp[m - 1][j] = grid[m - 1][j] + dp[m - 1][j + 1];
    }

    for (int i = m - 2; i >= 0; i--) {
      for (int j = n - 2; j >= 0; j--) {
        dp[i][j] = grid[i][j] + Math.min(dp[i + 1][j], dp[i][j + 1]);
      }
    }
    return dp[0][0];
  }
}
