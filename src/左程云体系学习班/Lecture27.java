package 左程云体系学习班;

public class Lecture27 {

 /*
  * KMP算法！！
  * 给定两个字符串s1和s2，分别长度为N和M，S1中是否存在连续的子字符串与s2相等？如果存在，返回s1中的子字符串开头位置；如果
  * 不存在，返回-1。N >= M。
  *
  * 例：
  * s1 = "abcdabca", s2 = "abca"
  * 返回4
  *
  * 如果用暴力解法，在s1中一个一个位置的字符与s2开头字符比对，匹配成功后继续匹配第二个字符，匹配失败继续往前尝试。】
  * 以此类推。这种算法最差情况下的时间复杂度是O(N^2) (比如说极端例子：s1 = "aaaaaaaaab", s2 = "aaaaaab")
  *
  * KMP算法可以用O(N)的时间复杂度来解决这个问题。
  *
  * 首先我们要理解一个辅助工具概念：next数组。该数组代表的含义是：在每一个i位置都与s1中的i位置对应，在s1的i位置之前，
  * 前缀子字符和后缀子字符相等的情况下，前后缀字符的最大值。什么意思呢，举个例子。
  *
  * s1 = "abcabck"，让我们来看看i=6位置上，也就是k字符的位置上的next数是什么含义。
  *
  * 举一个具体例子：
  * s1 = "a b a a b a k"
  * 前缀子字符：a  ab  aba  abaa  abaab
  * 后缀子字符：a  ba  aba  aabc  baaba
  * 是否相等：  T  F    T    F     F
  *
  * 那么此时next[6] = 3 (length("abc"))
  * 注意前后缀子字符的长度不能等于i，即前后缀子字符不能等于"abcabc"，否则肯定是最长且相等，没有意义。
  * 现在人为规定next数组中，第一个位置的值是-1，因为它前面没有任何字符。第二个位置的值是0，因为前面只有一个字符，
  * 不存在合理的前缀和后缀字符，也就不存在前后缀字符相等。
  *
  * 在上述s1例子中，next = [-1, 0, 0, 1, 1, 2, 3]
  *
  * 有了这样的一个next数组，就可以用O(N)来解决这个问题了。那么同时，获得next数组的方法也是O(N)。
  *
  * 算法实现流程和next数组实现流程的解释不好仅用文字叙述，请回看左成云体系学习班第27节。
  * KMP算法属于高阶算法入门，非常重要。
  *
  *  */

  public static int getIndexOf(String s1, String s2) {
    if (s1 == null || s2 == null || s2.length() < 1 || s1.length() < s2.length()) {
      return -1;
    }
    char[] str1 = s1.toCharArray();
    char[] str2 = s2.toCharArray();
    int x = 0;
    int y = 0;
    // O(M) m <= n
    int[] next = getNextArray(str2);
    // O(N)
    while (x < str1.length && y < str2.length) {
      if (str1[x] == str2[y]) {
        x++;
        y++;
      } else if (next[y] == -1) { // y == 0
        x++;
      } else {
        y = next[y];
      }
    }
    return y == str2.length ? x - y : -1;
  }

  public static int[] getNextArray(char[] str2) {
    if (str2.length == 1) {
      return new int[] { -1 };
    }
    int[] next = new int[str2.length];
    next[0] = -1;
    next[1] = 0;
    int i = 2; // 目前在哪个位置上求next数组的值
    int cn = 0; // 当前是哪个位置的值再和i-1位置的字符比较
    while (i < next.length) {
      if (str2[i - 1] == str2[cn]) { // 配成功的时候
        next[i++] = ++cn;
      } else if (cn > 0) {
        cn = next[cn];
      } else {
        next[i++] = 0;
      }
    }
    return next;
  }
}
