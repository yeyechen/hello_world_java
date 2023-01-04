package 左程云体系学习班.Lecture41;

import static Util.Utility.getSum;

import Util.Utility;
import java.util.Arrays;

public class Code02_BestSplit {

  /*
   * 把题目一中提到的，min{左部分累加和，右部分累加和}，定义为S(N-1)，也就是说：
   * S(N-1)：在arr[0…N-1]范围上，做最优划分所得到的min{左部分累加和，右部分累加和}的最大值
   * 现在要求返回一个长度为N的s数组，
   * s[i] =在arr[0…i]范围上，做最优划分所得到的min{左部分累加和，右部分累加和}的最大值
   * 得到整个s数组的过程，做到时间复杂度O(N)
   *
   * 利用分割点不回退，做到O(N)
   * */

  public static int[] bestSplit(int[] arr) {
    if (arr == null || arr.length == 0) {
      return new int[0];
    }

    // calculate pre-sum
    // sum[L..R] = preSum[R+1] - preSum[L]
    int[] preSum = Utility.calcPreSum(arr);
    // 最优划分
    // 0~range-1上，最优划分是左部分[0~split]  右部分[split+1~range-1]
    int[] result = new int[arr.length];
    int split = 0;
    for (int range = 1; range < arr.length; range++) {
      while (split + 1 < range) {
        int before = Math.min(getSum(preSum, 0, split), getSum(preSum, split + 1, range));
        int after = Math.min(getSum(preSum, 0, split + 1), getSum(preSum, split + 2, range));
        if (after >= before) { // only ">" will lead to error, try example
          split++;
        } else {
          break;
        }
      }
      result[range] = Math.min(getSum(preSum, 0, split), getSum(preSum, split + 1, range));
    }
    return result;
  }

  public static int[] bruteForce(int[] arr) {
    if (arr == null || arr.length == 0) {
      return new int[0];
    }

    int[] result = new int[arr.length];
    for (int range = 1; range < arr.length; range++) {
      for (int split = 0; split < range; split++) {
        int leftSum = 0;
        for (int i = 0; i <= split; i++) {
          leftSum += arr[i];
        }
        int rightSum = 0;
        for (int i = split + 1; i <= range; i++) {
          rightSum += arr[i];
        }
        result[range] = Math.max(result[range], Math.min(leftSum, rightSum));
      }
    }
    return result;
  }

  public static void main(String[] args) {
    int maxLen = 20;
    int maxVal = 20;
    int testCycles = 10000;
    System.out.println("test start");
    for (int i = 0; i < testCycles; i++) {
      int[] arr = Utility.randomArrayGenerator(maxLen, maxVal, true);
      int[] ans1 = bestSplit(arr);
      int[] ans2 = bruteForce(arr);
      if (!Arrays.equals(ans1, ans2)) {
        System.out.println("test fail");
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(ans1));
        System.out.println(Arrays.toString(ans2));
        break;
      }
    }
    System.out.println("test end");
  }
}
