package 左程云体系学习班;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Lecture25 {
 /*
  * 本课知识点：单调栈
  * 单调栈是一种特别设计的栈结构，为了解决如下问题：
  * 给定一个可能含有重复值的数组arr，i位置的数一定存在如下两个信息：
  * (a): arr[i]的左侧离i最近并且小于/大于arr[i]的数在哪？
  * (b): arr[i]的右侧离i最近并且小于/大于arr[i]的数在哪？
  * 如果想得到arr中所有位置的这两个信息，怎么能让得到信息的过程尽量快(O(N))
  *
  * 1. 单调栈功能实现
  *
  * 2. 子数组最小乘积的最大值(leetcode 1856)：给定一个只包含整数的数组arr，arr中任何一个子数组sub，
  * 一定都可以算出(sub 累加和)*(sub 中的最小值)的值。
  * 那么所有子数组中，这个值最大是多少。
  *
  * 3. 直方图最大面积(leetcode 84)：给定一个非负数组arr，代表直方图，返回直方图的最大长方形面积。
  *
  * 4. 二维数组最大矩形(leetcode 85)：给定一个仅包含 0 和 1、大小为 rows x cols 的二维二进制矩阵，
  * 找出只包含 1 的最大矩形，并返回其面积。
  * */

  // 1. 基本的单调栈功能，给定arr中不存在重复出现的数。维持一个栈底小栈口大的单调栈。
  // 返回一个二维数组，里面分别是左侧和右侧离i最近并且小于arr[i]的数的位置。
  // 例：
  //     左   右
  //[0: [-1,  1]
  // 1: [-1, -1]
  // 2: [1, -1]
  // 3: [2, -1]]
  public static int[][] getNearestLessNoDuplicate(int[] arr) {
    int[][] result = new int[arr.length][2];
    Stack<Integer> stack = new Stack<>();
    for (int i = 0; i < arr.length; i++) {
      // 新的数如果遇到栈中比自己大的数，就让该数弹出，为了维持从小到大的单调栈。
      while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
        // 弹出该数，此时该数下面的数就是左侧离它最近并且小于它的数；新进栈的数(i位置)就是右侧同理
        int curr = stack.pop();
        // 如果此时该数下面没数了，则左侧没有最小的数
        result[curr][0] = stack.isEmpty() ? -1 : stack.peek();
        result[curr][1] = i;
      }
      stack.push(i);
    }
    // 循环结束如果栈中还有数，则这些数右侧没有小于它的数了
    while (!stack.isEmpty()) {
      int curr = stack.pop();
      result[curr][0] = stack.isEmpty() ? -1 : stack.peek();
      result[curr][1] = -1;
    }
    return result;
  }

  // 这是有重复值的版本，过程基本类似，除了相同的数值在栈中的储存方式为一个list，为了记录先后顺序。
  public static int[][] getNearestLess(int[] arr) {
    int[][] result = new int[arr.length][2];
    Stack<List<Integer>> stack = new Stack<>();
    for (int i = 0; i < arr.length; i++) {
      while (!stack.isEmpty() && arr[stack.peek().get(0)] > arr[i]) {
        List<Integer> curr = stack.pop();
        // 这里就要注意拿list中最后的数的index，因为这个最后的数代表后进栈，也就说明离curr数从左更近
        int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
        // 更新list中所有的数，他们都有共同的左侧最近小于它们的数
        for (int index : curr) {
          result[index][0] = leftLessIndex;
          result[index][1] = i;
        }
      }
      // push时要检查是否已经含有重复数
      if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
        stack.peek().add(i);
      } else {
        List<Integer> list = new ArrayList<>();
        list.add(i);
        stack.push(list);
      }
    }
    while (!stack.isEmpty()) {
      List<Integer> curr = stack.pop();
      int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
      for (int index : curr) {
        result[index][0] = leftLessIndex;
        // 让这里返回arr的长度方便作为helper function给其他函数应用
        result[index][1] = arr.length;
      }
    }
    return result;
  }

  // 2. 子数组最小乘积的最大值
  // 表达式：(sub 累加和)*(sub 中的最小值)。那么arr中的任何一个数都能成为表达式中的sub中的最小值，那么我们的任务
  // 就是找到让该sub中累加和最大。因为该问题的特性，arr中只有正数，那么肯定是sub中的数越多，累加和越大。那么怎么找到
  // 以一个特定位置i的数，使得该index为最小值的同时，求到一个元素最多的子数组呢？答案就是单调栈，找到最左，最右比该
  // 位置i数小的数的位置，那么中间的子数组就是我们要找的目标子数组。只要遍历所有元素，使每一个元素为最小值，找到最大的
  // 累加和子数组范围，便能获得题解。
  public static int maxSumMinProduct(int[] arr) {
    if (arr == null) {
      return -1;
    }
    // 前缀和
    long[] preSum = new long[arr.length];
    preSum[0] = arr[0];
    for (int i = 1; i < arr.length; i++) {
      preSum[i] = preSum[i-1] + arr[i];
    }

    int[][] ranges = getNearestLess(arr);
    long max = 0;
    for (int i = 0; i < arr.length; i++) {
      // 以当前i为最小值，通过前缀和可以O(1)算出累加和
      int rangeL = ranges[i][0];
      int rangeR = ranges[i][1];
      long maxSubSum;

      // 特殊边界条件
      if (rangeL == -1) {
        maxSubSum = preSum[rangeR - 1];
      } else {
        maxSubSum = preSum[rangeR - 1] - preSum[rangeL];
      }
      max = Math.max(maxSubSum * arr[i], max);
    }
    return (int) (max % 1000000007);
  }

  // 3. 直方图最大面积
  public static int largestRectangleArea(int[] arr) {
    if (arr == null) {
      return 0;
    }
    int result = 0;
    int[][] ranges = getNearestLess(arr);

    // 让每一个位置的数作为长方形的高，向左右找到比自己高的，算出当前面积
    for (int i = 0; i < arr.length; i++) {
      int width = ranges[i][1] - ranges[i][0] - 1;
      int area = arr[i] * width;
      result = Math.max(result, area);
    }
    return result;
  }

  // 4. 二维数组最大矩形：二维数组压缩技巧。尝试每一行作为地基，模拟出每一行作为地基的直方图，用第三题的方法就可求。
  // 最优解就是O(N^2)
  public int maximalRectangle(char[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
      return 0;
    }
    // 用来模拟直方图
    int[] map = new int[matrix[0].length];

    int result = 0;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        if (matrix[i][j] == '1') {
          map[j]++;
        } else {
          // 如果某一列里遇到0了，那么就不能以该列为地基
          map[j] = 0;
        }
      }
      result = Math.max(largestRectangleArea(map), result);
    }
    return result;
  }

  public static void main(String[] args) {
    int[] arr = {2,5,4,2,4,5,3,1,2,4};
    System.out.println(maxSumMinProduct(arr));

  }
}
