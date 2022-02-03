package Bilibili;

public class Code01 {

  /*给定一个有序数组arr，代表坐落在X轴上面的点。 给定一个正数K，代表绳子长度。
  * 返回绳子最多能压住几个点？即使绳子边缘盖住也算盖住。*/
  public static int maxPoint(int[] arr, int l) {
    int res = 1;
    for (int i = 0; i < arr.length; i++) {
      int nearest = nearestIndex(arr, i, arr[i] - l);
      res = Math.max(res, nearest);
    }
    return res;
  }

  private static int nearestIndex(int[] arr, int r, int value) {
    int l = 0;
    int index = r;
    while (l <= r) {
      int mid = l + ((r - l) >> 1);
      if (arr[mid] >= value) {
        index = mid;
        r = mid - 1;
      } else {
        l = mid + 1;
      }
    }
    return index;
  }

  public static int maxPoint2(int[] arr, int l) {
    int left = 0, right = 0;
    int res = 0;

    for (int i = 0; i < arr.length; i++) {
      left = i;
      while (right<arr.length && arr[right] - arr[left] <= l) {
        right++;
      }
      res = Math.max(res, right - left);
    }
    return res;
  }
  public static void main(String[] args) {
    System.out.println(maxPoint(new int[]{1, 2, 4, 6, 7, 9}, 4));
  }
}
