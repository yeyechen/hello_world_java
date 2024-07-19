package 左程云体系学习班;

import static 左程云体系学习班.Lecture05.swap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Lecture06 {
  /*
   * 1.比较器：比较器的实质就是重载比较运算符；比较器可以很好的应用在特殊标准的排序上；
   *          比较器可以很好的应用在根据特殊标准排序的结构上(TreeMap)；写代码变得非常容易，还用于范型编程。
   * 1.1 compare方法遵循规范：
   * 返回负数时，认为第一个参数应该排在前面(默认谁小谁排在前面)；
   * 返回正数时，认为第二个参数应该排在前面；
   * 返回0时，认为无所谓谁放在前面。
   *
   * 1.2 可以用lambda表达式描述简单比较器：
   * 比如：(a, b) -> a.id - b.id
   *
   * 2. 堆(heap)：
   * 堆结构就是用数组实现的完全二叉树结构；(完全二叉树就是如果做节点未填满，则不能添加右节点)
   * 数组下标: [0, 1, 2, 3, 4, 5, 6, 7, 8]
   * 表示的二叉树:         0
   *                  /    \
   *                 1      2
   *               /  \    /  \            实际数组的长度可以很长，可以用一个变量heapSize来记录真正二叉树的大小
   *              3   4   5    6           一个节点i获得父节点的公式：(i-1)/2
   *            /  \                       一个节点i获得子节点的公式：2*i+1(左), 2*i+2(右)
   *           7    8
   * 完全二叉树中如果每棵子树的最大值都在顶部就是大根堆；
   * 完全二叉树中如果每棵子树的最小值都在顶部就是小根堆；
   * 任何大根堆/小根堆的子树，也都是大根堆/小根堆；
   * 当需要在任何堆结构里添加元素时，顺序调用heapInsert(上升)与heapify(下沉)操作(最多触发一个)；两个操作都是O(logN)；
   * 堆结构的增大和减少；
   * 优先级队列结构(Priority Queue)，就是堆结构。Java中的Priority Queue默认小根堆。
   *
   * 2.1 堆排序：把一个数组想象称一个完全二叉树，调整为大根堆，即下标0位置的数为数组中最大的数，
   * 将这个数跟heapSize-1所在的下标位置做交换，heapSize--，然后下标0位置的数heapify再调整为大根堆。
   * 这样的操作在堆的逻辑上删除了当前最大值(因为heapSize--了)，但在真实数组中并未删除，而是依次排在了最后。
   * 对子数组重复操作，直至 heapSize = 0。
   *
   * 2.2 小于k距离排序问题：排序一个无序数组，但是这个无序数组中的每一个数当前位置距离排序好的位置不超过k。
   * 举个例子，最小值位置0应该出现的数距离不超过0这个数k，那么就可以利用小根堆在0～k这个范围找0位置上的数，找到后交换。
   * 那么接下来1位置的数也不会超过k距离。重复操作。这个题解的时间复杂度为O(N*logK)，会优于O(N*logN)，当然因为数组中有额外条件限制。
   * */

  //1.比较器
  public static class Student {

    private int id;
    private int age;

    public Student(int id, int age) {
      this.id = id;
      this.age = age;
    }
  }

  public static class IdAscendingAgeDescending implements Comparator<Student> {

    //先根据id从小到大，如果id一样，按照年龄从大到小
    @Override
    public int compare(Student o1, Student o2) {
      return o1.id == o2.id ? o2.age - o1.age : o1.id - o2.id;
    }
  }


  //2.堆 (大根堆)
  // 给定一个array和一个index，让index上的数层层向上比较，让该数维持大根堆逻辑
  private static void heapInsert(int[] arr, int index) {
    //每次都跟自己的父节点做比较，如果比父节点大，交换。重复操作直到小于等于当前父节点，或者已经到顶
    while (arr[index] > arr[(index - 1) / 2]) {
      swap(arr, index, (index - 1) / 2);
      index = (index - 1) / 2;
    }
  }

  // 给定一个array和一个index，让index上的数层层向下比较，让该数维持大根堆逻辑
  private static void heapify(int[] arr, int index, int heapSize) {
    int left = index * 2 + 1;

    // 每次都和自己较大的子节点做比较，如果小于较大的子节点，交换。重复操作直到大于等于当前较大子节点，或已经越界
    // leftChild越界的话，rightChild也必定越界
    while (left < heapSize) {
      // 把leftChild和rightChild中值较大的下标赋给largest，但可能不存在rightChild
      int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
      // 如果index的数比较大的子节点还要大，说明不需要再下沉，退出loop
      largest = arr[largest] > arr[index] ? largest : index;
      if (largest == index) {
        break;
      }
      // 如果index的数比较大的子节点小，需要继续下沉；index和较大子节点互换
      swap(arr, largest, index);
      index = largest;
      left = index * 2 + 1;
    }
  }

  //2.1 堆排序
  public static void heapSort(int[] arr) {
    if (arr == null || arr.length < 2) {
      return;
    }
    // 先把arr调整为大根堆

    // 第一种方式，从上往下建堆，复杂度为O(N*logN)
    // 因为每一次heapInsert的时间复杂度是O(logN)，需要insert N次
    // for (int i = 0; i < arr.length; i++) {
    //   heapInsert(arr, i);
    // }
    // 第二种方式，从下往上建立堆，复杂度为O(N)
    // facts：一个总节点数为N的堆(完全二叉树)，叶节点(leaf nodes)的数量为N/2，叶节点往上(倒数第二层)的节点数量为N/4
    // 以此类推N/8, N/16... 叶节点heapify时不需要任何交换，因为已经在最底了。倒数第二层最多交换一次，倒数第三层
    // 最多交换两次，以此类推... 如果把if判断也算一次操作的话，总的时间复杂度需要算上叶节点，反之则不用。
    // 那么总的时间复杂度为：T(N) = N/2 + N/4 * 2 + N/8 * 3 + N/16 * 4 +... 等比数列，复杂度会收敛为O(N)
    for (int i = arr.length - 1; i >= 0; i--) {
      heapify(arr, i, arr.length);
    }
    // 最后一个步骤依次拿出大根堆0位置的最大数，放入一个新的arr也好，原arr也好，从而完成排序
    // 把大根堆0位置的数(最大的数)与最后的位置交换，然后固定住不动因为heapSize缩小了
    int heapSize = arr.length;
    swap(arr, 0, --heapSize);
    while (heapSize > 0) {
      heapify(arr, 0, heapSize);
      swap(arr, 0, --heapSize);
    }
  }

  //2.2 小于k距离排序问题
  public static void sortArrayLessK(int[] arr, int k) {
    if (arr == null || arr.length < 2) {
      return;
    }
    if (k <= 0) {
      return;
    }
    PriorityQueue<Integer> heap = new PriorityQueue();
    int index = 0;
    //0..k上的数加到小根堆里，那么0位置上的数自动是数组中最小的数，因为k的限制
    for (; index < Math.min(arr.length, k+1); index++) {
      heap.add(arr[index]);
    }
    int i = 0;
    //接下来寻找1..k+1; 2..k+2; ...中最小的数，然后放入对应的i中
    for (; index < arr.length; i++, index++) {
      arr[i] = heap.poll();
      heap.add(arr[index]);
    }
    //index到头了，heap中还有剩余的数
    while (!heap.isEmpty()) {
      arr[i++] = heap.poll();
    }
  }

  public static void main(String[] args) {
    int testAmount = 100000;
    int maxValue = 100;
    int maxLen = 60;

    System.out.println("Test begin!");
    for (int i = 0; i < testAmount; i++) {
      int k = 5;
      int[] arr = randomArrayNoMoveMoreK(maxLen, maxValue, k);
      int[] arr1 = Arrays.copyOf(arr, arr.length);
      int[] arr2 = Arrays.copyOf(arr, arr.length);
      Arrays.sort(arr1);
      sortArrayLessK(arr2, k);
      if (!Arrays.equals(arr1, arr2)) {
        System.out.println("error");
        break;
      }
    }
    System.out.println("Test finished");
  }

  public static int[] randomArrayNoMoveMoreK(int maxSize, int maxValue, int K) {
    int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
    }
    // 先排个序
    Arrays.sort(arr);
    // 然后开始随意交换，但是保证每个数距离不超过K
    // swap[i] == true, 表示i位置已经参与过交换
    // swap[i] == false, 表示i位置没有参与过交换
    boolean[] isSwap = new boolean[arr.length];
    for (int i = 0; i < arr.length; i++) {
      int j = Math.min(i + (int) (Math.random() * (K + 1)), arr.length - 1);
      if (!isSwap[i] && !isSwap[j]) {
        isSwap[i] = true;
        isSwap[j] = true;
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
      }
    }
    return arr;
  }

  private static int[] randomArrayGenerator(int maxLen, int maxValue) {
    int len = (int) (Math.random() * maxLen) + 1;
    int[] result = new int[len];
    for (int i = 0; i < len; i++) {
      result[i] = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * maxValue);
    }
    return result;
  }
}
