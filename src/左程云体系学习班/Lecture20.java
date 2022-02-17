package 左程云体系学习班;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Lecture20 {

  /*
   * 1. 最长回文子序列(leetcode 516题): 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
   * 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
   *
   * 2. 象棋马跳跃问题: 请同学们自行搜索或者想象一个象棋的棋盘，然后把整个棋盘放入第一象限，棋盘的最左下角是(0,0)位置
   * 那么整个棋盘就是横坐标上9条线、纵坐标上10条线的区域(10*9)
   * 给你三个 参数 x，y，k
   * 返回“马”从(0,0)位置出发，必须走k步
   * 最后落在(x,y)上的方法数有多少种?
   *
   * 3. 喝咖啡问题(京东面试题，暂时理解得不是很透彻)：
   * 数组arr代表每一个咖啡机冲一杯咖啡的时间，每个咖啡机只能串行的制造咖啡。
   * 现在有n个人需要喝咖啡，只能用咖啡机来制造咖啡。
   * 认为每个人喝咖啡的时间非常短，冲好的时间即是喝完的时间。
   * 每个人喝完之后咖啡杯可以选择洗或者自然挥发干净，只有一台洗咖啡杯的机器，只能串行的洗咖啡杯。
   * 洗杯子的机器洗完一个杯子时间为a，任何一个杯子自然挥发干净的时间为b。
   * 四个参数：arr, n, a, b
   * 假设时间点从0开始，返回所有人喝完咖啡并洗完咖啡杯的全部过程结束后，至少来到什么时间点。
   * */

  // 1. 最长回文子序列(leetcode 516题)
  public static int longestPalindromeSubseq(String s) {
    int N = s.length();
    if (N == 0) {
      return 0;
    }
    return palindromeSubseqProcess(s.toCharArray(), 0, N - 1);
  }

  // 给定一个字符串, 在[L .. R]区间内最长的回文子序列长度
  private static int palindromeSubseqProcess(char[] s, int L, int R) {
    if (L == R) {
      return 1;
    }
    if (L == R - 1) {
      return s[L] == s[R] ? 2 : 1;
    }
    int p1 = palindromeSubseqProcess(s, L + 1, R);
    int p2 = palindromeSubseqProcess(s, L, R - 1);
    int p3 = s[L] == s[R] ? 2 + palindromeSubseqProcess(s, L + 1, R - 1) : 0;
    return Math.max(p1, Math.max(p2, p3));
  }

  // 1. 最长回文子序列(leetcode 516题): 改动态规划(自己尝试)
  public static int longestPalindromeSubseqDp(String s) {
    int N = s.length();
    char[] str = s.toCharArray();
    if (N == 0) {
      return 0;
    }
    int[][] dp = new int[N][N];
    for (int i = 0; i < N; i++) {
      dp[i][i] = 1;
    }
    for (int i = 1; i < N; i++) {
      dp[i - 1][i] = str[i - 1] == str[i] ? 2 : 1;
    }
    for (int L = N - 3; L >= 0; L--) {
      for (int R = L + 2; R < N; R++) {
        int p1 = dp[L + 1][R];
        int p2 = dp[L][R - 1];
        int p3 = str[L] == str[R] ? 2 + dp[L + 1][R - 1] : 0;
        dp[L][R] = Math.max(p1, Math.max(p2, p3));
      }
    }
    return dp[0][N - 1];
  }

  // 2. 象棋马跳跃问题
  public static int jumpMethod(int x, int y, int k) {
    if (x < 0 || y < 0 || x > 9 || y > 8 || k < 0) {
      return 0;
    }
    return jumpProcess(0, 0, x, y, k);
  }

  private static int jumpProcess(int currX, int currY, int targetX, int targetY, int restK) {
    if (currX < 0 || currY < 0 || currX > 9 || currY > 8) {
      return 0;
    }
    if (restK == 0) {
      return (currX == targetX && currY == targetY) ? 1 : 0;
    }
    int ways = jumpProcess(currX + 1, currY + 2, targetX, targetY, restK - 1);
    ways += jumpProcess(currX - 1, currY + 2, targetX, targetY, restK - 1);
    ways += jumpProcess(currX + 1, currY - 2, targetX, targetY, restK - 1);
    ways += jumpProcess(currX - 1, currY - 2, targetX, targetY, restK - 1);
    ways += jumpProcess(currX + 2, currY + 1, targetX, targetY, restK - 1);
    ways += jumpProcess(currX - 2, currY + 1, targetX, targetY, restK - 1);
    ways += jumpProcess(currX + 2, currY - 1, targetX, targetY, restK - 1);
    ways += jumpProcess(currX - 2, currY - 1, targetX, targetY, restK - 1);
    return ways;
  }

  // 改动态规划: 三维表, 先把最后一层k==0填好, 所有8中情况都是依赖下一层k-1的
  public static int jumpMethodDp(int targetX, int targetY, int k) {
    if (targetX < 0 || targetY < 0 || targetX > 9 || targetY > 8 || k < 0) {
      return 0;
    }
    int[][][] dp = new int[10][9][k + 1];
    dp[targetX][targetY][0] = 1;
    for (int restK = 1; restK <= k; restK++) {
      for (int currX = 0; currX < 10; currX++) {
        for (int currY = 0; currY < 9; currY++) {
          int ways = pick(dp, currX + 1, currY + 2, restK - 1);
          ways += pick(dp, currX - 1, currY + 2, restK - 1);
          ways += pick(dp, currX + 1, currY - 2, restK - 1);
          ways += pick(dp, currX - 1, currY - 2, restK - 1);
          ways += pick(dp, currX + 2, currY + 1, restK - 1);
          ways += pick(dp, currX - 2, currY + 1, restK - 1);
          ways += pick(dp, currX + 2, currY - 1, restK - 1);
          ways += pick(dp, currX - 2, currY - 1, restK - 1);
          dp[currX][currY][restK] = ways;
        }
      }
    }
    return dp[0][0][k];
  }

  private static int pick(int[][][] dp, int currX, int currY, int restK) {
    if (currX < 0 || currY < 0 || currX > 9 || currY > 8) {
      return 0;
    }
    return dp[currX][currY][restK];
  }

  // 3. 喝咖啡问题
  public static class Machine {
    public int timePoint;
    public int workTime;

    public Machine(int t, int w) {
      timePoint = t;
      workTime = w;
    }
  }

  public static class MachineComparator implements Comparator<Machine> {

    @Override
    public int compare(Machine o1, Machine o2) {
      return (o1.timePoint + o1.workTime) - (o2.timePoint + o2.workTime);
    }
  }

  public static int Coffee(int[] arr, int n, int a, int b) {
    PriorityQueue<Machine> heap = new PriorityQueue<Machine>(new MachineComparator());
    for (int j : arr) {
      heap.add(new Machine(0, j));
    }
    int[] drinks = new int[n];
    for (int i = 0; i < n; i++) {
      Machine cur = heap.poll();
      cur.timePoint += cur.workTime;
      drinks[i] = cur.timePoint;
      heap.add(cur);
    }
    return minWashTime(drinks, a, b, 0, 0);
  }

  public static int minWashTime(int[] drinks, int wash, int air, int index, int free) {
    if (index == drinks.length) {
      return 0;
    }
    // index号杯子 决定洗
    int selfClean1 = Math.max(drinks[index], free) + wash;
    int restClean1 = minWashTime(drinks, wash, air, index + 1, selfClean1);
    int p1 = Math.max(selfClean1, restClean1);

    // index号杯子 决定挥发
    int selfClean2 = drinks[index] + air;
    int restClean2 = minWashTime(drinks, wash, air, index + 1, free);
    int p2 = Math.max(selfClean2, restClean2);
    return Math.min(p1, p2);
  }

  public static void main(String[] args) {
    System.out.println(longestPalindromeSubseqDp("bbbab"));
    System.out.println(jumpMethod(7, 7, 10));
    System.out.println(jumpMethodDp(7, 7, 10));
  }
}
