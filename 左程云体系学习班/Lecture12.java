package 左程云体系学习班;

import static 左程云体系学习班.Lecture11.binaryTreeGenerator;

import java.lang.ProcessHandle.Info;
import java.util.ArrayList;
import java.util.Queue;
import 左程云体系学习班.Lecture10.TreeNode;

public class Lecture12 {

  /*
   * 二叉树的递归套路：分析可能性是关键，可以解决面试中绝大多数的二叉树问题尤其是树形dp问题。本质是利用递归遍历二叉树的便利性。
   * 总结：1. 假设以X节点为头，假设可以向X左树和X右树要任何信息
   *      2. 在上一步的假设下，讨论以X为头节点的树，得到答案的可能性 (最重要)
   *      3. 列出所有可能性后，确定到底需要向左树和右树要什么样的信息
   *      4. 把左树信息和右树信息求全集，就是任何一棵子树都需要返回的信息Info
   *      5. 递归函数都返回Info，每一棵子树都这么要求
   *      6. 写代码，在代码中考虑如果把左树的信息和右树的信息整合出整棵树的信息
   *
   * (1) 判断平衡二叉树 (leetcode 110题)。平衡二叉树：所有子树中左树的深度和右侧深度的差值不超过1。
   * 递归条件：1. 每一棵子树左树得平衡; 2. 每一棵子树右树得平衡; 3.这棵子树左右高度绝对值 <= 1
   *
   * (2) 判断搜索二叉树 (leetcode 98题)。搜索二叉树：所有子树中，左树所有的值都比此节点小，右树所有的值都比此节点大，并且没有重复值。
   * 递归条件：1. 每一棵子树左树是搜索二叉树; 2. 每一棵子树右树是搜索二叉树; 3.左树的最大值比此节点小，右树的最小值比此节点大
   *
   * (3) 二叉树最大距离。在一棵二叉树中，任何两个节点之间都存在距离，距离的定义为从其中一个节点到另一个节点的路径中要经过边的数目。
   * 所以父节点和子节点的距离为1，一个节点与自己的距离为0。返回整棵树的最大距离。
   *
   * (4) 判断满二叉树。满二叉树：如果一棵满二叉树的高度为h，那么它的节点数量一定为2^h - 1。或者判断左树高度是否和右树高度一样
   *
   * (5) 子树搜索二叉树最大节点数：一棵普通的树，它自身或它的子树有可能是搜索二叉树，返回子树中最大的搜索二叉树的节点数量。
   * 判断：当前节点的头不是搜索二叉树的头时，返回左树和右树中最大的搜索二叉树节点数量；当前节点作为头，并且是搜索二叉树，返回左树的大小+右树的大小+1
   *
   * (6) 判断完全二叉树。第11节课用按层遍历写过，现在用二叉树的递归套路解。
   *
   * (7) 两个节点最低公共祖先问题 (leetcode 236题)。判断条件 (a)与当前节点X无关(X不是最低汇聚点)：1.左树有答案；2.右树有答案；3.两个子节点根本不存在在该树中
   *                                  (b)与当前节点X有关(X是最低汇聚点)：1.两个子节点分别存在在左树和右树中；2.当前节点X就是两个子节点中的其中一个
   * */


  // (1) 判断平衡二叉树
  public static boolean isBalanced(TreeNode root) {
    return checkBalanceProcess(root).isBalanced;
  }

  private static BalanceInfo checkBalanceProcess(TreeNode root) {
    if (root == null) {
      return new BalanceInfo(true, 0);
    }
    BalanceInfo leftInfo = checkBalanceProcess(root.left);
    BalanceInfo rightInfo = checkBalanceProcess(root.right);

    int height = Math.max(leftInfo.height, rightInfo.height) + 1;
    boolean isBalanced = leftInfo.isBalanced && rightInfo.isBalanced
        && Math.abs(leftInfo.height - rightInfo.height) <= 1;

    return new BalanceInfo(isBalanced, height);
  }

  public static class BalanceInfo {

    boolean isBalanced;
    int height;

    public BalanceInfo(boolean isBalanced, int height) {
      this.isBalanced = isBalanced;
      this.height = height;
    }
  }

  // (2) 判断搜索二叉树
  public static boolean isSearchTree(TreeNode root) {
    return checkSearchTreeProcess(root).isSearchTree;
  }

  private static SearchTreeInfo checkSearchTreeProcess(TreeNode root) {
    if (root == null) {
      return null;
    }
    SearchTreeInfo leftInfo = checkSearchTreeProcess(root.left);
    SearchTreeInfo rightInfo = checkSearchTreeProcess(root.right);

    int min = leftInfo == null ? root.value : Math.min(root.value, leftInfo.min);
    int max = rightInfo == null ? root.value : Math.max(root.value, rightInfo.max);

    boolean isBST = true;
    if (leftInfo != null && (!leftInfo.isSearchTree || leftInfo.max >= root.value)) {
      isBST = false;
    }
    if (rightInfo != null && (!rightInfo.isSearchTree || rightInfo.min <= root.value)) {
      isBST = false;
    }
    return new SearchTreeInfo(isBST, min, max);
  }

  public static class SearchTreeInfo {

    boolean isSearchTree;
    int min;
    int max;

    public SearchTreeInfo(boolean isSearchTree, int min, int max) {
      this.isSearchTree = isSearchTree;
      this.min = min;
      this.max = max;
    }
  }

  // (3) 二叉树最大距离
  public static int maxDist(TreeNode root) {
    return maxDistProcess(root).maxDist;
  }

  private static distanceInfo maxDistProcess(TreeNode root) {
    if (root == null) {
      return new distanceInfo(0, 0);
    }
    distanceInfo leftInfo = maxDistProcess(root.left);
    distanceInfo rightInfo = maxDistProcess(root.right);
    int height = Math.max(leftInfo.height, rightInfo.height) + 1;
    // 三种可能性：左子树中存在最大距离；右子树中存在最大距离；最大距离为从左子树某一节点到右子树某一节点。
    // 取三种可能性的最大值
    int maxDist = Math.max(leftInfo.height + rightInfo.height + 1,
        (Math.max(leftInfo.maxDist, rightInfo.maxDist)));
    return new distanceInfo(maxDist, height);
  }

  public static class distanceInfo {

    int maxDist;
    int height;

    public distanceInfo(int maxDistance, int height) {
      this.maxDist = maxDistance;
      this.height = height;
    }
  }

  // (4) 判断满二叉树
  public static boolean isFull(TreeNode root) {
    if (root == null) {
      return true;
    }
    return isFullProcess(root).isFull;
  }

  private static isFullInfo isFullProcess(TreeNode root) {
    if (root == null) {
      return new isFullInfo(true, 0);
    }
    isFullInfo leftInfo = isFullProcess(root.left);
    isFullInfo rightInfo = isFullProcess(root.right);
    boolean isFull = leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height;
    return new isFullInfo(isFull, Math.max(leftInfo.height, rightInfo.height) + 1);
  }

  public static class isFullInfo {

    boolean isFull;
    int height;

    public isFullInfo(boolean isFull, int height) {
      this.isFull = isFull;
      this.height = height;
    }
  }

  // (5) 子树搜索二叉树最大节点数
  public static int maxSubBSTSize(TreeNode root) {
    if (root == null) {
      return 0;
    }
    return maxSubBSTSizeProcess(root).maxSubBSTSize;
  }

  private static maxSubBSTSizeInfo maxSubBSTSizeProcess(TreeNode root) {
    if (root == null) {
      return null;
    }
    maxSubBSTSizeInfo leftInfo = maxSubBSTSizeProcess(root.left);
    maxSubBSTSizeInfo rightInfo = maxSubBSTSizeProcess(root.right);
    int max = root.value;
    int min = root.value;
    int fullSize = 1;

    // possibility 1 and 2: 最大搜索子树可能存在在左子树中或右子树中
    // possibility 3: 以当前root节点为头的整棵树为二叉搜索树
    int p1 = -1;
    int p2 = -1;
    int p3 = -1;

    boolean leftBST = true;
    boolean rightBST = true;
    if (leftInfo != null) {
      min = Math.min(min, leftInfo.min);
      max = Math.max(max, leftInfo.max);
      fullSize += leftInfo.fullSize;
      p1 = leftInfo.maxSubBSTSize;
      // 节省一个变量，通过如下判断该子树是否为搜索二叉树
      leftBST = leftInfo.maxSubBSTSize == leftInfo.fullSize;
    }
    if (rightInfo != null) {
      min = Math.min(min, rightInfo.min);
      max = Math.max(max, rightInfo.max);
      fullSize += rightInfo.fullSize;
      p2 = rightInfo.maxSubBSTSize;
      rightBST = rightInfo.maxSubBSTSize == rightInfo.fullSize;
    }

    if (leftBST && rightBST) {
      boolean leftCond = leftInfo == null || (leftInfo.max < root.value);
      boolean rightCond = rightInfo == null || (rightInfo.min > root.value);
      if (leftCond && rightCond) {
        p3 = fullSize;
      }
    }
    return new maxSubBSTSizeInfo(Math.max(p3, Math.max(p1, p2)), fullSize, min, max);
  }

  public static class maxSubBSTSizeInfo {

    int maxSubBSTSize;
    int fullSize;
    int min;
    int max;

    public maxSubBSTSizeInfo(int maxSubBSTSize, int fullSize, int min, int max) {
      this.maxSubBSTSize = maxSubBSTSize;
      this.fullSize = fullSize;
      this.min = min;
      this.max = max;
    }
  }

  // (6) 判断完全二叉树
  public static boolean isComplete(TreeNode root) {
    return isCompleteProcess(root).isComplete;
  }

  private static CompleteInfo isCompleteProcess(TreeNode root) {
    if (root == null) {
      return new CompleteInfo(true, true, 0);
    }
    CompleteInfo leftInfo = isCompleteProcess(root.left);
    CompleteInfo rightInfo = isCompleteProcess(root.right);

    boolean isComplete = false;
    boolean isFull = leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height;
    int height = Math.max(leftInfo.height, rightInfo.height) + 1;

    if (leftInfo.isComplete && rightInfo.isFull && leftInfo.height == rightInfo.height + 1) {
      isComplete = true;
    } else if (leftInfo.isFull && rightInfo.isComplete && leftInfo.height == rightInfo.height) {
      isComplete = true;
    } else if (leftInfo.isFull && rightInfo.isFull && (leftInfo.height == rightInfo.height
        || leftInfo.height == rightInfo.height + 1)) {
      isComplete = true;
    }
    return new CompleteInfo(isComplete, isFull, height);
  }

  public static class CompleteInfo {

    boolean isComplete;
    boolean isFull;
    int height;

    public CompleteInfo(boolean isComplete, boolean isFull, int height) {
      this.isComplete = isComplete;
      this.isFull = isFull;
      this.height = height;
    }
  }

  // (7) 两个节点最低公共祖先问题
  public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode a, TreeNode b) {
    return commonAncestorProcess(root, a, b).lowestCommonAncestor;
  }

  private static commonAncestorInfo commonAncestorProcess(TreeNode root, TreeNode a, TreeNode b) {
    if (root == null) {
      return new commonAncestorInfo(false, false, null);
    }
    commonAncestorInfo leftInfo = commonAncestorProcess(root.left, a, b);
    commonAncestorInfo rightInfo = commonAncestorProcess(root.right, a, b);
    boolean existA = leftInfo.existA || rightInfo.existA || root == a;
    boolean existB = leftInfo.existB || rightInfo.existB || root == b;

    TreeNode lowestCommonAncestor = null;

    if (leftInfo.lowestCommonAncestor != null) {
      lowestCommonAncestor = leftInfo.lowestCommonAncestor;
    } else if (rightInfo.lowestCommonAncestor != null) {
      lowestCommonAncestor = rightInfo.lowestCommonAncestor;
    } else if (existA && existB) {
      lowestCommonAncestor = root;
    }
    return new commonAncestorInfo(existA, existB, lowestCommonAncestor);
  }

  public static class commonAncestorInfo {

    boolean existA;
    boolean existB;
    TreeNode lowestCommonAncestor;

    public commonAncestorInfo(boolean existA, boolean existB,
        TreeNode lowestCommonAncestor) {
      this.existA = existA;
      this.existB = existB;
      this.lowestCommonAncestor = lowestCommonAncestor;
    }
  }


  // 判断是否完全二叉树对数器
  public static void main(String[] args) {
    int maxLevel = 8;
    int maxValue = 100;
    int testAmount = 100000;
    System.out.println("测试开始");
    for (int i = 0; i < testAmount; i++) {
      TreeNode root = binaryTreeGenerator(maxLevel, maxValue);
      boolean r1 = isComplete(root);
      boolean r2 = Lecture11.isComplete(root);
      if (r1 != r2) {
        System.out.println("测试失败");
        break;
      }
    }
    System.out.println("测试结束");
  }
}
