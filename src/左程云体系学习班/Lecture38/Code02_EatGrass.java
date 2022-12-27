package 左程云体系学习班.Lecture38;

public class Code02_EatGrass {

  /*
   * 给定一个正整数N，表示有N份青草统一堆放在仓库里，有一只牛和一只羊，牛先吃，羊后吃，它俩轮流吃草
   * 不管是牛还是羊，每一轮能吃的草量必须是：1，4，16，64…(4的某次方)
   * 谁最先把草吃完，谁获胜，假设牛和羊都绝顶聪明，都想赢，都会做出理性的决定。根据唯一的参数N，返回谁会赢
   * */

  public static String win(int n) {
    if (n <= 4) {
      return n == 0 || n == 2 ? "后手" : "先手";
    }
    int eat = 1;
    while (eat <= n) {
      // 在下一回合中是后手
      if (win(n - eat).equals("后手")) {
        return "先手";
      }
      eat *= 4;
    }
    return "后手";
  }

  public static void main(String[] args) {
    // 规律：后->先->后->先->先->...
    for (int i = 0; i < 30; i++) {
      System.out.print(win(i) + " ");
      System.out.print(winOptimise(i) + " ");
      System.out.println();
    }
  }

  public static String winOptimise(int n) {
    if (n % 5 == 0 || n % 5 == 2) {
      return "后手";
    }
    return "先手";
  }
}
