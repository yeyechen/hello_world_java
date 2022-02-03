package Bilibili;

import java.util.HashMap;
import java.util.Random;

public class Code04 {

  public static int maxIncome(int[][] income) {
    //income的长度是奇数的话无法平均分配
    if (income == null || income.length < 2 || (income.length & 1) != 0) {
      return 0;
    }
    //要去A的人
    int N = income.length >> 1;
    return process(income, 0, N);
  }

  /*
  * 对在index之后的司机进行分配，A区域还有rest个名额。
  * 返回把在index往后的司机平均分配到A和B区域内，这些司机的最大收入。
  * */
  private static int process(int[][] income, int index, int rest) {
    //没有司机了
    if (index == income.length) {
      return 0;
    }
    //还有司机:

    //A区域没有名额了
    if (rest == 0) {
      return income[index][1] + process(income, index + 1, rest);
    }
    //B区域没有名额了
    if (income.length - index == rest) {
      return income[index][0] + process(income, index + 1, rest - 1);
    }
  //  都还有名额:
    int option1 = income[index][0] + process(income, index + 1, rest - 1);
    int option2 = income[index][1] + process(income, index + 1, rest);
    return Math.max(option1, option2);
  }

  //  动态规划(加缓存):
  public static int maxIncome2(int[][] income) {
    if (income == null || income.length < 2 || (income.length & 1) != 0) {
      return 0;
    }
    int N = income.length >> 1;
    return process2(income, 0, N, new HashMap<>());
  }

  private static int process2(int[][] income, int index, int rest,
      HashMap<Integer, HashMap<Integer, Integer>> dp) {
    //check cache:
    if (dp.containsKey(index) && dp.get(index).containsKey(rest)) {
      return dp.get(index).get(rest);
    }

    int result = 0;
    if (index == income.length) {
      result = 0;
    } else if (rest == 0) {
      result = income[index][1] + process(income, index + 1, rest);
    } else if (income.length - index == rest) {
      result = income[index][0] + process(income, index + 1, rest - 1);
    } else {
      int option1 = income[index][0] + process(income, index + 1, rest - 1);
      int option2 = income[index][1] + process(income, index + 1, rest);
      result = Math.max(option1, option2);
    }
    //build cache:
    if (!dp.containsKey(index)) {
      dp.put(index, new HashMap<>());
    }
    dp.get(index).put(rest, result);
    return result;
  }

  public static void main(String[] args) {
    int N = 5;
    int value = 100;
    int testAmount = 500;
    System.out.println("测试开始");
    for (int i = 0; i < testAmount; i++) {
      int len = (int) (Math.random() * N) + 1;
      int[][] matrix = randomMatrix(len, value);
      int ans1 = maxIncome(matrix);
      int ans2 = maxIncome2(matrix);
      if (ans1 != ans2) {
        System.out.println(ans1);
        System.out.println(ans2);
        System.out.println("测试失败");
      }
    }
    System.out.println("测试结束");
  }

  private static int[][] randomMatrix(int len, int value) {
    Random random = new Random();
    int[][] res = new int[len][2];
    for (int i = 0; i < len; i++) {
      for (int j = 0; j < 2; j++) {
        res[i][j] = random.nextInt(value);
      }
    }
    return res;
  }
}
