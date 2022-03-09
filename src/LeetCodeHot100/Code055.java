package LeetCodeHot100;

import static 左程云体系学习班.Lecture05.randomArrayGenerator;

public class Code055 {

  public static boolean canJump(int[] nums) {
    if (nums.length == 0) {
      return false;
    }
    return jumpProcess(nums, 0);
  }

  private static boolean jumpProcess(int[] nums, int index) {
    if (index == nums.length - 1) {
      return true;
    }
    if (nums[index] == 0) {
      return false;
    }
    boolean result = false;
    for (int i = 1; i <= nums[index] && index + i < nums.length; i++) {
      result |= jumpProcess(nums, index + i);
    }
    return result;
  }

  // O(2N): dynamic programming
  public static boolean canJumpDp(int[] nums) {
    int N = nums.length;
    if (N == 0) {
      return false;
    }
    boolean[] dp = new boolean[N]; // boolean[] initialised to false for every element
    dp[N - 1] = true;

    for (int index = N - 2; index >= 0; index--) {
      boolean result = false;
      for (int i = Math.min(index + nums[index], N - 1); i > index; i--) { // a bit greedy on the for loop
        if (dp[i]) {
          result = true;
          break;
        }
      }
      dp[index] = result;
    }
    return dp[0];
  }

  // O(N): greedy
  public static boolean canJumpGreedy(int[] nums) {
    int n = nums.length;
    int rightmost = 0;
    for (int i = 0; i < n; ++i) {
      if (i <= rightmost) {
        rightmost = Math.max(rightmost, i + nums[i]);
        if (rightmost >= n - 1) {
          return true;
        }
      }
    }
    return false;
  }

  public static void main(String[] args) {
    int cycles = 20000;
    int maxLen = 100;
    int maxValue = 30;
    System.out.println("Test Begin");
    for (int i = 0; i < cycles; i++) {
      int[] nums = randomArrayGenerator(maxLen, maxValue);
      boolean ans1 = canJumpDp(nums);
      boolean ans2 = canJumpGreedy(nums);
      if (ans1 != ans2) {
        System.out.println("Test Fail!");
        break;
      }
    }
    System.out.println("Test End");
  }
}
