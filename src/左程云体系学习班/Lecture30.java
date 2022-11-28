package 左程云体系学习班;

public class Lecture30 {


  /* 二叉树的 Morris 遍历！
   * 想实现的目的：二叉树的遍历之前讲过，先序，中序，后序。在这些遍历的过程中，时间复杂度O(N)，而空间复杂度为二叉树的
   * 深度，最差情况下也是O(N)。而Morris遍历则实现了二叉树遍历时间复杂度O(N)，空间复杂度为O(1)。该算法是通过利用原本
   * 二叉树中大量空闲指针(null)的方式，达到节省空间的目的。
   * 也是一个在面试上聊的算法，在笔试中基本上用不上，因为笔试上基本限制的是时间复杂度。
   *
   * 先讲算法流程：
   * 假设来到当前节点cur，开始时cur来到头节点位置
   * 1）如果cur没有左子节点，cur向右移动(cur = cur.right)
   *
   * 2）如果cur有左子节点，找到左子树上最右的节点mostRight：
   * a.如果mostRight的右指针指向空，让其指向cur，
   * 然后cur向左移动(cur = cur.left)
   * b.如果mostRight的右指针指向cur，让其指向null，
   * 然后cur向右移动(cur = cur.right)
   *
   * 3）cur为空时遍历停止
   *
   * 例子：
   *          1
   *       2     3
   *     4   5 6   7
   * Morris遍历会经过：1,2,4,2,5,1,3,6,3,7
   *
   * 会经过有左子节点的cur节点2次，因为该cur节点的左子树最右节点被修改指向了cur。指针最后是会被还原的，但正因为这两次访问，
   * 我们可以从Morris序变到先序，中序，后序。
   *
   * 改先序很简单，第二次访问的时候不打印就可以。中序时相反，有左子节点的node在第二次访问的时候打印。
   * 唯独改后序遍历有些麻烦。后序遍历在递归中是第三次回到自己的时候打印，而morris中每个节点不可能回到自己三次。
   * 重点在能回到自己两次的节点上，在第二次访问时:
   * #逆序打印左子树的所有右边界 (即cur.left+cur.left.right, cur.left.right.right, ...)#
   * #所有节点结束后逆序打印整棵树的有边界#
   * 为了不浪费空间，打印左树右边界时逆转整个右边界，打印完调整回来。整体空间复杂度还是O(1)。
   *
   * 二叉树递归套路中，有小部分可以被改写成Morris遍历，来节省额外空间。比如求一棵树中最小的深度。如果一个算法，需要
   * 完全整合左子树和右子树的信息，返回给上面的node，那么就不能用Morris遍历，因为涉及到第三次回到节点。在这也不多
   * 赘述，例题(最小深度)课上讲了，但是代码比较难懂，判断条件较多。
   *
   * */
  public static class Node {

    public int value;
    Node left;
    Node right;

    public Node(int data) {
      this.value = data;
    }
  }

  public static void morrisTraversal(Node head) {
    if (head == null) {
      return;
    }
    Node cur = head;
    Node mostRight = null;
    while (cur != null) {
      mostRight = cur.left;
      if (mostRight != null) {
        while (mostRight.right != null && mostRight.right != cur) {
          mostRight = mostRight.right;
        }
        if (mostRight.right == null) {
          // System.out.print(cur.value + " "); // 先序
          mostRight.right = cur;
          cur = cur.left;
          continue;
        } else {
          mostRight.right = null;
        }
      }
      // else { // 先序
      //   System.out.print(cur.value + " "); // 先序
      // } // 先序

      System.out.print(cur.value + " "); // 中序
      cur = cur.right;
    }
  }

  public static void morrisTraversalPost(Node head) {
    if (head == null) {
      return;
    }
    Node cur = head;
    Node mostRight = null;
    while (cur != null) {
      mostRight = cur.left;
      if (mostRight != null) {
        while (mostRight.right != null && mostRight.right != cur) {
          mostRight = mostRight.right;
        }
        if (mostRight.right == null) {
          mostRight.right = cur;
          cur = cur.left;
          continue;
        } else {
          mostRight.right = null;
          // 第二次访问，打印左树的右边界
          printAlongRight(cur.left);
        }
      }
      cur = cur.right;
    }
    printAlongRight(head);
  }

  // 打印右边界：
  //                ()
  //     head (*)        ()
  //        ()  (*)    ()  ()
  //              (*)
  //                (*)
  // 逆转整个右边界，打印完调整回来
  private static void printAlongRight(Node head) {
    Node tail = reverseAlongRight(head);
    Node cur = tail;
    while (cur != null) {
      System.out.print(cur.value + " ");
      cur = cur.right;
    }
    reverseAlongRight(tail);
  }

  private static Node reverseAlongRight(Node from) {
    Node pre = null;
    Node next = null;
    while (from != null) {
      next = from.right;
      from.right = pre;
      pre = from;
      from = next;
    }
    return pre;
  }

  public static void main(String[] args) {
    //little example
    Node root = new Node(1);
    Node two = new Node(2);
    Node three = new Node(3);
    Node four = new Node(4);
    Node five = new Node(5);
    Node six = new Node(6);
    Node seven = new Node(7);

    root.left = two; root.right = three;
    two.left = four; two.right = five;
    three.left = six; three.right = seven;

    morrisTraversalPost(root);
  }
}
