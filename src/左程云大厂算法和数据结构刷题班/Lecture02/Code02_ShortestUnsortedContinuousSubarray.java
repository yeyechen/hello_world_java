package 左程云大厂算法和数据结构刷题班.Lecture02;

public class Code02_ShortestUnsortedContinuousSubarray {

  /*
   * leetcode 581: 给你一个整数数组 nums ，你需要找出一个 连续子数组 ，
   * 如果对这个子数组进行升序排序，那么整个数组都会变为升序排序。
   * 请你找出符合题意的 最短 子数组，并输出它的长度。
   * */
  public static int findUnsortedSubarray(int[] nums) {
    int N = nums.length;
    int leftMax = nums[0];
    int leftMark = -1;
    for (int i = 1; i < N; i++) {
      if (leftMax > nums[i]) {
        leftMark = i;
      } else {
        leftMax = nums[i];
      }
    }

    int rightMin = nums[N - 1];
    int rightMark = N;
    for (int i = N - 2; i >= 0; i--) {
      if (rightMin < nums[i]) {
        rightMark = i;
      } else {
        rightMin = nums[i];
      }
    }

    return Math.max(leftMark - rightMark + 1, 0);
  }
}
