package 左程云体系学习班;

public class Lecture04 {
  /*
   * 1. Merge Sort
   * 时间复杂度表达式: T(N) = 2 * T(N/2) + O(N)
   * 利用Master公式我们知道，时间复杂度为O(N*logN)
   *
   * 1.1 利用递归实现Merge Sort
   * 1.2 不利用递归，利用迭代实现Merge Sort
   *
   * 2. 关于Merge Sort延伸出来的一些题目
   * 2.1 小和问题：在一个数组中，每一个数的小和为该数左侧所有比这个数小的数的和，求整个数组的小和，即所有数的小和的和。
   * 2.2 逆序对问题(LeetCode LCR 170)：在一个数组中，一个数的右侧的数比这个数小的话，这两个数可以被称为一组逆序对，求这个数组总共有多少组逆序对。
   *               这个问题跟2.1非常类似，只不过在merge时两个指针从最右边开始比较。
   * 2.3 两倍问题(leetcode 493)：在一个数组中，如果一个数右侧的某一数的两倍比这个数还要小，这两个数算一组，总共有多少组这样的数。
   *
   *
   * */

  //1.1 利用递归实现Merge Sort
  public static void mergeSort(int[] arr) {
    if (arr == null || arr.length < 2) {
      return;
    }
    process(arr, 0, arr.length - 1);
  }

  //把arr中从index l 到index R排序
  private static void process(int[] arr, int l, int r) {
    if (l == r) {
      return;
    }
    int mid = l + ((r - l) >> 1);
    process(arr, l, mid);
    process(arr, mid + 1, r);
    merge(arr, l, mid, r);
  }

  private static void merge(int[] arr, int l, int mid, int r) {
    int[] help = new int[r - l + 1];
    int i = 0;
    int p1 = l;
    int p2 = mid + 1;

    while (p1 <= mid && p2 <= r) {
      help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
    }
    while (p1 <= mid) {
      help[i++] = arr[p1++];
    }
    while (p2 <= r) {
      help[i++] = arr[p2++];
    }
    System.arraycopy(help, 0, arr, l, help.length);
  }

  //1.2 不利用递归，利用迭代实现Merge Sort
  public static void mergeSort2(int[] arr) {
    int stepSize = 1;

    while (stepSize < arr.length) {
      int l = 0;
      while (l < arr.length) {
        //当右边没有元素时，跳过merge
        if (arr.length - l < stepSize) {
          break;
        }
        int mid = l + stepSize - 1;
        //右边含有数的数量可以在1到stepSize之间，检查边界
        int r = mid + Math.min(stepSize, arr.length - mid - 1);
        merge(arr, l, mid, r);
        l = r + 1;
      }
      //防止int溢出
      if (stepSize > arr.length / 2) {
        break;
      }
      stepSize = stepSize << 1;
    }
  }

  //2.1 小和问题
  public static int smallSum(int[] arr) {
    if (arr == null || arr.length < 2) {
      return 0;
    }
    return smallSumProcess(arr, 0, arr.length - 1);
  }

  private static int smallSumProcess(int[] arr, int l, int r) {
    if (l == r) {
      return 0;
    }
    int smallSum = 0;
    int mid = l + ((r - l) >> 1);
    smallSum += smallSumProcess(arr, l, mid);
    smallSum += smallSumProcess(arr, mid + 1, r);
    smallSum += smallSumMerge(arr, l, mid, r);
    return smallSum;
  }

  private static int smallSumMerge(int[] arr, int l, int mid, int r) {
    int sum = 0;

    int[] help = new int[r - l + 1];
    int i = 0;
    int p1 = l;
    int p2 = mid + 1;

    while (p1 <= mid && p2 <= r) {
      //如果两数相等，必须先放入右边的数，才能正确计算小和
      if (arr[p1] < arr[p2]) {
        //这个数比多少个右侧排序过的数要小，那么就加上多少个左侧小的数
        sum += arr[p1] * (r - p2 + 1);

        help[i] = arr[p1];
        p1++;
      } else {
        help[i] = arr[p2];
        p2++;
      }
      i++;
    }
    while (p1 <= mid) {
      help[i++] = arr[p1++];
    }
    while (p2 <= r) {
      help[i++] = arr[p2++];
    }
    System.arraycopy(help, 0, arr, l, help.length);

    return sum;
  }

  //用O(N^2)的方法实现
  public static int smallSumTest(int[] arr) {
    int sum = 0;
    for (int i = 0; i < arr.length; i++) {
      for (int j = 0; j < i; j++) {
        if (arr[j] < arr[i]) {
          sum += arr[j];
        }
      }
    }
    return sum;
  }

  //2.2 逆序对问题
  public static int descendingPairs(int[] arr) {
    if (arr == null || arr.length < 2) {
      return 0;
    }
    return descendingPairsProcess(arr, 0, arr.length - 1);
  }

  private static int descendingPairsProcess(int[] arr, int l, int r) {
    if (l == r) {
      return 0;
    }
    int mid = l + ((r - l) >> 1);
    return descendingPairsProcess(arr, l, mid) + descendingPairsProcess(arr, mid + 1, r)
        + descendingMerge(arr, l, mid, r);
  }

  private static int descendingMerge(int[] arr, int l, int mid, int r) {
    int sum = 0;
    int[] help = new int[r - l + 1];
    int i = r - l;
    int p1 = mid;
    int p2 = r;

    while (p1 >= l && p2 > mid) {
      sum += arr[p1] > arr[p2] ? p2 - mid : 0;
      help[i--] = arr[p1] > arr[p2] ? arr[p1--] : arr[p2--];
    }
    while (p1 >=l) {
      help[i--] = arr[p1--];
    }
    while (p2 > mid) {
      help[i--] = arr[p2--];
    }
    System.arraycopy(help, 0, arr, l, help.length);
    return sum;
  }

  public static int reversePairs(int[] arr) {
    if (arr == null || arr.length < 2) {
      return 0;
    }
    return reversePairsProcess(arr, 0, arr.length - 1);
  }

  private static int reversePairsProcess(int[] arr, int l, int r) {
    if (l == r) {
      return 0;
    }
    int mid = l + ((r - l) >> 1);
    return reversePairsProcess(arr, l, mid) + reversePairsProcess(arr, mid + 1,
        r) + reverseMerge(arr, l, mid, r);
  }

  private static int reverseMerge(int[] arr, int l, int mid, int r) {
    int result = 0;
    int windowR = mid + 1;
    for (int i = l; i <= mid; i++) {
      while (windowR <= r && (long) arr[i] > (long) arr[windowR] * 2) {
        windowR++;
      }
      result += windowR - mid - 1;
    }

    int[] help = new int[r - l + 1];
    int p1 = l;
    int p2 = mid + 1;
    int index = 0;

    while (p1 <= mid && p2 <= r) {
      help[index++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
    }

    while (p1 <= mid) {
      help[index++] = arr[p1++];
    }
    while (p2 <= r) {
      help[index++] = arr[p2++];
    }
    System.arraycopy(help, 0, arr, l, help.length);
    return result;
  }

  public static void main(String[] args) {
    int testAmount = 10000;
    int maxValue = 100;
    int maxLen = 100;

    System.out.println("Test begin!");
    for (int i = 0; i < testAmount; i++) {
      int[] arr = randomArrayGenerator(maxLen, maxValue);
      int test = smallSumTest(arr);
      int result = smallSum(arr);

      if (test != result) {
        System.out.println("error!");
        break;
      }
    }
    System.out.println("Test done!");
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
