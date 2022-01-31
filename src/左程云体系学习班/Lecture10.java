package 左程云体系学习班;

import com.sun.source.tree.Tree;
import java.util.Objects;
import java.util.Stack;
import 左程云体系学习班.Lecture03.Node;

public class Lecture10 {
  /*
   * 1.二叉树遍历：先序(头左右)，中序(左头右)，和后序(左右头)，都是由递归序加工而来。或者不用递归，自己压栈。
   * */

  public static class TreeNode {

    int value;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      TreeNode treeNode = (TreeNode) o;
      return value == treeNode.value && Objects.equals(left, treeNode.left)
          && Objects.equals(right, treeNode.right);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value, left, right);
    }
  }

  // 先序
  public static void pre(TreeNode head) {
    if (head == null) {
      return;
    }
    System.out.println(head.value);
    pre(head.left);
    pre(head.right);
  }

  // 中序
  public static void in(TreeNode head) {
    if (head == null) {
      return;
    }
    in(head.left);
    System.out.println(head.value);
    in(head.right);
  }

  // 后序
  public static void post(TreeNode head) {
    if (head == null) {
      return;
    }
    post(head.left);
    post(head.right);
    System.out.println(head.value);
  }

  //不使用递归实现先序
  public static void pre2(TreeNode head) {
    System.out.println("pre-order");
    if (head != null) {
      Stack<TreeNode> stack = new Stack<>();
      stack.add(head);
      while (!stack.isEmpty()) {
        head = stack.pop();
        System.out.print(head.value + " ");
        if (head.right != null) {
          stack.push(head.right);
        }
        if (head.left != null) {
          stack.push(head.left);
        }
      }
      System.out.println();
    }
  }

  //不使用递归实现中序(一直往左递归，左边为空了，那么打印并且让右节点入栈)
  public static void in2(TreeNode curr) {
    System.out.println("in-order");
    if (curr != null) {
      Stack<TreeNode> stack = new Stack<>();
      while (!stack.isEmpty() || curr != null) {
        if (curr != null) {
          stack.push(curr);
          curr = curr.left;
        } else {
          curr = stack.pop();
          System.out.print(curr.value + " ");
          curr = curr.right;
        }
      }
    }
    System.out.println();
  }

  //不实用递归实现后序(我们实现了不使用递归实现先序，头左右，那么同样的我们可以实现头右左。那么每次打印时把头右左放入另外一个栈，
  //最后再出栈时就会整体变成左右头，也就是后序)
  public static void post2(TreeNode head) {
    System.out.println("post-order");
    if (head != null) {
      Stack<TreeNode> stack = new Stack<>();
      Stack<TreeNode> helper = new Stack<>();
      stack.add(head);
      while (!stack.isEmpty()) {
        head = stack.pop();
        helper.push(head);
        if (head.left != null) {
          stack.push(head.left);
        }
        if (head.right != null) {
          stack.push(head.right);
        }
      }
      while (!helper.isEmpty()) {
        System.out.print(helper.pop().value + " ");
      }
      System.out.println();
    }
  }

  public static void main(String[] args) {
    TreeNode head = new TreeNode(1);
    head.left = new TreeNode(2);
    head.right = new TreeNode(3);
    head.left.left = new TreeNode(4);
    head.left.right = new TreeNode(5);
    head.right.left = new TreeNode(6);
    head.right.right = new TreeNode(7);

    pre2(head);
    System.out.println("=============");
    in2(head);
    System.out.println("=============");
    post2(head);
    System.out.println("=============");
  }
}
