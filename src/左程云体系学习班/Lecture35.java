package 左程云体系学习班;

public class Lecture35 {
  /*
   * AVL Tree
   * https://www.cs.usfca.edu/~galles/visualization/AVLtree.html
   * 就是一个搜索二叉树，每次add或者delete之后会re-balance一次，保证整个二叉树的平衡，从而获得O(logN)的整体操作
   * 时间复杂度。否则最差情况下是log(N)。
   *
   * 其他不多说了。
   *  */

  public static class AVLNode<K extends Comparable<K>, V> {

    public K key; // key
    public V value; // value
    public AVLNode<K, V> l; // left tree
    public AVLNode<K, V> r; // right tree
    public int h; // current tree height

    public AVLNode(K key, V value) {
      this.key = key;
      this.value = value;
      this.h = 1;
    }
  }

  public static class AVLTreeMap<K extends Comparable<K>, V> {

    private AVLNode<K, V> root;
    private int size;

    public AVLTreeMap() {
      this.root = null;
      this.size = 0;
    }

    private AVLNode<K, V> rightRotate(AVLNode<K, V> cur) {
      AVLNode<K, V> left = cur.l;
      cur.l = left.r;
      left.r = cur;
      // adjust height
      cur.h = Math.max(cur.l == null ? 0 : cur.l.h, cur.r == null ? 0 : cur.r.h) + 1;
      left.h = Math.max(left.l == null ? 0 : left.l.h, cur.h) + 1;
      return left;
    }

    private AVLNode<K, V> leftRotate(AVLNode<K, V> cur) {
      AVLNode<K, V> right = cur.r;
      cur.r = right.l;
      right.l = cur;
      // adjust height
      cur.h = Math.max(cur.l == null ? 0 : cur.l.h, cur.r == null ? 0 : cur.r.h) + 1;
      right.h = Math.max(right.r == null ? 0 : right.r.h, cur.h) + 1;
      return right;
    }

    private AVLNode<K, V> maintain(AVLNode<K, V> cur) {
      if (cur == null) {
        return null;
      }
      int leftHeight = cur.l == null ? 0 : cur.l.h;
      int rightHeight = cur.r == null ? 0 : cur.r.h;
      // 分4种情况, LL, LR, RL, RR, 其中LL和RR只需要rotate一次，LR和RL需要两次
      if (Math.abs(leftHeight - rightHeight) > 1) {
        if (leftHeight > rightHeight) {
          int leftLeftHeight = cur.l == null || cur.l.l == null ? 0 : cur.l.l.h;
          int leftRightHeight = cur.l == null || cur.l.r == null ? 0 : cur.l.r.h;
          if (leftLeftHeight >= leftRightHeight) { // LL
            cur = rightRotate(cur);
          } else { // LR
            cur.l = leftRotate(cur.l);
            cur = rightRotate(cur);
          }
        } else {
          int rightLeftHeight = cur.r == null || cur.r.l == null ? 0 : cur.r.l.h;
          int rightRightHeight = cur.r == null || cur.r.r == null ? 0 : cur.r.r.h;
          if (rightRightHeight >= rightLeftHeight) { // RR
            cur = leftRotate(cur);
          } else { // RL
            cur.r = rightRotate(cur.r);
            cur = leftRotate(cur);
          }
        }
      }
      return cur;
    }

    private AVLNode<K, V> add(AVLNode<K, V> cur, K key, V value) {
      if (cur == null) {
        return new AVLNode<>(key, value);
      } else {
        if (key.compareTo(cur.key) < 0) {
          cur.l = add(cur.l, key, value);
        } else {
          cur.r = add(cur.r, key, value);
        }
        cur.h = Math.max(cur.l == null ? 0 : cur.l.h, cur.r == null ? 0 : cur.r.h);
        return maintain(cur);
      }
    }

    private AVLNode<K, V> delete(AVLNode<K, V> cur, K key) {
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
        } else { // tougher to handle
          AVLNode<K, V> des = cur.r;
          // find the closest key that is bigger than cur.k, can also be the closest key that
          // is smaller than cur.k, doesn't matter.
          while (des.l != null) {
            des = des.l;
          }
          cur.r = delete(cur.r, des.key); // let the "delete" do the re-balancing for us
          des.l = cur.l;
          des.r = cur.r;
          cur = des;
        }
      }
      if (cur != null) {
        cur.h = Math.max(cur.l == null ? 0 : cur.l.h, cur.r == null ? 0 : cur.r.h);
      }
      return maintain(cur);
    }
  }

}
