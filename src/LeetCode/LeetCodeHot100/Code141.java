package LeetCode.LeetCodeHot100;

import java.util.HashSet;

public class Code141 {

  class ListNode {

    int val;
    ListNode next;

    ListNode(int x) {
      val = x;
      next = null;
    }
  }

    // normal method, use hashmap
    public static boolean hasCycle0(ListNode head) {
      if (head == null) {
        return false;
      }

      HashSet<ListNode> set = new HashSet<>();

      ListNode curr = head;
      while (curr != null) {
        if (set.contains(curr)) {
          return true;
        }
        set.add(curr);
        curr = curr.next;
      }

      return false;
    }

  //  龟兔赛跑算法，无额外空间复杂度
  public static boolean hasCycle(ListNode head) {
    if (head == null) {
      return false;
    }
    ListNode slow = head;
    ListNode fast = head.next;

    while (fast != null) {
      if (slow == fast) {
        return true;
      }
      slow = slow.next;
      fast = fast.next == null ? null : fast.next.next;
    }

    return false;
  }

}

