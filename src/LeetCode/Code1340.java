package LeetCode;

import static 左程云体系学习班.Lecture05.randomArrayGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Code1340 {

  // 暴力递归+枚举
  public static int maxJumps(int[] arr, int d) {
    int max = 0;
    Map<Integer, Integer> dp = new HashMap<>();
    for (int i = 0; i < arr.length; i++) {
      max = Math.max(max, jump(arr, d, i, 1));
    }
    return max;
  }

  public static int jump(int[] arr, int d, int i, int steps) {

    if ((i + 1 == arr.length || arr[i + 1] >= arr[i]) && (i - 1 < 0 || arr[i - 1] >= arr[i])) {
      return steps;
    }
    int p1 = 0;
    for (int x = 1; x <= d && i + x < arr.length && arr[i + x] < arr[i]; x++) {
      p1 = Math.max(p1, jump(arr, d, i + x, steps + 1));
    }
    int p2 = 0;
    for (int x = 1; x <= d && i - x >= 0 && arr[i - x] < arr[i]; x++) {
      p2 = Math.max(p2, jump(arr, d, i - x, steps + 1));
    }
    return Math.max(p1, p2);
  }

  public static int maxJumpsDp(int[] arr, int d) {
    int[] f = new int[arr.length];
    Arrays.fill(f, -1);
    for (int i = 0; i < arr.length; i++) {
      dfs(arr, d, i, f);
    }
    return Arrays.stream(f).max().orElse(-1);
  }

  private static void dfs(int[] arr, int d, int i, int[] f) {
    if (f[i] != -1) {
      return;
    }
    f[i] = 1;
    for (int x = 1; x <= d && i + x < arr.length && arr[i + x] < arr[i]; x++) {
      dfs(arr, d, i + x, f);
      f[i] = Math.max(f[i], f[i + x] + 1);
    }
    for (int x = 1; x <= d && i - x >= 0 && arr[i - x] < arr[i]; x++) {
      dfs(arr, d, i - x, f);
      f[i] = Math.max(f[i], f[i - x] + 1);
    }
  }

  public static void main(String[] args) {
    int cycles = 10000;
    int maxValue = 100;
    int maxLen = 100;
    System.out.println("Test Begin!");
    for (int i = 0; i < cycles; i++) {
      int[] arr = randomArrayGenerator(maxValue, maxLen);
      int d = (int) (Math.random() * maxLen);
      int ans1 = maxJumps(arr, d);
      int ans2 = maxJumpsDp(arr, d);
      if (ans1 != ans2) {
        System.out.println("Fail!");
        break;
      }
    }
    System.out.println("Test End!");
  }
}
