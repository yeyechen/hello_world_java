package 左程云体系学习班;

import java.util.Stack;
import 左程云体系学习班.Lecture03.Node;

public class Lecture09 {
  /*
   * 链表相关面试问题
   *
   * 1. 获得链表中的中间节点：可以用快慢指针，快指针每次跳两个node，慢指针每次跳一个node，当快指针跳完了，慢指针所在位置就是中间位置。
   * 或者用额外容器(array)，把链表遍历并逐个值放入容器中，求出容器中间位置的值。但是会使用额外空间。
   *
   * 2. 检查单链表是否为回文结构：用容器的话(栈)非常简单，不赘述。不使用容器，实现O(1)额外空间复杂度。在内部实现翻转，然后逐个node检查。
   *
   * 3. 将单链表按某值划分成左边小，中间相等，右边大的形式：把链表放入数组中，在数组上做partition；或分成小中大三个部分，
   * 再把各个部分之间串起来。
   *
   * 4. (leetcode第138题)一种特殊的单链表，其中新增一个random指针，指向链表中随机的node，也可以指向null。那么给定一个链表的头节点
   * 请实现一个函数完成这个链表的复制，并返回复制的新链表的头节点。
   * 第一种方法用map做，先遍历一次链表，复制值放入新的节点中，一一对应。再遍历一遍链表，按照map中对应的节点把next指针和
   * random指针串好。
   * 第二种方法不使用容器。将复制的节点嵌在原来链表中，也能达到一一对应的效果。
   *
   * 5. 给定两个可能有环也可能无环的单链表，头节点head1和head2。请实现一个函数，如果两个链表相交，请返回相交的第一个节点。
   * 如果不相交，返回null。要求时间复杂度达到O(N)，额外空间复杂度达到O(1)。如果没有额外空间复杂度的限制，可以用额外容器map，
   * 会比较容易实现。
   *
   * */

  // 1. 获得链表中中间点的位置(如果数量为偶数，返回中间两个中的前一个node)
  public static Node getMidPosition(Node head) {
    //链表必须有三个或以上的节点
    if (head == null || head.next == null || head.next.next == null) {
      return head;
    }
    Node slow = head;
    Node fast = head;
    while (fast.next != null && fast.next.next != null) {
      slow = slow.next;
      fast = fast.next.next;
    }
    return slow;
  }

  //2. 使用栈
  public static boolean isPalindrome1(Node head) {
    Stack<Node> stack = new Stack<>();
    Node node = head;
    while (node != null) {
      stack.push(node);
      node = node.next;
    }
    while (head != null) {
      if (stack.pop().value != head.value) {
        return false;
      }
      head = head.next;
    }
    return true;
  }

  //2. 不使用容器，实现O(1)额外空间复杂度。在内部实现翻转，然后逐个node检查。
  public static boolean isPalindrome2(Node head) {
    //因为求链表中点需要长度为3及以上
    if (head == null || head.next == null) {
      return true;
    }
    Node mid = getMidPosition(head);
    Node curr = mid.next;
    Node next = curr.next;
    mid.next = null;
    while (curr != null) {
      curr.next = mid;
      mid = curr;
      curr = next;
      //边界检查：如果next变为null了，我们还是需要让curr往右移到最后一个node
      next = next == null ? null : next.next;
    }

    //开始检查回文，此时mid为最后一个node
    Node L = head;
    Node R = mid;
    boolean result = true;
    while (L != null && R != null) {
      if (L.value != R.value) {
        result = false;
        break;
      }
      L = L.next;
      R = R.next;
    }

    //检查完毕，在返回之前需要还原这个链表
    curr = mid.next;
    next = curr.next;
    mid.next = null;
    while (curr != null) {
      curr.next = mid;
      mid = curr;
      curr = next;
      next = next == null ? null : next.next;
    }
    return result;
  }

  //3. 将单链表按某值划分成左边小，中间相等，右边大的形式。用O(1)额外空间复杂度
  public static Node partitionList(Node head, int pivot) {
    Node sH = null; // small head
    Node sT = null; // small tail
    Node eH = null; // equal head
    Node eT = null; // equal tail
    Node mH = null; // big head
    Node mT = null; // big tail
    Node next = null; // save next node
    while (head != null) {
      next = head.next;
      head.next = null;

      //把所有node按情况分到不同list里面去
      if (head.value < pivot) {

        //当头节点没有任何数时，头尾都指向这个数
        if (sH == null) {
          sH = head;
          sT = head;
        } else {
          //否则就将这个数加在尾巴的后面，让这个数变成尾巴
          sT.next = head;
          sT = head;
        }
      } else if (head.value == pivot) {
        if (eH == null) {
          eH = head;
          eT = head;
        } else {
          eH.next = head;
          eT = head;
        }
      } else {
        if (mH == null) {
          mH = head;
          mT = head;
        } else {
          mT.next = head;
          mT = head;
        }
      }
      head = next;
    }
    //三个list都分配完毕了，开始分情况连接

    //如果有小于区域，等于区域可能为空或不为空
    if (sT != null) {
      sT.next = eH;
      eT = eT == null ? sT : eT; //如果等于区域为空，就让等于区域的尾巴变成小于区域的尾巴，因为在接下来的步骤中一定
      //是等于区域的尾巴连接大于区域的头
    }
    if (eT != null) {
      eT.next = mH;
    }
    //如果小于区域和等于区域都没有，则返回大于区域的头
    return sH != null ? sH : (eH != null ? eH : mH);
  }

  //4. (leetcode第138题)不利用容器，纯纯自己写出来的
  private static class NodeRandom {

    int val;
    NodeRandom next;
    NodeRandom random;

    public NodeRandom(int val) {
      this.val = val;
      this.next = null;
      this.random = null;
    }
  }

  public static NodeRandom copyListWithRandom(NodeRandom head) {
    if (head == null) {
      return null;
    }

    NodeRandom curr = head;
    NodeRandom next = head.next;

    //第一次遍历建立对应关系
    while (curr != null) {
      NodeRandom copy = new NodeRandom(curr.val);
      //将curr的复制放在curr和next中间
      curr.next = copy;
      copy.next = next;
      curr = next;
      next = next == null ? null : next.next;
    }
    //第二次遍历复制random指针
    curr = head;
    next = head.next.next;
    while (curr != null) {
      curr.next.random = curr.random == null ? null : curr.random.next;
      curr = next;
      next = next == null ? null : next.next.next;
    }
    //第三次遍历分离原链表和复制链表
    curr = head;
    next = head.next.next;
    NodeRandom copy = head.next;
    while (curr != null) {
      curr.next.next = next == null ? null : next.next;
      curr.next = next;
      curr = next;
      next = next == null ? null : next.next.next;
    }
    return copy;
  }

  //5. 分3种情况：
  //   a.两个链表都无环
  //   b.两个链表都有环
  //   c.一个链表有环一个链表无环，这种情况是不可能相交都，因为next指针只有一个。如果相交，相交后的部分只有一条路线，
  //     不可能交叉分出两条。
  public static Node getIntersectNode(Node head1, Node head2) {
    if (head1 == null || head2 == null) {
      return null;
    }
    Node loopNode1 = getLoopNode(head1);
    Node loopNode2 = getLoopNode(head2);

    if (loopNode1 == null && loopNode2 == null) {
      return noLoopGetIntersect(head1, head2);
    } else if (loopNode1 != null && loopNode2 != null) {
      return bothLoopGetIntersect(head1, loopNode1, head2, loopNode2);
    } else {
      return null;
    }
  }

  private static Node noLoopGetIntersect(Node head1, Node head2) {
    if (head1 == null || head2 == null) {
      return null;
    }
    //先把两个链表都遍历一遍，如果他们最后的尾节点不相同，则不相交。同时记录下他们的长度差.
    Node curr1 = head1;
    int n = 0;
    while (curr1.next != null) {
      curr1 = curr1.next;
      n++;
    }
    Node curr2 = head2;
    while (curr2.next != null) {
      curr2 = curr2.next;
      n--;
    }
    if (curr1 != curr2) {
      return null;
    }

    //curr1现在是长的链表，curr2是短的链表
    curr1 = n > 0 ? head1 : head2;
    curr2 = curr1 == head1 ? head2 : head1;
    //让长链表先多走n步，最后一定会相遇
    n = Math.abs(n);
    while (n != 0) {
      curr1 = curr1.next;
      n--;
    }
    while (curr1 != curr2) {
      curr1 = curr1.next;
      curr2 = curr2.next;
    }
    return curr1;
  }

  private static Node bothLoopGetIntersect(Node head1, Node loopNode1, Node head2,
      Node loopNode2) {
    Node curr1 = null;
    Node curr2 = null;

    //如果两个链表入环点相同，那么和上一种情况类似，记录下长度差让长链表多走n步，最后一定会相遇在相交点。
    if (loopNode1 == loopNode2) {
      curr1 = head1;
      int n = 0;
      while (curr1 != loopNode1) {
        curr1 = curr1.next;
        n++;
      }
      curr2 = head2;
      while (curr2 != loopNode2) {
        curr2 = curr2.next;
        n--;
      }
      //curr1现在是长的链表，curr2是短的链表
      curr1 = n > 0 ? head1 : head2;
      curr2 = curr1 == head1 ? head2 : head1;
      //让长链表先多走n步，最后一定会相遇
      n = Math.abs(n);
      while (n != 0) {
        curr1 = curr1.next;
        n--;
      }
      while (curr1 != curr2) {
        curr1 = curr1.next;
        curr2 = curr2.next;
      }
      return curr1;
    } else { //如果两个链表入环点不同，那么又分两种情况。a.两个链表根本没有相交；b.两个链表相交，但是各自入环(画个图会更理解)
      //让current node 1 从loop node 1绕一圈，看看会不会碰到loop node 2。如果碰到说明是b情况，如果没碰到说明是a情况。
      curr1 = loopNode1.next;
      while (curr1 != loopNode1) {
        if (curr1 == loopNode2) {
          return loopNode1;
        }
        curr1 = curr1.next;
      }
      return null;
    }
  }
  //helper:在一个链表中，找到链表的第一个入环点。利用快慢指针，快指针每次跳两步，慢指针每次跳一步。如果快指针跳到null了，
  //说明链表无环。如果链表有环，两个指针最后肯定会在一个节点上重合。然后让其中任意一个指针回到头节点，另一个留在相遇的节点。

  //两个节点都每次走一步，在下一次相遇的节点就是第一个入环点。(不需要管证明，记住就好)
  public static Node getLoopNode(Node head) {
    if (head == null || head.next == null || head.next.next == null) {
      return null;
    }
    Node slow = head.next;
    Node fast = head.next.next;

    while (fast != slow) {
      if (fast.next == null || fast.next.next == null) {
        return null;
      }
      slow = slow.next;
      fast = fast.next.next;
    }
    //让快指针回到头部
    fast = head;
    while (fast != slow) {
      slow = slow.next;
      fast = fast.next;
    }
    //当他们再次相遇，这个节点就是第一个入环节点
    return fast;
  }
  public static void main(String[] args) {
    // 1->2->3->4->5->6->7->null
    Node head1 = new Node(1);
    head1.next = new Node(2);
    head1.next.next = new Node(3);
    head1.next.next.next = new Node(4);
    head1.next.next.next.next = new Node(5);
    head1.next.next.next.next.next = new Node(6);
    head1.next.next.next.next.next.next = new Node(7);

    // 0->9->8->6->7->null
    Node head2 = new Node(0);
    head2.next = new Node(9);
    head2.next.next = new Node(8);
    head2.next.next.next = head1.next.next.next.next.next; // 8->6
    System.out.println(getIntersectNode(head1, head2).value);

    // 1->2->3->4->5->6->7->4...
    head1 = new Node(1);
    head1.next = new Node(2);
    head1.next.next = new Node(3);
    head1.next.next.next = new Node(4);
    head1.next.next.next.next = new Node(5);
    head1.next.next.next.next.next = new Node(6);
    head1.next.next.next.next.next.next = new Node(7);
    head1.next.next.next.next.next.next = head1.next.next.next; // 7->4

    // 0->9->8->2...
    head2 = new Node(0);
    head2.next = new Node(9);
    head2.next.next = new Node(8);
    head2.next.next.next = head1.next; // 8->2
    System.out.println(getIntersectNode(head1, head2).value);

    // 0->9->8->6->4->5->6..
    head2 = new Node(0);
    head2.next = new Node(9);
    head2.next.next = new Node(8);
    head2.next.next.next = head1.next.next.next.next.next; // 8->6
    System.out.println(getIntersectNode(head1, head2).value);

  }
}
