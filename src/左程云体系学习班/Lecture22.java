package 左程云体系学习班;

public class Lecture22 {
  /*
   * 1. 打怪兽问题(和鲍勃死亡问题类似): 给定3个参数，N，M，K
   * 怪兽有N滴血，等着英雄来砍自己
   * 英雄每一次打击，都会让怪兽流失[0~M]的血量
   * 到底流失多少？每一次在[0~M]上等概率的获得一个值
   * 求K次打击之后，英雄把怪兽砍死的概率
   * */

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

  public static void main(String[] args) {
    int NMax = 10;
    int MMax = 10;
    int KMax = 10;
    int testTime = 20000;
    double threshold = 0.00001;
    System.out.println("测试开始");
    for (int i = 0; i < testTime; i++) {
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
  }
}
