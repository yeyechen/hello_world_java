package 左程云体系学习班;

public class Lecture18 {

  /*
   * 动态规划：空间换时间。
   *
   * 1. 机器人位置问题：假设有排成一行的N个位置记为1~N，N一定大于或等于2。开始时机器人在其中的start位置上(start一定是1~N中的一个)
   * 如果机器人来到1位置，那么下一步只能往右来到2位置；
   * 如果机器人来到N位置，那么下一步只能往左来到N-1位置；
   * 如果机器人来到中间位置，那么下一步可以往左走或者往右走；
   * 规定机器人必须走k步，最终能来到end位置(P也是1~N中的一个)的方法有多少种
   * 给定四个参数 N、start、k、end，返回方法数
   *
   * */


  // 1. 机器人位置问题
  // 1~N个位置，起始位置为start，结束位置为end，走k步
  public static int ways(int N, int start, int k, int end) {
    return waysProcess(N, start, k, end);
  }

  // 1~N个位置，现在位置为curr，结束位置为end，剩余restK步可以走
  private static int waysProcess(int N, int curr, int restK, int end) {
    // 没有步数可以走了
    if (restK == 0) {
      return curr == end ? 1 : 0;
    }
    // 还有步数可以走
    // 当位置为1(最左边)时，只能往右，所以1种可能性
    if (curr == 1) {
      return waysProcess(N, curr + 1, restK - 1, end);
    }
    // 当位置为N(最右边)时，只能往左，所以1种可能性
    if (curr == N) {
      return waysProcess(N, curr - 1, restK - 1, end);
    }
    // 当位置在中间时，可以往左也可以往右，2种可能性
    return waysProcess(N, curr - 1, restK - 1, end) + waysProcess(N, curr + 1, restK - 1, end);
  }

  // 机器人问题转动态规划。用缓存，每次碰到过的情况直接从缓存里拿，避免重复计算
  public static int waysDp(int N, int start, int k, int end) {
    // 新建一个缓存，大小为curr位置的范围*剩余步数restK的范围
    int[][] dp = new int[N + 1][k + 1];
    // 初始化缓存，把每个(curr, restK)的值设为-1
    for (int i = 0; i <= N; i++) {
      for (int j = 0; j <= k; j++) {
        dp[i][j] = -1;
      }
    }
    return waysDpProcess(N, start, k, end, dp);
  }

  private static int waysDpProcess(int N, int curr, int restK, int end, int[][] dp) {
    // 之前算过(curr, restK)的值了，直接从缓存里拿
    if (dp[curr][restK] != -1) {
      return dp[curr][restK];
    }
    // 之前没算过，计算出结果并加入缓存
    int ans;
    if (restK == 0) {
      ans = curr == end ? 1 : 0;
    } else if (curr == 1) {
      ans = waysProcess(N, curr + 1, restK - 1, end);
    } else if (curr == N) {
      ans = waysProcess(N, curr - 1, restK - 1, end);
    } else {
      ans = waysProcess(N, curr - 1, restK - 1, end) + waysProcess(N, curr + 1, restK - 1, end);
    }
    dp[curr][restK] = ans;
    return ans;
  }

  public static void main(String[] args) {
    System.out.println(ways(5, 2, 6, 4));
    System.out.println(waysDp(5, 2, 6, 4));
  }
}
