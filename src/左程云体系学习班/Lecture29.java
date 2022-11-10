package 左程云体系学习班;

import java.util.Arrays;

public class Lecture29 {

 /*
  * bfprt算法!!
  * 该算法也是比较经典的算法，被写进算法导论中。
  *
  * 问题：给定一个整数arr，求出该arr中第k小的数。k >= 1
  * 比较直观的方法是给这个arr排序，取出index为k-1的数。时间复杂度为O(NlogN)。而bfprt算法能达到O(N)。
  *
  * 尝试方法一：利用快速排序中的荷兰国旗问题
  * 在arr中随机选择一个数v，给整个数组排荷兰国旗(即小于v的数放数组最左边，大于v的数放数组最右边，等于v的数放中间)。
  * [   ...v...   ] ---> [  <v  |  =v  |  >v  ]
  * 1.那么如果给定的k-1落在等于v的数的下标范围中，那么v就是第k小的数，直接返回。（比如要求第5小的数，而正好等于v的
  * 下标范围是3到6，即比v小的数只有3个，从第4小到第7小就是v，那可不就返回v嘛）。
  * 2.如果k-1小于=v的最小下标范围，那就只考虑<v的部分。如果k-1大于=v的最小下标范围，那就只考虑>v的部分，继续递归。
  *
  * 这个方法很棒，可以做到O(N)，但是最开始的随机选择数字给这个算法添加了不确定性。O(N)是通过数学中的Expectation算
  * 出来的，而最差劲最差劲的情况会比O(N)要高。
  *
  * bfprt算法的过程与方法一的过程类似，除了最开始的数字选择是精挑细选，从而提高算法下限。
  * 精挑细选过程:
  * 1. 给arr分成每5个数一组[.....|.....|.....|.....|.....]
  * 2. 每一组中都排序取中位数，时间复杂度是O(N)，因为5个数排序被认为是O(1)的复杂度。
  * [..a..|..b..|..c..|..d..|..e..|..f..] ---> [a,b,c,d,e,f...], length = N/5
  * 3. 在从处理好的中位数数组中，取中位数
  * [a,b,c,d,e,f...] -> m
  *
  * 该数即为精挑细选的划分数。该数可以保证每次划分有(7/10)N的下限。因为数组中至少有(3/10)N是大于m的。
  *
  * */

  // 1.改写快排的方法一
  public static int minKth(int[] array, int k) {
    int[] arr = array.clone();
    return process(arr, 0, arr.length - 1, k - 1);
  }

  // arr 第k小的数
  // arr[L..R]  范围上，如果排序的话(不是真的去排序)，找位于index的数
  // index [L..R]
  private static int process(int[] arr, int L, int R, int index) {
    // L == R == index
    if (L == R) {
      return arr[L];
    }

    // [   ...v...   ] ---> [  <v  |  =v  |  >v  ]
    int pivot = arr[L + (int) (Math.random() * (R - L + 1))]; // 随机选一个数
    int[] range = partition(arr, L, R, pivot); // <v， >v 与 =v 的分界位置

    // k-1落在等于v的数的下标范围中，该数就是第k小的数
    if (range[0] <= index && index <= range[1]) {
      return arr[index];
    } else if (index < range[0]) { // 落入 <v 区域
      return process(arr, L, range[0] - 1, index);
    } else { // 落入 >v 区域
      return process(arr, range[1] + 1, R, index);
    }
  }

  private static int[] partition(int[] arr, int L, int R, int pivot) {
    int less = L - 1;
    int more = R + 1;
    int cur = L;
    while (cur < more) {
      if (arr[cur] < pivot) {
        swap(arr, ++less, cur++);
      } else if (arr[cur] > pivot) {
        swap(arr, cur, --more);
      } else {
        cur++;
      }
    }
    return new int[] { less + 1, more - 1 };
  }

  private static void swap(int[] arr, int i1, int i2) {
    int tmp = arr[i1];
    arr[i1] = arr[i2];
    arr[i2] = tmp;
  }

  // 利用bfprt算法，时间复杂度O(N)
  public static int minKth2(int[] array, int k) {
    int[] arr = array.clone();
    return bfprt(arr, 0, arr.length - 1, k - 1);
  }

  // arr[L..R]  如果排序的话，位于index位置的数是什么
  private static int bfprt(int[] arr, int L, int R, int index) {
    if (L == R) {
      return arr[L];
    }

    // [   ...v...   ] ---> [  <v  |  =v  |  >v  ]
    int pivot = medianOfMedian(arr, L, R); // bfprt与方法一唯一不同的地方，精挑细选一个数作为划分
    int[] range = partition(arr, L, R, pivot); // <v， >v 与 =v 的分界位置

    // k-1落在等于v的数的下标范围中，该数就是第k小的数
    if (range[0] <= index && index <= range[1]) {
      return arr[index];
    } else if (index < range[0]) { // 落入 <v 区域
      return bfprt(arr, L, range[0] - 1, index);
    } else { // 落入 >v 区域
      return bfprt(arr, range[1] + 1, R, index);
    }
  }

  // arr[L...R]  五个数一组
  // 每个小组内部排序
  // 每个小组中位数领出来，组成marr
  // marr中的中位数，返回
  private static int medianOfMedian(int[] arr, int L, int R) {
    int size = R - L + 1;
    int offset = size % 5 == 0 ? 0 : 1;
    int[] mArr = new int[size / 5 + offset]; // median array (array of medians)
    for (int team = 0; team < mArr.length; team++) {
      int teamFirst = L + team * 5; // 每一组的开头位置
      mArr[team] = getMedian(arr, teamFirst, Math.min(R, teamFirst + 4));
    }

    // 利用上面bfprt来找到mArr的中位数，即第 k=length/2 小的数。bfprt方法时间复杂度为O(N)，从而整体达到复杂度O(N)
    return bfprt(mArr, 0, mArr.length - 1, mArr.length / 2);
  }

  private static int getMedian(int[] arr, int L, int R) {
    Arrays.sort(arr, L, R);
    return arr[(L + R) / 2];
  }

  public static int[] generateRandomArray(int maxSize, int maxValue) {
    int[] arr = new int[(int) (Math.random() * maxSize) + 1];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = (int) (Math.random() * (maxValue + 1));
    }
    return arr;
  }

  public static void main(String[] args) {
    int testTime = 1000000;
    int maxSize = 100;
    int maxValue = 100;
    System.out.println("test begin");
    for (int i = 0; i < testTime; i++) {
      int[] arr = generateRandomArray(maxSize, maxValue);
      int k = (int) (Math.random() * arr.length) + 1;
      int ans1 = minKth(arr, k);
      int ans2 = minKth2(arr, k);
      if (ans1 != ans2) {
        System.out.println("Oops!");
      }
    }
    System.out.println("test finish");
  }
}
