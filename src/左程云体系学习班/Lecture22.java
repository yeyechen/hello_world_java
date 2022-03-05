package 左程云体系学习班;

import static 左程云体系学习班.Lecture21.randomArray;

public class Lecture22 {
  /*
   * 1. 打怪兽问题(和鲍勃死亡问题类似，样本对应模型): 给定3个参数，N，M，K
   * 怪兽有N滴血，等着英雄来砍自己
   * 英雄每一次打击，都会让怪兽流失[0~M]的血量
   * 到底流失多少？每一次在[0~M]上等概率的获得一个值
   * 求K次打击之后，英雄把怪兽砍死的概率
   *
   * 2. 货币最小张数问题：arr是面值数组，其中的值都是正数且没有重复。再给定一个正数aim。
   * 每个值都认为是一种面值，且认为张数是无限的。
   * 返回组成aim的最少货币总张数
   *
   * 3. 数字分裂问题：给定一个正数n，求n的裂开方法数，
   * 规定：后面的数不能比前面的数小
   * 比如4的裂开方法有：
   * 1+1+1+1、1+1+2、1+3、2+2、4
   * 5种，所以返回5
   * */

  // 1. 打怪兽问题
  public static double slay(int N, int M, int K) {
    if (N < 1 || M < 1 || K < 1) {
      return 0;
    }
    long totalPossibility = (long) Math.pow(M + 1, K);
    long alive = aliveProcess(N, M, K);
    return (double) 1 - ((double) alive / (double) (totalPossibility));
  }

  // 1. 打怪兽问题
  private static long aliveProcess(int hp, int m, int restK) {
    if (hp <= 0) {
      return 0;
    }
    if (restK == 0) {
      return 1;
    }
    long ways = 0;
    // 每砍一刀的血量减少可能性都算一遍
    for (int i = 0; i <= m; i++) {
      ways += aliveProcess(hp - i, m, restK - 1);
    }
    return ways;
  }

  // 打怪兽问题改动态规划
  public static double slayDp(int N, int M, int K) {
    if (N < 1 || M < 1 || K < 1) {
      return 0;
    }
    long totalPossibility = (long) Math.pow(M + 1, K);

    long[][] dp = new long[N + 1][K + 1];
    for (int hp = 1; hp <= N; hp++) {
      dp[hp][0] = 1;
    }
    for (int restK = 1; restK <= K; restK++) {
      for (int hp = 1; hp <= N; hp++) {
        if (hp == 1) {
          dp[hp][restK] = dp[hp][restK - 1];
        }
        // 简化枚举，画图可知
        dp[hp][restK] =
            dp[hp - 1][restK] + dp[hp][restK - 1] - (hp - M - 1 > 0 ? dp[hp - M - 1][restK - 1]
                : 0);
      }
    }
    long alive = dp[N][K];
    return (double) 1 - ((double) alive / (double) (totalPossibility));
  }

  public static double dp2(int N, int M, int K) {
    if (N < 1 || M < 1 || K < 1) {
      return 0;
    }
    long all = (long) Math.pow(M + 1, K);
    long[][] dp = new long[K + 1][N + 1];
    dp[0][0] = 1;
    for (int times = 1; times <= K; times++) {
      dp[times][0] = (long) Math.pow(M + 1, times);
      for (int hp = 1; hp <= N; hp++) {
        dp[times][hp] = dp[times][hp - 1] + dp[times - 1][hp];
        if (hp - 1 - M >= 0) {
          dp[times][hp] -= dp[times - 1][hp - 1 - M];
        } else {
          dp[times][hp] -= Math.pow(M + 1, times - 1);
        }
      }
    }
    long kill = dp[K][N];
    return (double) kill / (double) all;
  }

  //2. 货币最小张数问题
  public static int minCoins(int[] arr, int aim) {
    return coinProcess(arr, 0, aim);
  }

  private static int coinProcess(int[] arr, int index, int rest) {
    // 用 Integer.MAX_VALUE 来代表无效解
    if (index == arr.length) {
      return rest == 0 ? 0 : Integer.MAX_VALUE;
    }
    int ans = Integer.MAX_VALUE;
    for (int num = 0; rest - arr[index] * num >= 0; num++) {
      int next = coinProcess(arr, index + 1, rest - arr[index] * num);
      if (next != Integer.MAX_VALUE) {
        ans = Math.min(ans, next + num);
      }
    }
    return ans;
  }

  // 货币最小张数问题直接改动态规划的斜率优化(位置依赖关系)
  public static int minCoinsDpOptimum(int[] arr, int aim) {
    int N = arr.length;
    int[][] dp = new int[N + 1][aim + 1];
    //最后一行除了rest=0时格子是0，其余都是系统最大值
    for (int rest = 1; rest <= aim; rest++) {
      dp[N][rest] = Integer.MAX_VALUE;
    }
    for (int index = N - 1; index >= 0; index--) {
      for (int rest = 0; rest <= aim; rest++) {

        int ans = dp[index + 1][rest];
        int dependence = rest - arr[index] >= 0 ? dp[index][rest - arr[index]] : -1;
        if (dependence != -1 && dependence != Integer.MAX_VALUE) {
          ans = Math.min(ans, dependence + 1);
        }
        dp[index][rest] = ans;

      }
    }
    return dp[0][aim];
  }

  // 3. 数字分裂问题
  public static int split(int n) {
    if (n <= 0) {
      return 0;
    }
    return splitProcess(1, n);
  }

  private static int splitProcess(int pre, int rest) {
    if (rest == 0) {
      return 1;
    }
    if (pre > rest) {
      return 0;
    }
    // 分裂尝试
    int result = 0;
    for (int i = pre; i <= rest; i++) {
      result += splitProcess(i, rest - i);
    }
    return result;
  }


  // 数字分裂问题改动态规划
  public static int splitDpOptimum(int n) {
    if (n <= 0) {
      return 0;
    }
    int[][] dp = new int[n + 1][n + 1];
    for (int pre = 1; pre <= n; pre++) {
      dp[pre][0] = 1;
      dp[pre][pre] = 1;
    }
    for (int pre = n; pre >= 1; pre--) {
      for (int rest = pre + 1; rest <= n; rest++) {
        dp[pre][rest] = dp[pre + 1][rest] + dp[pre][rest - pre];
      }
    }
    return dp[1][n];
  }

  public static void main(String[] args) {
    //打怪兽问题测试
    int NMax = 10;
    int MMax = 10;
    int KMax = 10;
    int testCycles = 20000;
    double threshold = 0.00001;
    System.out.println("打怪兽问题测试开始");
    for (int i = 0; i < testCycles; i++) {
      int N = (int) (Math.random() * NMax);
      int M = (int) (Math.random() * MMax);
      int K = (int) (Math.random() * KMax);
      double ans1 = slayDp(N, M, K);
      double ans2 = dp2(N, M, K);
      if (Math.abs(ans1 - ans2) > threshold) {
        System.out.println("Oops!");
        System.out.println(ans1);
        System.out.println(ans2);
        break;
      }
    }
    System.out.println("测试结束");

    //货币最小张数测试
    int maxLen = 20;
    int maxValue = 30;
    System.out.println("货币最小张数测试开始");
    for (int i = 0; i < testCycles; i++) {
      int N = (int) (Math.random() * maxLen);
      int[] arr = randomArray(N, maxValue);
      int aim = (int) (Math.random() * maxValue);
      int ans1 = minCoins(arr, aim);
      int ans2 = minCoinsDpOptimum(arr, aim);
      if (ans1 != ans2) {
        System.out.println("Oops!");
        System.out.println(aim);
        System.out.println(ans1);
        break;
      }
    }
    System.out.println("测试结束");

    // 数字分裂问题测试
    int num = (int) (Math.random() * 30);
    System.out.println("数字分裂问题测试开始");

    for (int i = 0; i < testCycles; i++) {
      int ans1 = split(num);
      int ans2 = splitDpOptimum(num);
      if (ans1 != ans2) {
        System.out.println("Oops!");
        break;
      }
    }
    System.out.println("测试结束");
  }
}

