package 左程云体系学习班;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import 左程云体系学习班.Lecture10.TreeNode;

public class Lecture11 {

  /*
   * 1. 实现二叉树的按层遍历：其实就是宽度优先遍历，用队列；或者通过设置flag变量的方式，来发现某一层的结束。
   *
   * 2. 实现二叉树的序列化和反序列化：在程序中树是储存在内存中的，那么当程序停止或想让这棵树放在别的程序上，就可以让树变成
   * 一种字符串的形式，这个过程叫序列化。那么在别的程序上进行类似解码的操作，这个过程叫做反序列化。
   * 可以按照先序方式序列化，或按层序列化。没有中序方式序列化，因为有歧义。后序和先序差不多，一般用先序。
   * 在序列化过程中，一定要把每个空的树也表示出来，可以是null也可以用其他字符代替，比如"#"。
   *
   * 3. N叉数到二叉树的编码与解码(leetcode 431题)：可以将N叉树编码为二叉树，并能将该二叉树解码为原N叉树。
   * 一个 N 叉树是指每个节点都有不超过N个孩子节点的有根树。类似地，一个二叉树是指每个节点都有不超过 2 个孩子节点的有根树。
   * 你的编码/解码的算法的实现没有限制，你只需要保证一个N叉树可以编码为二叉树且该二叉树可以解码回原始N叉树即可。
   * 图解：
   * 这是一个多叉数
   *                               a
   *                          /    |    \
   *                         b     c     d
   *                     / / | \   | \
   *                    e f  g  h  i  j
   *
   * 编码后的二叉树：将所有的子树放在左树右边界上
   *                              a
   *                             /
   *                            b
   *                           /   \
   *                          e      c
   *                           \    / \
   *                            f  i   d
   *                             \  \
   *                              g  j
   *                               \
   *                                h
   *
   *
   * 4. 返回二叉树最宽的层有多少节点：二叉树按层遍历的升级版，需要设置一些变量来判断当前层是否已经结束遍历了。
   *
   * 5. 求二叉树中某一节点的后继节点：后继节点是二叉树中序遍历时的下一个节点。普通情况下需要O(N)的时间复杂度，但如果给每一个节点增加
   * 一个指针，让它可以指向自己的父节点，那么时间复杂度可以达到O(K)，K为树中该节点到后继节点的距离。
   *
   * 6. 微软面试题：请把一段纸条竖着放在桌子上，然后从纸条的下边向上方对折一次，压出折痕后展开。此时的折痕是凹下去的，即折痕凸起的
   * 方向指向纸条的背面。如果从纸条的下边向上方对折两次，先出折痕后展开，此时有三条折痕，从上往下分别是凹折痕，凹折痕，和凸折痕。
   * 给定一个参数N，代表纸条从下边向上对折的次数。请从上到下打印所有折痕的方向。
   *
   * 题解：第一次对折后的折痕为凹折痕，再接下来所有对折时，可想象为上一个折痕的二叉树，左凹右凸。打印过程即为二叉树的中序遍历过程。
   *
   * (对折三次示意图)
   *
   *       *上*                             *下*
   *      ———————————————————————————————————————
   *          ｜   ｜   ｜   ｜   ｜   ｜   ｜
   *      ———————————————————————————————————————
   *                      凹
   *                    /    \
   *                  凹      凸
   *                /   \   /  \
   *              凹     凸 凹   凸
   *
   *
   * 7. 完全二叉树的判断：按层遍历改写。1. 有右无左，则不是完全二叉树。2. 当第一次遇到左右孩子不双全的时候，剩下的节点
   * 必须是叶节点。
   * */

  // 1. 实现二叉树的按层遍历(用队列)
  public static void level(TreeNode root) {
    if (root == null) {
      return;
    }
    Queue<TreeNode> nodes = new LinkedList<>();
    nodes.add(root);
    while (!nodes.isEmpty()) {
      root = nodes.poll();
      System.out.print(root.value + " ");
      if (root.left != null) {
        nodes.add(root.left);
      }
      if (root.right != null) {
        nodes.add(root.right);
      }
    }
  }

  //2. 实现二叉树的序列化

  // 先序序列化(就是先序遍历，把每个节点的值作为字符串放进一个队列中)
  public static Queue<String> preSerialize(TreeNode head) {
    Queue<String> ans = new LinkedList<>();
    preSerializeProcess(head, ans);
    return ans;
  }

  private static void preSerializeProcess(TreeNode head, Queue<String> ans) {
    if (head == null) {
      ans.add(null);
    } else {
      ans.add(String.valueOf(head.value));
      preSerializeProcess(head.left, ans);
      preSerializeProcess(head.right, ans);
    }
  }

  // 按层序列化
  public static Queue<String> levelSerialize(TreeNode head) {
    Queue<String> ans = new LinkedList<>();

    if (head == null) {
      ans.add(null);
    } else {
      Queue<TreeNode> nodes = new LinkedList<>();
      nodes.add(head);
      ans.add(String.valueOf(head.value));
      while (!nodes.isEmpty()) {
        head = nodes.poll();
        //左右两边同理，如果子树为空，那么直接在序列化中加空；如果不为空，序列化的同时在队列中也加入以便访问它的子树
        if (head.left != null) {
          nodes.add(head.left);
          ans.add(String.valueOf(head.left.value));
        } else {
          ans.add(null);
        }
        if (head.right != null) {
          nodes.add(head.right);
          ans.add(String.valueOf(head.right.value));
        } else {
          ans.add(null);
        }
      }
    }
    return ans;
  }

  // 2.实现二叉树的反序列化

  // 先序反序列化
  public static TreeNode preReverseSerialize(Queue<String> serial) {
    String value = serial.poll();
    if (value == null) {
      return null;
    }
    TreeNode head = new TreeNode(Integer.parseInt(value));
    head.left = preReverseSerialize(serial);
    head.right = preReverseSerialize(serial);
    return head;
  }

  // 按层反序列化
  public static TreeNode levelReverseSerialize(Queue<String> serial) {
    if (serial == null || serial.size() == 0) {
      return null;
    }
    String value = serial.poll();
    TreeNode head = value == null ? null : new TreeNode(Integer.parseInt(value));
    Queue<TreeNode> nodes = new LinkedList<>();
    if (head != null) {
      nodes.add(head);
    }
    while (!nodes.isEmpty()) {
      TreeNode node = nodes.poll();
      String left = serial.poll();
      node.left = left == null ? null : new TreeNode(Integer.parseInt(left));
      String right = serial.poll();
      node.right = right == null ? null : new TreeNode(Integer.parseInt(right));

      if (node.left != null) {
        nodes.add(node.left);
      }
      if (node.right != null) {
        nodes.add(node.right);
      }
    }
    return head;
  }

  // 3. N叉数到二叉树的编码与解码(leetcode 431题)

  // 多叉树的表达形式
  public static class MultiTreeNode {

    int val;
    List<MultiTreeNode> children;

    public MultiTreeNode(int val) {
      this.val = val;
    }

    public MultiTreeNode(int val, List<MultiTreeNode> children) {
      this.val = val;
      this.children = children;
    }
  }

  // 多叉树到二叉树的编码
  public TreeNode multiTreeBinaryTreeEncoder(MultiTreeNode root) {
    if (root == null) {
      return null;
    }
    TreeNode head = new TreeNode(root.val);
    head.left = encode(root.children);
    return head;
  }

  private TreeNode encode(List<MultiTreeNode> children) {
    TreeNode head = null;
    TreeNode curr = null;
    for (MultiTreeNode child : children) {
      TreeNode tNode = new TreeNode(child.val);
      //head相当于在上述例子中的b和e，每个多叉树节点的孩子们中只有一个head
      if (head == null) {
        head = tNode;
      } else {
        curr.right = tNode;
      }
      curr = tNode;
      curr.left = encode(child.children);
    }
    return head;
  }

  // 二叉树到多叉树的解码
  private MultiTreeNode binaryTreeMultiTreeDecoder(TreeNode root) {
    if (root == null) {
      return null;
    }
    return new MultiTreeNode(root.value, decode(root.left));
  }

  private List<MultiTreeNode> decode(TreeNode root) {
    List<MultiTreeNode> children = new ArrayList<>();
    while (root != null) {
      MultiTreeNode curr = new MultiTreeNode(root.value, decode(root.left));
      children.add(curr);
      root = root.right;
    }
    return children;
  }

  // 4. 返回二叉树最宽的层有多少节点
  public static int countWidestLevelNodes(TreeNode currNode) {
    if (currNode == null) {
      return 0;
    }
    Queue<TreeNode> nodes = new LinkedList<>();
    //记录当前层以哪个节点结束
    TreeNode currLevelEndNode = null;
    //记录下一层以哪个节点结束
    TreeNode nextLevelEndNode = null;

    int currCount = 0;
    int maxCount = 0;

    nodes.add(currNode);
    currLevelEndNode = currNode;
    while (!nodes.isEmpty()) {
      //每次从队列里拿出一个节点，就在当前层加一
      currNode = nodes.poll();
      currCount++;

      //反复更新下一层可能的结束节点
      if (currNode.left != null) {
        nodes.add(currNode.left);
        nextLevelEndNode = currNode.left;
      }
      if (currNode.right != null) {
        nodes.add(currNode.right);
        nextLevelEndNode = currNode.right;
      }
      //检查该节点是否为当前层的结束节点
      //如果是，则当前层结束遍历->更新最大值，当前计数归零，让当前层结束节点变为下一层结束节点，即进入下一层。
      if (currNode == currLevelEndNode) {
        maxCount = Math.max(maxCount, currCount);
        currCount = 0;
        currLevelEndNode = nextLevelEndNode;
      }
    }
    return maxCount;
  }

  //5. 求二叉树中某一节点的后继节点
  public static class TreeNodeWithParent {

    int value;
    TreeNodeWithParent left;
    TreeNodeWithParent right;
    TreeNodeWithParent parent;

    public TreeNodeWithParent(int value) {
      this.value = value;
    }
  }

  public static TreeNodeWithParent getSuccessorNode(TreeNodeWithParent node) {
    if (node == null) {
      return null;
    }
    //1.自己有right返回right中最左的child
    //2.自己没有right，那么往上找：
    //  ->2.1 自己是left那么返回parent
    //  ->2.2 自己是right那么一直往上找，直到某一个grandParent是另一个grandGrandParent的left，返回grandGrandParent
    if (node.right != null) {
      TreeNodeWithParent curr = node.right;
      while (curr.left != null) {
        curr = curr.left;
      }
      //出来后curr有left，即右边最左的孩子
      return curr;
    } else {
      TreeNodeWithParent curr = node.parent;
      while (curr != null && curr.left != node) {
        node = curr;
        curr = curr.parent;
      }
      return curr;
    }
  }

  // 6.折痕方向问题
  public static void printFoldDirection(int N) {
    foldProcess(1, N, true);
  }

  //第三个变量用来判断凹凸，true为凹，false为凸
  private static void foldProcess(int curr, int N, boolean isCave) {
    if (curr > N) {
      return;
    }
    foldProcess(curr + 1, N, true);
    System.out.print(isCave ? "凹" : "凸");
    foldProcess(curr + 1, N, false);
  }

  // 7. 完全二叉树的判断
  public static boolean isComplete(TreeNode root) {
    if (root == null) {
      return true;
    }
    Queue<TreeNode> nodes = new LinkedList<>();
    boolean leaf = false;
    nodes.add(root);
    while (!nodes.isEmpty()) {
      root = nodes.poll();
      TreeNode l = root.left;
      TreeNode r = root.right;
      if ((leaf && (l != null || r != null) || (l == null && r != null))) {
        return false;
      }
      if (root.left != null) {
        nodes.add(root.left);
      }
      if (root.right != null) {
        nodes.add(root.right);
      }
      if (l == null || r == null) {
        leaf = true;
      }
    }
    return true;
  }

  //二叉树序列化反序列化的对数器
  public static void main(String[] args) {
    int maxLevel = 5;
    int maxValue = 100;
    int testAmount = 100000;
    System.out.println("测试开始");
    for (int i = 0; i < testAmount; i++) {
      TreeNode head = binaryTreeGenerator(maxLevel, maxValue);
      Queue<String> preSerialization = preSerialize(head);
      Queue<String> levelSerialization = levelSerialize(head);
      TreeNode tree1 = preReverseSerialize(preSerialization);
      TreeNode tree2 = levelReverseSerialize(levelSerialization);
      if (!tree1.equals(tree2)) {
        System.out.println(preSerialization);
        System.out.println(levelSerialization);
        System.out.println("测试失败");
        break;
      }
    }
    System.out.println("测试结束");
  }

  public static TreeNode binaryTreeGenerator(int maxLevel, int maxValue) {
    return generateProcess(1, maxLevel, maxValue);
  }

  private static TreeNode generateProcess(int currLevel, int maxLevel, int maxValue) {
    //子数有50%几率为null
    if (currLevel > maxLevel || (Math.random() < 0.5 && currLevel != 1)) {
      return null;
    }
    TreeNode head = new TreeNode((int) (Math.random() * maxValue));
    head.left = generateProcess(currLevel + 1, maxLevel, maxValue);
    head.right = generateProcess(currLevel + 1, maxLevel, maxValue);
    return head;
  }
}
