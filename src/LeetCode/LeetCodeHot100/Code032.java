package LeetCode.LeetCodeHot100;

import java.util.Stack;

public class Code032 {

  // 使用栈的方法
  public static int longestValidParentheses(String s) {
    int maxLength = 0;
    Stack<Integer> stack = new Stack<>();
    stack.push(-1);
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == '(') {
        stack.push(i);
      } else if (s.charAt(i) == ')') {
        stack.pop();
        if (stack.isEmpty()) {
          stack.push(i);
        } else {
          maxLength = Math.max(maxLength, i - stack.peek());
        }
      }
    }
    return maxLength;
  }

  // 用栈模拟一遍，建出一个array，将无法匹配的括号位置标为1
  // "()(()"的mark为[0, 0, 1, 0, 0]
  // ")()((())"的mark为[1, 0, 0, 1, 0, 0, 0, 0]
  // 最后寻找最长的连续的0的长度
  public static int longestValidParentheses2(String s) {
    Stack<Integer> stack = new Stack<>();
    int[] mark = new int[s.length()];
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == '(') {
        stack.push(i);
      } else {
        // 如果stack空了，那么当前index的')'是多余的，标记为1
        if (stack.isEmpty()) {
          mark[i] = 1;
        } else {
          stack.pop(); // pop出来的一定是 '('
        }
      }
    }
    // 未匹配的左括号也是多余的，标记为1
    while (!stack.isEmpty()) {
      mark[stack.pop()] = 1;
    }
    // 寻找最长的valid的0的长度
    int len = 0;
    int ans = 0;
    for (int i = 0; i < s.length(); i++) {
      if (mark[i] == 1) {
        len = 0;
        continue;
      }
      len++;
      ans = Math.max(ans, len);
    }
    return ans;
  }

  private static String randomParenthesesGenerator(int maxLength) {
    StringBuilder parentheses = new StringBuilder();
    int len = (int) (Math.random() * maxLength);
    while (len != 0) {
      if (Math.random() < 0.5) {
        parentheses.append('(');
      } else {
        parentheses.append(')');
      }
      len--;
    }
    return parentheses.toString();
  }

  public static void main(String[] args) {
    int cycles = 10000;
    int len = 10;
    System.out.println("Test Begin");
    for (int i = 0; i < cycles; i++) {
      String p = randomParenthesesGenerator(len);
      int ans1 = longestValidParentheses(p);
      int ans2 = longestValidParentheses2(p);
      if (ans1 != ans2) {
        System.out.println("test passed: " + i + "/" + cycles);
        System.out.println(p);
        System.out.println(ans1);
        System.out.println(ans2);
        System.out.println("Failed!");
        break;
      }
    }
    System.out.println("Test End");
  }
}
