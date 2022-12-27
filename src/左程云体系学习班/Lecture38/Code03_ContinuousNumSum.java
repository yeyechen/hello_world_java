package 左程云体系学习班.Lecture38;

public class Code03_ContinuousNumSum {

  /*
   *
   * 定义一种数：可以表示成若干（数量>1）连续正数和的数
   * 比如，5=2+3，5就是这样的数；12=3+4+5，12就是这样的数
   * 2=1+1，2不是这样的数，因为等号右边不是连续正数
   * 给定一个参数N，返回是不是可以表示成若干连续正数和的数
   *
   * */

  public static boolean isContinuousNumSum(int n) {
    int sum;
    for (int i = 1; i < n; i++) {
      sum = 0;
      int acc = i;
      while (sum < n) {
        sum += acc;
        acc++;
      }
      if (sum == n) {
        return true;
      }
    }
    return false;
  }

  public static void main(String[] args) {
    //规律：1*ture, false, 3*true, false, 7*true, false, 15*true, false, 31*ture, false,...
    // false: 1, 2, 4, 8, 16, 32...
    for (int i = 1; i < 100; i++) {
      System.out.print(Integer.toString(i) + isContinuousNumSum(i));
      System.out.print(optimise(i));
      System.out.println();
    }
  }

  public static boolean optimise(int n) {
    // 如果二进制形式只有一个1，则为2的某次方
    // 这里要取反
    return (n & (~n + 1)) != n;
    // 2的某次方另外的写法：
    // return n == (n & -n)
    // return n & (n - 1) == 0
  }
}
