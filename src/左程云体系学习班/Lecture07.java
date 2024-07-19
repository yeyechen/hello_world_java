package 左程云体系学习班;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Lecture07 {

  /* 1. 关于堆的题目：
   * 1.1 最大线段重合问题：给定多条线段，每条线段的表示方式为[start, end]，分别表示该线段的开始以及结束位置。线段的开始和
   * 结束位置都是整数，并且线段重合区域的长度必须 >=1 (两点重合不算线段重合)。求线段最多重合区域中，包含了几条线段。
   *
   * 1.2 LeetCode 452 用最少数量的箭引爆气球: 与1.1非常类似。
   * https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
   *
   * 2. 加强堆(增加反向索引表)
   * 系统提供的堆无法做到的事情：已经入堆的元素，如果参与排序的指标发生变化，系统提供的堆无法做到时间复杂度O(logN)，都是O(N)的调整；
   *                       系统提供的堆只能弹出堆顶，做不到自由删除任何一个堆中的元素，或者说没无法在时间复杂度O(logN)内完成。
   *
   * */

  // 1.1.1 O(N^2)方法
  public static int maxCover(int[][] lines) {
    //先求出所有线段中的最小值的点和最大值的点，那么所有点都在这个范围中
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    for (int[] line : lines) {
      min = Math.min(min, line[0]);
      max = Math.max(max, line[1]);
    }
    //再求出每个.5被多少个线段包含，就是有多少个线段重合，取其中最大值
    int maxCover = 0;
    double p = min + 0.5;
    while (p < max) {
      int currentCover = 0;
      for (int[] line : lines) {
        if (line[0] < p && p < line[1]) {
          currentCover++;
        }
      }
      maxCover = Math.max(maxCover, currentCover);
      p += 1;
    }
    return maxCover;
  }

  // 1.1.2 利用小根堆
  // 自定义Line class
  private static class Line {

    private final int start;
    private final int end;

    public Line(int start, int end) {
      this.start = start;
      this.end = end;
    }
  }

  // 这样就可以自定义比较器，从而让给定的线段按照一定顺序排列；这里按线段开始位置从小到大排序。
  private static class startAscendingOrder implements Comparator<Line> {

    @Override
    public int compare(Line o1, Line o2) {
      // prevent integer overflow
      if (o1.start < 0 && o2.start > 0) {
        return -1;
      }
      if (o1.start > 0 && o2.start < 0) {
        return 1;
      }
      return o1.start - o2.start;
    }
  }

  public static int maxCover2(int[][] lines) {
    //所有线段按照开始位置从小到大排序
    Line[] orderedLines = new Line[lines.length];
    for (int i = 0; i < orderedLines.length; i++) {
      orderedLines[i] = new Line(lines[i][0], lines[i][1]);
    }
    Arrays.sort(orderedLines, new startAscendingOrder());

    //创建一个小根堆，这个堆存的数值是当前线段之前所有线段的结束的位置，然后剔除结束位置比当前线段的开始位置小或等于的，因为
    //如果之前线段的结束位置小于或等于当前线段的开始位置，那么这些线段与当前线段不重叠，反之则重叠。
    PriorityQueue<Integer> heap = new PriorityQueue<>();
    int maxCover = 0;
    for (Line line : orderedLines) {
      while (!heap.isEmpty() && heap.peek() <= line.start) {
        heap.poll();
      }
      heap.add(line.end);
      maxCover = Math.max(maxCover, heap.size());
    }
    return maxCover;
  }

  private static int[][] generateLines(int N, int L, int R) {
    int size = (int) (Math.random() * N) + 1;
    int[][] ans = new int[size][2];
    for (int i = 0; i < size; i++) {
      int a = L + (int) (Math.random() * (R - L + 1));
      int b = L + (int) (Math.random() * (R - L + 1));
      if (a == b) {
        b = a + 1;
      }
      ans[i][0] = Math.min(a, b);
      ans[i][1] = Math.max(a, b);
    }
    return ans;
  }

  public static void main(String[] args) {
    int testTimes = 200000;
    int N = 100;
    int L = 0;
    int R = 200;
    System.out.println("test begin");
    for (int i = 0; i < testTimes; i++) {
      int[][] lines = generateLines(N, L, R);
      int ans1 = maxCover(lines);
      int ans2 = maxCover2(lines);
      if (ans1 != ans2) {
        System.out.println("Oops!");
        break;
      }
    }
    System.out.println("test end");
  }

  // 1.2 LeetCode 452 用最少数量的箭引爆气球
  public static int findMinArrowShots(int[][] lines) {
    Line[] orderedLines = new Line[lines.length];
    for (int i = 0; i < lines.length; i++) {
      orderedLines[i] = new Line(lines[i][0], lines[i][1]);
    }
    Arrays.sort(orderedLines, new startAscendingOrder());

    PriorityQueue<Integer> heap = new PriorityQueue<>();
    int numArrows = 0;
    for (Line line : orderedLines) {
      while (!heap.isEmpty() && heap.peek() < line.start) {
        heap.clear();
        numArrows++;
      }
      heap.add(line.end);
    }

    // all balloons left can be shot with one arrow
    return ++numArrows;
  }

  //2. 加强堆
  //只能允许非基础类型
  public static class HeapGreater<T> {

    private List<T> heap;
    //反向索引表，记录元素以及该元素在heap中的位置
    private Map<T, Integer> indexMap;
    private int heapSize;
    private Comparator<? super T> comparator;

    public HeapGreater(Comparator<? super T> comparator) {
      this.heap = new ArrayList<>();
      this.indexMap = new HashMap<>();
      this.heapSize = 0;
      this.comparator = comparator;
    }

    public boolean isEmpty() {
      return heapSize == 0;
    }

    public int size() {
      return heapSize;
    }

    public boolean contains(T obj) {
      return indexMap.containsKey(obj);
    }

    public T peek() {
      return heap.get(0);
    }

    public void push(T obj) {
      heap.add(obj);
      indexMap.put(obj, heapSize);
      heapInsert(heapSize++);
    }

    public T pop() {
      T ans = heap.get(0);
      swap(0, --heapSize);
      indexMap.remove(ans);
      heap.remove(heapSize);
      heapify(0);
      return ans;
    }

    //在加强堆中，remove的时间复杂度为O(logN)
    public void remove(T obj) {
      T replace = heap.get(--heapSize);
      int index = indexMap.get(obj);
      indexMap.remove(obj);
      heap.remove(heapSize);
      //边界检查：如果要删除的元素正好是heap尾部的元素，就不需要再在heap和indexMap中重新添加
      if (obj != replace) {
        heap.set(index, replace);
        indexMap.put(replace, index);
        resign(replace);
      }
    }

    public void resign(T obj) {
      heapInsert(indexMap.get(obj));
      heapify(indexMap.get(obj));
    }

    private void heapInsert(int index) {
      //parent node: (i-1)/2
      while (comparator.compare(heap.get(index), heap.get((index - 1) / 2)) < 0) {
        swap(index, (index - 1) / 2);
        index = (index - 1) / 2;
      }
    }

    private void heapify(int index) {
      int left = index * 2 + 1;
      while (left < heapSize) {
        int best =
            left + 1 < heapSize && comparator.compare(heap.get(left), heap.get(left + 1)) < 0 ?
                left + 1 : left;
        best = comparator.compare(heap.get(index), heap.get(best)) < 0 ? index : best;
        if (best == index) {
          break;
        }
        swap(best, index);
        index = best;
        left = index * 2 + 1;
      }
    }

    private void swap(int i, int j) {
      T o1 = heap.get(i);
      T o2 = heap.get(j);
      heap.set(j, o1);
      heap.set(i, o2);
      indexMap.put(o1, j);
      indexMap.put(o2, i);
    }
  }
}
