package 左程云体系学习班;

public class Lecture21 {
  /*
   *
   * a. 空间压缩技巧: 有时在动态规划的时候, 我们不需要整张表的空间, 我们只需要一个长度为列数, 或行数的数组, 根据依赖关系动态地更新.
   * 在极端情况下, 比如行数 >> 列数, 那么我们就动态更新行; 如果列数 >> 行数, 动态更新列.
   * b. 优化枚举: 在特定题目条件下, 改完动态规划后的每一个格子的依赖是枚举行为, 我们可以继续分析, 找到更优的解
   *
   * 1. 对角路径问题: 给定一个二维数组matrix，一个人必须从左上角出发，最后到达右下角
   * 沿途只可以向下或者向右走，沿途的数字都累加, 返回最小距离累加和
   *
   * 2. 数字组合问题: arr是面值数组，其中的值都是正数且没有重复。再给定一个正数aim。
   * 每个值都认为是一种面值，且认为张数是无限的。返回组成aim的方法数
   * 例如：arr = {1,2}，aim = 4
   * 方法如下：1+1+1+1、1+1+2、2+2
   * 一共就3种方法，所以返回3.
   *
   * 3. 鲍勃的生死问题: 给定5个参数，N，M，row，col，k
   * 表示在N*M的区域上，醉汉Bob初始在(row,col)位置
   * Bob一共要迈出k步，且每步都会等概率向上下左右四个方向走一个单位
   * 任何时候Bob只要离开N*M的区域，就直接死亡
   * 返回k步之后，Bob还在N*M的区域的概率
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

  // 2. 数字组合问题
  public static int combination(int[] arr, int aim) {
    if (arr.length == 0 || aim < 0) {
      return 0;
    }
    return combineProcess(arr, 0, aim);
  }
  // 在arr[index .. ]中, 有多少种组合方式

  private static int combineProcess(int[] arr, int index, int rest) {
    if (rest < 0) {
      return 0;
    }
    if (index == arr.length) {
      return rest == 0 ? 1 : 0;
    }
    // 当前面值, 有无限张, 所以要遍历每种张数的可能性
    int result = 0;
    for (int numOf = 0; numOf * arr[index] <= rest; numOf++) {
      result += combineProcess(arr, index + 1, rest - numOf * arr[index]);
    }
    return result;
  }

  // 数字组合问题改动态规划
  public static int combinationDp(int[] arr, int aim) {
    if (arr.length == 0 || aim < 0) {
      return 0;
    }
    int N = arr.length;
    int[][] dp = new int[N + 1][aim + 1];
    dp[N][0] = 1;

    for (int index = N - 1; index >= 0; index--) {
      for (int rest = 0; rest <= aim; rest++) {
        int result = 0;
        // 注意这里的for循环, 可以通过分析依赖关系优化
        for (int numOf = 0; numOf * arr[index] <= rest; numOf++) {
          result += dp[index + 1][rest - numOf * arr[index]];
        }
        dp[index][rest] = result;
      }
    }
    return dp[0][aim];
  }

  // 数字组合问题改动态规划优化成常数时间复杂度(优化枚举for循环), 画图可以分析出位置关系依赖
  public static int combinationDpOpt(int[] arr, int aim) {
    if (arr.length == 0 || aim < 0) {
      return 0;
    }
    int N = arr.length;
    int[][] dp = new int[N + 1][aim + 1];
    dp[N][0] = 1;

    for (int index = N - 1; index >= 0; index--) {
      for (int rest = 0; rest <= aim; rest++) {
        dp[index][rest] = dp[index + 1][rest] + (rest - arr[index] >= 0 ? dp[index][rest - arr[index]] : 0);
      }
    }
    return dp[0][aim];
  }

  // 3. 鲍勃的生死问题: 和Lecture20的象棋跳马问题类似
  public static double bobLiveProbability(int row, int col, int k, int N, int M) {
    return (double) live(row, col, k, N, M) / Math.pow(4, k);
  }

  private static long live(int row, int col, int rest, int N, int M) {
    if (row < 0 || row == N || col < 0 || col == M) {
      return 0;
    }
    // 还在棋盘中！
    if (rest == 0) {
      return 1;
    }
    // 还在棋盘中！还有步数要走
    long up = live(row - 1, col, rest - 1, N, M);
    long down = live(row + 1, col, rest - 1, N, M);
    long left = live(row, col - 1, rest - 1, N, M);
    long right = live(row, col + 1, rest - 1, N, M);
    return up + down + left + right;
  }

  // 该动态规划
  public static double bobLiveProbabilityDp(int row, int col, int k, int N, int M) {
    long[][][] dp = new long[N][M][k + 1];
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        dp[i][j][0] = 1;
      }
    }
    for (int rest = 1; rest <= k; rest++) {
      for (int r = 0; r < N; r++) {
        for (int c = 0; c < M; c++) {
          dp[r][c][rest] = pick(dp, N, M, r - 1, c, rest - 1);
          dp[r][c][rest] += pick(dp, N, M, r + 1, c, rest - 1);
          dp[r][c][rest] += pick(dp, N, M, r, c - 1, rest - 1);
          dp[r][c][rest] += pick(dp, N, M, r, c + 1, rest - 1);
        }
      }
    }
    return (double) dp[row][col][k] / Math.pow(4, k);
  }

  private static long pick(long[][][] dp, int N, int M, int r, int c, int rest) {
    if (r < 0 || r == N || c < 0 || c == M) {
      return 0;
    }
    return dp[r][c][rest];
  }

  /*----------------------------------------测试----------------------------------------*/
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

  public static int[] randomArray(int maxLen, int maxValue) {
    int N = (int) (Math.random() * maxLen);
    int[] arr = new int[N];
    boolean[] has = new boolean[maxValue + 1];
    for (int i = 0; i < N; i++) {
      do {
        arr[i] = (int) (Math.random() * maxValue) + 1;
      } while (has[arr[i]]);
      has[arr[i]] = true;
    }
    return arr;
  }

  public static void main(String[] args) {
    int testAmount = 10000;
    int rowSize = 10;
    int colSize = 10;
    for (int i = 0; i < testAmount; i++) {
      int[][] m = generateRandomMatrix(rowSize, colSize);
      if (pathDp(m) != pathDpCompress(m)) {
        System.out.println("测试失败!");
        break;
      }
    }
    System.out.println("测试1结束");

    int maxLen = 10;
    int maxValue = 30;
    for (int i = 0; i < testAmount; i++) {
      int[] arr = randomArray(maxLen, maxValue);
      int aim = (int) (Math.random() * maxValue);
      int ans1 = combination(arr, aim);
      int ans2 = combinationDp(arr, aim);
      int ans3 = combinationDpOpt(arr, aim);
      if (ans1 != ans2 || ans1 != ans3) {
        System.out.println("测试失败!");
        break;
      }
    }
    System.out.println("测试2结束");
  }
}
