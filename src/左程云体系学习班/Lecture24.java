package 左程云体系学习班;

import java.util.Arrays;
import java.util.LinkedList;

public class Lecture24 {

 /*
  * 滑动窗口：滑动窗口有左边界L和右边界R，在数组或者字符串或者一个序列上，记为S，则窗口就意味着S[L..R]这一部分。
  * L和R都只能往右滑，L往右滑意味着一个样本出了窗口，R往右滑意味着一个样本进了窗口。
  *
  * 1. 滑动窗口最大值(leetcode 239)：假设一个固定大小为W的窗口，依次滑过arr。返回每次滑过窗口内的最大值。
  *
  * 2. 不等式达标子数组数量问题：给定一个整形数组arr，和一个整数num。
  * 某个arr中的子数组sub如果想达标，必须满足：sub中最大值 - sub中最小值 <= num
  * 返回arr中达标的子数组的数量。
  * (使用滑动窗口可以达到O(N)的时间复杂度，前提是有两个结论：
  * a. 子数组[L..R]如果达标，那么[L..R]的子数组肯定也达标。因为子数组中数少了，最大值只可能减小，最小值只可能增大，
  * 那么必定是<=num
  * b. 子数组[L..R]如果不达标，那么[L..R]往外扩展(R++)的数组也不可能达标。与a同理。)
  *
  * 3. 加油站问题(leetcode 134)：在一条环路上有n个加油站，其中第i个加油站有汽油gas[i]升。
  * 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第i+1个加油站需要消耗汽油cost[i]升。你从其中的一个加油站出发，
  * 开始时油箱为空。给定两个整数数组 gas 和 cost，如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
  * 如果存在解则保证它是唯一的。
  *
  * */

  // 1. 滑动窗口最大值
  public static int[] getWindowMax(int[] arr, int W) {
    if (arr == null || W <= 0 || arr.length < W) {
      return null;
    }
    // 构造双端队列结构，这个结构很重要。该队列储存的是arr中的下标，其中下标对应的值应严格从大到小排序。
    // 每次R向右更新：获得一个新样本，在双端队列中从后往前比较，踢出比新样本小或相等的数。
    // 每次L向右更新：一个旧样本被滑出了窗口，比较旧样本的下标和双端队列值最前的下标值，如果下标已被踢出，则也在双端
    // 队列中踢出。
    LinkedList<Integer> queue = new LinkedList<>();

    // 最后输出的结果arr长度是固定的
    int[] result = new int[arr.length - W + 1];
    // result的当前index
    int index = 0;

    //滑动窗口模型，因为这里窗口长度固定，不需要额外的参数L。或者说，L=R-W+1。
    for (int R = 0; R < arr.length; R++) {
      // 双端队列R向右更新操作：
      while (!queue.isEmpty() && arr[queue.peekLast()] <= arr[R]) {
        queue.pollLast();
      }
      queue.addLast(R);

      // 双端队列L向右更新操作:
      if (queue.peekFirst() == R - W) {
        queue.pollFirst();
      }

      // 在窗口正式形成后，存入输出值
      if (R >= W - 1) {
        result[index++] = arr[queue.peekFirst()];
      }
    }
    return result;
  }

  // 2. 不等式达标子数组数量问题
  // 利用结论，让R一直往右扩，直到不达标。那么可以算出[L..R]中，以L为开头的所有子数组的数量。然后L++，再尝试R一直往右
  // 扩，再算出以L为开头所有子数组的数量，直到R遇到边界。
  public static int conditionMetNum(int[] arr, int sum) {
    if (arr == null || sum < 0) {
      return 0;
    }
    int result = 0;
    // 此题中维持两个双端队列，一个储存窗口内最大值，一个储存窗口内最小值。
    LinkedList<Integer> windowMax = new LinkedList<>();
    LinkedList<Integer> windowMin = new LinkedList<>();
    int R = 0;
    for (int L = 0; L < arr.length; L++) {
      // 让R一直往右扩，直到不达标
      while (R < arr.length) {
        // 维持最大值队列
        while (!windowMax.isEmpty() && arr[windowMax.peekLast()] <= arr[R]) {
          windowMax.pollLast();
        }
        windowMax.addLast(R);

        // 维持最小值队列
        while (!windowMin.isEmpty() && arr[windowMin.peekLast()] >= arr[R]) {
          windowMin.pollLast();
        }
        windowMin.addLast(R);

        if (arr[windowMax.peekFirst()] - arr[windowMin.peekFirst()] > sum) {
          break;
        }
        R++;
      }
      //算出所有达标的子数组数量，注意此时R--才是当前以L开头正好达标的最大子数组
      result += R - L;

      // 两个双端队列L向右更新操作:
      if (windowMax.peekFirst() == L) {
        windowMax.pollFirst();
      }

      if (windowMin.peekFirst() == L) {
        windowMin.pollFirst();
      }
    }
    return result;
  }

  public static int conditionMetNumBruteForce(int[] arr, int sum) {
    if (arr == null || sum < 0) {
      return 0;
    }
    int result = 0;
    for (int L = 0; L < arr.length; L++) {
      for (int R = L; R < arr.length; R++) {
        // 遍历出每一个子数组
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        // 遍历出最大值和最小值
        for (int i = L; i <= R; i++) {
          if (arr[i] > max) {
            max = arr[i];
          }
          if (arr[i] < min) {
            min = arr[i];
          }
        }
        if (max - min <= sum) {
          result++;
        }
      }
    }
    return result;
  }

  // 3. 加油站问题：
  // 思路：如果gas[i] + cost[i]的和为整数，那么说明从i加油站出发去i+1的加油站成功。
  // 如果选定一个出发点为a，[a..x]中 所有gas[i] + cost[i]都为整数，那么以a为起始点能走完一圈。
  // 构造一个数组，restGas[i] = gas[i] + cost[i]，再构造一个rest的累加和sum。累加两圈rest到一个数组中。
  // 累加和技巧中，从i出发，只需减去i-1位置的累加和即可。
  // 拓展：leetcode上的题目是保证只有唯一解，左神说的题目是返回一个boolean的array，可能存在多个解。但也仅需修改最后
  // 的一块代码，和返回值。
  public static int canCompleteCircuit(int[] gas, int[] cost) {
    if (gas == null || cost == null) {
      return -1;
    }
    int N = gas.length;
    int[] restGas = new int[N];
    for (int i = 0; i < N; i++) {
      restGas[i] = gas[i] - cost[i];
    }

    // 构造累加和(加两圈，因为可能从数组最后的加油站为出发点)：
    int[] sum = new int[N * 2];
    int s = 0;
    for (int i = 0; i < N; i++) {
      s += restGas[i];
      sum[i] = s;
    }
    for (int i = 0; i < N; i++) {
      s += restGas[i];
      sum[N + i] = s;
    }

    // 滑动窗口+双端队列维持最小值，如果最小值为非负数，那么该窗口内所有的值都为非负数，即可以行驶一圈。窗口长度为N。
    // L = R - N + 1
    LinkedList<Integer> windowMin = new LinkedList<>();
    for (int R = 0; R < sum.length; R++) {
      int sub = R - N >= 0 ? sum[R - N] : 0;
      // 双端队列R向右更新操作：
      while (!windowMin.isEmpty() && sum[windowMin.peekLast()] >= sum[R]) {
        windowMin.pollLast();
      }
      windowMin.addLast(R);

      // 双端队列L向右更新操作:
      if (windowMin.peekFirst() == R - N) {
        windowMin.pollFirst();
      }

      // 在窗口正式形成后，检查最小值
      if (R >= N - 1) {
        if (sum[windowMin.peekFirst()] - sub >= 0) {
          System.out.println(R);
          return R - N + 1;
        }
      }
    }
    return -1;
  }

  public static void main(String[] args) {
    // 第二题对数器
    // int testCycles = 1000;
    // int maxLength = 100;
    // int maxValue = 100;
    // System.out.println("test begin");
    // for (int i = 0; i < testCycles; i++) {
    //   int sum = (int) (Math.random() * 100);
    //   int[] arr = randomArrayGenerator(maxLength, maxValue);
    //   int r1 = conditionMetNum(arr, sum);
    //   int r2 = conditionMetNumBruteForce(arr, sum);
    //   if (r1 != r2) {
    //     System.out.println("error!!");
    //     break;
    //   }
    // }
    // System.out.println("test end");
    int[] arr = {1,3,1,2,0,5};

    System.out.println(Arrays.toString(getWindowMax(arr, 3)));
  }
}
