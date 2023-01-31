package 左程云体系学习班.Lecture46;

public class Code01_BurstBalloons {

  /*
   * 有时动态规划并不常规，解法需要一定经验和尝试。比如这题 L 到 R 范围上尝试气球爆了的话，是不行的；范围上尝试气球没
   * 爆就可以。
   *
   * 本题测试链接(leetcode312): https://leetcode.com/problems/burst-balloons/
   * 有 n 个气球，编号为0 到 n - 1，每个气球上都标有一个数字，这些数字存在数组nums中。
   * 现在要求你戳破所有的气球。戳破第 i 个气球，你可以获得 nums[i - 1] * nums[i] * nums[i + 1] 枚硬币。
   * 这里的i-1和 i+1 代表和 i 相邻的两个气球的序号。如果 i-1 或 i+1 超出了数组的边界，那么就当它是一个数字为1的气球。
   * 求所能获得硬币的最大数量。
   *
   * */

  // 暴力递归，改缓存可以过leetcode
  public static int maxCoins(int[] nums) {
    int N = nums.length;
    int[] help = new int[N + 2];
    for (int i = 0; i < N; i++) {
      help[i + 1] = nums[i];
    }
    help[0] = 1;
    help[N + 1] = 1;
    return process(help, 1, N);
  }

  // L-1位置，和R+1位置，永远不越界，并且，[L-1] 和 [R+1] 一定没爆呢！
  // arr[L...R]范围上的气球都还没爆，返回arr[L...R]打爆所有气球，最大得分是什么
  private static int process(int[] arr, int L, int R) {
    if (L == R) {
      return arr[L - 1] * arr[L] * arr[R + 1];
    }
    // 尝试每一种情况，最后打爆的气球是什么位置
    // L 位置的气球最后打爆
    int maxCoins = process(arr, L+1, R) + arr[L - 1] * arr[L] * arr[R + 1];
    // R 位置的气球最后打爆
    maxCoins = Math.max(maxCoins, process(arr, L, R - 1) + arr[L - 1] * arr[R] * arr[R + 1]);
    // (L,R) 中间位置的气球，尝试依次被最后打爆的情况
    for (int i = L + 1; i < R; i++) {
      int leftCoins = process(arr, L, i - 1);
      int rightCoins = process(arr, i + 1, R);
      int currCoins = arr[L - 1] * arr[i] * arr[R + 1];
      int coins = leftCoins + rightCoins + currCoins;
      maxCoins = Math.max(maxCoins, coins);
    }
    return maxCoins;
  }
}
