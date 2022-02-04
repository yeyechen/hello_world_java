package 左程云体系学习班;

import java.util.*;

public class Lecture14 {

  /*
   * 并查集 (Disjoint-set data structure) 是一种数据结构，用于处理一些不交集（Disjoint sets，一系列没有重复元素的集合）的合并及查询问题。
   * 并查集支持如下操作：
   * 1. 查询：查询某个元素属于哪个集合，通常是返回集合内的一个“代表元素”。这个操作是为了判断两个元素是否在同一个集合之中。
   * 2. 合并：将两个集合合并为一个。
   * 3. 添加：添加一个新集合，其中有一个新元素。添加操作不如查询和合并操作重要，常常被忽略。
   *
   * 并查集单次操作的平均时间复杂度为O(1)
   * */

  public static class UnionFind<V> {
    HashMap<V, Node<V>> nodes;
    HashMap<Node<V>, Node<V>> parents; // 只记录直系父子关系: key -> 子, value ->父
    HashMap<Node<V>, Integer> sizeMap; // 记录每个代表节点(parent指针指向自己的节点)

    public UnionFind(List<V> values) {
      nodes = new HashMap<>();
      parents = new HashMap<>();
      sizeMap = new HashMap<>();

      // 初始化时每个集合只有自己一个节点
      for (V cur : values) {
        Node<V> node = new Node<>(cur);
        nodes.put(cur, node);
        parents.put(node, node);
        sizeMap.put(node, 1);
      }
    }

    public Node<V> findFather(Node<V> cur) {
      Stack<Node<V>> path = new Stack<>();
      // 一路找到代表节点
      while (cur != parents.get(cur)) {
        path.push(cur);
        cur = parents.get(cur);
      }
      // 优化: 扁平化处理, 下次查询时就只需要跳一次
      while (!path.isEmpty()) {
        parents.put(path.pop(), cur);
      }
      // 举个例子, findFather(a) 会让:
      // a -> b -> c -> d 变成         d
      //                                      /  |  \
      //                                    a    b   c
      return cur;
    }

    public void union(V a, V b) {
      Node<V> aHead = findFather(nodes.get(a));
      Node<V> bHead = findFather(nodes.get(b));
      if (aHead != bHead) {
        int aSetSize = sizeMap.get(aHead);
        int bSetSize = sizeMap.get(bHead);
        // 让小的集合去连接大的集合, 能让集合的长度增长速度最小化
        Node<V> big = aSetSize >= bSetSize ? aHead : bHead;
        Node<V> small = big == aHead ? bHead : aHead;
        parents.put(small, big);
        sizeMap.put(big, aSetSize + bSetSize);
        // 此处移除是因为小的集合已经连接到大的集合, 没有小集合的代表节点了
        sizeMap.remove(small);
      }
    }

    private boolean isSameSet(V a, V b) {
      return findFather(nodes.get(a)) == nodes.get(b);
    }

  }

  private static class Node<V> {

    V value;

    public Node(V value) {
      this.value = value;
    }
  }
}
