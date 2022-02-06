package 左程云体系学习班;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import 左程云体系学习班.Lecture14.UnionFind;

public class Lecture15 {

  /*
   * 并查集应用相关题目
   *
   * 1. 朋友圈(leetcode 547题)：给你一个N*N的矩阵，其中friend[i][j] = 1 表示 i 和 j 是朋友，如果为0表示不认识。
   * 返回矩阵中朋友圈的数量。
   *
   * 2. 岛屿数量(leetcode 200题)：给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
   * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
   * 此外，你可以假设该网格的四条边均被水包围。
   * 方法1: 感染方法。遍历整个矩阵, 当遇到'1'时, 感染整片岛屿, 然后岛屿数量+1。时间复杂度为O(m * n), m 为矩阵列数, n 为矩阵行数。
   * 方法2: 并查集法。把每一个'1'在并查集中初始化成每一个小集合, 然后朝自己的左边和上边合并。最后返回集合数量。
   * 方法1和方法2同为最优解, 时间复杂度相同, 但是方法1的常数时间优与方法2。方法2仍可以让并查集用数组方式完成，提升常数时间复杂度。
   *
   * 3. 岛屿数量II(leetcode 305题)：起始的时候，每个格子的地形都被默认标记为「水」。我们可以通过使用 addLand 进行操作，将位置 (row, col) 的「水」变成「陆地」。
   * 你将会被给定一个列表，来记录所有需要被操作的位置，然后你需要返回计算出来 每次 addLand 操作后岛屿的数量。
   * 用并查集方法做为最优解，时间复杂度为O(m * n) + O(k)，m和n为矩阵长宽，k为矩阵中1的数量。初始化矩阵会比较费时间，如果矩阵规模很大那么时间复杂度为O(m * n)，
   *
   * */

  // 优化并查集: 不用hashmap实现而用数组时间, 寻址时间比hash查找快
  public static class UnionFindOptimise {

    private final int[] help; // 辅助数组, 用于查找的扁平化处理优化
    private final int[] parents; // 记录父亲节点
    private final int[] sizeArray; // 记录代表节点所在的集合大小 (index必须是代表节点)
    private int size; // 集合数量

    // *岛屿问题专用*
    private int col; // 矩阵总共的列数
    private int row; // 矩阵总共的行数

    public UnionFindOptimise(int N) {
      help = new int[N];
      parents = new int[N];
      sizeArray = new int[N];
      size = N;

      // 初始化时每个集合只有自己一个节点, 且都为代表节点
      for (int i = 0; i < N; i++) {
        parents[i] = i;
        sizeArray[i] = 1;
      }
    }

    // *岛屿问题专用*
    public UnionFindOptimise(char[][] board) {
      row = board.length;
      col = board[0].length;
      size = 0;
      int row = board.length;
      int len = row * col;
      parents = new int[len];
      sizeArray = new int[len];
      help = new int[len];
      for (int r = 0; r < row; r++) {
        for (int c = 0; c < col; c++) {
          if (board[r][c] == '1') {
            int i = index(r, c);
            parents[i] = i;
            sizeArray[i] = 1;
            size++;
          }
        }
      }
    }

    // *岛屿问题专用II*
    public UnionFindOptimise(int m, int n) {
      row = m;
      col = n;
      size = 0;
      int num = m * n;
      parents = new int[num];
      sizeArray = new int[num];
      help = new int[num];
    }

    // *岛屿问题专用*
    private int index(int r, int c) {
      return r * col + c;
    }

    // 返回改节点所在的代表节点下标
    private int findFather(int i) {
      int hi = 0;
      while (i != parents[i]) {
        help[hi++] = i;
        i = parents[i];
      }
      // i 现在为代表节点的下标
      for (hi--; hi >= 0; hi--) {
        parents[help[hi]] = i;
      }
      return i;
    }

    // 将i下标的元素所在集合和j下标元素所在集合合并
    public void union(int i, int j) {
      int set1 = findFather(i);
      int set2 = findFather(j);
      if (set1 != set2) {
        if (sizeArray[set1] >= sizeArray[set2]) {
          sizeArray[set1] += sizeArray[set2];
          parents[set2] = set1;
        } else {
          sizeArray[set2] += sizeArray[set2];
          parents[set1] = set2;
        }
        size--;
      }
    }

    // *岛屿问题专用*
    public void union(int r1, int c1, int r2, int c2) {
      if (r1 < 0 || r1 == row || r2 < 0 || r2 == row || c1 < 0 || c1 == col || c2 < 0 || c2 == col) {
        return;
      }
      int i1 = index(r1, c1);
      int i2 = index(r2, c2);
      // i1和i2中只要有一个，从来都没有被初始化过，肯定不是岛屿，不需要合并
      if (sizeArray[i1] == 0 || sizeArray[i2] == 0) {
        return;
      }
      int f1 = findFather(i1);
      int f2 = findFather(i2);
      if (f1 != f2) {
        if (sizeArray[f1] >= sizeArray[f2]) {
          sizeArray[f1] += sizeArray[f2];
          parents[f2] = f1;
        } else {
          sizeArray[f2] += sizeArray[f1];
          parents[f1] = f2;
        }
        size--;
      }
    }

    public int size() {
      return size;
    }

    // *岛屿问题专用II*
    public int connect(int r, int c) {
      int index = index(r, c);
      // 如果之前的位置已经被初始化过，直接跳过过程因为已经连接过了
      if (sizeArray[index] == 0) {
        parents[index] = index;
        sizeArray[index] = 1;
        size++;
        union(r - 1, c, r, c);
        union(r + 1, c, r, c);
        union(r, c - 1, r, c);
        union(r, c + 1, r, c);
      }
      return size;
    }
  }

  // 1. 朋友圈(leetcode 547题)
  public int findCircleNum(int[][] M) {
    int N = M.length;
    UnionFindOptimise unionFind = new UnionFindOptimise(N);
    // 因为矩阵从中间对角线对称，只用遍历矩阵右上方区域
    for (int i = 0; i < N; i++) {
      for (int j = i + 1; j < N; j++) {
        // i 和 j相互认识
        if (M[i][j] == 1) {
          unionFind.union(i, j);
        }
      }
    }
    return unionFind.size();
  }

  // 2. 岛屿数量(leetcode 200题)：感染方式
  public static int numIslands(char[][] board) {
    int island = 0;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        if (board[i][j] == '1') {
          island++;
          infect(board, i, j);
        }
      }
    }
    return island;
  }

  private static void infect(char[][] board, int i, int j) {
    if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] != '1') {
      return;
    }
    board[i][j] = '0';
    // 感染上下左右
    infect(board, i - 1, j);
    infect(board, i + 1, j);
    infect(board, i, j - 1);
    infect(board, i, j + 1);
  }

  // 并查集法1(使用hashmap)
  public static int numIslands2(char[][] board) {
    int row = board.length;
    int col = board[0].length;
    Dot[][] dots = new Dot[row][col];
    List<Dot> dotList = new ArrayList<>();
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        if (board[i][j] == '1') {
          dots[i][j] = new Dot();
          dotList.add(dots[i][j]);
        }
      }
    }
    UnionFind<Dot> unionFind = new UnionFind<>(dotList);
    // 让每个有岛屿的点往左方和上方合并
    // 第一行只往左边合并，因为没有上边
    for (int j = 1; j < col; j++) {
      if (board[0][j - 1] == '1' && board[0][j] == '1') {
        unionFind.union(dots[0][j - 1], dots[0][j]);
      }
    }
    // 第一列只往上边合并，因为没有左边
    for (int i = 1; i < row; i++) {
      if (board[i - 1][0] == '1' && board[i][0] == '1') {
        unionFind.union(dots[i - 1][0], dots[i][0]);
      }
    }
    for (int i = 1; i < row; i++) {
      for (int j = 1; j < col; j++) {
        if (board[i][j] == '1') {
          if (board[i][j - 1] == '1') {
            unionFind.union(dots[i][j - 1], dots[i][j]);
          }
          if (board[i - 1][j] == '1') {
            unionFind.union(dots[i - 1][j], dots[i][j]);
          }
        }
      }
    }
    return unionFind.size();
  }

  // 这个类是用来使用内存地址，表示岛屿，如果dots[i][j] == null则无岛屿
  public static class Dot {

  }

  // 并查集法2(使用array)
  public static int numIslands3(char[][] board) {
    int row = board.length;
    int col = board[0].length;
    UnionFindOptimise uf = new UnionFindOptimise(board);
    for (int j = 1; j < col; j++) {
      if (board[0][j - 1] == '1' && board[0][j] == '1') {
        uf.union(0, j - 1, 0, j);
      }
    }
    for (int i = 1; i < row; i++) {
      if (board[i - 1][0] == '1' && board[i][0] == '1') {
        uf.union(i - 1, 0, i, 0);
      }
    }
    for (int i = 1; i < row; i++) {
      for (int j = 1; j < col; j++) {
        if (board[i][j] == '1') {
          if (board[i][j - 1] == '1') {
            uf.union(i, j - 1, i, j);
          }
          if (board[i - 1][j] == '1') {
            uf.union(i - 1, j, i, j);
          }
        }
      }
    }
    return uf.size();
  }

  // 3. 岛屿数量II 并查集法
  public static List<Integer> numIslandsII(int m, int n, int[][] positions) {
    UnionFindOptimise uf = new UnionFindOptimise(m, n);
    List<Integer> ans = new ArrayList<>();
    for (int[] position : positions) {
      ans.add(uf.connect(position[0], position[1]));
    }
    return ans;
  }

  // 测试
  public static List<Integer> numIslandsII2(int m, int n, int[][] positions) {
    // 初始化
    char[][] board = new char[m][n];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        board[i][j] = '0';
      }
    }
    List<Integer> result = new ArrayList<>();
    for (int[] pos : positions) {
      board[pos[0]][pos[1]] = '1';
      char[][] boardCopy = new char[m][n];
      for (int i = 0; i < m; i++) {
        System.arraycopy(board[i], 0, boardCopy[i], 0, n);
      }
      result.add(numIslands(boardCopy));
    }
    return result;
  }

  // 岛屿对数器
  public static void main(String[] args) {
    int testAmount = 10000;
    int maxRow = 10;
    int maxCol = 10;
    int maxNumPositions = 30;
    System.out.println("测试开始");
    for (int i = 0; i < testAmount; i++) {
      int[][] positions = generateRandomPositions(maxRow, maxCol, maxNumPositions);
      List<Integer> result1 = numIslandsII(maxRow, maxCol, positions);
      List<Integer> result2 = numIslandsII2(maxRow, maxCol, positions);
      if (!result1.equals(result2)) {
        System.out.println("测试失败");
        break;
      }
    }
    System.out.println("测试结束");
  }

  // 随机生成maxRow * maxCol 矩阵里面的位置
  private static int[][] generateRandomPositions(int maxRow, int maxCol, int maxNumPositions) {
    int numPositions = (int) ((Math.random()) * maxNumPositions);
    int[][] positions = new int[numPositions][2];
    for (int i = 0; i < numPositions; i++) {
      positions[i] = new int[]{(int) ((Math.random()) * maxRow),
          ((int) ((Math.random()) * maxCol))};
    }
    return positions;
  }
}
