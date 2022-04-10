package InterviewQuestions;

public class Code004_JumpGame {

  /* 字节跳动：
   * 公司有一个活泼的字节正在练习自己的跳动技巧。
   * 跳跃行为可以认为是在一个数轴上移动。练习开始时他处在数轴原点位置。
   * 然后他开始确定自己的n个跳跃目标点，x1—xn，他需要一次跳跃到这n个目标点上。
   * 为了练习跳跃，他每次从目标点出发时，都会先挑一个单位长度的距离，之后每次跳跃都比上一次跳跃愿一个单位长度。
   * 他每次跳跃时，都可以选择向左或向右。到达当前点的地后，跳跃距离便会重制。
   * 他想知道他依次前往的每个目标点，最少需要挑多少次才能到达。
   *
   * 问题：每次的向左向右没有限制，最终导致stack overflow。
   * 需要一个LIMIT？或者一个heuristic function来引导跳跃方向的选择？
   *  */
  public static final int LIMIT = 10;
  public static void jumps(int[] targets) {
    int curr = 0;
    int steps = 0;
    for (int t : targets) {
      steps += jumpTo(curr, t, 1, 0);
      System.out.println(steps);
      curr = t;
    }
  }

  private static int jumpTo(int curr, int target, int dis, int steps) {
    if (curr == target) {
      return steps;
    }
    if (curr > LIMIT || curr < -LIMIT) {
      return Integer.MAX_VALUE;
    }
    // 可以向右也可以向左
    int c1 = jumpTo(curr + dis, target, dis + 1, steps + 1);
    int c2 = jumpTo(curr - dis, target, dis + 1, steps + 1);
    return Math.min(c1, c2);
  }


  public static void main(String[] args) {
    int[] targets = new int[]{1, 0, 3};
    jumps(targets);
  }
}
