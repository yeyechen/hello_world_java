package 左程云体系学习班.Lecture40;

import Util.Utility;

public class Code03_LongestLessSumSubArrayLength {

  /*
   * 给定一个整数组成的无序数组arr，值可能正、可能负、可能0，给定一个整数值K
   * 找到arr的所有子数组里，哪个子数组的累加和<=K，并且是长度最大的，返回其长度
   *
   * 主要技巧：假设答案法 + 淘汰可能性
   *
   * */

  // optimum time complexity of O(N)
  public static int getMaxLength(int[] arr, int k) {
    if (arr == null || arr.length == 0 || k == 0) {
      return 0;
    }
    // generate two helper arrays
    // recording the minimum sub-sum, starting from i, ending at minSumEnd[i]
    int[] minSum = new int[arr.length];
    int[] minSumEnd = new int[arr.length];
    // initialise
    int endIndex = arr.length - 1;
    minSum[endIndex] = arr[endIndex];
    minSumEnd[endIndex] = endIndex;

    // walk through from right to left
    for (int i = endIndex - 1; i >= 0; i--) {
      if (minSum[i + 1] <= 0) {
        minSum[i] = arr[i] + minSum[i + 1];
        minSumEnd[i] = minSumEnd[i+1];
      } else {
        minSum[i] = arr[i];
        minSumEnd[i] = i;
      }
    }

    // use the "longest length" constraint, to filter out shorter possible answers.
    // use sliding window [L,R)
    int R = 0;
    int sum = 0;
    int maxLength = 0;
    for (int L = 0; L < minSum.length; L++) {
      while (R < arr.length && sum + minSum[R] <= k ) {
        sum += minSum[R];
        R = minSumEnd[R] + 1;
      }
      maxLength = Math.max(maxLength, R - L);
      if (R > L) {
        sum -= arr[L];
      } else { // maintain the window
        R = L + 1;
      }
    }
    return maxLength;
  }

  // O(N*logN)
  public static int slower(int[] arr, int k) {
    int[] h = new int[arr.length + 1];
    int sum = 0;
    h[0] = sum;
    for (int i = 0; i != arr.length; i++) {
      sum += arr[i];
      h[i + 1] = Math.max(sum, h[i]);
    }
    sum = 0;
    int res = 0;
    int pre = 0;
    int len = 0;
    for (int i = 0; i != arr.length; i++) {
      sum += arr[i];
      pre = getLessIndex(h, sum - k);
      len = pre == -1 ? 0 : i - pre + 1;
      res = Math.max(res, len);
    }
    return res;
  }

  private static int getLessIndex(int[] arr, int num) {
    int low = 0;
    int high = arr.length - 1;
    int mid = 0;
    int res = -1;
    while (low <= high) {
      mid = (low + high) / 2;
      if (arr[mid] >= num) {
        res = mid;
        high = mid - 1;
      } else {
        low = mid + 1;
      }
    }
    return res;
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
      int ans2 = slower(arr, K);
      if (ans1 != ans2) {
        System.out.println("Oops!");
        System.out.println("K : " + K);
        System.out.println(ans1);
        System.out.println(ans2);
        break;
      }
    }
    System.out.println("test end");
  }
}
