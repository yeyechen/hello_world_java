package 左程云体系学习班.Lecture46;

public class Code02_RemoveBoxes {

  /* 本题测试链接(leetcode 546) : https://leetcode.com/problems/remove-boxes/
   * 给出一些不同颜色的盒子 boxes，盒子的颜色由不同的正数表示。
   * 你将经过若干轮操作去去掉盒子，直到所有的盒子都去掉为止。每一轮你可以移除具有相同颜色的连续 k 个盒子（k>= 1），
   * 这样一轮之后你将得到 k * k 个积分。
   * 返回 你能获得的最大积分和。
   *
   * */

  // 同样改缓存能过
  public static int removeBoxes(int[] boxes) {
    return process(boxes, 0, boxes.length - 1, 0);
  }

  // arr[L...R]消除，而且前面跟着K个arr[L]这个数
  // 返回：所有东西都消掉，最大得分
  private static int process(int[] arr, int L, int R, int K) {
    if (L > R) {
      return 0;
    }

    // 直接消除连带 L 在内的 K+1 个连续的数
    int score = process(arr, L + 1, R, 0) + (K + 1) * (K + 1);
    for (int i = L + 1; i <= R; i++) {
      if (arr[i] == arr[L]) {
        // 尝试把中间消掉，连成更多的arr[L]这个数
        score = Math.max(score, process(arr, L + 1, i - 1, 0) + process(arr, i, R, K + 1));
      }
    }
    return score;
  }
}
