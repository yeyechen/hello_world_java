package 左程云体系学习班;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class Lecture02 {

  public static final int numBits = 32;

  /*
   * 1. 亦或运算(^): 又称无进位相加，二进制数位相同返回0，不同返回1。
   * 1.1. 性质:
   * 0 ^ N = N
   * N ^ N = 0
   * a ^ b = b ^ a
   * (a ^ b) ^ c = a ^ (b ^ c)
   *
   * 1.2 运用:
   * (1). 不申请额外变量temp，交换两数A, B的值:
   * A = A ^ B (let A = a, B = b; A = a ^ b, B = b)
   * B = A ^ B (now B = (a ^ b) ^ b = a ^ (b ^ b) = a ^ 0 = a; A = a ^ b, B = a)
   * A = A ^ B (A = (a ^ b) ^ a = (a ^ a) ^ b = 0 ^ b = b; A = b, B = a)
   * 但是运用在array里时，比如我们想交换index i和j中的数，i和j不能指向同一块内存区域，即i不能等于j。
   *
   * (2). 一个数组中有一种数出现了奇数次，其他数都出现了偶数次，找到这个数并打印:
   * 亦或所有arr里的数，利用亦或的性质，偶数次亦或相同的数等于0，奇数次亦或相同的数等于这个数。
   * arr = [..]
   * int xor = 0;
   * xor = xor ^ arr[0];
   * xor = xor ^ arr[1];
   * ...
   * xor = xor ^ arr[arr.length - 1];
   * return xor;
   *
   * (3). 怎么把一个int类型的数，把最右侧的1提取出来:
   * a = 001100100 -> return 000000100
   * a = a & ((~a) + 1) = a & (-a) "~"是取反; (~a) + 1 === -a; 至于为什么取反加一是相反数不多赘述
   * a = 001100100; ~a = 110011011; (~a) + 1 = 110011100; a & ((~a) + 1) = 000000100
   *
   * (4). (2)进阶版，一个数组中有两个数出现了奇数次，其他数都出现了偶数次，找到并打印这两个数:
   *
   * 第一步同(2): 找到xor = a ^ b, a和b在这个数组中出现了奇数次.
   * 第二步: 利用(3)找到xor最右侧的1(或者其他任何位上的1)，说明a和b在这一数位上的数不相同。再遍历一次数组，这个数组中的数分为两类，
   * 一类是这一位上的数为1，另一类是这一位上的数为0。那么假设a在这一位上的数是1(或者0，无所谓)，用一个新的变量，
   * xorA = 0 去亦或这一类的所有数，得出的结果为xorA = a。那么b = xor ^ xorA。
   *
   * (5). 一个数组中有一个数出现K次，其他数都出现了M次，并且M>1, K<M。找到出现了K次的数，要求额外空间复杂度为O(1)(不能用哈希表),
   * 时间复杂度为O(N).
   *
   * 因为我们知道K<M，我们可以遍历数组计算每一位二进制上的1总共出现了几次，那么出现K次的数的所有数位上出现1的个数肯定不是
   * M的倍数，因为K不可能是M的倍数。而其余所有其他数都出现了M次，那么不管这些数具体是什么，这些所有其他数的所有数位上1出现
   * 的次数都能被M整除，除非这个数位上包含出现K次数的数位的1。
   * 举个例子 K = 2, M = 7,
   * 那么3这个数出现了K=2次，1, 2, 4出现了M=5次。
   * arr = [2, 2, 1, 4, 4, 1, 2, 3, 1, 1, 2, 3, 4, 4, 4]
   * 申请一个额外的array int[] cnt = [0, 0, ..., 0] 共32个位置因为int是32位。
   * 那么1的二进制是 00...001, 2的二进制是00...010, 3的二进制是00...011, 4的二进制是00...100。
   * 遍历arr，把这些位上出现1的次数相加, cnt = [0, 0, ..., 5, 7, 7]
   * 那么因为 7 mod 5 != 0, 所以出现K次的数在第一位和第二位是1，由此可得这个数为3。
   *
   * */


  //(2). 一个数组中有一种数出现了奇数次，其他数都出现了偶数次，找到这个数:
  public static int findOneOddTimesNum(int[] arr) {
    int xor = 0;
    for (int n : arr) {
      xor ^= n;
    }
    return xor;
  }

  //(4). (2)进阶版，一个数组中有两个数出现了奇数次，其他数都出现了偶数次，找到并打印这两个数:
  public static void printTwoOddTimesNum(int[] arr) {
    // xor = a ^ b，a和b是两个出现了奇次的数
    int xor = findOneOddTimesNum(arr);

    // 提取xor最右的1得出的数
    // 比如rightOne = 0000001000
    int rightOne = xor & (-xor);

    int xorA = 0;

    // 找到所有在rightOne这一位上为1的数，并全部亦或
    for (int n : arr) {
      if ((n & rightOne) != 0) {
        xorA ^= n;
      }
    }
    // 那么最后xorA的值即其中一个出现奇数次的数a，另外一个数b = (a ^ b) ^ a = xor ^ xorA
    System.out.println(xorA + ", " + (xor ^ xorA));
  }

  // (5). 一个数组中有一个数出现K次，其他数都出现了M次，并且M>1, K<M。找到出现了K次的数，要求额外空间复杂度为O(1)(不能用哈希表),
  // 时间复杂度为O(N).
  public static int kTimesNum(int[] arr, int k, int m) {
    int[] bitCount = new int[numBits];
    for (int n : arr) {
      //把arr里的数数所有数位上的1都加在bitCount里
      for (int i = 0; i < numBits; i++) {
        bitCount[i] += ((n >> i) & 1);
      }
    }
    int result = 0;
    for (int i = 0; i < numBits; i++) {
      result |= bitCount[i] % m == 0 ? 0 : 1 << i;
    }
    return result;
  }

  //实现(5)的对数器，使用任何能获得正确结果的方法，比如Hashmap
  public static int test(int[] arr, int k, int m) {
    //count -> {一个数: 这个数出现的次数}
    HashMap<Integer, Integer> count = new HashMap<>();
    for (int n : arr) {
      if (count.containsKey(n)) {
        count.put(n, count.get(n) + 1);
      } else {
        count.put(n, 1);
      }
    }
    //遍历整个map，找到这个出现k次的数
    for (Entry<Integer, Integer> entry : count.entrySet()) {
      if (entry.getValue() == k) {
        return entry.getKey();
      }
    }
    return -1;
  }

  public static void main(String[] args) {


    int testAmount = 100000;
    int maxValue = 100; //所有数在-100 ~ 100范围内
    int numKinds = 10; //有7个数出现了M次，一个数出现了K次

    System.out.println("测试开始");

    for (int i = 0; i < testAmount; i++) {
      //remember: Math.random() -> [0, 1)
      int a = (int) (Math.random() * 9) + 1; // a:[1, 9]
      int b = (int) (Math.random() * 9) + 1; // b:[1, 9]
      //k:[1, 9], m:[1, 10]
      int k = Math.min(a, b);
      int m = Math.max(a, b);
      if (k == m) {
        m++;
      }

      int[] arr = randomArrayGenerator(numKinds, maxValue, k, m);
      int result = kTimesNum(arr, k, m);
      int test = test(arr, k, m);

      if (result != test) {
        System.out.println("测试失败");
        break;
      }
    }
    System.out.println("测试结束");
  }

  private static int[] randomArrayGenerator(int numKinds, int range, int k, int m) {
    int[] arr = new int[k + (numKinds - 1) * m];
    int theOne = (int) (Math.random() * (range + 1)) - (int) (Math.random() * (range + 1));

    //Set防止重复
    Set<Integer> set = new HashSet<>();
    set.add(theOne);
    //放入出现K次的数
    int index = 0;
    for (; index < k; index++) {
      arr[index] = theOne;
    }
    numKinds--;

    //放入其他所有出现M次的数
    while (numKinds > 0) {
      int num;
      do {
        num = (int) (Math.random() * (range + 1)) - (int) (Math.random() * (range + 1));
      } while (set.contains(num));
      set.add(num);
      numKinds--;
      for (int i = 0; i < m; i++) {
        arr[index++] = num;
      }
    }

    //打乱array:
    for (int i = 0; i < arr.length; i++) {
      //i位置的数和随机j位置的数做交换
      int j = (int) (Math.random() * arr.length); //j: [0, N-1]
      int temp = arr[i];
      arr[i] = arr[j];
      arr[j] = temp;
    }
    return arr;
  }

}
