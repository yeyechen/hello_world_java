package 左程云体系学习班;

public class Lecture08 {

  /*
   * 1. 前缀树(prefix tree/trie): 优于哈希表的地方，可以查到出现多少字符串包含特定前缀
   * 关于哈希表增删改查O(1)时间复杂度：其实跟单样本长度大小有关(比如字符串的长度)，因为数据进哈希表需要算一次哈希值，
   * 必须遍历一遍改字符串。那么时间复杂度为O(K), k为字符串平均长度。但是如果进入哈希表的是内存地址，是可以当作O(1)的操作。
   * 总结：哈希表增删改查的时间复杂度为单样本的平均长度。
   *
   *
   * 2. 排序算法总结：
   *
   *            时间复杂度     额外空间复杂度     稳定性
   * 选择排序       O(N^2)         O(1)          无
   * 冒泡排序       O(N^2)         O(1)          有
   * 插入排序       O(N^2)         O(1)          有
   * 归并排序       O(N*logN)      O(N)          有
   * 随机快排       O(N*logN)      O(logN)       无
   * 堆排序        O(N*logN)      O(1)          无
   * ============================================
   * 计数排序       O(N)           O(M)          有
   * 基数排序       O(N)           O(N)          有
   *
   * 不基于比较排序(桶排序)：利用容器，不基于比较，比如计数排序，基数排序。适用窄，对样本数据有严格要求，不易改写。
   * 计数排序：把每个数出现的次数统计一次。
   * 基数排序：从个位开始让每个数进入0～9的对应桶，再按0～9顺序拿出；再十位，再百位，等等。
   *
   * 基于比较排序：适用广，只要规定好两个样本怎么比大小就可以直接复用，比如选择排序，冒泡排序等。时间复杂度的极限是O(N*logN)。
   * 为了绝对的速度选快排(虽然时间复杂度一样，但是快排的常数时间是最好的)，为了省空间选堆排，为了稳定性选归并。
   * 时间复杂度为O(N*logN)，并且额外空间复杂度低于O(N)并且稳定的，基于比较的排序是不存在的。
   *
   * 稳定性：相同大小的数的相对位置在排序前后能否保持一致，如果能，则具有稳定性。对于基础类型来说稳定性无用，但是对于
   * 非基础类型有用。比如学校里所有的同学有两个属性，班级，年龄。对所有学生先按照年龄排序，再按照班级排序，那么对于有稳定性
   * 的算法，能保证班级内部年龄按大小排序。
   *
   * 常见的坑：
   * (1). 归并排序的额外空间复杂度可以变成O(1)，"归并排序内部缓存法"，但是将变得不再稳定。
   * (2). "原地归并排序"是垃圾帖，会让时间复杂度变成O(N^2)。
   * (3). 快速排序稳定性改进，"01 stable sort"，但会对样本数据要求更多。
   * */

  public static class Node {

    //记录有多少字符串中含有这个node
    private int pass;
    //记录有多少字符串以这个node结尾
    private int end;
    //记录后面的node
    private Node[] nexts;

    public Node() {
      this.pass = 0;
      this.end = 0;
      //默认是字符串中含有的字母都为小写字母，所以有26个
      this.nexts = new Node[26];
    }
  }

  public static class Trie {

    private Node root;

    public Trie() {
      this.root = new Node();
    }

    public void insert(String word) {
      if (word == null) {
        return;
      }

      Node node = root;
      node.pass++;
      int path = 0;
      for (int i = 0; i < word.length(); i++) {
        path = word.charAt(i) - 'a'; //找到在nexts中对应index的位置
        //如果以前没有这条路，新建一个node
        if (node.nexts[path] == null) {
          node.nexts[path] = new Node();
        }
        node = node.nexts[path];
        node.pass++;
      }
      node.end++;
    }

    public void delete(String word) {
      //如果word根本没在树中出现，直接返回
      if (search(word) == 0) {
        return;
      }
      Node node = root;
      node.pass--;
      int path = 0;
      for (int i = 0; i < word.length(); i++) {
        path = word.charAt(i) - 'a';
        //如果node.pass被减完之后变成0了，我们就把内存也删除(在jvm里，前面的node为null了，后面连着的node内存也会被自动回收)
        //防止内存泄漏，如果pass等于0了又占着内存，树会越来越大
        if (--node.nexts[path].pass == 0) {
          node.nexts[path] = null;
          return;
        }
        node = node.nexts[path];
      }
      node.end--;
    }

    //返回word在树中被加入了几次
    private int search(String word) {
      if (word == null) {
        return 0;
      }
      Node node = root;
      int path = 0;
      for (int i = 0; i < word.length(); i++) {
        path = word.charAt(i) - 'a';
        if (node.nexts[path] == null) {
          return 0;
        }
        node = node.nexts[path];
      }
      return node.end;
    }

    //返回在所有加入的字符串中，有多少个是以pre这个字符串作为前缀的
    public int prefixNum(String pre) {
      if (pre == null) {
        return 0;
      }
      Node node = root;
      int path = 0;
      for (int i = 0; i < pre.length(); i++) {
        path = pre.charAt(i) - 'a';
        if (node.nexts[path] == null) {
          return 0;
        }
        node = node.nexts[path];
      }
      return node.pass;
    }
  }
}

