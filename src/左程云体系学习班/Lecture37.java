package 左程云体系学习班;

import java.util.HashSet;

public class Lecture37 {
  /*
   * 基于有序表的题目
   * (题目二，三完整答案略，https://github.com/algorithmzuo/algorithmbasic2020/tree/master/src/class37)
   * 1. 题目一：给定一个数组arr，和两个整数a和b（a<=b）。求arr中有多少个子数组，累加和在[a,b]这个范围上。
   * 返回达标的子数组数量
   *
   * 2. 题目二：
   * 有一个滑动窗口：
   * 1）L是滑动窗口最左位置、R是滑动窗口最右位置，一开始LR都在数组左侧
   * 2）任何一步都可能R往右动，表示某个数进了窗口
   * 3）任何一步都可能L往右动，表示某个数出了窗口
   * 想知道每一个窗口状态的中位数
   *
   * 3. 题目三：设计一个结构包含如下两个方法：
   * void add(int index, int num)：把num加入到index位置
   * int get(int index) ：取出index位置的值
   * void remove(int index) ：把index位置上的值删除
   * 要求三个方法时间复杂度O(logN)
   *
   * */

  // 1. 题目一
  // 在第5节课的时候讲过一种方法，通过改merge sort得出一个O(logN)的方法。用有序表更好改。
  // 同样的，求所有i位置上结尾的子数组中，有多少个在[lower, upper]范围上，等同于求i之前的所有
  // 前缀和中有多少前缀和在[x-upper, x-lower]范围上。
  public static int countRangeSum(int[] nums, int lower, int upper) {
    SBTreeSet treeSet = new SBTreeSet();

    long sum = 0; // 记录前缀和
    int ans = 0;
    treeSet.add(0); // 一个数都没有的时候，就已经有一个前缀和累加和为0
    for (int i = 0; i < nums.length; i++) {
      sum += nums[i];
      // 求现在的i结尾的子数组中，有多少前缀和在[sum-upper, sum-lower]范围上
      long a = treeSet.sizeLessThan(sum - lower + 1);
      long b = treeSet.sizeLessThan(sum - upper);
      ans += a - b;
      treeSet.add(sum);
    }
    return ans;
  }

  private static class SBTNode {

    public long key;
    public SBTNode l;
    public SBTNode r;
    public long treeSize; // size of keys that are different
    public long allElemSize; // key size that are the different or the same,
    // because we allow duplication

    public SBTNode(long key) {
      this.key = key;
      this.treeSize = 1;
      this.allElemSize = 1;
    }
  }

  private static class SBTreeSet {

    private SBTNode root;
    private HashSet<Long> set = new HashSet<>(); // for storing duplicates?

    private SBTNode rightRotate(SBTNode cur) {
      long curDuplicateNum =
          cur.allElemSize - (cur.l == null ? 0 : cur.l.allElemSize) - (cur.r == null ? 0
              : cur.r.allElemSize);
      SBTNode left = cur.l;
      cur.l = left.r;
      left.r = cur;
      // adjust size
      left.treeSize = cur.treeSize;
      cur.treeSize =
          (cur.l == null ? 0 : cur.l.treeSize) + (cur.r == null ? 0 : cur.r.treeSize) + 1;
      left.allElemSize = cur.allElemSize;
      cur.allElemSize =
          (cur.l == null ? 0 : cur.l.allElemSize) + (cur.r == null ? 0 : cur.r.allElemSize)
              + curDuplicateNum;
      return left;
    }

    private SBTNode leftRotate(SBTNode cur) {
      long curDuplicateNum =
          cur.allElemSize - (cur.l == null ? 0 : cur.l.allElemSize) - (cur.r == null ? 0
              : cur.r.allElemSize);
      SBTNode right = cur.r;
      cur.r = right.l;
      right.l = cur;
      // adjust size
      right.treeSize = cur.treeSize;
      cur.treeSize =
          (cur.l == null ? 0 : cur.l.treeSize) + (cur.r == null ? 0 : cur.r.treeSize) + 1;
      right.allElemSize = cur.allElemSize;
      cur.allElemSize =
          (cur.l == null ? 0 : cur.l.allElemSize) + (cur.r == null ? 0 : cur.r.allElemSize)
              + curDuplicateNum;
      return right;
    }

    private SBTNode maintain(SBTNode cur) {
      if (cur == null) {
        return null;
      }
      long leftSize = cur.l == null ? 0 : cur.l.treeSize;
      long leftLeftSize = (cur.l == null || cur.l.l == null) ? 0 : cur.l.l.treeSize;
      long leftRightSize = (cur.l == null || cur.l.r == null) ? 0 : cur.l.r.treeSize;

      long rightSize = cur.r == null ? 0 : cur.r.treeSize;
      long rightLeftSize = (cur.r == null || cur.r.l == null) ? 0 : cur.r.l.treeSize;
      long rightRightSize = (cur.r == null || cur.r.r == null) ? 0 : cur.r.r.treeSize;

      if (leftLeftSize > rightSize) { // LL
        cur = rightRotate(cur);
        cur.r = maintain(cur.r);
        cur = maintain(cur);
      } else if (leftRightSize > rightSize) { // LR
        cur.l = leftRotate(cur.l);
        cur = rightRotate(cur);
        cur.l = maintain(cur.l);
        cur.r = maintain(cur.r);
        cur = maintain(cur);
      } else if (rightRightSize > leftSize) { // RR
        cur = leftRotate(cur);
        cur.l = maintain(cur.l);
        cur = maintain(cur);
      } else if (rightLeftSize > leftSize) { // RL
        cur.r = rightRotate(cur.r);
        cur = leftRotate(cur);
        cur.l = maintain(cur.l);
        cur.r = maintain(cur.r);
        cur = maintain(cur);
      }
      return cur;
    }

    public void add(long sum) {
      boolean contains = set.contains(sum);
      root = add(root, sum, contains);
      set.add(sum);
    }

    private SBTNode add(SBTNode cur, long key, boolean contains) {
      if (cur == null) {
        return new SBTNode(key);
      } else {
        cur.allElemSize++;
        if (key == cur.key) {
          return cur;
        } else {
          cur.treeSize += contains ? 0 : 1;
          if (key < cur.key) {
            cur.l = add(cur.l, key, contains);
          } else {
            cur.r = add(cur.r, key, contains);
          }
          return maintain(cur);
        }
      }
    }

    // cur往左走不加结果，往右走加结果
    public long sizeLessThan(long key) {
      SBTNode cur = root;
      long ans = 0;
      while (cur != null) {
        if (key == cur.key) {
          return ans + (cur.l != null ? cur.l.allElemSize : 0);
        } else if (key < cur.key) {
          cur = cur.l;
        } else {
          ans += cur.allElemSize - (cur.r == null ? 0 : cur.r.allElemSize);
          cur = cur.r;
        }
      }
      return ans;
    }
  }
}
