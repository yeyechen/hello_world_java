package 左程云体系学习班.Lecture38;

import Util.Utility;

public class Code04_MonsterMoneyProblem {

  /*
   * 根据数据规模猜解法技巧
   * 1）C/C++，1秒处理的指令条数为10的8次方
   * 2）Java等语言，1~4秒处理的指令条数为10的8次方
   * 比如Java，要求时间为2~3秒完成算法，并且数据量为10的6次方。那么一个O(N^2)的算法肯定过不了，至少要O(N*logN)，
   * 最好O(N)拿下。
   *
   * 问题描述：
   * int[] d，d[i]：i号怪兽的能力
   * int[] p，p[i]：i号怪兽要求的钱
   * 开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的怪兽。
   * 如果你当前的能力，小于i号怪兽的能力，你必须付出p[i]的钱，贿赂这个怪兽，然后怪兽就会加入你
   * 他的能力直接累加到你的能力上；如果你当前的能力，大于等于i号怪兽的能力
   * 你可以选择直接通过，你的能力并不会下降，你也可以选择贿赂这个怪兽，然后怪兽就会加入你
   * 他的能力直接累加到你的能力上
   * 返回通过所有的怪兽，需要花的最小钱数
   *
   * */

  // 第一种方法是直观的方法
  public static long minMoney1(int[] d, int[] p) {
    return process1(d, p, 0, 0);
  }

  private static long process1(int[] d, int[] p, int ability, int index) {
    if (index == d.length) {
      return 0;
    }
    // 两种选项：花钱贿赂或直接通过

    // 情况1：没办法，必须花钱贿赂
    if (ability < d[index]) {
      return p[index] + process1(d, p, ability + d[index], index + 1);
    }
    // 情况2：直接通过，和花钱贿赂中选最小值
    return Math.min(process1(d, p, ability, index + 1),
        p[index] + process1(d, p, ability + d[index], index + 1));
  }

  public static long minMoney2(int[] d, int[] p) {
    int allMoney = 0;
    for (int i = 0; i < p.length; i++) {
      allMoney += p[i];
    }
    int N = d.length;
    for (int money = 0; money < allMoney; money++) { // 找到花的钱最少时，能力值不为-1的钱数（可以通过）
      if (process2(d, p, money, N - 1) != -1) {
        return money;
      }
    }
    return allMoney;
  }

  // 方法二：以钱作为参数。
  // 从0~index号怪兽，花的钱，必须严格等于money
  // 如果不能通过，返回-1
  // 如果可以通过，返回能通过情况下的最大能力值
  private static long process2(int[] d, int[] p, int money, int index) {
    if (index == -1) {
      return money == 0 ? 0 : -1;
    }
    // 选项1：不贿赂当前怪兽
    long preMaxAbility = process2(d, p, money, index - 1);
    long p1 = -1;
    if (preMaxAbility != -1 && preMaxAbility >= d[index]) {
      p1 = preMaxAbility;
    }

    // 选项2：贿赂当前怪兽
    long preMaxAbility2 = process2(d, p, money - p[index],
        index - 1); // 如果想贿赂当前怪兽，之前花的钱必须正好等于现在的钱减去p[index]，这样才能严格等于money
    long p2 = -1;
    if (preMaxAbility2 != -1) {
      p2 = d[index] + preMaxAbility2;
    }
    return Math.max(p1, p2);
  }

  // 两种方法都可以被改写成动态规划，唯一的区别就是方法一是以ability作为参数，方法二是以money作为参数。
  // 当ability的数据大小过大时，选方法二
  // 当money的数据大小过大时，选方法一
  // 灵活选择，是 根据数据规模猜解法技巧 的目的。
  public static void main(String[] args) {
    int len = 10;
    int value = 20;
    int testTimes = 10000;
    for (int i = 0; i < testTimes; i++) {
      int[] d = Utility.randomArrayGenerator(10, 20, true);
      int[] p = Utility.randomArrayGenerator(10, 20, true);
      if (d.length != p.length) {
        break;
      }
      long ans1 = minMoney1(d, p);
      long ans2 = minMoney2(d, p);
      if (ans1 != ans2) {
        System.out.println("oops!");
      }
    }

  }
}
