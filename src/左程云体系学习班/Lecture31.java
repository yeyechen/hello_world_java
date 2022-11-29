package 左程云体系学习班;

public class Lecture31 {
  /*
   * 线段树！
   * 通常，在一个数组中范围上的方法，比如 3<=index<=500 的所有数加上5，或者update变成一个特定值，又或者query特定
   * index范围上的累加和，都需要O(N)时间复杂度(遍历)。
   * 我们想实现一个结构，在一棵树上表示不同位置范围上的累加和，从而加速上述方法的时间复杂度 -> O(logN)。
   * 例子：
   * (第0个index不使用)
   * index = [1, 2, 3, 4, 5, 6, 7, 8]
   * 线段树:               [1-8]
   *            [1-4]                [5-8]
   *      [1-2]     [3-4]       [5-6]     [7-8]
   *    [1-1][2-2] [3-3][4-4] [5-5][6-6] [7-7][8-8]
   *
   * 其中[i-j]表示对应index的累加和。
   *
   * 线段树不一定是树的结构，可以通过数组来实现(和堆类似)。
   * 规则是：假设当前节点为i，父节点为i/2，左子节点为2i，右子节点为2i+1。这也是不使用index = 0的原因。
   * 上述例子：
   * [[1-8],[1-4],[5-8],[1-2],[3-4],[5-6],[7-8],[1-1],[2-2],[3-3][4-4],[5-5],[6-6],[7-7],[8-8]]
   * 并且准备一个长度为4N的数组足以(证明省略)。
   *
   * 有了这样的概念，我们就可以分别实现add(),update(),query()方法。
   *
   * 应用：掉落的方块 -> leetcode 699
   * */

  public static class SegmentTree {

    private int MAXN;
    private int[] arr; // arr[]为原序列的信息从0开始，但在arr里是从1开始的
    private int[] sum; // sum[]模拟线段树维护区间和
    private int[] lazy; // lazy[]为累加和懒惰标记
    private int[] change; // change[]为更新的值
    private boolean[] update; // update[]为更新慵懒标记(因为change中的值不明确是否需要update，比如change中
    // 有一个index的值为0，我们不知道是update的值是0还是说不需要update)

    public SegmentTree(int[] original) {
      this.MAXN = original.length + 1; // index = 0 不使用
      arr = new int[MAXN]; // arr只是一个original的复制，往右移一个index
      for (int i = 1; i < MAXN; i++) {
        arr[i] = original[i - 1];
      }

      // 长度为4N就足够了, MAXN << 2 == MAXN * 4
      this.sum = new int[MAXN << 2];
      this.lazy = new int[MAXN << 2];
      this.change = new int[MAXN << 2];
      this.update = new boolean[MAXN << 2];
    }

    // 向上整合信息，rt表示在数组实现的树中的index
    private void pushUp(int rt) {
      // equivalent to:
      // sum[rt] = sum[2 * rt] + sum[2 * rt + 1];
      sum[rt] = sum[rt << 1] + sum[rt << 1 | 1]; // 当前root的累加和为左右子树的和；左子树2i，右子树2i+1
    }

    // 之前的所有懒增加和懒更新，从父范围发给左右两个子范围
    // ln表示左子树元素结点个数，rn表示右子树结点个数
    // 必须要先检查懒更新再检查懒累加(lazy)，因为只要rt还有懒累加，就说明更新发生在前，累加发生在后。
    private void pushDown(int rt, int ln, int rn) {
      if (update[rt]) {
        // 向下分发给左树和右树懒更新标记
        update[rt << 1] = true;
        update[rt << 1 | 1] = true;
        change[rt << 1] = change[rt];
        change[rt << 1 | 1] = change[rt];
        // 向下清空懒累加
        lazy[rt << 1] = 0;
        lazy[rt << 1 | 1] = 0;
        // 向下累加
        sum[rt << 1] = change[rt] * ln;
        sum[rt << 1 | 1] = change[rt] * rn;
        // 完成分发
        update[rt] = false;
      }
      if (lazy[rt] != 0) {
        // 向下分发懒累加
        lazy[rt << 1] += lazy[rt];
        lazy[rt << 1 | 1] += lazy[rt];
        // 向下累加
        sum[rt << 1] += lazy[rt] * ln;
        sum[rt << 1 | 1] += lazy[rt] * rn;
        // 完成累加
        lazy[rt] = 0;
      }
    }

    // 在初始化阶段，先把sum数组填好
    // 在arr[l~r]范围上，去build，1~N，
    // rt : 这个范围在sum中的下标
    public void build(int l, int r, int rt) {
      // 叶节点情况：
      if (l == r) {
        sum[rt] = arr[l];
        return;
      }
      int mid = (l + r) >> 1;
      build(l, mid, rt << 1);
      build(mid + 1, r, rt << 1 | 1);
      pushUp(rt);
    }

    // L~R  所有的值加上C -> 任务
    // 当前位置为rt，累加和l~r
    public void add(int L, int R, int C, int l, int r, int rt) {
      // 如果任务的范围把当前l~r范围全包了，就不用继续下放任务
      if (L <= l && r <= R) {
        sum[rt] += C * (r - l + 1);
        lazy[rt] += C;
        return;
      }
      // 无法懒累加，必须往下发
      int mid = (l + r) >> 1;
      pushDown(rt, mid - l + 1, r - mid);
      if (L <= mid) {
        add(L, R, C, l, mid, rt << 1); // 分配给左子树 2i
      }
      if (R > mid) {
        add(L, R, C, mid + 1, r, rt << 1 | 1);// 分配给右子树 2i+1
      }
      pushUp(rt); // 最后向上整合信息
    }


    // L~R  所有的值变成C -> 任务
    // 当前位置为rt，累加和l~r
    public void update(int L, int R, int C, int l, int r, int rt) {
      // 如果任务的范围把当前l~r范围全包了，就不用继续下放任务
      if (L <= l && r <= R) {
        change[rt] = C;
        update[rt] = true;
        sum[rt] = C * (r - l + 1);
        lazy[rt] = 0; // 把所有懒累加都删除
        return;
      }
      // 无法懒更新，必须往下发
      int mid = (l + r) >> 1;
      pushDown(rt, mid - l + 1, r - mid);
      if (L <= mid) {
        update(L, R, C, l, mid, rt << 1); // 分配给左子树 2i
      }
      if (R > mid) {
        update(L, R, C, mid + 1, r, rt << 1 | 1);// 分配给右子树 2i+1
      }
      pushUp(rt); // 最后向上整合信息
    }

    // 与add()，update()类似
    public long query(int L, int R, int l, int r, int rt) {
      if (L <= l && r <= R) {
        return sum[rt];
      }
      int mid = (l + r) >> 1;
      pushDown(rt, mid - l + 1, r - mid);
      long ans = 0;
      if (L <= mid) {
        ans += query(L, R, l, mid, rt << 1);
      }
      if (R > mid) {
        ans += query(L, R, mid + 1, r, rt << 1 | 1);
      }
      return ans;
    }
  }
  // 对数器省略，可以用纯遍历的方法验证。
}
