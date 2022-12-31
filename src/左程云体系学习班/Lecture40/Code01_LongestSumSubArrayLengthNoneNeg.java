package 左程云体系学习班.Lecture40;

import Util.Utility;

public class Code01_LongestSumSubArrayLengthNoneNeg {
  /*
   * 子数组达到规定累加和的最大长度系列问题
   *
   * 给定一个非负整数组成的无序数组arr，给定一个正整数值K，找到arr的所有子数组里，哪个子数组的累加和等于K
   * 并且是长度最大的，返回其长度.
   *
   * 主要技巧：利用单调性优化
   *
   * */

  public static int getMaxLength(int[] arr, int k) {
    if (arr == null || arr.length == 0 || k <= 0) {
      return 0;
    }

    int L = 0;
    int R = 0;
    int sum = arr[0];
    int maxLength = 0;
    while (R < arr.length) {
      if (sum < k) {
        R++;
        if (R == arr.length) {
          break;
        }
        sum += arr[R];
      } else if (sum == k) {
        maxLength = Math.max(maxLength, R - L + 1);
        // 累加和等于要求的k了，要继续向右寻找有没有0，以扩大长度
        R++;
        if (R == arr.length) {
          break;
        }
        sum += arr[R];
      } else {
        sum -= arr[L++];
      }
    }
    return maxLength;
  }

  public static int bruteForce(int[] arr, int K) {
    int max = 0;
    for (int i = 0; i < arr.length; i++) {
      for (int j = i; j < arr.length; j++) {
        if (valid(arr, i, j, K)) {
          max = Math.max(max, j - i + 1);
        }
      }
    }
    return max;
  }

  public static boolean valid(int[] arr, int L, int R, int K) {
    int sum = 0;
    for (int i = L; i <= R; i++) {
      sum += arr[i];
    }
    return sum == K;
  }

  public static void main(String[] args) {
    int len = 50;
    int value = 100;
    int testTime = 500000;
    System.out.println("test begin");
    for (int i = 0; i < testTime; i++) {
      int[] arr = Utility.randomArrayGenerator(len, value, true);
      int K = (int) (Math.random() * value) + 1;
      int ans1 = getMaxLength(arr, K);
      int ans2 = bruteForce(arr, K);
      if (ans1 != ans2) {
        System.out.println("Oops!");
        System.out.println("K : " + K);
        System.out.println(ans1);
        System.out.println(ans2);
        break;
      }
    }
    System.out.println("test end");
  }
}
