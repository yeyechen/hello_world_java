package InterviewQuestions;

import java.util.Arrays;

public class Code003_PressingButtons {

  /* 字节跳动
   * 小红拿到了一个仅有小写字母构成的字母串。她有一个按钮可以生成字母，按1下得到'a'，按2下得到'b'，按3下得到'c'，
   * 以此类推。
   * 小红可以选择字符串的某些字符，通过按按钮生成该字符对应的字母获得得分。每个字符可以获得1分，但每个字符最多选择一次。
   * 小红最多可按k次按钮。她想知道最多可以获得多少分？
   * */
  public static int score(String s, int k) {
    int[] count = new int[26];
    int score = 0;
    char[] chars = s.toCharArray();
    Arrays.sort(chars);
    // sort the target string with greedy strategy: score from lower alphabetic order
    for (char c : chars) {
      if (c - 'a' + 1 <= k) {
        k -= (c - 'a' + 1);
        score += 1;
      }
    }
    return score;
  }

  public static void main(String[] args) {
    System.out.println(score("edcda", 11));
  }
}
