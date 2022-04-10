package LeetCode.LeetCodeHot100;

import java.util.Arrays;

public class Code031 {

  public static void nextPermutation(int[] nums) {
    if (nums.length == 1) {
      return;
    }
    int left = nums.length - 2;
    int right = nums.length - 1;
    while (right != 0) {
      if (nums[right] > nums[left]) {
        // find the smallest number that is bigger than the left pointer number, and swap them
        for (int i = right; i < nums.length; i++) {
          if (nums[i] < nums[right] && nums[i] > nums[left]) {

            right = i;
          }
        }
        swap(nums, left, right);
        // after swapping sort the [left+1 : N-1]
        Arrays.sort(nums, left + 1, nums.length);
        return;
      }
      left--;
      right--;
    }
    // could not find, meaning the arr is in descending order
    for (int i = 0; i < nums.length >> 1; i++) {
      swap(nums, i, nums.length - i - 1);
    }
  }

  private static void swap(int[] nums, int i, int j) {
    int temp =nums[j];
    nums[j] = nums[i];
    nums[i] = temp;
  }

  public static void main(String[] args) {
    int[] arr = new int[]{3,2,1};
    nextPermutation(arr);
    System.out.println(Arrays.toString(arr));
  }
}
