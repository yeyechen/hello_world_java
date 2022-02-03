package Bilibili;

public class Code02 {

  /*一个数组中只有两个字符'G'和'B'，想让所有G放在左侧，所有B放在右侧，但是只能相邻字符之间进行交换操作。
  * 返回至少需要交换几次。*/

  public static int sort(String s) {
    if (s == null || s.equals("")) {
      return 0;
    }

    int gi = 0;
    int bi = 0;
    int index = 0;
    int sum1 = 0;
    int sum2 = 0;

    while (index < s.length()) {
      if (s.charAt(index) == 'G') {
        sum1 += index - (gi++);
      } else {
        sum2 += index - (bi++);
      }
      index++;
    }
    return Math.min(sum1, sum2);
  }

  public static void main(String[] args) {
    System.out.println(sort("BBGGBBGBG"));
  }
}
