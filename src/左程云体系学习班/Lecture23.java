package 左程云体系学习班;

import static 左程云体系学习班.Lecture21.randomArray;

import java.util.Arrays;

public class Lecture23 {

  /*
   * 1. 数组分裂最小差值问题： 给定一个正数数组arr，请把arr中所有的数分成两个集合，尽量让两个集合的累加和接近，
   * 返回：最接近的情况下，较小集合的累加和。
   *
   * 2. 数组分裂最小差值问题： 给定一个正数数组arr，请把arr中所有的数分成两个集合，尽量让两个集合的累加和接近，
   * 并且，如果数组长度为偶数，两个集合长度必须一样；如果数组长度为奇数，两个数组长度差值为1。
   * 返回：最接近的情况下，较小集合的累加和。
   *
   * 3. N皇后问题
   * */

  //1. 数组分裂最小差值问题
  public static int minDiff(int[] arr) {
    if (arr.length < 2) {
      return 0;
    }
    int sum = Arrays.stream(arr).sum();
    return minDiffProcess(arr, 0, sum >> 1);
  }

  private static int minDiffProcess(int[] arr, int i, int rest) {
    if (i == arr.length) {
      return 0;
    }
    // 情况1：不选择当前index的数
    int p1 = minDiffProcess(arr, i + 1, rest);
    // 情况2：选择当前数，前提是不能大于rest
    int p2 = rest >= arr[i] ? arr[i] + minDiffProcess(arr, i + 1, rest - arr[i]) : 0;
    return Math.max(p1, p2);
  }

  public static int minDiffDp(int[] arr) {
    if (arr.length < 2) {
      return 0;
    }
    int sum = Arrays.stream(arr).sum();
    int N = arr.length;
    int[][] dp = new int[N + 1][(sum >> 1) + 1];
    for (int i = N - 1; i >= 0; i--) {
      for (int rest = 0; rest <= sum >> 1; rest++) {
        int p1 = dp[i + 1][rest];
        int p2 = rest >= arr[i] ? arr[i] + dp[i + 1][rest - arr[i]] : 0;
        dp[i][rest] = Math.max(p1, p2);
      }
    }
    return dp[0][sum >> 1];
  }

  // 2. 数组分裂最小差值问题
  public static int minDiffSameSize(int[] arr) {
    if (arr.length < 2) {
      return 0;
    }
    int sum = Arrays.stream(arr).sum();
    // arr长度为偶数
    if ((arr.length & 1) == 0) {
      return minDiffSameSizeProcess(arr, arr.length >> 1, 0, sum >> 1);
    } else { // arr长度为奇数
      int p1 = minDiffSameSizeProcess(arr, arr.length >> 1, 0, sum >> 1);
      int p2 = minDiffSameSizeProcess(arr, (arr.length >> 1) + 1, 0, sum >> 1);
      return Math.max(p1, p2);
    }
  }

  private static int minDiffSameSizeProcess(int[] arr, int size, int i, int rest) {
    if (i == arr.length) {
      return size == 0 ? 0 : -1;
    }
    int p1 = minDiffSameSizeProcess(arr, size, i + 1, rest);
    int next = arr[i] <= rest ? minDiffSameSizeProcess(arr, size - 1, i + 1, rest - arr[i]) : -1;
    int p2 = next != -1 ? p2 = arr[i] + next : -1;
    return Math.max(p1, p2);
  }

  public static int minDiffSameSizeDp(int[] arr) {
    int N = arr.length;
    if (N < 2) {
      return 0;
    }
    int sum = Arrays.stream(arr).sum();
    int picks = (sum + 1) >> 1;
    int[][][] dp = new int[picks + 1][N + 1][(sum >> 1) + 1];

    // if (i == arr.length) -> return size == 0 ? 0 : -1;
    for (int size = 1; size <= picks; size++) {
      for (int rest = 0; rest <= sum >> 1; rest++) {
        dp[size][N][rest] = -1;
      }
    }

    for (int i = N - 1; i >= 0; i--) {
      for (int size = 0; size <= picks ; size++) {
        for (int rest = 0; rest <= sum >> 1; rest++) {
          int p1 = dp[size][i + 1][rest];
          int next = arr[i] <= rest && size >= 1 ? dp[size - 1][i + 1][rest - arr[i]] : -1;
          int p2 = next != -1 ? p2 = arr[i] + next : -1;
          dp[size][i][rest] = Math.max(p1, p2);
        }
      }
    }

    if ((N & 1) == 0) {
      return dp[N >> 1][0][sum >> 1];
    } else { // arr长度为奇数
      int p1 =  dp[N >> 1][0][sum >> 1];
      int p2 =  dp[(N >> 1) + 1][0][sum >> 1];
      return Math.max(p1, p2);
    }
  }

  public static int NQueens(int n) {
    if (n < 1) {
      return 0;
    }
    int[] record = new int[n];
    return process1(0, record, n);
  }

  // 当前来到i行，一共是0~N-1行
  // 在i行上放皇后，所有列都尝试
  // 必须要保证跟之前所有的皇后不打架
  // int[] record record[x] = y 之前的第x行的皇后，放在了y列上
  // 返回：不关心i以上发生了什么，i.... 后续有多少合法的方法数
  public static int process1(int i, int[] record, int n) {
    if (i == n) {
      return 1;
    }
    int res = 0;
    for (int j = 0; j < n; j++) {
      if (isValid(record, i, j)) {
        record[i] = j;
        res += process1(i + 1, record, n);
      }
    }
    return res;
  }

  public static boolean isValid(int[] record, int i, int j) {
    // 0..i-1
    for (int k = 0; k < i; k++) {
      if (j == record[k] || Math.abs(record[k] - j) == Math.abs(i - k)) {
        return false;
      }
    }
    return true;
  }

  // 用位运算来代表每个位置能否填入，大大加速运算速率
  public static int NQueensOptimise(int n) {
    if (n < 1 || n > 32) {
      return 0;
    }
    // 如果你是13皇后问题，limit 最右13个1，其他都是0
    int limit = n == 32 ? -1 : (1 << n) - 1;
    return process2(limit, 0, 0, 0);
  }

  public static int process2(int limit, int colLim, int leftDiaLim, int rightDiaLim) {
    if (colLim == limit) {
      return 1;
    }
    // pos中所有是1的位置，是你可以去尝试皇后的位置
    int pos = limit & (~(colLim | leftDiaLim | rightDiaLim));
    int mostRightOne = 0;
    int res = 0;
    while (pos != 0) {
      mostRightOne = pos & (~pos + 1);
      pos = pos - mostRightOne;
      res += process2(limit, colLim | mostRightOne, (leftDiaLim | mostRightOne) << 1,
          (rightDiaLim | mostRightOne) >>> 1);
    }
    return res;
  }
  public static void main(String[] args) {
    int n = 10;

    long start = System.currentTimeMillis();
    System.out.println(NQueens(n));
    long end = System.currentTimeMillis();
    System.out.println("cost time: " + (end - start) + "ms");

    start = System.currentTimeMillis();
    System.out.println(NQueensOptimise(n));
    end = System.currentTimeMillis();
    System.out.println("cost time: " + (end - start) + "ms");
  }
}
