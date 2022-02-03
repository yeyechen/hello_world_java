package 左程云体系学习班;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Lecture14 {
  /*
   * 贪心算法的题解套路实战
   *
   * 1. 安排宣讲日程：一些项目要占用一个会议室宣讲，会议室不能同时容纳两个项目的宣讲。给定每个项目的开始和结束时间点，
   * 假设时间点不为负数，要求安排进行的宣讲次数最多，返回最多的宣讲次数。
   * 贪心策略：选结束时间最早的。按结束时间点排序，从小到大依次选择开始时间与之前结束时间不冲突的会议。
   *
   * 2. 项目最大收益(leetcode 502题)：给你n个项目。对于每个项目 i，它都有一个纯利润 profits[i]，和启动该项目需要的最小资本 capital[i]。
   * 最初，你的资本为w。当你完成一个项目时，你将获得纯利润，且利润将被添加到你的总资本中。总而言之，从给定项目中选择 最多 k 个不同项目的列表，
   * 以最大化最终资本，并输出最终可获得的最多资本。
   * 贪心策略：按照纯利润从大到小排序，每次从能够满足最小资本的所有项目中，选择最大利润的项目，直到做完k个项目。
   * */

  // 1. 安排宣讲日程
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

  // 2. 项目最大收益
  public static int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
    assert (profits.length == capital.length);

    // 将项目花费和绑定成array
    CapitalProfitsPair[] programs = new CapitalProfitsPair[profits.length];
    for (int i = 0; i < profits.length; i++) {
      programs[i] = new CapitalProfitsPair(capital[i], profits[i]);
    }

    // 纯利润大根堆
    PriorityQueue<CapitalProfitsPair> maxProfitQueue = new PriorityQueue<>(new CapitalProfitsPairComparator());
    for (CapitalProfitsPair program : programs) {

    }

    return 0;
  }

  public static class CapitalProfitsPair {

    int capital;
    int profits;

    public CapitalProfitsPair(int capital, int profits) {
      this.capital = capital;
      this.profits = profits;
    }
  }

  public static class CapitalProfitsPairComparator implements Comparator<CapitalProfitsPair> {
    @Override
    public int compare(CapitalProfitsPair o1, CapitalProfitsPair o2) {
      return o2.profits - o1.profits;
    }
  }
}
