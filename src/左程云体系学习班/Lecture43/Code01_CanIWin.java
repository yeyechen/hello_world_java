package 左程云体系学习班.Lecture43;

public class Code01_CanIWin {

  /*
   * leetcode 464: 在 "100 game" 这个游戏中，两名玩家轮流选择从 1 到 10 的任意整数，累计整数和，
   * 先使得累计整数和 达到或超过 100 的玩家，即为胜者。
   * 如果我们将游戏规则改为 “玩家 不能 重复使用整数” 呢？
   * 例如，两个玩家可以轮流从公共整数池中抽取从 1 到 15 的整数（不放回），直到累计整数和 >= 100。
   * 给定两个整数 maxChoosableInteger（整数池中可选择的最大数）和 desiredTotal（累计和）
   * 若先出手的玩家是否能稳赢则返回 true，否则返回 false 。假设两位玩家游戏时都表现 最佳 。
   *
   * 状态压缩 -> 参数中带array或者set，用位运算转换成int参数，就可以使用动态规划技巧减少时间复杂度。
   * 但是array或者set中的element只能是true或者false。
   * */

  // 原始暴力尝试
  public static boolean canIWin(int maxChoosableInteger, int desiredTotal) {
    if (desiredTotal == 0) {
      return true;
    }
    // 把所有数字拿了都达不到total
    if ((maxChoosableInteger * (maxChoosableInteger + 1) >> 1) < desiredTotal) {
      return false;
    }
    int[] arr = new int[maxChoosableInteger];
    for (int i = 0; i < maxChoosableInteger; i++) {
      arr[i] = i + 1;
    }
    return process(arr, desiredTotal);
  }

  private static boolean process(int[] arr, int rest) {
    if (rest <= 0) {
      return false;
    }
    // 先手去尝试所有的情况
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] != -1) {
        int cur = arr[i];
        arr[i] = -1;
        boolean next = process(arr, rest - cur);
        arr[i] = cur;
        if (!next) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean canIWin2(int maxChoosableInteger, int desiredTotal) {
    if (desiredTotal == 0) {
      return true;
    }
    if ((maxChoosableInteger * (maxChoosableInteger + 1) >> 1) < desiredTotal) {
      return false;
    }
    return process2(maxChoosableInteger, 0, desiredTotal);
  }

  // 因为原始暴力尝试中 int[] arr 参数不能做动态规划表，利用状态压缩，用int代表位信息
  // status 中i位如果为0，代表没拿，当前可以拿
  //        中i为如果为1，代表已经拿过了，当前不能拿
  private static boolean process2(int maxChoosableInteger, int status, int rest) {
    if (rest <= 0) {
      return false;
    }
    for (int i = 1; i <= maxChoosableInteger; i++) {
      if (((1 << i) & status) == 0) { // 判断当前i位置数的状态
        if (!process2(maxChoosableInteger, (1 << i) | status, rest - i)) {
          return true;
        }
      }
    }
    return false;
  }

  // 实际上是利用状态压缩，然后做的傻缓存。仍存在递归行为，但是时间复杂度大大降低
  public static boolean canIWin2Dp(int maxChoosableInteger, int desiredTotal) {
    if (desiredTotal == 0) {
      return true;
    }
    if ((maxChoosableInteger * (maxChoosableInteger + 1) >> 1) < desiredTotal) {
      return false;
    }
    // dp 表的范围为0..00 -> 1..11 二进制，并且二进制的长度为 maxChoosableInteger
    // 所以长度为 10..00 二进制
    int[] dp = new int[1 << (maxChoosableInteger + 1)];
    return processDp(maxChoosableInteger, 0, desiredTotal, dp);
  }

  private static boolean processDp(int maxChoosableInteger, int status, int rest, int[] dp) {
    // dp[status] == 1 -> true
    // dp[status] == -1 -> false
    // dp[status] == 0 -> haven't calculated, go over the process
    if (dp[status] != 0) {
      return dp[status] == 1;
    }
    if (rest <= 0) {
      return false;
    }
    // go over the process and put the result in the dp table.
    boolean result = false;
    for (int i = 1; i <= maxChoosableInteger; i++) {
      if (((1 << i) & status) == 0) { // 判断当前i位置数的状态
        if (!processDp(maxChoosableInteger, (1 << i) | status, rest - i, dp)) {
          result = true;
          break;
        }
      }
    }
    dp[status] = result ? 1 : -1;
    return result;
  }

}
