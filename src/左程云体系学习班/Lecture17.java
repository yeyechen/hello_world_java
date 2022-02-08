package 左程云体系学习班;

import java.util.ArrayList;
import java.util.List;

public class Lecture17 {

  /*
   * 暴力递归：把问题转化为规模缩小了的同类问题的子问题；
   *         有明确的不需要继续进行递归的条件(base case)；
   *         有当得到了子问题的节后之后的决策过程；
   *         不记录每一个子问题的解。
   *
   * 1. 汉诺塔问题：n个圆盘，三个柱子分别为左，中，右。圆盘从小到大依次垒在左柱子上，怎么样把圆盘移动到右柱子上，并且
   * 同样从小到大。规定小圆盘上不能放大圆盘，并且只能移动柱子最顶端的圆盘。
   *
   * 2. 打印字符串子序列问题：比如"abc" -> ["a", "b", "c", "ab", "ac", "bc, "abc"]
   *
   * */

  // 1. 汉诺塔问题
  public static void hanoi(int n) {
    if (n > 0) {
      func(n, "left", "right", "mid");
    }
  }

  private static void func(int n, String from, String to, String other) {
    if (n == 1) {
      System.out.println("Move 1 from " + from + " to " + to);
    } else {
      func(n - 1, from, other, to);
      System.out.println("Move " + n + " from " + from + " to " + to);
      func(n - 1, other, to, from);
    }
  }

  // 2. 打印字符串子序列问题
  public static List<String> subs(String s) {
    char[] str = s.toCharArray();
    String path = "";
    List<String> ans = new ArrayList<>();
    process1(str, 0, ans, path);
    return ans;
  }

  // 关键在于每个index的字符可以要可以不要
  private static void process1(char[] str, int index, List<String> ans, String path) {
    if (index == str.length) {
      if (!path.equals("")) {
        ans.add(path);
      }
      return;
    }
    // 没有要index位置的字符
    process1(str, index + 1, ans, path);
    // 要index位置的字符
    process1(str, index + 1, ans, path + str[index]);
  }

  public static void main(String[] args) {
    List<String> result = subs("abc");
    System.out.println(result);
  }
}
