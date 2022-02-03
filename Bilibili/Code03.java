package Bilibili;

import java.util.HashMap;

public class Code03 {

//  leetcode 494: 目标和。

  //基础:加缓存
  public static int findTargetSumWays(int[] nums, int target) {
    return findSubSumWays(nums, 0, target, new HashMap<>());
  }

  //index = 7 target = 13 -> 256
  // {
  //  7: {13: 256},{...}
  //  ...
  // }

  private static int findSubSumWays(int[] nums, int index, int target,
      HashMap<Integer, HashMap<Integer, Integer>> cache) {
    //  check cache:
    if (cache.containsKey(index) && cache.get(index).containsKey(target)) {
      return cache.get(index).get(target);
    }

    //  base case:
    int ans = 0;
    if (index == nums.length) {
      ans = target == 0 ? 1 : 0; // if the target == 0, counts as one way.
    } else {
      ans = findSubSumWays(nums, index + 1, target - nums[index], cache) + findSubSumWays(nums,
          index + 1,
          target + nums[index], cache);
    }
    //  build cache:
    if (!cache.containsKey(index)) {
      cache.put(index, new HashMap<>());
    }
    cache.get(index).put(target, ans);
    return ans;
  }

// 优化:

//  1. 可以认为(或者转换)arr中都是非负数。
//  2. 如果arr中都是非负数，所有数累加的totalSum如果小于target，返回0。
//  3. 如果totalSum和target的奇偶性不一致，返回0。
//  4. 分正集合P和负集合N，我们有sum(P) - sum(N) = target -> 2*sum(P) = target + totalSum ->
//     sum(P) = (target + totalSum) / 2 ，转变为经典背包问题。
//  5. 二维动态规划空间压缩技巧 (?)

  public static int findTargetSumWays2(int[] arr, int target) {
    int sum = 0;
    for (int n : arr) {
      sum += n < 0 ? -n : n;
    }

    if (sum < target) {
      return 0;
    }
    //检查奇偶:
    if (((sum & 1) ^ (target & 1)) != 0) {
      return 0;
    }

    return subset(arr, (target + sum) >> 1);
  }

  //背包问题(?):
  private static int subset(int[] nums, int sum) {
    int[] dp = new int[sum + 1];
    dp[0] = 1;
    for (int n : nums) {
      for (int i = sum; i >= n; i--) {
        dp[i] += dp[i - n];
      }
    }
    return dp[sum];
  }

  public static void main(String[] args) {
    System.out.println(findTargetSumWays2(new int[]{1, 2, 3, 4, 5}, 3));
  }
}
