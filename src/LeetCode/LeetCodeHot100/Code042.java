package LeetCode.LeetCodeHot100;

import java.util.Arrays;


import static 左程云体系学习班.Lecture21.randomArray;


public class Code042 {


  // count level by level but the time complexity it too high, with O(N*k), where N is the length of the heights,
  // and k is the maximum height (level) of the heights.
  public static int trap(int[] height) {
    int maxHeight = Arrays.stream(height).reduce(Math::max).orElse(0);
    int totalTrap = 0;
    for (int h = maxHeight; h > 0; h--) {
      int[] level = new int[height.length];
      for (int i = 0; i < level.length; i++) {
        level[i] = Math.max(height[i] - h + 1, 0);
      }

      // calculate at each level
      int left = 0;
      int right = 0;
      int levelTrap = 0;
      while (right < level.length) {
        if (level[right] >= 1) {
          if (level[left] >= 1 && right > 0) {
            levelTrap += right - left - 1;
            left = right;
          } else {
            left = right;
          }
        }
        right++;
      }
      totalTrap += levelTrap;
    }
    return totalTrap;
  }

  // double pointers
  public static int trapDoublePointers(int[] height) {
    int left = 0;
    int right = height.length - 1;
    int left_max = 0;
    int right_max = 0;
    int ans = 0;
    while (left < right) {
      if (height[left] < height[right]) {
        if (height[left] >= left_max) {
          left_max = height[left];
        } else {
          ans += left_max - height[left];
        }
        left++;
      } else {
        if (height[right] >= right_max) {
          right_max = height[right];
        } else {
          ans += right_max - height[right];
        }
        right--;
      }
    }
    return ans;
  }

  public static void main(String[] args) {
    int cycle = 10000;
    int maxLen = 100;
    int maxValue = 100;
    System.out.println("Test Begin");
    for (int i = 0; i < cycle; i++) {
      int[] heights = randomArray(maxLen, maxValue);
      int ans1 = trap(heights);
      int ans2 = trap(heights);
      if (ans1 != ans2) {
        System.out.println("Test Fail!");
        break;
      }
    }
    System.out.println("Test End");
  }
}
