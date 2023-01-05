package 左程云体系学习班.Lecture42;

public class Code02_SuperEggDropProblem {

  /*
   * leetcode887 鸡蛋掉落: https://leetcode.cn/problems/super-egg-drop/
   * 给你 k 枚相同的鸡蛋，并可以使用一栋从第 1 层到第 n 层共有 n 层楼的建筑。
   * 已知存在楼层 f ，满足 0 <= f <= n ，任何从 高于 f 的楼层落下的鸡蛋都会碎，从 f 楼层或比它低的楼层落下的鸡蛋都不会破。
   * 每次操作，你可以取一枚没有碎的鸡蛋并把它从任一楼层 x 扔下（满足 1 <= x <= n）。
   * 如果鸡蛋碎了，你就不能再次使用它。如果某枚鸡蛋扔下后没有摔碎，则可以在之后的操作中 重复使用 这枚鸡蛋。
   * 请你计算并返回要确定 f 确切的值 的 最小操作次数 是多少？
   *
   * */

  public static int superEggDrop(int k, int n) {
    return process(n, k);
  }

  // 还有nLevel层楼要去尝试，还有kEgg个鸡蛋可以使用
  // 验证出最高的不会碎的楼层，返回至少需要扔几次
  private static int process(int nLevels, int kEggs) {
    if (nLevels == 0 || kEggs == 0) {
      return 0;
    }
    // 注意至少，要考虑最坏情况
    if (kEggs == 1) {
      return nLevels;
    }

    int min = Integer.MAX_VALUE;
    // 平行宇宙，不同的我拿着相同数量的鸡蛋在各个楼层尝试，试出最小值
    for (int i = 1; i <= nLevels; i++) {
      // 1. 鸡蛋碎了, 往下试
      int p1 = process(i - 1, kEggs - 1);
      // 2. 鸡蛋没碎, 往上试
      int p2 = process(nLevels - i, kEggs);
      min = Math.min(min, Math.max(p1, p2));
    }
    return min + 1;
  }

  public static int superEggDropDp(int kEggs, int nLevels) {
    if (kEggs < 1 || nLevels < 1) {
      return 0;
    }
    int[][] dp = new int[nLevels + 1][kEggs + 1];
    for (int i = 1; i <= nLevels; i++) {
      dp[i][1] = i;
    }
    for (int k = 2; k <= kEggs; k++) {
      for (int level = 1; level <= nLevels; level++) {
        int curMin = Integer.MAX_VALUE;
        for (int i = 1; i <= nLevels; i++) {
          // 1. 鸡蛋碎了, 往下试
          int p1 = process(i - 1, kEggs - 1);
          // 2. 鸡蛋没碎, 往上试
          int p2 = process(nLevels - i, kEggs);
          curMin = Math.min(curMin, Math.max(p1, p2));
        }
        dp[level][k] = curMin + 1;
      }
    }
    return dp[nLevels][kEggs];
  }

  static public int superEggQuad(int kEggs, int nLevels) {
    if (kEggs < 1 || nLevels < 1) {
      return 0;
    }
    int[][] dp = new int[nLevels + 1][kEggs + 1];
    int[][] bestLevels = new int[nLevels + 1][kEggs + 1];
    for (int i = 1; i <= nLevels; i++) {
      dp[i][1] = i;
      bestLevels[i][1] = 0; // =1?
    }
    for (int k = 0; k <= kEggs; k++) {
      dp[1][k] = 1;
      bestLevels[1][k] = 1;
    }
    // 从上往下，从右往左填，因为优化关系变成右边和上边格子了
    for (int level = 2; level <= nLevels; level++) {
      for (int k = kEggs; k > 1; k--) {
        int min = Integer.MAX_VALUE;
        int curBestLevel = -1;
        int lowerLim = bestLevels[level - 1][k];
        int upperLim = k == kEggs ? level : bestLevels[level][k + 1];
        for (int i = lowerLim; i < upperLim; i++) {
          int p1 = process(i - 1, kEggs - 1);
          int p2 = process(nLevels - i, kEggs);
          int cur = Math.max(p1, p2);
          if (cur < min) {
            min = cur;
            curBestLevel = i;
          }
        }
        dp[level][k] = min + 1;
        bestLevels[level][k] = curBestLevel;
      }
    }
    return dp[nLevels][kEggs];
  }

  // 最优解，代码有点抽象，建议回看录像
  public static int superEggOptimal(int kEggs, int nLevels) {
    if (kEggs < 1 || nLevels < 1) {
      return 0;
    }
    int[] dp = new int[kEggs];
    int result = 0;
    while (true) {
      result++;
      int previous = 0;
      for (int i = 0; i < dp.length; i++) {
        int tmp = dp[i];
        dp[i] = dp[i] + previous + 1;
        previous = tmp;
        if (dp[i] >= nLevels) {
          return result;
        }
      }
    }
  }

  public static void main(String[] args) {
    int testCycles = 10000;
    int maxVal = 10;
    System.out.println("test begin");
    for (int i = 0; i < testCycles; i++) {
      int n = (int) (Math.random() * maxVal) + 1;
      int k = (int) (Math.random() * maxVal) + 1;
      int ans1 = superEggDrop(n, k);
      int ans2 = superEggDropDp(n, k);
      int ans3 = superEggQuad(n, k);
      if (ans1 != ans2 || ans2 != ans3) {
        System.out.println("test fail");
        System.out.println(ans1);
        System.out.println(ans2);
        break;
      }
    }
    System.out.println("test end");
  }
}
