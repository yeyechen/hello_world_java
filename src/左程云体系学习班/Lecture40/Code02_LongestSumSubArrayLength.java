package 左程云体系学习班.Lecture40;

import Util.Utility;
import java.util.Arrays;
import java.util.HashMap;

public class Code02_LongestSumSubArrayLength {

  /*
   * 给定一个整数组成的无序数组arr，值可能正、可能负、可能0，给定一个整数值K
   * 找到arr的所有子数组里，哪个子数组的累加和等于K，并且是长度最大的，返回其长度
   *
   * 有负数就代表单调性消失了，不能用滑动窗口了。
   * 看到题目中"所有子数组"首先第一想法应该是 找以每个位置结尾的情况下答案是什么，并且利用累加和。
   *
   * 主要技巧：利用预处理结构优化 + 讨论开头结尾
   *
   * */

  public static int getMaxLength(int[] arr, int k) {
    if (arr == null || arr.length == 0 || k == 0) {
      return 0;
    }
    // calculate pre-sum
    int[] preSum = new int[arr.length];
    int sum = 0;
    for (int i = 0; i < arr.length; i++) {
      sum += arr[i];
      preSum[i] = sum;
    }

    int maxLength = 0;
    HashMap<Integer, Integer> preSumIndex = new HashMap<>();
    preSumIndex.put(0, -1); // initial step, otherwise loose answers which is the first element.
    for (int i = 0; i < arr.length; i++) {
      int requiredPreSum = preSum[i] - k;

      Integer L = preSumIndex.get(requiredPreSum);
      if (L != null) {
        maxLength = Math.max(maxLength, i - L);
      }

      // only put the first time the same pre-sum occurs, ensures the longest length.
      if (!preSumIndex.containsKey(preSum[i])) {
        preSumIndex.put(preSum[i], i);
      }
    }
    return maxLength;
  }

  public static void main(String[] args) {
    int len = 50;
    int value = 100;
    int testTime = 500000;
    System.out.println("test begin");
    for (int i = 0; i < testTime; i++) {
      int[] arr = Utility.randomArrayGenerator(len, value, false);
      int K = (int) (Math.random() * value) + 1;
      int ans1 = getMaxLength(arr, K);
      int ans2 = Code01_LongestSumSubArrayLengthNoneNeg.bruteForce(arr, K);
      if (ans1 != ans2) {
        System.out.println("Oops!");
        System.out.println("K : " + K);
        System.out.println(Arrays.toString(arr));
        System.out.println(ans1);
        System.out.println(ans2);
        break;
      }
    }
    System.out.println("test end");
  }
}
