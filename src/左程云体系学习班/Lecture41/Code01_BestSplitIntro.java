package 左程云体系学习班.Lecture41;

import Util.Utility;
import java.util.Arrays;

public class Code01_BestSplitIntro {

  /*
   * 给定一个非负数组arr，长度为N，
   * 那么有N-1种方案可以把arr切成左右两部分
   * 每一种方案都有，min{左部分累加和，右部分累加和}
   * 求这么多方案中，min{左部分累加和，右部分累加和}的最大值是多少？
   * 整个过程要求时间复杂度O(N)
   *
   * */

  public static int bestSplit(int[] arr) {
    if (arr == null || arr.length <= 1) {
      return 0;
    }

    // calculate pre-sum
    int[] preSum = new int[arr.length];
    int sum = 0;
    for (int i = 0; i < arr.length; i++) {
      sum += arr[i];
      preSum[i] = sum;
    }

    int result = 0;
    for (int i = 0; i < arr.length - 2; i++) {
      int leftSum = preSum[i];
      int rightSum = sum - preSum[i];
      result = Math.max(result, Math.min(leftSum, rightSum));
    }

    return result;
  }

  public static int bruteForce(int[] arr) {
    if (arr == null || arr.length <= 1) {
      return 0;
    }

    int result = 0;
    for (int split = 1; split < arr.length - 1; split++) {
      int leftSum = 0;
      for (int i = 0; i < split; i++) {
        leftSum += arr[i];
      }
      int rightSum = 0;
      for (int i = split; i < arr.length; i++) {
        rightSum += arr[i];
      }
      result = Math.max(result, Math.min(leftSum, rightSum));
    }
    return result;
  }

  public static void main(String[] args) {
    int maxLen = 20;
    int maxVal = 20;
    int testCycles = 10000;
    System.out.println("test start");
    for (int i = 0; i < testCycles; i++) {
      int[] arr = Utility.randomArrayGenerator(maxLen, maxVal, true);
      int ans1 = bestSplit(arr);
      int ans2 = bruteForce(arr);
      if (ans1 != ans2) {
        System.out.println("test fail");
        System.out.println(Arrays.toString(arr));
        System.out.println("ans1: " + ans1 + ", ans2: " + ans2);
        break;
      }
    }
    System.out.println("test end");
  }
}
