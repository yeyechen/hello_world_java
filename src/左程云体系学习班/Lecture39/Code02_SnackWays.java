package 左程云体系学习班.Lecture39;

import static java.lang.System.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Code02_SnackWays {

  /*
   *
   * 牛牛家里一共有n袋零食, 第i袋零食体积为v[i]，背包容量为w，牛牛想知道在总体积不超过背包容量的情况下,
   * 一共有多少种零食放法，体积为0也算一种放法
   * 1 <= n <= 30, 1 <= w <= 2 * 10^9，v[I] (0 <= v[i] <= 10^9）
   * 背包容量和体积很大，如果用从左往右的尝试模型，2^30的计算量。
   * 如果我们用分治，2^15+2^15的计算量。
   *
   * 这是牛客的测试链接：
   * https://www.nowcoder.com/questionTerminal/d94bb2fa461d42bcb4c0f2b94f5d4281
   * */


  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    StreamTokenizer in = new StreamTokenizer(br);
    PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    while (in.nextToken() != StreamTokenizer.TT_EOF) {
      int n = (int) in.nval;
      in.nextToken();
      int bag = (int) in.nval;
      int[] arr = new int[n];
      for (int i = 0; i < n; i++) {
        in.nextToken();
        arr[i] = (int) in.nval;
      }
      long ways = ways(arr, bag);
      out.println(ways);
      out.flush();
    }
  }

  public static long ways(int[] arr, int bag) {
    if (arr == null || arr.length == 0) {
      return 0;
    }
    if (arr.length == 1) {
      return arr[0] <= bag ? 2 : 1;
    }
    int mid = (arr.length - 1) >> 1;
    TreeMap<Long, Long> lmap = new TreeMap<>();
    long ways = process(arr, 0, 0, mid, bag, lmap);
    TreeMap<Long, Long> rmap = new TreeMap<>();
    ways += process(arr, mid + 1, 0, arr.length - 1, bag, rmap);
    TreeMap<Long, Long> rpre = new TreeMap<>();
    long pre = 0;
    for (Entry<Long, Long> entry : rmap.entrySet()) {
      pre += entry.getValue();
      rpre.put(entry.getKey(), pre);
    }
    for (Entry<Long, Long> entry : lmap.entrySet()) {
      long lweight = entry.getKey();
      long lways = entry.getValue();
      Long floor = rpre.floorKey(bag - lweight);
      if (floor != null) {
        long rways = rpre.get(floor);
        ways += lways * rways;
      }
    }
    return ways + 1;
  }

  public static long process(int[] arr, int index, long w, int end, int bag, TreeMap<Long, Long> map) {
    if (w > bag) {
      return 0;
    }
    if (index > end) {
      if (w != 0) {
        if (!map.containsKey(w)) {
          map.put(w, 1L);
        } else {
          map.put(w, map.get(w) + 1);
        }
        return 1;
      } else {
        return 0;
      }
    } else {
      long ways = process(arr, index + 1, w, end, bag, map);
      ways += process(arr, index + 1, w + arr[index], end, bag, map);
      return ways;
    }
  }
}