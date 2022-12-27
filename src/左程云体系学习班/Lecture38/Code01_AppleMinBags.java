package 左程云体系学习班.Lecture38;

public class Code01_AppleMinBags {
  /*
   * 讲解根据数据量猜解法的技巧
   * 比如某个面试题，输入参数类型简单，并且只有一个实际参数；
   * 要求的返回值类型也简单，并且只有一个；
   * 那么我们可以用暴力方法，把输入参数对应的返回值打印出来，找规律/优化。
   * 如果一开始就寻找规律或者数学公式，会花费大量时间。
   *
   * 小虎去买苹果，商店只提供两种类型的塑料袋，每种类型都有任意数量
   * 1）能装下6个苹果的袋子
   * 2）能装下8个苹果的袋子
   * 小虎可以自由使用两种袋子来装苹果，但是小虎有强迫症，他要求自己使用的袋子数量必须最少，
   * 且使用的每个袋子必须装满，给定一个正整数N，返回至少使用多少袋子。如果N无法让使用的每个袋子必须装满，返回-1
   *
   *
   * */

  public static int minBags(int appleNum) {
    if (appleNum < 0) {
      return -1;
    }
    int bigBag = appleNum / 8;
    int rest;
    while (bigBag >= 0) {
       rest = appleNum - bigBag * 8;
      if (rest % 6 == 0) {
        return bigBag + rest / 6;
      }
      bigBag--;
    }
    return -1;
  }

  public static void main(String[] args) {
    // 我们发现从3个袋子开始，都是8个为1组，奇数返回-1，偶数返回袋数
    for (int i = 1; i < 200; i++) {
      System.out.print(minBags(i)+" ");
      System.out.print(minBagsOptimise(i) + " ");
      System.out.println();
    }
  }

  public static int minBagsOptimise(int apples) {
    // 奇数返回-1
    if ((apples ^ 1) != 0) {
      return -1;
    }
    if (apples <= 17) {
      return apples == 0 ? 0
          : apples == 6 || apples == 8 ? 1 : apples == 12 || apples == 14 || apples == 16 ? 2 : -1;
    }
    return (apples - 18) / 8 + 3;
  }
}
