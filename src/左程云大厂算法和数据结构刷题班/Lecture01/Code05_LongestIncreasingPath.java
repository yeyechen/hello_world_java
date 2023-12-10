package 左程云大厂算法和数据结构刷题班.Lecture01;

public class Code05_LongestIncreasingPath {

  /*
   * leetcode 329: 给定一个 m x n 整数矩阵 matrix ，找出其中 最长递增路径 的长度。
   * 对于每个单元格，你可以往上，下，左，右四个方向移动。 你 不能 在 对角线 方向上移动或移动到 边界外（即不允许环绕）。
   * */

  // 带个记忆化搜索直接变成最优解
  public static int longestIncreasingPath(int[][] matrix) {
    int result = 0;
    int N = matrix.length;
    int M = matrix[0].length;
    int[][] dp = new int[N][M];
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        result = Math.max(result, process(matrix, i, j, dp));
      }
    }
    return result;
  }

  private static int process(int[][] m, int i, int j, int[][] dp) {
    if (dp[i][j] != 0) {
      return dp[i][j];
    }
    int up = i > 0 && m[i][j] < m[i - 1][j] ? process(m, i - 1, j, dp) : 0;
    int down = i < m.length - 1 && m[i][j] < m[i + 1][j] ? process(m, i + 1, j, dp) : 0;
    int left = j > 0 && m[i][j] < m[i][j - 1] ? process(m, i, j - 1, dp) : 0;
    int right = j < m[0].length - 1 && m[i][j] < m[i][j + 1] ? process(m, i, j + 1, dp) : 0;
    int result = Math.max(Math.max(up, down), Math.max(left, right)) + 1;
    dp[i][j] = result;
    return result;
  }

}
