package InterviewQuestions;

import java.util.Arrays;

public class Code003_PressingButtons {

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
