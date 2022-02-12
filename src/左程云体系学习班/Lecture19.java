package 左程云体系学习班;

public class Lecture19 {
  /*
   * 动态规划
   *
   * 1. 背包问题(knapsack problem)：给定两个长度都为N的数组weights和values，
   * weights[i]和values[i]分别代表 i号物品的重量和价值
   * 给定一个正数bag，表示一个载重bag的袋子，装的物品不能超过这个重量，返回能装下的最大价值。
   *
   * 2. 字母转换问题：规定1和A对应、2和B对应、3和C对应...26和Z对应
   * 那么一个数字字符串比如"111”就可以转化为:"AAA"、"KA"和"AK"
   * 给定一个只有数字字符组成的字符串str，返回有多少种转化结果
   * */

  // 1. 背包问题(knapsack problem)
  public static int maxVlaue(int[] w, int[] v, int bag) {
    if (w == null || v == null || w.length != v.length || w.length == 0) {
      return 0;
    }
    return knapsackProcess(w, v, 0, bag);
  }

  private static int knapsackProcess(int[] w, int[] v, int index, int restBagVol) {
    if (restBagVol < 0) {
      return -1;
    }
    if (index == w.length) {
      return 0;
    }
    // 情况1：没要当前index的物品
    int p1 = knapsackProcess(w, v, index + 1, restBagVol);
    // 情况2：要了当前index的物品，但是需要判断要了这个物品是否超重，如果是，则不能要
    int next = knapsackProcess(w, v, index + 1, restBagVol - w[index]);
    int p2 = next != -1 ? v[index] + next : 0;
    return Math.max(p1, p2);
  }

  // 动态规划
  public static int maxValueDp(int[] w, int[] v, int bag) {
    if (w == null || v == null || w.length != v.length || w.length == 0) {
      return 0;
    }
    int[][] dp = new int[w.length + 1][bag + 1];
    // 动态规划表最下面一行一定是0因为(index, restBagVol), index == w.length -> 0
    for (int index = w.length - 1; index >= 0; index--) {
      // 从倒数第二行开始，每一个数据只依赖自己下面行的数据
      for (int restBagVol = 0; restBagVol <= bag; restBagVol++) {
        int p1 = dp[index + 1][restBagVol];
        int p2 = restBagVol - w[index] >= 0 ? v[index] + dp[index + 1][restBagVol - w[index]] : 0;
        dp[index][restBagVol] = Math.max(p1, p2);
      }
    }
    return dp[0][bag];
  }

  // 2.字母转换问题
  public static int translate(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    return translateProcess(s, 0);
  }

  // 在index往后，有多少种转换方法
  private static int translateProcess(String s, int index) {
    // index到最后了，说明找到了一种方法
    if (index == s.length()) {
      return 1;
    }
    // index还没有到最后
    // 情况1：只考虑单独index位置的字符转换，但不能是'0'，因为'0'不对应任何字符
    if (s.charAt(index) == '0') {
      return 0;
    }
    int ways = translateProcess(s, index + 1);
    // 情况2：考虑index位置和index+1位置组成的两位数，不能超过26，而且index+1不能越界
    if (index + 1 < s.length() && (s.charAt(index) - '0') * 10 + s.charAt(index + 1) - '0' < 27) {
      ways += translateProcess(s, index + 2);
    }
    return ways;
  }

  // 转动态规划，因为可变参数只有一个，所以是个一维数组
  public static int translateDp(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    int[] dp = new int[s.length() + 1];
    dp[s.length()] = 1;
    for (int index = s.length() - 1; index >= 0; index--) {
      // 因为初始化已经是0了，不需要在s.charAt(index) == '0'时单独设置0
      if (s.charAt(index) != '0') {
        int ways = dp[index + 1];
        if (index + 1 < s.length()
            && (s.charAt(index) - '0') * 10 + s.charAt(index + 1) - '0' < 27) {
          ways += dp[index + 2];
        }
        dp[index] = ways;
      }
    }
    return dp[0];
  }

  public static void main(String[] args) {
    int[] weights = {3, 2, 4, 7, 3, 1, 7};
    int[] values = {5, 6, 3, 19, 12, 4, 2};
    int bag = 15;
    System.out.println(maxVlaue(weights, values, bag));
    System.out.println(maxValueDp(weights, values, bag));

    String s = "111";
    System.out.println(translate(s));
    System.out.println(translateDp(s));

  }
}
