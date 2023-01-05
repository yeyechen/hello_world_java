package 左程云体系学习班.Lecture42;

import Util.Utility;

public class Code01_PostOfficeProblem {

  /*
   * 邮局问题
   * 一条直线上有居民点，邮局只能建在居民点上
   * 给定一个有序正数数组arr，每个值表示 居民点的一维坐标，再给定一个正数 num，表示邮局数量
   * 选择num个居民点建立num个邮局，使所有的居民点到最近邮局的总距离最短，返回最短的总距离
   * arr=[1,2,3,4,5,1000]，num=2
   * 第一个邮局建立在3位置，第二个邮局建立在1000位置
   * 那么1位置到邮局的距离为2，2位置到邮局距离为1，3位置到邮局的距离为0，4位置到邮局的距离为1，5位置到邮局的距离为21000位置到邮局的距离为0
   * 这种方案下的总距离为6，其他任何方案的总距离都不会比该方案的总距离更短，所以返回6
   * */

  public static int minDistanceDp(int[] arr, int num) {
    if (arr == null || arr.length < num || num < 1) {
      return 0;
    }
    int N = arr.length;
    // 准备一个二维的辅助数组，w[L][R]的含义是arr[L..R]范围上，只建一个邮局，总体距离最小是多少
    // (邮局一定在中点，证明省略。偶数个点时，建在上中点和下中点距离一样)
    // 长度为N+1因为后续不用处理边界条件。最后L>R默认超出限制，为0。
    int[][] w = new int[N + 1][N + 1];
    for (int L = 0; L < N; L++) {
      for (int R = L + 1; R < N; R++) {
        w[L][R] = w[L][R - 1] + (arr[R] - arr[(R + L) >> 1]); // 加上新进来的点到中点的距离
      }
    }
    // dp[i][j] 的含义是：arr[0..i]范围上，建j个邮筒的最小总体距离
    int[][] dp = new int[N][num + 1];
    for (int i = 0; i < N; i++) {
      dp[i][1] = w[0][i]; //直接从w数组里拿就行
    }
    for (int i = 1; i < N; i++) {
      for (int j = 2; j <= Math.min(i, num); j++) { // j <= i因为0..i有i+1个点，邮局数大于或等于地点数没意义
        int result = Integer.MAX_VALUE;
        for (int split = 0; split <= i; split++) {
          result = Math.min(result, dp[i][j - 1] + w[i + 1][i]);
        }
        dp[i][j] = result;
      }
    }
    return dp[N - 1][num];
  }

  // 与上节课画家问题非常相似
  public static int minDistanceQuad(int[] arr, int num) {
    if (arr == null || arr.length < num || num < 1) {
      return 0;
    }
    int N = arr.length;
    int[][] w = new int[N + 1][N + 1];
    for (int L = 0; L < N; L++) {
      for (int R = L + 1; R < N; R++) {
        w[L][R] = w[L][R - 1] + (arr[R] - arr[(R + L) >> 1]); // 加上新进来的点到中点的距离
      }
    }
    // dp[i][j] 的含义是：arr[0..i]范围上，建j个邮筒的最小总体距离
    int[][] dp = new int[N][num + 1];
    int[][] bestSplit = new int[N][num + 1];
    for (int i = 0; i < N; i++) {
      dp[i][1] = w[0][i]; //直接从w数组里拿就行
      bestSplit[i][1] = -1;
    }
    for (int j = 2; j <=num; j++) {
      for (int i = N-1; i >= j; i--) { // j <= i因为0..i有i+1个点，邮局数大于或等于地点数没意义
        int result = Integer.MAX_VALUE;
        int curBestSplit = -1;
        int lowerLim = bestSplit[i][j - 1];
        int upperLim = i == N - 1 ? N - 1 : bestSplit[i + 1][j];
        for (int split = lowerLim; split <= upperLim; split++) {
          int curDis = dp[i][j - 1] + w[i + 1][i];
          if (curDis < result) { // 这里小于或者小于等于都能过。我们不想证明，直接对数器试
            result = curDis;
            curBestSplit = split;
          }
        }
        dp[i][j] = result;
        bestSplit[i][j] = curBestSplit;
      }
    }
    return dp[N - 1][num];
  }

  public static void main(String[] args) {
    int testCycles = 100000;
    int maxLen = 40;
    int maxVal = 20;
    System.out.println("test begin");
    for (int i = 0; i < testCycles; i++) {
      int num = (int) (Math.random() * maxVal) + 1;
      int[] arr = Utility.randomArrayGenerator(maxLen, maxVal, true);
      int ans1 = minDistanceDp(arr, num);
      int ans2 = minDistanceQuad(arr, num);
      if (ans1 != ans2) {
        System.out.println("test fail");
        break;
      }
    }
    System.out.println("test end");
  }
}
