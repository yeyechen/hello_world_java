package 左程云体系学习班.Lecture43;

import java.util.ArrayList;
import java.util.List;

public class Code02_TSP {

  /*
   * Travelling Salesman Problem:
   * 有N个城市，任何两个城市之间的都有距离，任何一座城市到自己的距离都为0
   * 所有点到点的距离都存在一个N*N的二维数组matrix里，也就是整张图由邻接矩阵表示
   * 现要求一旅行商从k城市出发必须经过每一个城市且只在一个城市逗留一次，最后回到出发的k城
   * 参数给定一个matrix，给定k。返回总距离最短的路的距离
   * */

  public static int t1(int[][] matrix) {
    int N = matrix.length; // 0...N-1
    // set
    // set.get(i) != null i这座城市在集合里，未访问过
    // set.get(i) == null i这座城市不在集合里，访问过
    List<Integer> set = new ArrayList<>();
    for (int i = 0; i < N; i++) {
      set.add(1);
    }
    // 人为规定起点为 0
    return func1(matrix, set, 0);
  }

  // 从start出发，要把set中所有的城市过一遍，最终回到0这座城市，最小距离是多少
  private static int func1(int[][] matrix, List<Integer> set, int start) {
    // 未访问过的城市数量
    int cityNum = 0;
    for (int i = 0; i < set.size(); i++) {
      if (set.get(i) != null) {
        cityNum++;
      }
    }
    // 仅剩一座城市未访问，即start城市，直接从start返回0城市
    if (cityNum == 1) {
      return matrix[start][0];
    }

    // 把当前城市标记访问过
    set.set(start, null);
    int min = Integer.MAX_VALUE;
    for (int i = 0; i < set.size(); i++) {
      if (set.get(i) != null) {
        int cost = matrix[start][i] + func1(matrix, set, i);
        min = Math.min(min, cost);
      }
    }
    set.set(start, 1);
    return min;
  }

  public static int t2(int[][] matrix) {
    int N = matrix.length;
    // cities = 1..11 all unvisited
    int cities = (1 << N) - 1;
    return func2(matrix, cities, 0);
  }

  private static int func2(int[][] matrix, int cities, int start) {
    // 仅剩一座城市未访问，即start城市，直接从start返回0城市
    // (~n + 1) & n 把最低位的1提取出来，如果和原数一样，那么仅剩一位 1
    if (((~cities + 1) & cities) == cities) {
      return matrix[start][0];
    }

    // 把当前城市标记访问过
    cities &= ~(1 << start);
    int min = Integer.MAX_VALUE;
    for (int i = 0; i < matrix.length; i++) {
      // 检查当前城市是否被访问过
      if ((cities & (1 << i)) != 0) {
        int cost = matrix[start][i] + func2(matrix, cities, i);
        min = Math.min(min, cost);
      }
    }
    cities |= (1 << start);
    return min;
  }

  // 根据压缩过的参数改dp(傻缓存)
  public static int dp(int[][] matrix) {
    int N = matrix.length;
    // cities = 1..11 all unvisited
    int cities = (1 << N) - 1;
    int[][] dp = new int[1 << N][N];
    // initialise
    for (int i = 0; i < (1 << N); i++) {
      for (int j = 0; j < N; j++) {
        dp[i][j] = -1;
      }
    }
    return funcDp(matrix, cities, 0, dp);
  }

  private static int funcDp(int[][] matrix, int cities, int start, int[][] dp) {
    if (dp[cities][start] != -1) {
      return dp[cities][start];
    }
    if (((~cities + 1) & cities) == cities) {
      dp[cities][start] = matrix[start][0];
    } else {
      cities &= ~(1 << start);
      int min = Integer.MAX_VALUE;
      for (int i = 0; i < matrix.length; i++) {
        if ((cities & (1 << i)) != 0) {
          int cost = matrix[start][i] + funcDp(matrix, cities, i, dp);
          min = Math.min(min, cost);
        }
      }
      cities |= (1 << start);
      dp[cities][start] = min;
    }
    return dp[cities][start];
  }
}
