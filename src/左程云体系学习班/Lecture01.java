package 左程云体系学习班;

import java.util.Arrays;

public class Lecture01 {
  /*
   * 1. 时间复杂度
   *
   * 常数时间操作 (O(1))：数组寻址操作；算数运算；赋值，比较，自增，自减等。
   * 单链表取元素 list.get(i) 是O(N)操作
   *
   * big Omega: 平均情况时间复杂度
   * big Theta: 最好情况时间复杂度
   * big O:     最差情况时间复杂度
   *
   * 2. 额外空间复杂度: 与parameter和最后return的东西无关，只与为了完成算法申请的额外空间有关。
   *
   * 3. 算法流程常数项: 当两个算法拥有相同的时间和空间复杂度时，为了区别哪个算法更优秀，此时需要比较常数项，即
   *                 aN^2 + bN + c中的a, b, c.
   * 方法: 放弃理论分析，生成随机数据测试。
   *
   * 4. 二分法: 时间复杂度O(logN)，使用前提是有序数组(除非数据状况特殊，问题情况特殊，比如局部最小问题)。
   * */

  // time complexity: O(n^2)
  public static void selectionSort(int[] arr) {
    if (arr == null || arr.length < 2) {
      return;
    }

    for (int i = 0; i < arr.length - 1; i++) {
      int minIndex = i;
      //找到 i ~ n-1 之间的最小值
      for (int j = i; j < arr.length; j++) {
        minIndex = arr[j] < arr[minIndex] ? j : minIndex;
      }
      //放入i位置
      swap(arr, i, minIndex);
    }
  }

  // O(n^2) in the worst case
  // O(n) in the best case
  public static void insertionSort(int[] arr) {
    if (arr == null || arr.length < 2) {
      return;
    }

    //把从index 0～0 排序好(已经完成)
    //把从index 0～1 排序好(默认0～0排序完成)
    //把从index 0～2 排序好(默认0～1排序完成)
    //...
    //把从index 0～N-1 排序好(默认0～N-2排序完成)
    for (int i = 1; i < arr.length; i++) {
      for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
        swap(arr, j, j + 1);
      }
    }
  }

  // O(n^2) time complexity
  public static void bubbleSort(int[] arr) {
    if (arr == null || arr.length < 2) {
      return;
    }

    for (int i = arr.length - 1; i > 0; i--) {
      for (int j = 0; j < i; j++) {
        if (arr[j] > arr[j + 1]) {
          swap(arr, j, j + 1);
        }
      }
    }
  }

  //二分查找:
  public static boolean exist(int[] sortedArr, int num) {
    if (sortedArr == null || sortedArr.length == 0) {
      return false;
    }
    int L = 0;
    int R = sortedArr.length - 1;
    int mid = 0;
    // L..R
    while (L < R) { // L..R 至少两个数的时候
      mid = L + ((R - L) >> 1); // -> mid = (L + R) / 2 因为 L+R 可能因为太大溢出
      if (sortedArr[mid] == num) {
        return true;
      } else if (sortedArr[mid] > num) {
        R = mid - 1;
      } else {
        L = mid + 1;
      }
    }
    return sortedArr[L] == num;
  }

  // 局部最小问题: 在一个无序数组中，任何相邻两个数都不同，找出局部最小的index(即 arr[i-1] > arr[i] < arr[i+1])。
  // 题解: 很像computational optimisation里的问题。首先检查边界，如果边界都不是局部最小，那么这说明:
  // index 从 0 -> 1 的数字成下降趋势(decreasing);
  // index 从 N-2 -> N-1 的数字成上升趋势(increasing);
  // 已知任何相邻两个数都不同，想象一下一个函数图像，从0到N-1中间一定会有一个局部最小(local minimum/optimum)
  public static int getLocalMinIndex(int[] arr) {
    if (arr == null || arr.length == 0) {
      return -1;
    }
    //检查边界:
    if (arr.length == 1 || arr[0] < arr[1]) {
      return 0;
    }
    if (arr[arr.length - 1] < arr[arr.length - 2]) {
      return arr.length - 1;
    }
    int L = 1;
    int R = arr.length - 2;
    int mid = 0;
    while (L < R) {
      mid = L + ((R - L) >> 1);
      if (arr[mid] > arr[mid - 1]) {
        R = mid - 1;
      } else if (arr[mid] > arr[mid + 1]) {
        L = mid + 1;
      } else {
        return mid;
      }
    }
    return L;
  }

  private static void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }

  private static int[] generateRandomArray(int maxSize, int maxValue) {
    // Math.random() -> [0,1) 所有的小数，等概率返回一个
    // Math.random() * N -> [0,N) 所有小数，等概率返回一个
    // (int)(Math.random() * N) -> [0,N-1] 所有的整数，等概率返回一个
    int[] arr = new int[(int) ((maxSize + 1) * Math.random())]; // 长度随机
    for (int i = 0; i < arr.length; i++) {
      arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
    }
    return arr;
  }

  public static void main(String[] args) {
    //  对数器:
    int testAmount = 5000;
    int maxSize = 100; // 随机数组的长度为 0～100
    int maxValue = 100; // 随机数组中的值为 -100~100
    boolean succeed = true;
    for (int i = 0; i < testAmount; i++) {
      int[] arr = generateRandomArray(maxSize, maxValue);
      int[] arr1 = Arrays.copyOf(arr, arr.length);
      int[] arr2 = Arrays.copyOf(arr, arr.length);
      bubbleSort(arr1);
      insertionSort(arr2);
      if (!Arrays.equals(arr1, arr2)) {
        succeed = false;
        break;
      }
    }
    System.out.println(succeed ? "测试通过" : "测试失败");
  }
}
