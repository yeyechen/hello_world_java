package 左程云体系学习班;

public class Lecture32 {

  /* 1. Index Tree：上节课我们了解了线段树，是一个实现累加和功能，并能快速在范围上update，add的数据结构。而这节课
   * 的Index Tree更轻量化，但只支持单点更新。同时有一个优点是非常容易改成二维三维的结构。当然同时也支持区间累加和查询。
   * 用一个数组来表示树结构，那么在遍历辅助累加和数组时，规定index=1存1的累加和；index=2 存1~2 的累加和；index=3存
   * 3的累加和；index=4存1~4的累加和；index=5存5的累加和；index=6存5~6的累加和；index=7存7的累加和；index=8存
   * 1~8的累加和...用人话说就是当前面有配对了，就自己；没配对，就配对。比如index=3，前面1和2配对了，3只能自己；index=4
   * 时，3没配对，可以和3配对形成一个两个数的对，又和前面1，2能形成四个数的配对，所以存1~4的累加和。
   * [1, 2, 3, 4, 5, 6, 7, 8]
   *
   * 在求累加和时，我们要求1~i位置的累加和，我们把i位置用二进制表示，一直累加并减去最后一位1，直到0。
   * 比如1011的十进制时11：
   * index=11存的是11位置自己的累加和，result加上。1011-0001 = 1010十进制10
   * index=10存的是9~10位置的累加和，result加上。1010-0010 = 1000十进制8
   * index=8存的是1~8位置的累加和，result加上。1000-1000=0，结束返回result
   * 我们就获得了1~11位置所有数的累加和，这个过程是logN的。想要获得范围(L,R)上的累加和，用1~R累加和减去1~L-1累加和
   * 即可。
   *
   * 在单点更新时，我们更新index=i上的数，把i位置用二进制表示，一直累加并加上最后一位1，直到树的最大值N
   * 比如树的最大值N=1011二进制11，index=10 二进制2位置上更新了：
   * index=2存的是1~2位置的累加和，数组中index=2位置加上矢量。10+10=100十进制4
   * index=4存的是1~4位置的累加和，数组中index=4位置加上矢量。100+100=1000十进制8
   * index=8存的是1~8位置的累加和，数组中index=8位置加上矢量。1000+1000=10000十进制16，大于11，结束。
   * 所有包含2位置的累加和位置都被更新了。
   *
   * 原理不用太过纠结，感受其中的奥妙然后记住就行了。
   *
   * 2. AC自动机。有点复杂，代码就不写了。
   *  */

  // 1. Index Tree
  public static class IndexTree {

    private int[] tree;
    private int N;

    // 同样的，index=0不使用，下标从1开始
    public IndexTree(int size) {
      this.N = size;
      this.tree = new int[size + 1];
    }

    // 用来查询 1~index 的累加和
    public int sum(int index) {
      int ret = 0;
      while (index > 0) {
        ret += sum(index);
        index -= index & -index; //(a&-a 是取最低位的1)
      }
      return ret;
    }

    public void add(int index, int k) {
      while (index <= N) {
        tree[index] += k;
        index += index & -index;
      }
    }
  }
}
