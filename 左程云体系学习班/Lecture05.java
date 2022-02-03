package 左程云体系学习班;

import java.util.Arrays;

public class Lecture05 {
  /*
   *
   * 1. Merge Sort附加题(leetcode 327)：给你一个整数数组，nums以及两个整数lower和upper。求数组中，
   * 和的值位于范围[lower, upper]之内的子数组的个数。
   * 精髓：假设0～i整体累加和是x，那么求以i位置结尾的子数组中，有多少个在[lower, upper]范围上，等同于求i之前的所有
   * 前缀和中有多少前缀和在[x-upper, x-lower]范围上。
   *
   * 2. 快速排序
   * 2.1 快速排序1.0: 每次只确定一个数字的位置，数组变为[<=x, x, >x]，其中<=x和>x部分无序
   * 2.2 快速排序2.0: 每次确定一批数的位置，把所有==x的数放在中间，数组变为[<x, ==x, >x]，其中<x和>x部分无序
   * 2.3 快速排序3.0: 在partition时，最右侧的数通过随机抽取。因为在2.0和1.0中，最差的情况下时间复杂度为O(N^2)，
   *                 因为每次recurse时最右侧的书可能是当前子数组中对大的数。在3.0中，将这种可能性随机化，最差时间复杂度的
   *                 概率为1/N。通过数学证明，每次随机抽取可使时间复杂度降为O(N * log(N))。此外，额外空间复杂度也从原来的
   *                 O(N) 降为 O(logN)。
   *
   * */

  //1. leetcode 327
  public static int countRangeSum(int[] nums, int lower, int upper) {
    if (nums == null || nums.length == 0) {
      return 0;
    }
    long sum[] = new long[nums.length];
    sum[0] = nums[0];
    for (int i = 1; i < nums.length; i++) {
      sum[i] = sum[i - 1] + nums[i];
    }
    return countRangeSumProcess(sum, 0, nums.length - 1, lower, upper);
  }

  //在原始数组arr[l..r]中，返回有多少子数组的和在[lower, upper]中
  private static int countRangeSumProcess(long[] sum, int l, int r, int lower, int upper) {
    if (l == r) {
      return sum[l] >= lower && sum[l] <= upper ? 1 : 0;
    }
    int mid = l + ((r - l) >> 1);
    return countRangeSumProcess(sum, l, mid, lower, upper) + countRangeSumProcess(sum, mid + 1, r,
        lower, upper) + merge(sum,
        l, mid, r, lower, upper);
  }

  private static int merge(long[] sum, int l, int mid, int r, int lower, int upper) {
    int ans = 0;
    int windowL = l;
    int windowR = l;

    //[windowL, windowR)
    for (int i = mid + 1; i <= r; i++) {
      long min = sum[i] - upper;
      long max = sum[i] - lower;
      while (windowR <= mid && sum[windowR] <= max) {
        windowR++;
      }
      while (windowL <= mid && sum[windowL] < min) {
        windowL++;
      }
      ans += windowR - windowL;
    }

    long[] help = new long[r - l + 1];

    int i = 0;
    int p1 = l;
    int p2 = mid + 1;
    while (p1 <= mid && p2 <= r) {
      help[i++] = sum[p1] <= sum[p2] ? sum[p1++] : sum[p2++];
    }
    while (p1 <= mid) {
      help[i++] = sum[p1++];
    }
    while (p2 <= r) {
      help[i++] = sum[p2++];
    }
    for (i = 0; i < help.length; i++) {
      sum[l + i] = help[i];
    }
    return ans;
  }

  //快速排序1.0
  public static void quickSort1(int[] arr) {
    if (arr == null && arr.length < 2) {
      return;
    }
    quickSort1Process(arr, 0, arr.length - 1);
  }

  private static void quickSort1Process(int[] arr, int l, int r) {
    if (l >= r) {
      return;
    }
    int mid = partition(arr, l, r);
    quickSort1Process(arr, l, mid - 1);
    quickSort1Process(arr, mid + 1, r);
  }

  private static int partition(int[] arr, int l, int r) {
    if (l > r) {
      return -1;
    }
    if (l == r) {
      return l;
    }
    int lessEqualIndex = l - 1;
    int index = l;
    // [0, 1, 2, 3, 4, 5, 6]
    while (index < r) {
      if (arr[index] <= arr[r]) {
        swap(arr, ++lessEqualIndex, index);
      }
      index++;
    }
    swap(arr, ++lessEqualIndex, r);
    return lessEqualIndex;
  }

  static void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }

  // 快速排序2.0
  public static void quickSort2(int[] arr) {
    if (arr == null && arr.length < 2) {
      return;
    }
    quickSort2Process(arr, 0, arr.length - 1);
  }

  private static void quickSort2Process(int[] arr, int l, int r) {
    if (l >= r) {
      return;
    }
    int[] mid = netherlandsFlag(arr, l, r);
    quickSort2Process(arr, l, mid[0] - 1);
    quickSort2Process(arr, mid[1] + 1, r);
  }

  //荷兰国旗问题，把一个arr通过给定一个x分为[<x, ==x, >x]三个部分，返回==x部分的开头和结尾index。相当于优化版partition
  private static int[] netherlandsFlag(int[] arr, int l, int r) {
    if (l == r) {
      return new int[]{l, r};
    }
    int lessEqualIndex = l - 1;
    int largerIndex = r;
    int index = l;
    while (index < largerIndex) {
      if (arr[index] < arr[r]) {
        swap(arr, ++lessEqualIndex, index++);
      } else if (arr[index] > arr[r]) {
        swap(arr, --largerIndex, index);
      } else {
        index++;
      }
    }
    swap(arr, largerIndex, r);
    return new int[]{lessEqualIndex + 1, largerIndex - 1};
  }

  //快速排序3.0
  public static void quickSort3(int[] arr) {
    if (arr == null || arr.length < 2) {
      return;
    }
    quickSort3Process(arr, 0, arr.length - 1);
  }

  private static void quickSort3Process(int[] arr, int l, int r) {
    if (l >= r) {
      return;
    }
    //随机选择arr[l..r]中的数字，与r位置进行交换
    int randomIndex = l + (int) (Math.random() * (r - l + 1));
    swap(arr, randomIndex, r);

    int[] mid = netherlandsFlag(arr, l, r);
    quickSort3Process(arr, l, mid[0] - 1);
    quickSort3Process(arr, mid[1] + 1, r);
  }

  public static void main(String[] args) {
    int testAmount = 10000;
    int maxValue = 10;
    int maxLen = 5;

    System.out.println("Test begin!");
    long startTime = System.currentTimeMillis();
    for (int i = 0; i < testAmount; i++) {
      int[] arr = randomArrayGenerator(maxLen, maxValue);
      quickSort3(arr);
    }
    long endTime = System.currentTimeMillis();
    System.out.println("quickSort 3.0 finish in " + (endTime - startTime) + " ms");
  }

  private static int[] randomArrayGenerator(int maxLen, int maxValue) {
    int len = (int) (Math.random() * maxLen) + 1;
    int[] result = new int[len];
    for (int i = 0; i < len; i++) {
      result[i] = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * maxValue);
    }
    return result;
  }


}
