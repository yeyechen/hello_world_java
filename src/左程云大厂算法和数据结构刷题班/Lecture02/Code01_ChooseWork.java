package 左程云大厂算法和数据结构刷题班.Lecture02;

import Util.Utility;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

public class Code01_ChooseWork {

  /*
   * 题目：给定数组level和money，长度都为N，level[i]表示i号工作的难度， money[i]表示i号工作的收入
   * 给定数组ability，长度都为M，ability[j]表示j号人的能力，每一号工作，都可以提供无数的岗位，难度和收入都一样
   * 但是人的能力必须>=这份工作的难度，才能上班。返回一个长度为M的数组ans，ans[j]表示j号人能获得的最好收入
   * */

  private static class Job {

    private int level;
    private int money;

    public Job(int level, int money) {
      this.level = level;
      this.money = money;
    }

  }

  public static class JobComparator implements Comparator<Job> {

    @Override
    public int compare(Job o1, Job o2) {
      return o1.level != o2.level ? (o1.level - o2.level) : (o2.money - o1.money);
    }
  }

  public static int[] getMoney(int[] level, int[] money, int[] ability) {
    // create self defined class object
    int N = level.length;
    Job[] jobs = new Job[N];
    for (int i = 0; i < N; i++) {
      jobs[i] = new Job(level[i], money[i]);
    }

    // sort using comparator (level, if same level, money) ascending
    Arrays.sort(jobs, new JobComparator());

    TreeMap<Integer, Integer> map = new TreeMap<>();
    map.put(jobs[0].level, jobs[0].money);
    Job pre = jobs[0];
    // level and money must be monotonically increasing, otherwise never choose high level but low money
    for (int i = 1; i < N; i++) {
      if (jobs[i].level != pre.level && jobs[i].money > pre.money) {
        pre = jobs[i];
        map.put(jobs[i].level, jobs[i].money);
      }
    }

    // generate result array
    map.put(Integer.MIN_VALUE, 0);
    int[] result = new int[N];
    for (int i = 0; i < N; i++) {
      Integer key = map.floorKey(ability[i]);
      result[i] = map.get(key);
    }
    return result;
  }

  public static int[] getMoneyBrute(int[] level, int[] money, int[] ability) {
    int N = ability.length;
    int[] result = new int[N];
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        if (ability[i] >= level[j]) {
          result[i] = Math.max(result[i], money[j]);
        }
      }
    }
    return result;
  }

  public static void main(String[] args) {
    int testCycles = 10000;
    int maxLen = 10;
    int maxVal = 50;

    System.out.println("test begin");
    for (int i = 0; i < testCycles; i++) {
      int[][] pair = Utility.randomArrayPairGenerator(maxLen, maxVal, true);
      int[] level = pair[0];
      int[] money = pair[1];
      int[] ability = Utility.randomArrayGenerator(maxLen, maxVal, true);
      if (ability.length != level.length) {
        continue;
      }
      int[] ans1 = getMoney(level, money, ability);
      int[] ans2 = getMoneyBrute(level, money, ability);
      if (!Arrays.equals(ans1, ans2)) {
        System.out.println("level:");
        System.out.println(Arrays.toString(level));
        System.out.println("money:");
        System.out.println(Arrays.toString(money));
        System.out.println("ability");
        System.out.println(Arrays.toString(ability));
        System.out.println("answers");
        System.out.println(Arrays.toString(ans1));
        System.out.println(Arrays.toString(ans2));
        System.out.println("error");
        break;
      }
    }
    System.out.println("test end");
  }
}
