package 左程云体系学习班;

public class Lecture28 {
 /*
  * Manacher算法！！
  *
  * 要解决的问题：找到一个字符串中的最长的回文子字符串的长度。(Palindrome).
  * 普通的暴力解决方法的时间复杂度为O(N^2)
  * Manacher算法可以做到O(N)
  *
  * 图示：
  *  ...[ i'            i ]...
  *     L        C        R
  *
  * C：当前R最右时回文串的中心  // 也就是说C和R是一对，在R往右更新时C才更新
  * R：以当前C为中心的回文串，能往右扩到的最大的距离
  * L：以当前C为中心的回文串，能往左扩到的最大的距离
  * i：当前字符
  * i'：当前字符以C为中心的对称字符
  *
  * 算法的核心思路：遍历每个字符，在每一次遍历到的时候记录下有用的关键信息，供后续遍历加速。记录每一个位置字符的回文半径。
  * 我个人理解的回文半径和左神的不一样:
  * 比如：#1#2#1# 我的理解是不加上中间的2，即半径=3
  * 左神是加上中间的2，半径为4。所以代码实现稍有不同。
  *
  * 分几种情况：
  * 1. 当前字符所在的i位置不在[L,R]范围内：
  *     ...[                 ]  i...  ---> ...[  i  ]
  *        L        C        R                L  C  R
  *     老老实实比较i+1和i-1的字符，相等就扩，C=i，R也更新。
  * 2. 当前字符所在的i位置在[L,R]范围内：
  *     2.1. i' 的最长回文串落在L外
  *         ...( [ i'  )            i ]...
  *              L          C         R
  *         那么i位置的最长回文半径就是R-i的这段距离。证明省略，可以通过分析R+1位置，L-1位置，R-2(R-i)位置，
  *         和L+(2i'-L)位置的相等及不等关系证明。
  *     2.2. i' 的最长回文串落在L内
  *         ... [( i')            ( i )]...
  *             L           C          R
  *         那么i位置的最长回文半径就是i'位置的回文半径，对称很容易可得。并且不用继续比较R+1的字符和(2i-R)-1可以被
  *         证明不相等
  *     2.3. i' 的最长回文串结尾正好落在L上
  *             ( i')              ( i )
  *         ... [                      ]...
  *             L           C          R
  *         需要继续比较R+1位置和(2i-R)位置的字符，但不用比较已知的回文半径了
  *
  * 具体详细的算法流程请回看左成云体系学习班28节
  * */

  public static int manacher(String s) {
    if (s.length() == 0) {
      return 0;
    }

    char[] S = manacherString(s); // 预先处理字符串，比如 s="abc" ---> S="#a#b#c#"，这样不用分奇偶情况
    int C = -1;
    int R = -1; // 这里的R是包括。L 可以用2C-R表示
    int[] pArr = new int[S.length]; // 记录每一个位置字符的回文半径

    int max = 0;

    for (int i = 0; i < S.length; i++) {
      // i 不在[L,R]范围内
      if (i > R) {
        int range = 1;
        while (i - range > -1 && i + range < S.length && S[i - range] == S[i + range]) {
          range++;
        }
        pArr[i] = range - 1;
      } else { // 当前字符所在的i位置在[L,R]范围内
        int L = 2 * C - R;
        int i1 = 2 * C - i;
        if (L > i1 - pArr[i1]) { //2.1 L > i' - 回文半径
          pArr[i] = R - i;
        } else if (i1 - pArr[i1] == L) { // 2.3 i' 的最长回文串结尾正好落在L上
          int range = pArr[i1];
          while (i - range > -1 && i + range < S.length && S[i - range] == S[i + range]) {
            range++;
          }
          pArr[i] = range - 1;
        } else { // 2.2
          pArr[i] = pArr[i1];
        }
      }
      // 结束后更新C和R
      if (i + pArr[i] > R) {
        R = i + pArr[i];
        C = i;
      }
      max = Math.max(max, pArr[i]); // 因为是处理过的字符串，返回半径就相当于返回了原字符的最大回文子串长度。
    }
    return max;
  }

  // 左神写的精简版本，并且R是半不包含，回文半径比我的理解多一个
  public static int manacherRight(String s) {
    if (s == null || s.length() == 0) {
      return 0;
    }
    char[] str = manacherString(s);
    int[] pArr = new int[str.length];
    int C = -1;
    int R = -1;
    int max = Integer.MIN_VALUE;
    for (int i = 0; i < str.length; i++) {

      // 这里回文半径的初始值就是1了
      pArr[i] = R > i ? Math.min(pArr[2 * C - i], R - i) : 1; // 这一行是2.1和2.2的精简表达

      // 无论是否需要检查都会进循环，大不了比对一次之后就跳到break。可以精简代码
      while (i + pArr[i] < str.length && i - pArr[i] > -1) {
        if (str[i + pArr[i]] == str[i - pArr[i]])
          pArr[i]++;
        else {
          break;
        }
      }
      if (i + pArr[i] > R) {
        R = i + pArr[i];
        C = i;
      }
      max = Math.max(max, pArr[i]);
    }
    return max - 1; // 左神的半径定义不一样，所以要-1
  }

  private static char[] manacherString(String str) {
    char[] charArr = str.toCharArray();
    char[] res = new char[str.length() * 2 + 1];
    int index = 0;
    for (int i = 0; i != res.length; i++) {
      res[i] = (i & 1) == 0 ? '#' : charArr[index++];
    }
    return res;
  }

  public static String getRandomString(int possibilities, int size) {
    char[] ans = new char[(int) (Math.random() * size) + 1];
    for (int i = 0; i < ans.length; i++) {
      ans[i] = (char) ((int) (Math.random() * possibilities) + 'a');
    }
    return String.valueOf(ans);
  }

  public static void main(String[] args) {
    int possibilities = 5;
    int strSize = 20;
    int testTimes = 10000;
    System.out.println("test begin");
    for (int i = 0; i < testTimes; i++) {
      String str = getRandomString(possibilities, strSize);
      if (manacher(str) != manacherRight(str)) {
        System.out.println("Oops!");
        break;
      }
    }
    System.out.println("test finish");
  }
}
