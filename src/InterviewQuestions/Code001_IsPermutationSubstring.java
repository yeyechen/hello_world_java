package InterviewQuestions;

import static 左程云体系学习班.Lecture17.permutation;

public class Code001_IsPermutationSubstring {

  // 来自字节飞书团队
  // 小歪每次都会给你两个字符串，笔记s1和笔记s2，请你写一个函数：
  // 判断s2的排列之一时候是s1的子串

  // 滑动窗口
  public static boolean isPermutationSubstring(String s1, String s2) {
    if (s1.length() < s2.length()) {
      return false;
    }
    // char ascii: 0 - 255
    int[] cnt = new int[256];
    for (char c : s2.toCharArray()) {
      cnt[c]++;
    }
    int N = s2.length();
    int left = 0;
    int right = 0;
    int all = N;
    // 窗口形式是左闭右开: [left, right), 初次形成窗口
    for (; right < N; right++) {
      if (--cnt[s1.charAt(right)] >= 0) {
        all--;
      }
    }
    // 滑动窗口
    while (right < s1.length()) {
      if (all == 0) {
        return true;
      }
      if (--cnt[s1.charAt(right)] >= 0) {
        all--;
      }
      if (++cnt[s1.charAt(left)] > 0) {
        all++;
      }
      left++;
      right++;
    }
    return all == 0;
  }

  // 暴力
  public static boolean isPermutationSubstringBrute(String s1, String s2) {
    if (s2.isEmpty()) {
      return true;
    }
    boolean result = false;
    for (String perm : permutation(s2)) {
      result |= s1.contains(perm);
    }
    return result;
  }

  public static void main(String[] args) {
    int cycles = 1000;
    int maxLen = 10;
    System.out.println("Test Begin");
    for (int i = 0; i < cycles; i++) {
      String s1 = randomStringGenerator(maxLen);
      String s2 = randomStringGenerator(maxLen);
      boolean ans1 = isPermutationSubstring(s1, s2);
      boolean ans2 = isPermutationSubstringBrute(s1, s2);
      if (ans1 != ans2) {
        System.out.println("Test Fail");
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(ans1);
        System.out.println(ans2);
        break;
      }
    }
    System.out.println("Test End");
  }

  // generate random strings with 26 lowercase letters
  // can also generate all valid char by changing the only line in the loop to:
  // result.append((char) ((int) (Math.random() * 256)));
  public static String randomStringGenerator(int maxLen) {
    int len = (int) (Math.random() * maxLen);
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < len; i++) {
      result.append((char) ('z' - (int) (Math.random() * 26)));
    }
    return result.toString();
  }
}
