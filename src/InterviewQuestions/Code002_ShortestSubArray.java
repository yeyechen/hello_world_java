package InterviewQuestions;

public class Code002_ShortestSubArray {
  /*
  * 来自字节跳动
  * 给定一个数组arr，其中的值有可能正、负、0
  * 给定一个正数k
  * 返回累加和>=k的所有子数组中，最短的子数组长度
  * */

  // 暴力+滑动窗口 (O(N^2))
  public static int shortestSubarray(int[] nums, int k) {
    for (int i = 1; i <= nums.length; i++) {
      if (slideFind(nums, k, i)) {
        return i;
      }
    }
    return -1;
  }

  private static boolean slideFind(int[] nums, int k, int windowLen) {
    int left = 0;
    int right = windowLen;
    // 窗口左闭右开
    while (right <= nums.length) {
      int currSum = 0;
      // 计算窗口内的和
      for (int i = left; i < right; i++) {
        currSum += nums[i];
      }
      if (currSum >= k) {
        return true;
      }
      left++;
      right++;
    }
    return false;
  }

  public static int shortestSubarray2(int[] nums, int k) {
    int N = nums.length;
    long[] sum = new long[N + 1];
    // 前缀和
    for (int i = 0; i < N; i++) {
      sum[i + 1] = sum[i] + nums[i];
    }
    int ans = Integer.MAX_VALUE;
    int[] dq = new int[N + 1];
    int l = 0;
    int r = 0;

    for (int i = 0; i <= N; i++) {
      while (l != r && sum[i] - sum[dq[l]] >= k) {
        ans = Math.min(ans, i - dq[l++]);
      }
      while (l != r && sum[dq[r - 1]] >= sum[i]) {
        r--;
      }
      dq[r++] = i;
    }
    return ans != Integer.MAX_VALUE ? ans : -1;
  }

  public static void main(String[] args) {
    int[] nums = new int[]{2, -1, 2};
    System.out.println(shortestSubarray2(nums, 3));
  }
}
