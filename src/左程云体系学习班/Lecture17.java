package 左程云体系学习班;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
   * 2. 字符串子序列问题：比如"abc" -> ["a", "b", "c", "ab", "ac", "bc", "abc"]。如果要求结果无重复，在用set。
   *
   * 3. 字符串的全排列：比如"abc" -> ["abc", "acb", "bac", "bca", "cab", "cba"]。如果要求结果无重复，在交换前判断
   *
   * 4. 栈的逆序问题：给你一个栈, 请你逆序这个栈, 不能申请额外的数据结构, 只能使用递归函数。(其实就是用系统栈作为数据结构来实现)
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

  // 2. 字符串子序列问题
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

  // 3. 字符串的全排列
  public static List<String> permutation(String s) {
    List<String> ans = new ArrayList<>();
    if (s == null || s.length() == 0) {
      return ans;
    }
    permProcess(s.toCharArray(), 0, ans);
    return ans;
  }

  private static void permProcess(char[] s, int index, List<String> ans) {
    if (index == s.length) {
      ans.add(String.valueOf(s));
      return;
    }
    boolean[] visited = new boolean[256]; // 去重, ascii数值范围是0-255
    for (int i = index; i < s.length; i++) {
      if (!visited[s[i]]) { // 去重, 如果之间交换过相同的字符, 那么跳过这次交换(剪枝)
        visited[s[i]] = true;
        // index位置的字符依次跟后面的位置的字符交换
        swap(s, index, i);
        permProcess(s, index + 1, ans);
        // 恢复原始状态
        swap(s, index, i);
      }
    }
  }

  private static void swap(char[] s, int i, int j) {
    char temp = s[i];
    s[i] = s[j];
    s[j] = temp;
  }

  // 4. 栈的逆序问题
  public static void reverse(Stack<Integer> stack) {
    if (stack.empty()) {
      return;
    }
    int last = lastElem(stack);
    reverse(stack);
    stack.push(last);
  }

  // 返回并移除栈底的元素, 比如 [1, 2, 3] 左边为栈顶, 右边为栈底, 变为 [1, 2], 并返回3
  private static int lastElem(Stack<Integer> stack) {
    int curr = stack.pop();
    if (stack.empty()) {
      return curr;
    }
    int last = lastElem(stack);
    stack.push(curr);
    return last;
  }

  public static void main(String[] args) {
    Stack<Integer> stack = new Stack<>();
    stack.push(3);
    stack.push(2);
    stack.push(1);
    System.out.println(stack);
    reverse(stack);
    System.out.println(stack);
  }
}
