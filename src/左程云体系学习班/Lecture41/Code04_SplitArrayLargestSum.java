package 左程云体系学习班.Lecture41;

import Util.Utility;

public class Code04_SplitArrayLargestSum {

  /*
   * leetcode 410: https://leetcode.com/problems/split-array-largest-sum/
   * 给定一个整型数组 arr，数组中的每个值都为正数，表示完成一幅画作需要的时间，再给定一个整数k
   * 表示画匠的数量，每个画匠只能画连在一起的画作
   * 所有的画家并行工作，返回完成所有的画作需要的最少时间
   * arr=[3,1,4]，k=2。
   * 最的分配方式为第一个画匠画3和1，所需时间为4
   * 第二个画匠画4，所需时间为4
   * 所以返回4
   * arr=[1,1,1,4,3]，k=3
   * 最好的分配方式为第一个画匠画前三个1，所需时间为3
   * 第二个画匠画4，所需时间为4
   * 第三个画匠画3，所需时间为3
   * 返回4
   *
   * 同样运用四边形不等式技巧
   * */

  public static int splitArray(int[] arr, int k) {
    if (arr == null || arr.length == 0 || k == 0) {
      return 0;
    }
    int[] preSum = Utility.calcPreSum(arr);
    return process(preSum, arr.length - 1, k);
  }

  // arr[0..index]范围上，用k个画家，返回最少用时
  private static int process(int[] preSum, int index, int k) {
    if (index == 0) {
      return Utility.getSum(preSum, 0, 0);
    }
    if (k == 1) {
      return Utility.getSum(preSum, 0, index);
    }
    int result = Integer.MAX_VALUE;
    for (int split = 0; split <= index; split++) {
      // 右侧的所有画由k个画家负责
      int leftCost = process(preSum, split, k - 1);
      // 右侧的所有画只由一个画家负责
      int rightCost = Utility.getSum(preSum, split + 1, index);
      // 瓶颈
      int bottleNeck = Math.max(leftCost, rightCost);
      result = Math.min(result, bottleNeck);
    }
    return result;
  }

  // O(N^2*k)
  public static int splitArrayDp(int[] arr, int k) {
    if (arr == null || arr.length == 0 || k == 0) {
      return 0;
    }
    int[] preSum = Utility.calcPreSum(arr);
    int[][] dp = new int[arr.length][k + 1];

    // dp[i][1] = sum from 0 ~ i
    for (int i = 0; i < arr.length; i++) {
      dp[i][1] = Utility.getSum(preSum, 0, i);
    }
    // dp[0][j] = arr[0]
    for (int j = 1; j <= k; j++) {
      dp[0][j] = arr[0];
    }

    for (int i = 1; i < arr.length; i++) {
      for (int j = 2; j <= k; j++) {
        int result = Integer.MAX_VALUE;
        for (int split = 0; split <= i; split++) {
          // 左侧的所有画由k个画家负责
          int leftCost = dp[split][j - 1];
          // 右侧的所有画只由一个画家负责
          int rightCost = Utility.getSum(preSum, split + 1, i);
          // 瓶颈
          int bottleNeck = Math.max(leftCost, rightCost);
          result = Math.min(result, bottleNeck);
        }
        dp[i][j] = result;
      }
    }
    return dp[arr.length - 1][k];
  }

  // 利用四边形不等式优化, O(N*k)
  public static int splitArrayQuad(int[] arr, int k) {
    if (arr == null || arr.length == 0 || k == 0) {
      return 0;
    }
    int[] preSum = Utility.calcPreSum(arr);
    int[][] dp = new int[arr.length][k + 1];
    int[][] bestSplits = new int[arr.length][k + 1];

    // dp[i][1] = sum from 0 ~ i
    for (int i = 0; i < arr.length; i++) {
      dp[i][1] = Utility.getSum(preSum, 0, i);
      bestSplits[i][1] = -1;
    }
    // dp[0][j] = arr[0]
    for (int j = 1; j <= k; j++) {
      dp[0][j] = arr[0];
      bestSplits[0][j] = -1;
    }

    // dp[i][j] depends on dp[i][j-1], dp[i-1][j-1] ... dp[0][j-1]
    // ▢ ■ ▢ ▢ ▢
    // ▢ ■ ▢ ▢ ▢
    // ▢ ■ * ▢ ▢  *->(i, j)
    // ▢ ▢ ▢ ▢ ▢
    for (int j = 2; j <= k; j++) {
      for (int i = arr.length - 1; i >= 1; i--) {
        int result = Integer.MAX_VALUE;
        int curBestSplit = -1;
        // the bottom positions don't have upper limit
        int lowerLim = bestSplits[i][j - 1];
        int upperLim = i == arr.length - 1 ? arr.length - 1 : bestSplits[i + 1][j];
        // common positions
        for (int split = lowerLim; split <= upperLim; split++) {
          int leftCost = split == -1 ? 0 : dp[split][j - 1];
          int rightCost =
              split == i ? 0 : Utility.getSum(preSum, split + 1, i);
          int bottleNeck = Math.max(leftCost, rightCost);
          if (bottleNeck < result) { // 必须是小于。证明很复杂，有些题目是小于，另一些是小于等于。放进对数器验证就行了。
            result = bottleNeck;
            curBestSplit = split;
          }
        }
        dp[i][j] = result;
        bestSplits[i][j] = curBestSplit;
      }
    }
    return dp[arr.length - 1][k];
  }

  // Optimal solution, use the idea of binary search
  // O(N*log(sum)) and sum in the question is not too big (long type), can be treated as O(N),
  // because long type is 2^64, log(2^64) = 64, O(64N) = O(N)
  public static int splitArrayOptimal(int[] arr, int M) {
    long sum = 0;
    for (int i = 0; i < arr.length; i++) {
      sum += arr[i];
    }
    long l = 0;
    long r = sum;
    long ans = 0;
    while (l <= r) {
      long mid = (l + r) / 2;
      long cur = getNeedParts(arr, mid);
      if (cur <= M) {
        ans = mid;
        r = mid - 1;
      } else {
        l = mid + 1;
      }
    }
    return (int) ans;
  }

  public static int getNeedParts(int[] arr, long aim) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] > aim) {
        return Integer.MAX_VALUE;
      }
    }
    int parts = 1;
    int all = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (all + arr[i] > aim) {
        parts++;
        all = arr[i];
      } else {
        all += arr[i];
      }
    }
    return parts;
  }

  public static void main(String[] args) {
    // cost too large ->O(N^3), test takes long
    int testCycles = 1000;
    int maxLen = 10;
    int maxVal = 20;
    System.out.println("test begin");
    for (int i = 0; i < testCycles; i++) {
      int k = (int) (Math.random() * maxVal) + 1;
      int[] arr = Utility.randomArrayGenerator(maxLen, maxVal, true);
      int ans1 = splitArray(arr, k);
      int ans2 = splitArrayDp(arr, k);
      int ans3 = splitArrayQuad(arr, k);
      int ans4 = splitArrayOptimal(arr, k);
      if (ans1 != ans2 || ans2 != ans3 || ans3!= ans4) {
        System.out.println("test fail");
        break;
      }
    }
    System.out.println("test end");
  }
}
