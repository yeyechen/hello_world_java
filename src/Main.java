import Util.Utility;
import java.util.Arrays;
import 左程云体系学习班.Lecture05;
import 左程云体系学习班.Lecture37;

public class Main {

  public static void main(String[] args) {
    int len = 200;
    int varible = 50;
    for (int i = 0; i < 10000; i++) {
      int[] test = Utility.randomArrayGenerator(len, varible, false);
      int lower = (int) (Math.random() * varible) - (int) (Math.random() * varible);
      int upper = lower + (int) (Math.random() * varible);
      int ans1 = Lecture05.countRangeSum(test, lower, upper);
      int ans2 = Lecture37.countRangeSum(test, lower, upper);
      if (ans1 != ans2) {
        System.out.println(Arrays.toString(test));
        System.out.println(lower);
        System.out.println(upper);
        System.out.println(ans1);
        System.out.println(ans2);
      }
    }
  }

}