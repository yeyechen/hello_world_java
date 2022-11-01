package 左程云体系学习班;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Lecture26 {

 /* 套路模型总结：1.二叉树递归模型 2.从左往右尝试模型 3.范围尝试模型 4.样本对应模型 5.业务限制模型 6.斐波那契公式O(logN)模型
  * 1. 子数组的最小值之和(leetcode 907): 给定一个整数数组 arr，找到 min(b)的总和，
  * 其中 b 的范围为 arr 的每个（连续）子数组。
  * 由于答案可能很大，因此 返回答案模 10^9 + 7。(上节课单调栈回顾)
  *
  * 2. 现在引进一个新的模型：斐波那契公式O(logN)模型。意思是，所有 F(n) = a*F(n-1) + b*F(n-2) + ... 的提供公式，并
  * 递归的公式，都可以用套路O(logN)来解。
  *
  * 原理：拿斐波那契数列举例：F(n) = F(n-1) + F(n-2)，最多递推到n-2，那么需要一个二阶矩阵 A = |ab|
  *                                                                                 |cd|
  * 即可满足: |F(n) F(n-1)| = |F(n-1) F(n-2)| * A 的矩阵等式。
  * 通过带入初始值F(1), F(2)... 来获得矩阵A。
  * A = |1 1|
  *     |1 0|
  *
  * 因为 |F(n) F(n-1)| = |F(n-1) F(n-2)| * A 可以无限通过前一项带入，推导得出：
  *  -> |F(n) F(n-1)| = |F(2) F(1)| * A^n-2
  * 那么只要这个矩阵的n-2次方算得足够快，F(n)就能算得足够快。
  *
  * 此时我们需要一个方法让一个矩阵的次方算得足够快，我们首先思考怎么让一个数的次方算得足够快：
  * 举个例子: 9^75
  * 先让9跟自己玩儿，算出9^1, 9^2, 9^4, 9^8, 9^16, 9^32, 9^64。(次方数小于75即可, 时间复杂度O(logN), N=75)
  * 那么结果 9^75 就等于：1 * 9^1 * 9^2 * 9^8 * 9^64 (75 = 101011二进制。乘的复杂度也是O(logN), N=75)
  * 矩阵同理。
  *
  * 那么此时我们就有一个O(logN)的算法来计算斐波那契公式模型
  *
  *
  * 3. 第一年农场有一只成熟的母牛A，往后的每年：
  *   (a). 每一只成熟的母牛都会生一只母牛
  *   (b). 每一只新出生的母牛都在出生的第三年成熟
  *   (c). 每一只母牛永远不死
  * 即：F(n) = F(n-1) + F(n-3)，初始值为：1，2，3，4，6，9...
  *
  * 4. 给定一个数N，只有0和1两种字符，组成的所有长度为N的字符串中，如果任何0字符的左边都有1紧挨着，认为这个字符串达标
  * 返回有多少达标的字符串。例如：0101不达标，1010和11达标。
  * 题解：此问题可以先尝试，有N个数位，那么最左第一位一定要是1，不然直接不达标。
  * [][][]...[] N 个数位
  * [1][][]...[]，第n数位确定为1，要求的达标数量为F(n)。那就是求剩下N-1个数位有多少个达标的。
  * 有两种情况:
  * [1][1][]...[]，第n-1数位为1，那么剩下数位达标数量为F(n-1)
  * [1][0][1]...[]，第n-1数位为0，第n-2数位必须为1，否则不达标。那么剩下数位达标数量为F(n-2)
  *
  * 公式就得到了F(n) = F(n-1) + F(n-2)，初始值为1,2,3...
  * 代码就不写了，就是斐波那契数列换了初始值
  * */

  // 1.子数组的最小值之和: 这题还是用到单调栈，求左右大于位置为i的数的位置在哪里。那么其中就会有多个以i位置为最小值
  // 的子数字，通过整体计算即可提高算法效率。
  //     [n0, n1, n2, ... , nn]
  // i =  0 , 1 , 2 , ... , n
  // 比如位置为7的左右比它小的数的位置分别是4和9，那么可以以位置7为最小值的子数组为:
  // [5-7],[5-8],[6-7],[6-8],[7-7],[7-8], 9个 -> (7-4) * (9-7)
  // 那么如果位置为i的左右比它小的数的位置分别是j和k，那么可以以位置i为最小值的子数组的数量为: (i-j) * (k-i)
  // 那么子数组最小值之和便为: (i-j) * (k-i) * arr[i]
  // 如果左右没有小于改数的话，分别返回-1 和 arr.length, 也适用公式。
  // 关键点：处理重复值时，不要重复计算子数组数量。
  public static int sumSubarrayMins(int[] arr) {
    if (arr == null || arr.length == 0) {
      return 0;
    }
    int[][] ranges = helper(arr);
    long sum = 0;
    for (int i = 0; i < arr.length; i++) {
      int left = ranges[i][0];
      int right = ranges[i][1];
      sum += (i - left) * (right - i) * (long) arr[i];
      sum %= (100000007);
    }
    return (int) sum ;
  }

  public static int[][] helper(int[] arr) {
    int[][] result = new int[arr.length][2];
    Stack<List<Integer>> stack = new Stack<>();
    for (int i = 0; i < arr.length; i++) {
      // 此时注意 arr[stack.peek().get(0)] >= arr[i] 而不是单纯 >，因为要保证上有不调取重复子数组，因此==的
      // 情况也弹出
      while (!stack.isEmpty() && arr[stack.peek().get(0)] >= arr[i]) {
        List<Integer> curr = stack.pop();
        int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
        for (int index : curr) {
          result[index][0] = leftLessIndex;
          result[index][1] = i;
        }
      }
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
        result[index][1] = arr.length;
      }
    }
    return result;
  }

  // 2. 斐波那契公式O(logN)模型
  public static int f(int n) {
    if (n < 1) {
      return 0;
    }
    if (n == 1 || n == 2) {
      return 1;
    }
    // A = [1 1]
    //     [1 0]
    int[][] base = {
        { 1, 1 },
        { 1, 0 }
    };
    int[][] res = matrixPower(base, n - 2);
    // |F(n) F(n-1)| = |F(2) F(1)| * A^n-2
    // F(2) = F(1) = 1
    return res[0][0] + res[1][0];
  }

  private static int[][] matrixPower(int[][] m, int p) {
    int[][] res = new int[m.length][m[0].length];
    // res初始值为单位矩阵
    for (int i = 0; i < res.length; i++) {
      res[i][i] = 1;
    }
    int[][] t = m;
    // t自己和自己乘，p右移是利用二进制。例75 = 101011，不断右移，遇到1就乘，0不变，但t一直和自己相乘
    for (; p != 0; p >>= 1) {
      if ((p & 1) != 0) {
        res = matrixProduct(res, t);
      }
      t = matrixProduct(t, t);
    }
    return res;
  }

  private static int[][] matrixProduct(int[][] a, int[][] b) {
    int n = a.length;
    int m = b[0].length;
    int k = a[0].length; // a的列数同时也是b的行数
    int[][] ans = new int[n][m];
    for(int i = 0 ; i < n; i++) {
      for(int j = 0 ; j < m;j++) {
        for(int c = 0; c < k; c++) {
          ans[i][j] += a[i][c] * b[c][j];
        }
      }
    }
    return ans;
  }

  // 3. 母牛生小牛问题: 利用上述模型，此时公式为: F(n) = F(n-1) + F(n-3)
  // 初始值为：1，2，3，4，6，9...
  public static int cow(int n) {
    if (n < 1) {
      return 0;
    }
    if (n == 1 || n == 2 || n == 3) {
      return n;
    }
    // 因为涉及n-3，我们需要一个三阶矩阵。通过解方程可得，虽然有9个未知数。
    int[][] base = {
        { 1, 1, 0 },
        { 0, 0, 1 },
        { 1, 0, 0 } };
    int[][] res = matrixPower(base, n - 3);
    // |F(n) F(n-1) F(n-3)| = |F(3) F(2) F(1)| * A^n-2
    // F(3) = 3, F(2) = 2, F(1) = 1
    return 3 * res[0][0] + 2 * res[1][0] + res[2][0];
  }
}
