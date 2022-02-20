package 左程云体系学习班;

public class Lecture21 {
  /*
   *
   * 空间压缩技巧: 有时在动态规划的时候, 我们不需要整张表的空间, 我们只需要一个长度为列数, 或行数的数组, 根据依赖关系动态地更新.
   * 在极端情况下, 比如行数 >> 列数, 那么我们就动态更新行; 如果列数 >> 行数, 动态更新列.
   *
   * 1.对角路径问题: 给定一个二维数组matrix，一个人必须从左上角出发，最后到达右下角
   * 沿途只可以向下或者向右走，沿途的数字都累加, 返回最小距离累加和
   *
   * */

  // 1.对角路径问题
  public static int path(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
      return 0;
    }
    return pathProcess(0, 0, matrix);
  }

  private static int pathProcess(int i, int j, int[][] matrix) {
    if (i == matrix.length - 1 && j == matrix[0].length - 1) {
      return matrix[i][j];
    }
    if (i == matrix.length || j == matrix[0].length) {
      return Integer.MAX_VALUE;
    }
    return Math.min(pathProcess(i + 1, j, matrix), pathProcess(i, j + 1, matrix)) + matrix[i][j];
  }

  // 对角路径问题改动态规划
  public static int pathDp(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
      return 0;
    }
    int row = matrix.length;
    int col = matrix[0].length;
    int[][] dp = new int[row][col];
    dp[row - 1][col - 1] = matrix[row - 1][col - 1];
    // 先填最后一行
    for (int j = col - 2; j >= 0; j--) {
      dp[row - 1][j] = dp[row - 1][j + 1] + matrix[row - 1][j];
    }
    // 填最后一列
    for (int i = row - 2; i >= 0; i--) {
      dp[i][col - 1] = dp[i + 1][col - 1] + matrix[i][col - 1];
    }
    for (int i = row - 2; i >= 0; i--) {
      for (int j = col - 2; j >= 0; j--) {
        dp[i][j] = Math.min(dp[i + 1][j], dp[i][j + 1]) + matrix[i][j];
      }
    }
    return dp[0][0];
  }

  public static int pathDpCompress(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
      return 0;
    }
    int row = matrix.length;
    int col = matrix[0].length;
    // 先把最后一行填好
    int[] arr = new int[col];
    arr[col - 1] = matrix[row - 1][col - 1];
    for (int j = col - 2; j >= 0; j--) {
      arr[j] = arr[j + 1] + matrix[row - 1][j];
    }

    // 从最后一行往上滚动更新, 依赖右边和下面(下面就是现在数组的数字)
    for (int i = row - 2; i >= 0; i--) {
      arr[col - 1] = arr[col - 1] + matrix[i][col - 1];
      for (int j = col - 2; j >= 0; j--) {
        arr[j] = Math.min(arr[j], arr[j + 1]) + matrix[i][j];
      }
    }
    return arr[0];
  }

  public static int[][] generateRandomMatrix(int rowSize, int colSize) {
    if (rowSize < 0 || colSize < 0) {
      return null;
    }
    int[][] result = new int[rowSize][colSize];
    for (int i = 0; i != result.length; i++) {
      for (int j = 0; j != result[0].length; j++) {
        result[i][j] = (int) (Math.random() * 100);
      }
    }
    return result;
  }

  public static void main(String[] args) {
    int testAmount = 10000;
    int rowSize = 10;
    int colSize = 10;
    for (int i = 0; i < testAmount; i++) {
      int[][] m = generateRandomMatrix(rowSize, colSize);
      if (pathDp(m) != pathDpCompress(m)) {
        System.out.println("fail!");
        break;
      }
    }
    System.out.println("test end");
  }
}
