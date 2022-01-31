package 左程云体系学习班;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import 左程云体系学习班.Lecture03.StackImplementationQueue.QueueImplementationStack;

public class Lecture03 {

  /*
   * 1.链表(单链表，双链表)
   * 1.1 单链表和双链表的反转
   * 1.2 把给定的值都删除
   *
   * 2.栈和队列
   * 栈:数据先进后出；队列:数据先进先出
   * 2.1 实际实现方法：双向链表实现；数组实现
   *
   * 2.2 用两个常规栈实现一种新的栈使这种栈获得最小值的时间复杂度为O(1)
   *
   * 2.3 用栈实现队列:用两个栈，一个叫pushStack另一个叫popStack，两个栈初始化都是空的。当push队列时，push到pushStack，
   * 当第一次pop时，因为队列遵循先进先出，把所有pushStack中的东西倒入popStack，顺序就正确了，可以正常pop。接下来
   * 如果继续push的话就push到pushStack，pop的话就从popStack中pop，直到popStack为空。此时再重复操作。
   * 注意：从pushStack倒入popStack要一次性倒完；只有当popStack为空时才能讲pushStack中的数倒入popStack。
   *
   * 2.4 用队列实现栈:用两个队列，每次push时push到其中一个队列中，另外一个为空。要pop时，因为遵循先进先出，
   * 将数据前n-1个元素倒入空队列中，返回剩下最后一个元素。如此这个队列就为空了，下次push时就加入另一个元素多的队列，重复操作。
   *
   * 3. 递归
   * 任何递归都能改成非递归；把调用的过程画出结构图是必须的，有利于分析递归；
   * 3.1 Master公式：分析递归函数时间复杂度，只针对子递归规模一致的情况下使用
   * 形如 T(N) = a * T(N/b) + O(N^d) 其中a，b，c都是常数 -> if log(b, a) < d then O(N^d)
   *                                                    if log(b, a) > d then O(N^log(b, a))
   *                                                    if log(b, a) == d then O(N^d * logN)
   *
   * 4. 哈希表(HashMap)和有序表(TreeMap)
   * 哈希表的增删改查都是常数时间操作，跟内部数据量无关，不过常数有点大。哈希表在记录非基础类型元素时，占用空间非常少，
   * 因为只记录内存地址(8字节)。在判断哈希表内是否含有某一个key，是使用.equals()来判断的。
   * 有序表为接口名，具体可以用红黑树，AVL树，SizeBalance树，跳表实现。有序表的增删改查的时间复杂度为O(logN)，N为内部
   * 数据量。Java中的TreeMap由红黑树实现。有序表能实现哈希表的功能，并且能实现所有key按序组织。对于非基础类型，需要提供
   * 比较器(comparator)。
   * */

  //单链表
  public static class Node {

    int value;
    Node next;

    public Node(int value) {
      this.value = value;
    }
  }

  //双链表
  public static class DoublyLinkedNode {

    private int value;
    private DoublyLinkedNode last;
    private DoublyLinkedNode next;

    public DoublyLinkedNode(int value) {
      this.value = value;
    }
  }

  //1.1 单链表和双链表的反转
  public static Node reverseLinkedList(Node head) {
    Node pre = null;
    Node cur = null;

    while (head != null) {
      cur = head;
      head = head.next;
      cur.next = pre;
      pre = cur;
    }
    return pre;
  }

  public static DoublyLinkedNode reverseDoublyLinkedList(DoublyLinkedNode head) {
    DoublyLinkedNode pre = null;
    DoublyLinkedNode cur = null;

    while (head != null) {
      cur = head;
      head = head.next;
      cur.next = pre;
      cur.last = head;
      pre = cur;
    }
    return pre;
  }

  //1.2 把给定的值都删除
  public static Node removeValue(Node head, int value) {
    //最前面可能有连续需要删除的node
    while (head != null) {
      if (head.value != value) {
        break;
      }
      head = head.next;
    }

    Node pre = head;
    Node cur = head;
    while (cur != null) {
      if (cur.value == value) {
        pre.next = cur.next;
      } else {
        pre = cur;
      }
      cur = cur.next;
    }
    return head;
  }

  //2.1 用数组实现队列，并且长度固定，超出限定长度报错。(其余具体实现方法略过)
  public static class MyQueue {

    private final int[] arr;
    private int begin;
    private int end;
    private int size;
    private final int limit;

    public MyQueue(int limit) {
      this.arr = new int[limit];
      this.begin = 0;
      this.end = 0;
      this.size = 0;
      this.limit = limit;
    }

    public void push(int value) {
      if (size == limit) {
        throw new RuntimeException("Queue is full, cannot push anymore!");
      }
      size++;
      arr[end] = value;
      end = end < limit - 1 ? end + 1 : 0;
    }

    public int pop() {
      if (size == 0) {
        throw new RuntimeException("Queue is empty, cannot pop anymore!");
      }
      size--;
      int result = arr[begin];
      begin = begin < limit - 1 ? begin + 1 : 0;
      return result;
    }
  }

  //2.2 现在需要另一种stack使得获得stack中获得最小值的时间复杂度为O(1)
  public static class GetMinConstantTimeStack {

    private final Stack<Integer> stackData;
    private final Stack<Integer> stackMin;

    public GetMinConstantTimeStack() {
      this.stackData = new Stack<>();
      this.stackMin = new Stack<>();
    }

    public void push(int value) {
      if (this.stackMin.isEmpty()) {
        this.stackMin.push(value);
      } else if (value < this.getMin()) {
        this.stackMin.push(value);
      } else {
        this.stackMin.push(this.getMin());
      }
      this.stackData.push(value);
    }

    public int pop() {
      if (this.stackData.isEmpty()) {
        throw new RuntimeException("Your stack is empty.");
      }
      this.stackMin.pop();
      return this.stackData.pop();
    }

    public int getMin() {
      if (this.stackMin.isEmpty()) {
        throw new RuntimeException("Your stack is empty.");
      }
      return this.stackMin.peek();
    }
  }

  // 2.3 用栈实现队列
  public static class StackImplementationQueue {

    Stack<Integer> stackPush;
    Stack<Integer> stackPop;

    public StackImplementationQueue() {
      this.stackPush = new Stack<>();
      this.stackPop = new Stack<>();
    }

    //push栈向pop栈倒入数据
    private void pushToPop() {
      if (this.stackPop.isEmpty()) {
        while (!stackPush.isEmpty()) {
          this.stackPop.push(this.stackPush.pop());
        }
      }
    }

    public void add(Integer value) {
      this.stackPush.push(value);
      pushToPop();
    }

    public int poll() {
      if (this.stackPop.isEmpty() && this.stackPush.isEmpty()) {
        throw new RuntimeException("Queue is empty");
      }
      pushToPop();
      return this.stackPop.pop();
    }

    public int peek() {
      if (this.stackPop.isEmpty() && this.stackPush.isEmpty()) {
        throw new RuntimeException("Queue is empty");
      }
      pushToPop();
      return this.stackPush.peek();
    }

    //2.4 用栈实现队列
    public static class QueueImplementationStack<T> {

      private Queue<T> queue;
      private Queue<T> help;

      public QueueImplementationStack() {
        this.queue = new LinkedList<>();
        this.help = new LinkedList<>();
      }

      public void push(T value) {
        this.queue.offer(value);
      }

      public T pop() {
        while (this.queue.size() > 1) {
          this.help.offer(this.queue.poll());
        }

        T ans = this.queue.poll();
        //交换queue和help的地址，方便区分
        Queue<T> temp = this.queue;
        this.queue = this.help;
        this.help = temp;

        return ans;
      }

      public T peek() {
        T ans = this.pop();
        queue.offer(ans);
        return ans;
      }

      public boolean isEmpty() {
        return this.queue.isEmpty();
      }
    }
  }
}
