package 左程云体系学习班.Lecture41;

import Util.Utility;

public class Code03_StoneMerge {

  /*
   * 四边形不等式技巧特征
   * 1，两个可变参数的区间划分问题
   * 2，每个格子有枚举行为
   * 3，当两个可变参数固定一个，另一个参数和答案之间存在单调性关系
   * 4，而且两组单调关系是反向的：(升 升，降 降)  (升 降，降 升)
   * 5，能否获得指导枚举优化的位置对：上+右，或者，左+下
   *
   * 四边形不等式技巧注意点
   * 1，不要证明！用对数器验证！
   * 2，枚举的时候面对最优答案相等的时候怎么处理？用对数器都试试！
   * 3，可以把时间复杂度降低一阶
   * O(N^3) -> O(N^2)
   * O(N^2 * M) -> O(N * M)
   * O(N * M^2) -> O(N * M)
   * 4，四边形不等式有些时候是最优解，有些时候不是。不是的原因：尝试思路，在根儿上不够好
   *
   * 题目：摆放着n堆石子。现要将石子有次序地合并成一堆，规定每次只能选相邻的2堆石子合并成新的一堆
   * 并将新的一堆石子数记为该次合并的得分，求出将n堆石子合并成一堆的最小得分合并方案
   *
   * */

  public static int minMergeCost(int[] arr) {
    if (arr == null || arr.length == 0) {
      return 0;
    }
    int[] preSum = Utility.calcPreSum(arr);
    return process(preSum, 0, arr.length - 1);
  }

  // in the range arr[L..R], return the minimum merge cost
  private static int process(int[] preSum, int L, int R) {
    if (L == R) {
      return 0;
    }
    int next = Integer.MAX_VALUE;
    for (int split = L; split < R; split++) {
      next = Math.min(next, process(preSum, L, split) + process(preSum, split + 1, R));
    }
    return next + Utility.getSum(preSum, L, R);
  }

  // The classic dp approach
  public static int minMergeCostDp(int[] arr) {
    if (arr == null || arr.length == 0) {
      return 0;
    }
    int[] preSum = Utility.calcPreSum(arr);
    int[][] dp = new int[arr.length][arr.length];
    // dp[i][i] = 0
    for (int L = arr.length - 2; L >= 0; L--) {
      for (int R = L + 1; R < arr.length; R++) {
        int next = Integer.MAX_VALUE;
        for (int split = L; split < R; split++) {
          next = Math.min(next, dp[L][split] + dp[split + 1][R]);
        }
        dp[L][R] = next + Utility.getSum(preSum, L, R);
      }
    }

    return dp[0][arr.length-1];
  }

  // 利用四边形不等式优化: 减少枚举的计算
  public static int minMergeCostQuad(int[] arr) {
    if (arr == null || arr.length == 0) {
      return 0;
    }
    int[] preSum = Utility.calcPreSum(arr);
    int[][] dp = new int[arr.length][arr.length];
    // bestSplit[L][R]用来记录dp[L][R]中的最优划分点
    int[][] bestSplit = new int[arr.length][arr.length];
    // dp[i][i] = 0
    // dp[i][i+1] = arr[i]+arr[i+1]
    for (int i = 0; i < arr.length - 1; i++) {
      dp[i][i + 1] = arr[i] + arr[i + 1];
      bestSplit[i][i + 1] = i;
    }
    for (int L = arr.length - 3; L >= 0; L--) {
      for (int R = L + 2; R < arr.length; R++) {
        int next = Integer.MAX_VALUE;
        int curBestSplit = -1;
        // 利用bestSplit来优化枚举的计算：下限是左边格子split点，上限是下边格子split点
        for (int split = bestSplit[L][R - 1]; split <= bestSplit[L + 1][R]; split++) {
          int cur = dp[L][split] + dp[split + 1][R];
          if (cur < next) {
            next = cur;
            curBestSplit = split;
          }
        }
        dp[L][R] = next + Utility.getSum(preSum, L, R);
        bestSplit[L][R] = curBestSplit;
      }
    }
    return dp[0][arr.length - 1];
  }

  public static void main(String[] args) {
    // cost too large ->O(N^3), test takes long
    int testCycles = 100;
    int maxLen = 10;
    int maxVal = 10;
    System.out.println("test begin");
    for (int i = 0; i < testCycles; i++) {
      int[] stones = Utility.randomArrayGenerator(maxLen, maxVal, true);
      int ans1 = minMergeCost(stones);
      int ans2 = minMergeCostDp(stones);
      int ans3 = minMergeCostQuad(stones);
      if (ans1 != ans2 || ans2 != ans3) {
        System.out.println("test fail");
        break;
      }
    }
    System.out.println("test end");
  }
}
