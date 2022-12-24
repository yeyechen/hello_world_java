package 左程云体系学习班;

import java.util.ArrayList;

public class Lecture36 {

  /*
   * 1. Size Balanced Tree!
   * sb树也是有序表的实现形式之一。sb树和AVL树非常类似，区别就是不平衡的容量放宽了，re-balance的动作频率变少了，
   * 从而优化常数时间，总体时间复杂度还是O(logN)。sb tree在IO bound的系统中被经常使用，因为平衡频率比AVL要少。
   *
   * 2. Skip List Map (跳表)
   * 相对新概念的有序表实现，利用了概率。
   * */

  public static class SBTNode<K extends Comparable<K>, V> {

    public K key;
    public V value;
    public SBTNode<K, V> l;
    public SBTNode<K, V> r;
    public int size;

    public SBTNode(K key, V value) {
      this.key = key;
      this.value = value;
      this.size = 1;
    }
  }

  public static class SizeBalancedTreeMap<K extends Comparable<K>, V> {

    private SBTNode<K, V> root;

    private SBTNode<K, V> rightRotate(SBTNode<K, V> cur) {
      SBTNode<K, V> left = cur.l;
      cur.l = left.r;
      left.r = cur;
      // adjust size
      left.size = cur.size;
      cur.size = (cur.l == null ? 0 : cur.l.size) + (cur.r == null ? 0 : cur.r.size) + 1;
      return left;
    }

    private SBTNode<K, V> leftRotate(SBTNode<K, V> cur) {
      SBTNode<K, V> right = cur.r;
      cur.r = right.l;
      right.l = cur;
      right.size = cur.size;
      cur.size = (cur.l == null ? 0 : cur.l.size) + (cur.r == null ? 0 : cur.r.size) + 1;
      return right;
    }

    private SBTNode<K, V> maintain(SBTNode<K, V> cur) {
      if (cur == null) {
        return null;
      }
      // sb树的re-balance判定条件要更宽松，任何子节点的size如果比自己的叔叔节点大，则实行re-balance。叔叔节点
      // 为父节点的兄弟节点。
      int leftSize = cur.l == null ? 0 : cur.l.size;
      int leftLeftSize = cur.l == null || cur.l.l == null ? 0 : cur.l.l.size;
      int leftRightSize = cur.l == null || cur.l.r == null ? 0 : cur.l.r.size;

      int rightSize = cur.r == null ? 0 : cur.r.size;
      int rightLeftSize = cur.r == null || cur.r.l == null ? 0 : cur.r.l.size;
      int rightRightSize = cur.r == null || cur.r.r == null ? 0 : cur.r.size;

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
      } else if (rightLeftSize > leftSize) {
        cur.r = rightRotate(cur.r);
        cur = leftRotate(cur);
        cur.l = maintain(cur.l);
        cur.r = maintain(cur.r);
        cur = maintain(cur);
      }
      return cur;
    }

    private SBTNode<K, V> add(SBTNode<K, V> cur, K key, V value) {
      if (cur == null) {
        return new SBTNode<>(key, value);
      } else {
        if (key.compareTo(cur.key) < 0) {
          cur.l = add(cur.l, key, value);
        } else {
          cur.r = add(cur.r, key, value);
        }
        cur.size++;
        return maintain(cur);
      }
    }

    private SBTNode<K, V> delete(SBTNode<K, V> cur, K key) {
      cur.size--;
      if (key.compareTo(cur.key) < 0) {
        cur.l = delete(cur.l, key);
      } else if (key.compareTo(cur.key) > 0) {
        cur.r = delete(cur.r, key);
      } else { // found the node to be deleted
        if (cur.l == null && cur.r == null) {
          cur = null;
        } else if (cur.l == null && cur.r != null) {
          cur = cur.r;
        } else if (cur.l != null && cur.r == null) {
          cur = cur.l;
        } else {
          SBTNode<K, V> pre = null;
          SBTNode<K, V> des = cur.r;
          des.size--;
          while (des.l != null) {
            pre = des;
            des = des.l;
            des.size--;
          }
          if (pre != null) {
            pre.l = des.r;
            des.r = cur.r;
          }
          des.l = cur.l;
          des.size = des.l.size + (des.r == null ? 0 : des.r.size) + 1;
          cur = des;
        }
      }
      // cur = maintain(cur) // no need to maintain, because when add is executed the unbalanced
      // tree will become balance immediately. reduce IO.
      return cur;
    }
  }

  // 跳表
  public static class SkipListNode<K extends Comparable<K>, V> {

    public K key;
    public V value;
    public ArrayList<SkipListNode<K, V>> nextNodes;

    public SkipListNode(K key, V value) {
      this.key = key;
      this.value = value;
      this.nextNodes = new ArrayList<SkipListNode<K, V>>();
    }

    // 遍历的时候，如果是往右遍历到的null(next == null), 遍历结束
    // 头(null), 头节点的null，认为最小
    // node  -> 头，node(null, "")  node.isKeyLess(!null)  true
    // node里面的key是否比otherKey小，true，不是false
    public boolean isKeyLess(K otherKey) {
      //  otherKey == null -> false
      return otherKey != null && (key == null || key.compareTo(otherKey) < 0);
    }

    public boolean isKeyEqual(K otherKey) {
      return (key == null && otherKey == null)
          || (key != null && otherKey != null && key.compareTo(otherKey) == 0);
    }
  }

  public static class SkipListMap<K extends Comparable<K>, V> {

    private static final double PROBABILITY = 0.5;
    private SkipListNode<K, V> head;
    private int size;
    private int maxLevel;

    public SkipListMap() {
      this.head = new SkipListNode<K, V>(null, null);
      this.head.nextNodes.add(null); // 0
      this.size = 0;
      this.maxLevel = 0;
    }

    // 从最高层开始，一路找下去，
    // 最终，找到第0层的<key的最右的节点
    private SkipListNode<K, V> mostRightLessNodeInTree(K key) {
      if (key == null) {
        return null;
      }
      int level = maxLevel;
      SkipListNode<K, V> cur = head;
      while (level >= 0) { // 从上层跳下层
        //  cur  level  -> level-1
        cur = mostRightLessNodeInLevel(key, cur, level--);
      }
      return cur;
    }

    // 在level层里，如何往右移动
    // 现在来到的节点是cur，来到了cur的level层，在level层上，找到<key最后一个节点并返回
    private SkipListNode<K, V> mostRightLessNodeInLevel(K key,
        SkipListNode<K, V> cur,
        int level) {
      SkipListNode<K, V> next = cur.nextNodes.get(level);
      while (next != null && next.isKeyLess(key)) {
        cur = next;
        next = cur.nextNodes.get(level);
      }
      return cur;
    }

    public boolean containsKey(K key) {
      if (key == null) {
        return false;
      }
      SkipListNode<K, V> less = mostRightLessNodeInTree(key);
      SkipListNode<K, V> next = less.nextNodes.get(0);
      return next != null && next.isKeyEqual(key);
    }

    public void put(K key, V value) {
      if (key == null) {
        return;
      }
      // 0层上，最右一个，< key 的Node -> >key
      SkipListNode<K, V> less = mostRightLessNodeInTree(key);
      SkipListNode<K, V> find = less.nextNodes.get(0);
      if (find != null && find.isKeyEqual(key)) {
        find.value = value;
      } else { // find == null   8   7   9
        size++;
        int newNodeLevel = 0;
        while (Math.random() < PROBABILITY) {
          newNodeLevel++;
        }
        // newNodeLevel
        while (newNodeLevel > maxLevel) {
          head.nextNodes.add(null);
          maxLevel++;
        }
        SkipListNode<K, V> newNode = new SkipListNode<K, V>(key, value);
        for (int i = 0; i <= newNodeLevel; i++) {
          newNode.nextNodes.add(null);
        }
        int level = maxLevel;
        SkipListNode<K, V> pre = head;
        while (level >= 0) {
          // level 层中，找到最右的 < key 的节点
          pre = mostRightLessNodeInLevel(key, pre, level);
          if (level <= newNodeLevel) {
            newNode.nextNodes.set(level, pre.nextNodes.get(level));
            pre.nextNodes.set(level, newNode);
          }
          level--;
        }
      }
    }

    public V get(K key) {
      if (key == null) {
        return null;
      }
      SkipListNode<K, V> less = mostRightLessNodeInTree(key);
      SkipListNode<K, V> next = less.nextNodes.get(0);
      return next != null && next.isKeyEqual(key) ? next.value : null;
    }

    public void remove(K key) {
      if (containsKey(key)) {
        size--;
        int level = maxLevel;
        SkipListNode<K, V> pre = head;
        while (level >= 0) {
          pre = mostRightLessNodeInLevel(key, pre, level);
          SkipListNode<K, V> next = pre.nextNodes.get(level);
          // 1）在这一层中，pre下一个就是key
          // 2）在这一层中，pre的下一个key是>要删除key
          if (next != null && next.isKeyEqual(key)) {
            // free delete node memory -> C++
            // level : pre -> next(key) -> ...
            pre.nextNodes.set(level, next.nextNodes.get(level));
          }
          // 在level层只有一个节点了，就是默认节点head
          if (level != 0 && pre == head && pre.nextNodes.get(level) == null) {
            head.nextNodes.remove(level);
            maxLevel--;
          }
          level--;
        }
      }
    }
  }
}
