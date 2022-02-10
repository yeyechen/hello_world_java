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
   * 2. 拿牌最大分数问题：给定一个整型数组arr，代表数值不同的纸牌排成一条线
   * 玩家A和玩家B依次拿走最左或最右的纸牌
   * 规定玩家A先拿，玩家B后拿
   * 玩家A和玩家B都绝顶聪明
   * 请返回最后获胜者的分数
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

  // 真正意义上的动态规划：生成一个表，表里的位置代表(curr, restK)，然后依次根据依赖关系将表填满
  public static int waysDp2(int N, int start, int k, int end) {
    int[][] dp = new int[N + 1][k + 1];
    // 先把第一列填上，只有在curr=end，restK=0位置时(步数已经走完了，而且走到该到的位置时)等于1，其余restK=0都是0
    dp[end][0] = 1;
    // 遍历restK，一列一列填。
    for (int restK = 1; restK <= k; restK++) {
      // 第一行的数值只依赖左下角的数值，因为第一行代表位置为1，只能向右走，步数是和左下角一样的
      dp[1][restK] = dp[2][restK - 1];

      // 中间行同时依赖左上角和左下角的数值，步数为它们的和
      for (int curr = 2; curr < N; curr++) {
        dp[curr][restK] = dp[curr - 1][restK - 1] + dp[curr + 1][restK - 1];
      }

      // 最后一行的数值只依赖左上角的数值，因为最后一行只能向左走，是和左上角一样的步数
      dp[N][restK] = dp[N - 1][restK - 1];
    }
    // 整张表都填完了，直接返回(start, k)的数值即可
    return dp[start][k];
  }
  // let N = 5, start = 2, k = 6, end = 4
  // curr\restK  0  1  2  3  4  5  6
  //            --------------------
  //          0 |X  X  X  X  X  X  X
  //          1 |0  0  0  1  0  4  0
  //          2 |0  0  1  0  4  0  13
  //          3 |0  1  0  3  0  9  0
  //          4 |1  0  2  0  5  0  14
  //          5 |0  1  0  2  0  5  0

  // 2. 拿牌最大分数问题
  public static int win(int[] arr) {
    if (arr == null || arr.length == 0) {
      return 0;
    }
    int first = f(arr, 0, arr.length - 1);
    int second = g(arr, 0, arr.length - 1);
    return Math.max(first, second);
  }

  // f函数代表先手情况下，当前玩家作出的最好决策。L为左边界，R为右边界
  private static int f(int[] arr, int L, int R) {
    // 卡牌库只剩一张牌了，先手拿走
    if (L == R) {
      return arr[L];
    }
    // 卡牌数量大于1时有两种选择，分别都要考虑后手情况下的最好决定，并且取最大值
    int p1 = arr[L] + g(arr, L + 1, R);
    int p2 = arr[R] + g(arr, L, R - 1);
    return Math.max(p1, p2);
  }

  // g函数代表后手情况下，当前玩家作出的最好决策。L为左边界，R为右边界
  private static int g(int[] arr, int L, int R) {
    // 卡牌库只剩一张牌了，后手没得拿
    if (L == R) {
      return 0;
    }
    // 卡牌数量大于1时，因为是后手，对手做最好决定，所以是最小值
    int p1 = f(arr, L + 1, R);
    int p2 = f(arr, L, R - 1);
    return Math.min(p1, p2);
  }

  // 转动态规划，用傻缓存
  public static int winDp(int[] arr) {
    if (arr == null || arr.length == 0) {
      return 0;
    }
    int N = arr.length;
    // 先手后手各一张表，相互依赖。
    int[][] fmap = new int[N][N];
    int[][] gmap = new int[N][N];
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        fmap[i][j] = -1;
        gmap[i][j] = -1;
      }
    }
    int first = fDp(arr, 0, arr.length - 1, fmap, gmap);
    int second = gDp(arr, 0, arr.length - 1, fmap, gmap);
    return Math.max(first, second);
  }

  private static int fDp(int[] arr, int L, int R, int[][] fmap, int[][] gmap) {
    // 缓存命中
    if (fmap[L][R] != -1) {
      return fmap[L][R];
    }
    // 缓存没命中，计算结果并放入缓存
    int ans;
    if (L == R) {
      ans = arr[L];
    } else {
      int p1 = arr[L] + gDp(arr, L + 1, R, fmap, gmap);
      int p2 = arr[R] + gDp(arr, L, R - 1, fmap, gmap);
      ans = Math.max(p1, p2);
    }
    fmap[L][R] = ans;
    return ans;
  }

  private static int gDp(int[] arr, int L, int R, int[][] fmap, int[][] gmap) {
    // 缓存命中
    if (gmap[L][R] != -1) {
      return gmap[L][R];
    }
    // 缓存没命中，计算结果并放入缓存
    int ans = 0;
    if (L != R) {
      int p1 = fDp(arr, L + 1, R, fmap, gmap);
      int p2 = fDp(arr, L, R - 1, fmap, gmap);
      ans = Math.min(p1, p2);
    }
    gmap[L][R] = ans;
    return ans;
  }

  // 改动态规划，二维数组依赖关系
  public static int winDp2(int[] arr) {
    if (arr == null || arr.length == 0) {
      return 0;
    }
    int N = arr.length;
    int[][] fmap = new int[N][N];
    int[][] gmap = new int[N][N];
    // 初始化对角线，因为当L==R时，先手肯定拿走最后一张牌，而后手拿不了
    for (int i = 0; i < N; i++) {
      // 对角线的值就等于arr[L] (或arr[R])的值，没有初始化gmap因为本身就都是0
      fmap[i][i] = arr[i];
    }
    // 按对角线填充表，依赖关系都是左边的值和下面的值
    for (int startCol = 1; startCol < N; startCol++) {
      int L = 0;
      int R = startCol;
      // 起始位置为(0, startCol)向右下填充对角线
      while (R < N) {
        fmap[L][R] = Math.max(arr[L] + gmap[L + 1][R], arr[R] + gmap[L][R - 1]);
        gmap[L][R] = Math.min(fmap[L + 1][R], fmap[L][R - 1]);
        L++;
        R++;
      }
    }
    return Math.max(fmap[0][N - 1], gmap[0][N - 1]);
  }
  public static void main(String[] args) {
    System.out.println(ways(5, 2, 6, 4));
    System.out.println(waysDp(5, 2, 6, 4));
    System.out.println(waysDp2(5, 2, 6, 4));

    int[] arr = { 5, 7, 4, 5, 8, 1, 6, 0, 3, 4, 6, 1, 7 };
    System.out.println(win(arr));
    System.out.println(winDp(arr));
    System.out.println(winDp2(arr));
  }
}
