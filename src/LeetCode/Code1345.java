package LeetCode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Code1345 {

  // 跳跃游戏IV：
  // 不能用递归或者动态规划做了，得用BFS
  public static int minJumps(int[] arr) {
    Map<Integer, List<Integer>> idxSameValue = new HashMap<>();
    for (int i = 0; i < arr.length; i++) {
      idxSameValue.putIfAbsent(arr[i], new ArrayList<>());
      idxSameValue.get(arr[i]).add(i);
    }
    Set<Integer> visitedIndex = new HashSet<>();
    Queue<int[]> queue = new ArrayDeque<>();
    queue.offer(new int[]{0, 0});
    visitedIndex.add(0);

    while (!queue.isEmpty()) {
      int[] idxStep = queue.poll();
      int idx = idxStep[0];
      int step = idxStep[1];

      if (idx == arr.length - 1) {
        return step;
      }
      int value = arr[idx];
      step++;
      if (idxSameValue.containsKey(value)) {
        for (int i : idxSameValue.get(value)) {
          if (!visitedIndex.contains(i)) {
            visitedIndex.add(i);
            queue.offer(new int[]{i, step});
          }
        }
        idxSameValue.remove(value);
      }
      if (idx + 1 < arr.length && visitedIndex.add(idx + 1)) {
        queue.offer(new int[]{idx + 1, step});
      }
      if (idx - 1 >= 0 && visitedIndex.add(idx - 1)) {
        queue.offer(new int[]{idx - 1, step});
      }
    }
    return -1;
  }

  public static void main(String[] args) {
    int[] arr = new int[]{100, -23, -23, 404, 100, 23, 23, 23, 3, 404};
    System.out.println(minJumps(arr));
  }
}
