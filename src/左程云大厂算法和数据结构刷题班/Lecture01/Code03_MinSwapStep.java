package 左程云大厂算法和数据结构刷题班.Lecture01;

public class Code03_MinSwapStep {

  /*
  * 题目：一个数组中只有两种字符'G'和'B'，可以让所有的G都放在左侧，所有的B都放在右侧
  * 或者可以让所有的G都放在右侧，所有的B都放在左侧，但是只能在相邻字符之间进行交换操作，返回至少需要交换几次
  * */

  public static int minStep(String s) {
    if (s == null || s.equals("")) {
      return 0;
    }
    char[] str = s.toCharArray();
    // 所有G放左侧
    int step1 = 0;
    int gi = 0;
    for (int i = 0; i < str.length; i++) {
      if (str[i] == 'G') {
        step1 += i - gi;
        gi++;
      }
    }
    // 所有'B'放左侧
    int step2 = 0;
    int bi = 0;
    for (int i = 0; i < str.length; i++) {
      if (str[i] == 'B') {
        step2 += i - bi;
        bi++;
      }
    }
    return Math.min(step1, step2);
  }
}
