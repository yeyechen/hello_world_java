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

  public static int right(int[] arr) {
    if (arr == null || arr.length < 2) {
      return 0;
    }
    int sum = 0;
    for (int num : arr) {
      sum += num;
    }
    if ((arr.length & 1) == 0) {
      return process(arr, 0, arr.length / 2, sum / 2);
    } else {
      return Math.max(process(arr, 0, arr.length / 2, sum / 2), process(arr, 0, arr.length / 2 + 1, sum / 2));
    }
  }

  // arr[i....]自由选择，挑选的个数一定要是picks个，累加和<=rest, 离rest最近的返回
  public static int process(int[] arr, int i, int picks, int rest) {
    if (i == arr.length) {
      return picks == 0 ? 0 : -1;
    } else {
      int p1 = process(arr, i + 1, picks, rest);
      // 就是要使用arr[i]这个数
      int p2 = -1;
      int next = -1;
      if (arr[i] <= rest) {
        next = process(arr, i + 1, picks - 1, rest - arr[i]);
      }
      if (next != -1) {
        p2 = arr[i] + next;
      }
      return Math.max(p1, p2);
    }
  }

  public static void main(String[] args) {
    int cycles = 10000;
    System.out.println("测试开始");
    for (int i = 0; i < cycles; i++) {
      int[] arr = randomArray(10, 10);
      int ans1 = minDiffSameSizeDp(arr);
      int ans2 = right(arr);
      if (ans1 != ans2) {
        System.out.println(Arrays.toString(arr));
        System.out.println(ans1);
        System.out.println(ans2);
        System.out.println("测试失败");
        break;
      }
    }
    System.out.println("测试结束");
  }
}
