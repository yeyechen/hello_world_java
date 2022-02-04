package 左程云体系学习班;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Lecture13 {

  /*
   * 贪心算法：
   *    1. 最自然智慧的算法
   *    2. 用一种局部最功利的标准，总是做出在当前看来最好的选择
   *    3. 难点在于证明局部最功利的标准可以得到全局最优解
   *    4. 对于贪心算法的学习主要以增加阅历和经验为主
   *
   * 贪心算法普遍较为简单，主要是能否提出并证明正确的算法
   * 贪心算法不要纠结证明
   *
   * 1.1 最小字典序排列: 给定一个由字符串组成的数组，返回所有字符串拼接的可能性中，字典序最小的结果。
   * 贪心的第一次提出：把所有字符串用字典序比较，小的放前，大的放后 -> 错误，反例：["b", "ba"]，"b"的字典序比"ba"要小，
   * 那么组成的字符串为"bba"，是比另一种组合方式"bab"要大。
   * 修改：修改比较器，如果两个字符串str1, str2 -> if str1+str2 < str2+str1 then str1 放前 else str2放前。
   *
   * 1.2 安排宣讲日程：一些项目要占用一个会议室宣讲，会议室不能同时容纳两个项目的宣讲。给定每个项目的开始和结束时间点，
   * 假设时间点不为负数，要求安排进行的宣讲次数最多，返回最多的宣讲次数。
   * 贪心策略：选结束时间最早的。按结束时间点排序，从小到大依次选择开始时间与之前结束时间不冲突的会议。
   *
   * 1.3 项目最大收益(leetcode 502题)：给你n个项目。对于每个项目 i，它都有一个纯利润 profits[i]，和启动该项目需要的最小资本 capital[i]。
   * 最初，你的资本为w。当你完成一个项目时，你将获得纯利润，且利润将被添加到你的总资本中。总而言之，从给定项目中选择 最多 k 个不同项目的列表，
   * 以最大化最终资本，并输出最终可获得的最多资本。
   * 贪心策略：按照纯利润从大到小排序，每次从能够满足最小资本的所有项目中，选择最大利润的项目，直到做完k个项目。
   *
   * 1.4 点亮街区最少灯数: 给定一个字符串，只由'X'和'.' 两种字符组成。'X' 表示墙，不能放灯，也不强制需要点亮。'.' 表示街道，可以放灯，必须点亮。
   * 如果灯放在i位置, 可以让 i-1, i 和 i+1 三个位置都被点亮, 返回点亮所有街道至少需要多少盏灯。
   * 分类讨论, 小贪心就是灯放中间。
   *
   * */


  // 1.1 最小字典序排列
  public static String leastLexicographyOrderCombination(String[] strings) {
    if (strings == null || strings.length == 0) {
      return "";
    }
    Arrays.sort(strings, new LexicographyStringComparator());
    StringBuilder res = new StringBuilder();
    for (String string : strings) {
      res.append(string);
    }
    return res.toString();
  }

  public static class LexicographyStringComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
      return (o1 + o2).compareTo(o2 + o1);
    }
  }

  // 1.2 安排宣讲日程
  public static int maxArrangement(Meeting[] meetings) {
    Arrays.sort(meetings, new MeetingTimeComparator());
    int currEndTime = 0;
    int result = 0;
    for (Meeting meeting : meetings) {
      if (currEndTime <= meeting.startTime) {
        currEndTime = meeting.endTime;
        result++;
      }
    }
    return result;
  }

  public static class Meeting {
    int startTime;
    int endTime;

    public Meeting(int startTime, int endTime) {
      this.startTime = startTime;
      this.endTime = endTime;
    }
  }

  public static class MeetingTimeComparator implements Comparator<Meeting> {
    @Override
    public int compare(Meeting o1, Meeting o2) {
      //排序顺序按结束时间早的放前面
      return o1.endTime - o2.endTime;
    }
  }

  // 1.3 项目最大收益
  public static int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
    assert (profits.length == capital.length);

    // 花费小根堆
    PriorityQueue<CapitalProfitsPair> capitalAscendingQueue = new PriorityQueue<>(new CapitalAscendingComparator());

    // 将项目花费和绑定成array, 加入花费小根堆中
    for (int i = 0; i < profits.length; i++) {
      capitalAscendingQueue.add(new CapitalProfitsPair(capital[i], profits[i]));
    }

    // 选择项目
    PriorityQueue<CapitalProfitsPair> candidates = new PriorityQueue<>(new ProfitDescendingComparator());
    while (k != 0) {

      // 将所有能够启动的项目(现在的本金大于等于项目花费)放入纯利润大根堆
      while (!capitalAscendingQueue.isEmpty() && capitalAscendingQueue.peek().capital <= w) {
        candidates.add(capitalAscendingQueue.poll());
      }

      // 如果没有任何项目可以解锁了, 直接返回现有资金
      if (candidates.isEmpty()) {
        return w;
      }
      // 选择最优项目即大根堆堆顶的项目
      w += candidates.poll().profits;
      k--;
    }
    return w;
  }

  public static class CapitalProfitsPair {

    int capital;
    int profits;

    public CapitalProfitsPair(int capital, int profits) {
      this.capital = capital;
      this.profits = profits;
    }
  }

  public static class ProfitDescendingComparator implements Comparator<CapitalProfitsPair> {
    @Override
    public int compare(CapitalProfitsPair o1, CapitalProfitsPair o2) {
      return o2.profits - o1.profits;
    }
  }

  public static class CapitalAscendingComparator implements Comparator<CapitalProfitsPair> {
    @Override
    public int compare(CapitalProfitsPair o1, CapitalProfitsPair o2) {
      return o1.capital - o2.capital;
    }
  }

  // 1.4 点亮街区最少灯数
  public static int minLight(String street) {
    char[] str = street.toCharArray();
    int i = 0;
    int lightNum = 0;

    while (i < street.length()) {
      if (str[i] == 'X') {
        i++;
      } else {
        lightNum++;
        if (i + 1 == str.length) {
          break;
        } else {
          if (str[i + 1] == 'X') {
            i = i + 2;
          } else {
            i = i + 3;
          }
        }
      }
    }
    return lightNum;
  }
}
