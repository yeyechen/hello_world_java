package LeetCodeHot100;

public class Code010 {

  // s = "aaabb", p = "a*b*"
  public static boolean isMatch(String s, String p) {
    if (s == null || p == null) {
      return false;
    }
    Boolean[][] dp = new Boolean[s.length()+1][p.length()+1];
    return matchProcess(s, 0, p, 0, dp);
  }

  // 给定s和p和他们的现在的index，如果从现在index出发可以匹配则返回true，不能则返回false
  // 加入了缓存
  private static boolean matchProcess(String s, int sIndex, String p, int pIndex,
      Boolean[][] dp) {
    if (dp[sIndex][pIndex] != null) {
      return dp[sIndex][pIndex];
    }
    if (sIndex == s.length() && pIndex == p.length()) {
      dp[sIndex][pIndex] = true;
      return true;
    }
    if (pIndex == p.length()) {
      dp[sIndex][pIndex] = false;
      return false;
    }
    if (sIndex == s.length()) {
      //检查后续是否都可以为0个字符
      if (p.charAt(pIndex) == '*') {
        pIndex++;
      }
      while (pIndex < p.length()) {
        if (pIndex + 1 >= p.length()) {
          dp[sIndex][pIndex] = false;
          return false;
        }
        if (!(Character.isLetter(p.charAt(pIndex)) || p.charAt(pIndex) == '.') || p.charAt(pIndex + 1) != '*') {
          dp[sIndex][pIndex] = false;
          return false;
        }
        pIndex+=2;
      }
      dp[sIndex][pIndex] = true;
      return true;
    }

    char currS = s.charAt(sIndex);
    char currP = p.charAt(pIndex);

    if (Character.isLetter(currP)) {
      if (currS == currP) {
        // 可配对也可不配对
        boolean result = matchProcess(s, sIndex + 1, p, pIndex + 1, dp) || (pIndex + 1 < p.length()
            && p.charAt(pIndex + 1) == '*' && matchProcess(s, sIndex, p, pIndex + 2, dp));
        dp[sIndex][pIndex] = result;
        return result;
      } else {
        if (pIndex + 1 < p.length() && p.charAt(pIndex + 1) == '*') {
          boolean result = matchProcess(s, sIndex, p, pIndex + 2, dp);
          dp[sIndex][pIndex] = result;
          return result; // 当前字符表达式冲突，唯一情况就是当前*出现0次
        } else {
          dp[sIndex][pIndex] = false;
          return false;
        }
      }
    } else if (currP == '*') {
      boolean result = matchProcess(s, sIndex, p, pIndex + 1, dp) // 当前*出现0次的情况
          || ((currS == p.charAt(pIndex - 1) || p.charAt(pIndex - 1) == '.') // 当前*出现1次或以上的情况
          && (matchProcess(s, sIndex + 1, p, pIndex, dp) || matchProcess(s, sIndex + 1, p,
          // 当前*结束了或没结束
          pIndex + 1, dp)));
      dp[sIndex][pIndex] = result;
      return result;
    } else { // currP == '.'
      // 可配对也可不配对
      boolean result = matchProcess(s, sIndex + 1, p, pIndex + 1, dp) || (pIndex + 1 < p.length()
          && p.charAt(pIndex + 1) == '*' && matchProcess(s, sIndex, p, pIndex + 2, dp));
      dp[sIndex][pIndex] = result;
      return result;
    }
  }

  public static void main(String[] args) {
    String s = "a";
    String p = ".*";
    System.out.println(isMatch(s, p));
    // System.out.println(s.matches(p));
  }
}
