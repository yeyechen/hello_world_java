package LeetCode;

public class Code1306 {
  // 跳跃游戏III：
  public boolean canReach(int[] arr, int start) {
    int[] map = new int[arr.length];
    return jump(arr, start, map);
  }

  public boolean jump(int[] arr, int curr, int[] map) {
    if (arr[curr] == 0) {
      return true;
    }
    // 判断之前是否来过当前位置，如果来过就形成循环，直接返回false
    if (map[curr] > 0) {
      return false;
    }
    map[curr]++;

    boolean p1 = curr + arr[curr] < arr.length && jump(arr, curr + arr[curr], map);
    boolean p2 = curr - arr[curr] >= 0 && jump(arr, curr - arr[curr], map);
    return p1 || p2;
  }
}
