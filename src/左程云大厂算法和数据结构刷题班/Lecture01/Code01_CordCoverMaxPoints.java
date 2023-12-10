package 左程云大厂算法和数据结构刷题班.Lecture01;

import Util.Utility;
import java.util.Arrays;

public class Code01_CordCoverMaxPoints {

  /*
   * 题目：给定一个有序数组arr，代表坐落在X轴上的点，给定一个正数K，代表绳子的长度，返回绳子最多压中几个点？
   * 即使绳子边缘处盖住点也算盖住
   *
   * 两种解法，第一种O(N * logN); 第二种用滑动窗口O(N)。两种都比较常规。
   * */

  public static int maxPoints1(int[] arr, int K) {
    if (arr == null || arr.length == 0) {
      return 0;
    }
    // let the right end of the rope be at the each point, move to the right, and get max
    int result = 1;
    for (int i = 0; i < arr.length; i++) {
      int mostFarPoints = farPointWithinRange(arr, arr[i] - K, i);
      result = Math.max(result, i - mostFarPoints + 1);
    }
    return result;
  }

  // Binary search to find the most far away index within the range of rope
  private static int farPointWithinRange(int[] arr, int value, int R) {
    int L = 0;
    int result = R;
    while (L <= R) {
      int mid = L + ((R - L) >> 1);
      if (arr[mid] >= value) {
        result = mid;
        R = mid - 1;
      } else {
        L = mid + 1;
      }
    }
    return result;
  }

  // second method to use the sliding window
  public static int maxPoints2(int[] arr, int K) {
    if (arr == null || arr.length == 0) {
      return 0;
    }
    int L = 0;
    int R = 0;
    int result = 1;
    while (R < arr.length && L <= R) {
      // try to cover more
      if (arr[R] - arr[L] <= K) {
        R++;
      } else { //
        L++;
      }
      result = Math.max(result, R - L);
    }
    return result;
  }

  public static void main(String[] args) {
    int testCycles = 10000;
    int maxValue = 100;
    int maxLength = 50;

    System.out.println("test begins");
    for (int i = 0; i < testCycles; i++) {
      int[] arr = Utility.randomArrayGenerator(maxLength, maxValue, false);
      Arrays.sort(arr);
      int K = (int) (Math.random() * arr[arr.length - 1]);
      int ans1 = maxPoints1(arr, K);
      int ans2 = maxPoints2(arr, K);
      if (ans1 != ans2) {
        System.out.println(Arrays.toString(arr));
        System.out.println(K);
        System.out.println(ans1);
        System.out.println(ans2);
        System.out.println("error");
        break;
      }
    }
    System.out.println("test finished");
  }
}
