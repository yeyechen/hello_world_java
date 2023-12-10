package 左程云大厂算法和数据结构刷题班.Lecture01;

public class Code04_TargetSum {

  /*
   * leetcode 494题：给你一个整数数组 nums 和一个整数 target 。
   * 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
   * 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。
   * 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
   * */

  // 暴力递归
  public static int findTargetSumWays1(int[] nums, int target) {
    return process1(nums, 0, target);
  }

  // nums[index...]中，能够组成rest的方法有多少种
  private static int process1(int[] nums, int index, int rest) {
    if (index == nums.length) {
      return rest == 0 ? 1 : 0;
    }
    int p1 = process1(nums, index + 1, rest - nums[index]);
    int p2 = process1(nums, index + 1, rest + nums[index]);
    return p1 + p2;
  }

  // 优化点一 :
  // 你可以认为arr中都是非负数
  // 因为即便是arr中有负数，比如[3,-4,2]
  // 因为你能在每个数前面用+或者-号
  // 所以[3,-4,2]其实和[3,4,2]达成一样的效果
  // 那么我们就全把arr变成非负数，不会影响结果的
  // 优化点二 :
  // 如果arr都是非负数，并且所有数的累加和是sum
  // 那么如果target<sum，很明显没有任何方法可以达到target，可以直接返回0
  // 优化点三 :
  // arr内部的数组，不管怎么+和-，最终的结果都一定不会改变奇偶性
  // 所以，如果所有数的累加和是sum，
  // 并且与target的奇偶性不一样，没有任何方法可以达到target，可以直接返回0
  // 优化点四 :
  // 比如说给定一个数组, arr = [1, 2, 3, 4, 5] 并且 target = 3
  // 其中一个方案是 : +1 -2 +3 -4 +5 = 3
  // 该方案中取了正的集合为P = {1，3，5}
  // 该方案中取了负的集合为N = {2，4}
  // 所以任何一种方案，都一定有 sum(P) - sum(N) = target
  // 现在我们来处理一下这个等式，把左右两边都加上sum(P) + sum(N)，那么就会变成如下：
  // sum(P) - sum(N) + sum(P) + sum(N) = target + sum(P) + sum(N)
  // 2 * sum(P) = target + 数组所有数的累加和
  // sum(P) = (target + 数组所有数的累加和) / 2
  // 也就是说，任何一个集合，只要累加和是(target + 数组所有数的累加和) / 2
  // 那么就一定对应一种target的方式
  // 也就是说，比如非负数组arr，target = 7, 而所有数累加和是11
  // 求有多少方法组成7，其实就是求有多少种达到累加和(7+11)/2=9的方法
  // 优化点五 :
  // 二维动态规划的空间压缩技巧
  public static int findTargetSumWays(int[] arr, int target) {
    int sum = 0;
    for (int n : arr) {
      sum += n;
    }
    return sum < target || ((target & 1) ^ (sum & 1)) != 0 ? 0 : subset(arr, (target + sum) >> 1);
  }

  public static int subset(int[] nums, int s) {
    if (s < 0) {
      return 0;
    }
    int[] dp = new int[s + 1];
    dp[0] = 1;
    for (int n : nums) {
      for (int i = s; i >= n; i--) {
        dp[i] += dp[i - n];
      }
    }
    return dp[s];
  }
}
