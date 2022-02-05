package 左程云体系学习班;

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
   * 方法1和方法2同为最优解, 时间复杂度相同, 但是方法1的常数时间优与方法2。
   *
   * */

  // 优化并查集: 不用hashmap实现而用数组时间, 寻址时间比hash查找快
  public static class UnionFindOptimise {

    int[] help; // 辅助数组, 用于查找的扁平化处理优化
    int[] parents; // 记录父亲节点
    int[] sizeArray; // 记录代表节点所在的集合大小 (index必须是代表节点)
    int size; // 集合数量

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

    // 返回改节点所在的代表节点
    private int findFather(int i) {
      int hi = 0;
      while (i != parents[i]) {
        help[i++] = i;
        i = parents[i];
      }
      for (hi--; hi >= 0; hi--) {
        parents[help[hi]] = i;
      }
      return i;
    }
  }
}
