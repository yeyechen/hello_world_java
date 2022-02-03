import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class LeetCode {

  public static boolean judgePoint24(int[] cards) {
    List<Double> copyCards = new ArrayList<>();
    for (int n : cards) {
      copyCards.add((double) n);
    }
    return judge(copyCards, "+-*/", 1e-6);
  }

  private static boolean judge(List<Double> cards, String ops, double EPSILON) {
    if (cards.size() == 0) {
      return false;
    }
    if (cards.size() == 1 && Math.abs(cards.get(0) - 24) < EPSILON) {
      return true;
    }
    if (cards.size() == 1) {
      return false;
    }

    for (int i = 0; i < cards.size() - 1; i++) {
      for (int j = i+1; j < cards.size(); j++) {
        double n1 = cards.get(i);
        double n2 = cards.get(j);
        List<Double> subCards = new ArrayList<>();
        for (int k = 0; k < cards.size(); k++) {
          if (k != i && k != j) {
            subCards.add(cards.get(k));
          }
        }
        for (char op : ops.toCharArray()) {

          switch (op) {
            case ('+'):
              subCards.add(n1 + n2);
              if (judge(subCards, ops, EPSILON)) {
                return true;
              }
              subCards.remove(subCards.size() - 1);
            case ('-'):
              subCards.add(n1 - n2);
              if (judge(subCards, ops, EPSILON)) {
                return true;
              }
              subCards.remove(subCards.size() - 1);

              subCards.add(n2 - n1);
              if (judge(subCards, ops, EPSILON)) {
                return true;
              }
              subCards.remove(subCards.size() - 1);
            case ('*'):
              subCards.add(n1 * n2);
              if (judge(subCards, ops, EPSILON)) {
                return true;
              }
              subCards.remove(subCards.size() - 1);
            case ('/'):

              if (n1 == 0 && n2 == 0) {
                continue;
              } else if (n1 == 0 || n2 == 0) {
                subCards.add((double) 0);
                if (judge(subCards, ops, EPSILON)) {
                  return true;
                }
                subCards.remove(subCards.size() - 1);
              } else {
                subCards.add(n1 / n2);
                if (judge(subCards, ops, EPSILON)) {
                  return true;
                }
                subCards.remove(subCards.size() - 1);

                subCards.add(n2 / n1);
                if (judge(subCards, ops, EPSILON)) {
                  return true;
                }
                subCards.remove(subCards.size() - 1);
              }
          }
        }
      }
    }
    return false;
  }

  public static List<List<String>> solveNQueens(int n) {
    List<List<String>> result = new ArrayList<>();
    Map<Integer, Integer> map = new HashMap<>();
    Set<Integer> d1 = new HashSet<>();
    Set<Integer> d2 = new HashSet<>();
    solve(n, 0, map, result, d1, d2);
    return result;
  }

  private static void solve(int n, int y, Map<Integer, Integer> solution,
      List<List<String>> result, Set<Integer> diagonals1, Set<Integer> diagonals2) {

    if (y == n) {
      String[] board = new String[n];
      for (int col : solution.keySet()) {
        int row = solution.get(col);
        StringBuilder rowRep = new StringBuilder();
        rowRep.append(".".repeat(Math.max(0, col)));
        rowRep.append("Q");
        rowRep.append(".".repeat(Math.max(0, n - (col + 1))));
        board[row] = rowRep.toString();
      }

      result.add(new ArrayList<String>(List.of(board)));
      return;
    }

    for (int i = 0; i < n; i++) {
      if (solution.get(i) != null) {
        continue;
      }
      int d1 = y - i;
      int d2 = y + i;
      if (diagonals1.contains(d1) || diagonals2.contains(d2)) {
        continue;
      }
      solution.put(i, y);
      diagonals1.add(d1);
      diagonals2.add(d2);
      solve(n, y + 1, solution, result, diagonals1, diagonals2);

      solution.remove(i, y);
      diagonals1.remove(d1);
      diagonals2.remove(d2);
    }
  }

  public static String[] permutation(String s) {
    List<String> result = new ArrayList<>();
    String temp = "";
    Set<String> output = new LinkedHashSet<>(permute(s, temp, result));

    String[] convert = new String[output.size()];
    int i = 0;
    for (String c : output) {
      convert[i] = c;
      i++;
    }
    return convert;
  }

  private static List<String> permute(String s, String temp, List<String> result) {
    if (s.isEmpty()) {
      result.add(temp);
      return result;
    }
    if (s.length() == 1) {
      temp += s;
      s = s.substring(1, s.length());
      permute(s, temp, result);
    }
    for (int i = 0; i < s.length(); i++) {
      permute(s.substring(0, i) + s.substring(i + 1, s.length()), temp+s.charAt(i), result);
    }
    return result;
  }

  public static String simplifyPath(String path) {
    String curr = "";
    Stack<String> stack = new Stack<>();
    for (String s : path.split("/")) {
      if (s.isEmpty() || s.equals(".")) {
        continue;
      } else if (s.equals("..")) {
        if (!stack.isEmpty()) {
          stack.pop();
        }
      } else {
        stack.push(s);
      }
    }
    if (stack.isEmpty()) {
      return "/";
    }
    StringBuilder result = new StringBuilder();
    for (String s : stack) {
      if (s.isEmpty()) {
        continue;
      }
      result.append("/");
      result.append(s);
    }
    return result.toString();
  }

  public static List<String> buildArray(int[] target, int n) {
    List<String> result = new ArrayList<>();
    int j = 0;
    for (int i = 1; i <= n; i++) {
      if (j == target.length) {
        break;
      }
      if (target[j] == i) {
        result.add("Push");
        j++;
      } else {
        result.add("Push");
        result.add("Pop");
      }
    }
    return result;
  }

  //quality = [10,20,5], wage = [70,50,30], K = 2 -> 我们向 0 号工人支付 70，向 2 号工人支付 35。
  //quality-wage ratio = [0.7, 0.4, 1/6]
  public static double mincostToHireWorkers(int[] quality, int[] wage, int k) {
    double minPossiblePayment = Integer.MAX_VALUE;

    for (int i = 0; i < quality.length; i++) {
      double totalPayment = hireWorkersHelper(quality, wage, k, i);
      minPossiblePayment = Math.min(totalPayment, minPossiblePayment);
    }

    return minPossiblePayment;
  }

  //70/10 * 20 = x
  private static double hireWorkersHelper(int[] quality, int[] wage, int k, int i) {
    List<Double> payments = new ArrayList<>();
    double totalPayment = wage[i];
    for (int j = 0; j <quality.length; j++) {
      if (j != i) {
        double payment = ((double) wage[i] / quality[i]) * quality[j];
        if (payment >= wage[j]) {
          payments.add(payment);
        } else {
          payments.add(Double.MAX_VALUE);
        }
      }
    }

    //  find k minimum payments:
    Collections.sort(payments);
    for (int j = 0; j < k - 1; j++) {
      totalPayment += payments.get(j);
    }

    return totalPayment;
  }

  public static String reverseOnlyLetters(String s) {
    Stack<Character> stack = new Stack<>();
    List<Integer> noneLetterIndex = new ArrayList<>();
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < s.length(); i++) {
      if (Character.isLetter(s.charAt(i))) {
        stack.push(s.charAt(i));
      } else {
        noneLetterIndex.add(i);
      }
    }

    for (int i = 0; i < s.length(); i++) {
      if (noneLetterIndex.contains(i)) {
        result.append(s.charAt(i));
      } else {
        result.append(stack.pop());
      }
    }

    return result.toString();
  }

  public static void gameOfLife(int[][] board) {
    int numRow = board.length;
    int numCol = board[0].length;
    int[][] newBoard = new int[numRow][numCol];
    //y-axis
    for (int i = 0; i < numRow; i++) {
      //x-axis
      for (int j = 0; j < numCol; j++) {
        int cell = board[i][j];
        int numAlive = numAlive(board, i, j);
        //cell alive
        if (cell == 1) {
          if (numAlive == 2 || numAlive == 3) {
            newBoard[i][j] = cell;
          } else {
            newBoard[i][j] = 0;
          }
        //  cell dead
        } else {
          if (numAlive == 3) {
            newBoard[i][j] = 1;
          } else {
            newBoard[i][j] = cell;
          }
        }
        }
      }
    //deep copy
    for (int i = 0; i < board.length; i++) {
      board[i] = Arrays.copyOf(newBoard[i], newBoard[i].length);
    }

  }

  private static int numAlive(int[][] board, int i, int j) {
    int[] position = {0, 1, -1};
    int numAlive = 0;
    for (int k = 0; k < position.length; k++) {
      for (int l = 0; l < position.length; l++) {
        int x = j + position[l];
        int y = i + position[k];
        if (y > -1 && y < board.length && x > -1 && x < board[0].length && board[y][x] == 1) {
          numAlive++;
        }
      }
    }
    if (board[i][j] == 1) {
      numAlive--;
    }
    return numAlive;
  }

  public int missingNumber(int[] nums) {
    int n = nums.length;

    //create all valid numbers
    List<Integer> valid = new ArrayList<>();
    for (int i = 0; i <= n; i++) {
      valid.add(i);
    }

    for (int num : nums) {
      if (valid.contains(num)) {
        valid.remove((Object) num);
      }
    }
    return valid.get(0);
  }

  public int reverse(int x) {
    long n = 0;
    while(x != 0) {
      n = n*10 + x%10;
      x = x/10;
    }
    return (int)n==n? (int)n:0;
  }

  public static void quickSort(int[] arr, int begin, int end) {
    if (begin < end) {
      int partitionIndex = partition(arr, begin, end);

      quickSort(arr, begin, partitionIndex-1);
      quickSort(arr, partitionIndex+1, end);
    }
  }
  private static int partition(int[] arr, int begin, int end) {
    int pivot = arr[end];
    int i = (begin-1);

    for (int j = begin; j < end; j++) {
      if (arr[j] <= pivot) {
        i++;

        int swapTemp = arr[i];
        arr[i] = arr[j];
        arr[j] = swapTemp;
      }
    }

    int swapTemp = arr[i+1];
    arr[i+1] = arr[end];
    arr[end] = swapTemp;

    return i+1;
  }

  public static String multiply(String num1, String num2) {
    if (num2.equals("0") || num2.equals("0")) {
      return "0";
    }

    int carry = 0;
    StringBuilder result = new StringBuilder();
    for (int i = num1.length() - 1; i >= 0; i--) {
      StringBuilder temp = new StringBuilder();
      for (int j = num1.length() - 1; j > i; j--) {
        temp.append(0);
      }
      for (int j = num2.length() - 1; j >= 0 ; j--) {
        int digit1 = num1.charAt(i) - '0';
        int digit2 = num2.charAt(j) - '0';

        int product = digit1 * digit2 + carry;
        carry = product / 10;
        temp.append(product % 10);
      }
      if (carry != 0) {
        temp.append(carry);
        carry = 0;
      }
      result = new StringBuilder(addStrings(result.toString(), temp.reverse().toString()));
    }
    return result.toString();
  }

  //  17
  //+128
  public static String addStrings(String num1, String num2) {
    StringBuilder result = new StringBuilder();
    int carry = 0;
    int i = num1.length() - 1;
    int j = num2.length() - 1;
    while (i >= 0 || j >= 0 || carry != 0) {
      int digit1 = i >= 0 ? num1.charAt(i) - '0' : 0;
      int digit2 = j >= 0 ? num2.charAt(j) - '0' : 0;

      int sum = digit1 + digit2 + carry;
      carry = sum / 10;
      result.append(sum % 10);

      i--;
      j--;
    }
    return result.reverse().toString();
  }

  // Dynamic programming:
  public static int maxSubArray(int[] nums) {
    int[] sumList = new int[nums.length];
    for (int i = 0; i < nums.length; i++) {
      if (i == 0) {
        sumList[i] = nums[i];
      } else {
        if (sumList[i - 1] <= 0) {
          sumList[i] = nums[i];
        } else {
          sumList[i] = sumList[i - 1] + nums[i];
        }
      }
    }

  // get maximum:
    int max = Integer.MIN_VALUE;
    for (int n : sumList) {
      max = Math.max(max, n);
    }
    return max;
  }

  // brute force:
  // public static int maxSubArray(int[] nums) {
  //   int maxSum = Integer.MIN_VALUE;
  //   for (int i = 0; i < nums.length; i++) {
  //     int sum = 0;
  //     for (int j = i; j < nums.length; j++) {
  //       sum += nums[j];
  //       maxSum = Math.max(maxSum, sum);
  //     }
  //   }
  //   return maxSum;
  // }

  public String removeDuplicates(String s) {
    Stack<Character> stack = new Stack<>();

    for (char c : s.toCharArray()) {
      if (!stack.isEmpty() && stack.peek() == c) {
        stack.pop();
      } else {
        stack.push(c);
      }
    }

    StringBuilder result = new StringBuilder();
    for (char c : stack) {
      result.append(c);
    }
    return result.toString();
  }

  public static int climbStairs(int n) {
    if (n == 1 || n == 2) {
      return n;
    }
    int first = 1;
    int second = 2;

    for (int i = 3; i <= n; i++) {
      int third = first + second;
      first = second;
      second = third;
    }
    return second;
  }

  public static boolean isPalindrome(int x) {
    if (x < 0 || (x % 10 == 0 && x != 0)) {
      return false;
    }
    int reverted = 0;
    while (x > reverted) {
      reverted *= 10;
      reverted += (x % 10);
      x /= 10;
    }
    return reverted == x || reverted / 10 == x;
  }

//brute force:
  public static int largestRectangleArea(int[] heights) {
    int maxArea = 0;
    for (int i = 0; i < heights.length; i++) {
      int minHeight = Integer.MAX_VALUE;
      for (int j = i; j < heights.length; j++) {
        int width = j - i + 1;
        minHeight = Math.min(minHeight, heights[j]);
        maxArea = Math.max(maxArea, width * minHeight);
      }
    }
    return maxArea;
  }

//recursion version: (not working, ptr has to be global.)
  public static String decodeString(String s, int ptr) {
    String str = "";
    int numRepeat = 1;
    char cur = s.charAt(ptr);

    if (ptr == s.length() || cur == ']') {
      return "";
    }
    if (Character.isDigit(cur)) {
//    need to be able to take more than a single digit.
      numRepeat = Character.getNumericValue(cur);
      ptr += 2;
      String subStr = decodeString(s, ptr);
      ptr += 1;
      str += subStr.repeat(numRepeat);
    } else if (Character.isLetter(cur)) {
      str = String.valueOf(cur);
      ptr++;
    }
    return str + decodeString(s, ptr);
  }

  public static String decodeString(String s) {
    String num = "";
    String str = "";
    int repeat;
    Stack<String> stack = new Stack<>();
    Stack<Integer> numberStack = new Stack<>();

    for (char c : s.toCharArray()) {
      if (Character.isDigit(c)) {
        num += c;
      } else if (c == '[') {
        numberStack.push(Integer.valueOf(num));
        stack.push(str);
        num = "";
        str = "";
      } else if (c == ']') {
        repeat = numberStack.pop();
        str = str.repeat(repeat);
        str = stack.pop() + str;
      } else {
        str += c;
      }
    }
    return str;
  }

  public static int maximalSquare(char[][] matrix) {
    int maxSideLength = 0;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        if (matrix[i][j] == '1') {
          int l = largestSquareLength(matrix, i, j);
          maxSideLength = Math.max(maxSideLength, l);
        }
      }
    }
    return maxSideLength * maxSideLength;
  }

  private static int largestSquareLength(char[][] matrix, int i, int j) {
    int result = 1;
    for (int k = 1; k < Math.min(matrix.length - i, matrix[0].length - j); k++) {
      if (!allOnes(matrix, i, j, k)) {
        break;
      }
      result++;
    }
    return result;
  }

  private static boolean allOnes(char[][] matrix, int i, int j, int k) {
    for (int l = 0; l <= k; l++) {
      for (int m = 0; m <= k; m++) {
        if (matrix[i + l][j + m] != '1') {
          return false;
        }
      }
    }
    return true;
  }


  public static int numRabbits(int[] answers) {
    Map<Integer, Integer> count = new HashMap<>();
    int result = 0;
    for (int n : answers) {
      count.put(n, count.getOrDefault(n, 0) + 1);
    }
    for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
      int y = entry.getKey();
      int x = entry.getValue();
      result += Math.ceil((double) x/(y+1)) * (y + 1);
    }
    return result;
  }

  public static int numIslands(char[][] grid) {
    int numOfIsland = 0;

//  Y axis:
    for (int i = 0; i < grid.length; i++) {
//    X axis:
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j] == '1') {
          checkIsland(grid, i, j);
          numOfIsland++;
        }
      }
    }
    return numOfIsland;
  }

  private static void checkIsland(char[][] grid, int i, int j) {
    if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] != '1') {
      return;
    }

    grid[i][j] = '2';
    checkIsland(grid, i + 1, j);
    checkIsland(grid, i - 1, j);
    checkIsland(grid, i, j + 1);
    checkIsland(grid, i, j - 1);
  }


  public static int[] dailyTemperatures(int[] temperatures) {
    int[] days = new int[temperatures.length];
    int counter = 0;

    for (int i = 0; i < temperatures.length; i++) {
      counter = 0;
      for (int j = i + 1; j < temperatures.length; j++) {
        int curr = temperatures[i];
        int next = temperatures[j];
        counter++;
        if (curr < next) {
          days[i] = counter;
          counter = 0;
          break;
        }
      }
    }
    return days;
  }

  public static String reverseParentheses(String s) {
    Stack<String> stack = new Stack<>();
    String str = "";
    for (char c : s.toCharArray()) {
      if (c == '(') {
        stack.push(str);
        str = "";
      } else if (c == ')') {
        str = new StringBuilder(str).reverse().toString();
        str = stack.pop() + str;
      } else {
        str += c;
      }
    }

    return str;
  }

//  [2,3,1,1,4]
  public static int jump(int[] nums) {
    int length = nums.length;
    int maxPosition = 0;
    int end = 0; //the max distance reached after last jump step.
    int steps = 0;

    for (int i = 0; i < length - 1; i++) {
      maxPosition = Math.max(maxPosition, i + nums[i]);
      if (i == end) {
        end = maxPosition;
        steps++;
      }
    }
    return steps;
  }

  public List<String> generateParenthesis(int n) {
    List<String> result = new ArrayList<>();
    generate(result, "(", 1, 0, n);
    return result;
  }

  private void generate(List<String> result, String combination, int i, int j, int n) {
    if (j > i || i > n || j > n) {
      return;
    }

    if (i == n && j == n) result.add(combination);

    if (i >= j) {
      generate(result, combination + "(", i + 1, j, n);
      generate(result, combination + ")", i, j + 1, n);
    }
  }

  static public boolean isValid(String s) {
    Stack<Character> stack = new Stack<>();
    for (char c : s.toCharArray()) {
      if (c == '(') stack.push(')');
      else if (c == '[') stack.push(']');
      else if (c == '{') stack.push('}');
      else if (stack.isEmpty() || c != stack.pop()) return false;
    }
    return stack.isEmpty();
  }

  public static List<List<Integer>> threeSum(int[] nums) {
    if (nums.length < 3) {
      return new ArrayList<>();
    }
    Arrays.sort(nums);
    List<List<Integer>> result = new ArrayList<>();

    for (int i = 0; i < nums.length; i++) {

      if (nums[i] > 0) return result;
      if (i > 0 && nums[i] == nums[i - 1]) continue;

      int ptr1 = i + 1;
      int ptr2 = nums.length - 1;

      while (ptr1 < ptr2) {
        if (nums[i] + nums[ptr1] + nums[ptr2] == 0) {
          List<Integer> combination = Arrays.asList(nums[i], nums[ptr1], nums[ptr2]);
          result.add(combination);
          while (ptr1 < ptr2 && nums[ptr1] == nums[ptr1 + 1]) {
            ptr1++;
          }
          while (ptr1 < ptr2 && nums[ptr2] == nums[ptr2 - 1]) {
            ptr2--;
          }
          ptr1++;
          ptr2--;
        } else if (nums[i] + nums[ptr1] + nums[ptr2] > 0) {
          ptr2--;
        } else {
          ptr1++;
        }
      }
    }
    return result;
  }

  public static int maxArea(int[] height) {
    int ptr1 = 0, ptr2 = height.length - 1;
    int area = 0;

    while (ptr1 < ptr2) {
      if (height[ptr1] <= height[ptr2]) {
        ptr1++;
      } else {
        ptr2--;
      }
      area = Math.max(area, Math.min(height[ptr1], height[ptr2]) * (ptr2 - ptr1));
    }
    return area;
  }

  public static String longestPalindrome(String s) {
    int mid = s.length() / 2;
    String result = s.substring(mid, mid+1);

    while (mid < s.length()) {
      String temp = checkPalindrome(s, mid);
      if (temp.length() > result.length()) {
        result = temp;
      }
      mid++;
    }

    mid = s.length() / 2;

    while (mid > 0) {
      String temp = checkPalindrome(s, mid);
      if (temp.length() > result.length()) {
        result = temp;
      }
      mid--;
    }

    return result;
  }

  private static String checkPalindrome(String s, int mid) {
    String palindrome = s.substring(mid, mid+1);
    int left = mid - 1;
    int right = mid + 1;
    if (mid - 1 >= 0 && s.charAt(mid) == s.charAt(mid - 1)) {
      left--;
      palindrome = s.substring(left + 1, right);
    }
    else if (mid + 1 < s.length() && s.charAt(mid) == s.charAt(mid + 1)) {
      right++;
      palindrome = s.substring(left + 1, right);
    }

    while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
      palindrome = s.substring(left, right + 1);
      left--;
      right++;
    }

//  reset
    String temp = s.substring(mid, mid + 1);
    left = mid - 1;
    right = mid + 1;

    while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
      temp = s.substring(left, right + 1);
      left--;
      right++;
    }

    if (temp.length() > palindrome.length()) {
      palindrome = temp;
    }

    return palindrome;
  }

  public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
    int m = nums1.length;
    int n = nums2.length;

    int target = (m + n + 1) / 2;

    if ((m + n) % 2 == 1) {
      return getKth(target, nums1, 0, m - 1, nums2, 0, n - 1);
    } else {
      return (getKth(target, nums1, 0, m - 1, nums2, 0, n - 1) + getKth(target + 1, nums1, 0, m - 1,
          nums2, 0, n - 1)) * 0.5;
    }
  }

  private static double getKth(int k, int[] nums1, int start1, int end1, int[] nums2, int start2,
      int end2) {

    int len1 = end1 - start1 + 1;
    int len2 = end2 - start2 + 1;

    if (len1 > len2) return getKth(k, nums2, start2, end2, nums1, start1, end1);
    if (len1 == 0) return nums2[start2+k-1];
    if (k == 1) return Math.min(nums1[start1], nums2[start2]);

    int i = start1 + Math.min(len1, k/2) - 1;
    int j = start2 + Math.min(len1, k/2) - 1;
    if (nums1[i] > nums2[j]) {
      return getKth(k - (j - start2 + 1), nums1, start1, end1, nums2, j + 1, end2);
    } else {
      return getKth(k - (i - start1 + 1), nums1, i + 1, end1, nums2, start2, end2);
    }
  }

  public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {

    ListNode pointer1 = l1;
    ListNode pointer2 = l2;

    while (pointer1 != null) {
      if (pointer2 == null) {
        pointer2 = new ListNode(0);
      }
      int temp = pointer1.val + pointer2.val;

      if (temp >= 10) {
        temp -= 10;
        if (pointer1.next == null) {
          pointer1.next = new ListNode(1);
        } else {
          pointer1.next.val++;
        }
      }


      pointer1.val = temp;

      if (pointer1.next == null) {
        pointer1.next = pointer2.next;
        return l1;
      }

      pointer1 = pointer1.next;
      pointer2 = pointer2.next;
    }

    return l1;
  }

  public static List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    Deque<Integer> path = new ArrayDeque<>();
    List<Integer> output = new ArrayList<>();
    for (int n : nums) {
      output.add(n);
    }
    int limit = nums.length;

    permuteDFS(limit, output, result, 0);

    return result;
  }

  private static void permuteDFS(int limit, List<Integer> output,
      List<List<Integer>> result, int first) {
    if (first == limit) {
      result.add(new ArrayList<>(output));
      return;
    }

    for (int i = first; i < limit; i++) {
      Collections.swap(output, first, i);
      permuteDFS(limit, output, result, first + 1);
      Collections.swap(output, first, i);
    }
  }

  public static List<List<Integer>> combine(int n, int k) {
    List<List<Integer>> result = new ArrayList<>();

    if (k <= 0 || n < k) {
      return result;
    }

    Deque<Integer> path = new ArrayDeque<>();
    combineDFS(n, k, 1, path, result);

    return result;
  }

  private static void combineDFS(int n, int k, int begin, Deque<Integer> path, List<List<Integer>> result) {
    if (path.size() == k) {
      result.add(new ArrayList<>(path));
      return;
    }

    for (int i = begin; i <= n; i++) {
      path.addLast(i);
      combineDFS(n, k, i + 1, path, result);
      path.removeLast();
    }
  }

  public static int orangesRotting(int[][] grid) {
    int m = grid.length;
    int n = grid[0].length;

    int minute = 0;

    Queue<int[]> queue = new LinkedList<>();

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        if (grid[i][j] == 2) {
          queue.add(new int[]{i, j});
        }
      }
    }

    int[] dy = {0, 1, -1, 0};
    int[] dx = {1, 0, 0, -1};

    Queue<int[]> newRottenOrange = new LinkedList<>();

    while (!queue.isEmpty()) {
      int[] pos = queue.poll();
      int i = pos[0];
      int j = pos[1];


      for (int k = 0; k < 4; k++) {
        int newI = i + dy[k];
        int newJ = j + dx[k];

        if (newI >= 0 && newJ >= 0 && newJ < n && newI < m && grid[newI][newJ] == 1) {
          grid[newI][newJ] = 2;
          newRottenOrange.add(new int[]{newI, newJ});
        }
      }

      if (queue.isEmpty() && !newRottenOrange.isEmpty()) {
        queue.addAll(newRottenOrange);
        newRottenOrange.clear();
        minute++;
      }
    }

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        if (grid[i][j] == 1) {
          return -1;
        }
      }
    }
    return minute;
  }

  public static int[][] updateMatrix(int[][] mat) {
    int m = mat.length;
    int n = mat[0].length;
    int[][] dist = new int[m][n];

    Queue<int[]> queue = new LinkedList<>();
    Boolean[][] seen = new Boolean[m][n];

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        if (mat[i][j] == 0) {
          queue.add(new int[]{i, j});
          seen[i][j] = true;
        } else {
          seen[i][j] = false;
        }
      }
    }

    int[] dy = {0, 1, -1, 0};
    int[] dx = {1, 0, 0, -1};

    while (!queue.isEmpty()) {
      int[] pos = queue.poll();
      int y = pos[0];
      int x = pos[1];

      for (int i = 0; i < 4; i++) {
        int newY = y + dy[i];
        int newX = x + dx[i];
        if (newX >= 0 && newY >= 0 && newX < n && newY < m && !seen[newY][newX]) {
          dist[newY][newX] = dist[y][x] + 1;
          queue.add(new int[]{newY, newX});
          seen[newY][newX] = true;
        }
      }
    }
    return dist;
  }
// Definition for singly-linked list.
  private static class ListNode {

  int val;
  ListNode next;

  ListNode() {
  }

  ListNode(int val) {
    this.val = val;
  }

  ListNode(int val, ListNode next) {
    this.val = val;
    this.next = next;
  }
}



  public static ListNode reverseList(ListNode head) {
    ListNode curr = null;
    ListNode next = head;

    while (next != null) {
      ListNode temp = next.next;
      next.next = curr;
      curr = next;
      next = temp;
    }
    return curr;
  }


  public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    if (l1 == null) {
      return l2;
    }
    if (l2 == null) {
      return l1;
    }

    if (l1.val <= l2.val) {
      return new ListNode(l1.val, mergeTwoLists(l1.next, l2));
    } else {
      return new ListNode(l2.val, mergeTwoLists(l1, l2.next));
    }
  }

// Definition for a binary tree node.
  private class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }
  }

  public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
    if (root1 == null) {
      return root2;
    }
    if (root2 == null) {
      return root1;
    }

    TreeNode merged = new TreeNode(root1.val + root2.val);
    merged.left = mergeTrees(root1.left, root2.left);
    merged.right = mergeTrees(root1.right, root2.right);
    return merged;
  }

  public int maxAreaOfIsland(int[][] grid) {
    int count = 0;
    int y = grid.length;
    int x = grid[0].length;

    for (int i = 0; i < y; i++) {
      for (int j = 0; j < x; j++) {
        if (grid[i][j] == 1) {
          count = Math.max(count, maxAreaDFS(grid, i, j));
        }
      }
    }
    return count;
  }

  private int maxAreaDFS(int[][] grid, int sr, int sc) {
    if (sr < 0 || sc < 0 || sr == grid.length || sc == grid[0].length || grid[sr][sc] != 1) {
      return 0;
    }
    grid[sr][sc] = 0;
    int[] dx = {1, 0, 0, -1};
    int[] dy = {0, 1, -1, 0};

    int area = 1;
    for (int i = 0; i < 4; i++) {
      area += maxAreaDFS(grid, sr + dx[i], sc + dy[i]);
    }
    return area;
  }

  public static int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
    int currColor = image[sr][sc];
    if (currColor != newColor) {
      dfs(image, sr, sc, currColor, newColor);
    }
    return image;
  }

  private static void dfs(int[][] image, int sr, int sc, int currColor, int newColor) {
    int[] dx = {1, 0, 0, -1};
    int[] dy = {0, 1, -1, 0};

    if (image[sr][sc] == currColor) {
      image[sr][sc] = newColor;
      for (int i = 0; i < 4; i++) {
        if (sr + dx[i] >= 0 && sr + dx[i] < image.length && sc + dy[i] >= 0 && sc + dy[i] < image[0].length) {
          dfs(image, sr + dx[i], sc + dy[i], currColor, newColor);
        }
      }
    }
  }

  public static boolean checkInclusion(String s1, String s2) {
    int n = s1.length();
    int m = s2.length();

    if (n > m) {
      return false;
    }

    int[] cnt1 = new int[26];
    int[] cnt2 = new int[26];

    for (int i = 0; i < n; i++) {
      cnt1[s1.charAt(i) - 'a']++;
      cnt2[s2.charAt(i) - 'a']++;
    }

    if (Arrays.equals(cnt1, cnt2)) {
      return true;
    }

    for (int i = n; i < m; i++) {
      cnt2[s2.charAt(i) - 'a']++;
      cnt2[s2.charAt(i - n) - 'a']--;

      if (Arrays.equals(cnt1, cnt2)) {
        return true;
      }
    }
    return false;
  }

  public static ListNode removeNthFromEnd(ListNode head, int n) {
    int length = listLength(head);
    int target = length - n;
    ListNode prev = head;
    if (target == 0) {
      return head.next;
    }
    for (int i = 0; i < target - 1; i++) {
      prev = prev.next;
    }
    ListNode curr = prev.next;
    if (curr != null) {
      prev.next = curr.next;
    } else {
      prev.next = null;
    }
    return head;
  }

  private static int listLength(ListNode list) {
    int length = 0;
    ListNode pointer = list;
    while (pointer != null) {
      length++;
      pointer = pointer.next;
    }
    return length;
  }

  public ListNode middleNode(ListNode head) {
    ListNode fasterPointer = head;
    ListNode slowerPointer = head;
    while (fasterPointer != null && fasterPointer.next != null) {
      fasterPointer = fasterPointer.next.next;
      slowerPointer = slowerPointer.next;
    }
    return slowerPointer;
  }

//  String's in java is immutable so the space complexity cannot be O(1).
  public static String reverseWords(String s) {
    StringBuilder result = new StringBuilder();
    int beginOfWord = 0;
    boolean firstWord = true;
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == ' ') {
        char[] word = s.substring(beginOfWord, i).toCharArray();
        reverseString(word);
        result.append(word);
        result.append(" ");
        beginOfWord = i + 1;
      }
    }
    char[] word = s.substring(beginOfWord, s.length()).toCharArray();
    reverseString(word);
    result.append(word);
    return result.toString();
  }

  public static void reverseString(char[] s) {
    for (int i = 0; i < s.length / 2; i++) {
      char temp = s[i];
      s[i] = s[s.length - 1 - i];
      s[s.length - 1 - i] = temp;
    }
  }

  public static int[] twoSum(int[] numbers, int target) {
    int[] result = new int[2];
    for (int i = 0; i < numbers.length - 1; i++) {
      for (int j = i+1; j < numbers.length; j++) {
        if (numbers[i] + numbers[j] == target) {
          result[0] = i;
          result[1] = j;
          return result;
        }
      }
    }
    return result;
  }

  public static void moveZeroes(int[] nums) {
    int n = nums.length;
    int left = 0;
    for (int right = 0; right < n; right++) {
        if (nums[right] != 0) {
          int temp = nums[left];
          nums[left] = nums[right];
          nums[right] = temp;
          left++;
      }
    }
  }

  public static int[] sortedSquares(int[] nums) {
    for (int i = 0; i < nums.length; i++) {
      nums[i] = (int) Math.pow(nums[i], 2);
    }
    return iSort(nums);
  }

  private static int[] iSort(int[] nums) {
    for (int i = 0; i < nums.length - 1; i++) {
      for (int j = i + 1; j > 0; j--) {
        if (nums[j] < nums[j - 1]) {
          int temp = nums[j - 1];
          nums[j - 1] = nums[j];
          nums[j] = temp;
        }
      }
    }
    return nums;
  }

//  Time complexity: O(n)
//  Space complexity: O(n)
//  public static void rotate(int[] nums, int k) {
//    int[] temp = nums.clone();
//    int n = nums.length;
//    k = k % n;
//    for (int i = 0; i < n; i++) {
//      nums[i] = temp[(n - k + i) % n];
//    }
//  }

//  Time complexity: O(n)
//  Space complexity: O(1)
  public static void rotate(int[] nums, int k) {
    int n = nums.length;
    k = k % n;
    int count = gcd(k, n);
    for (int start = 0; start < count; ++start) {
      int current = start;
      int prev = nums[start];
      do {
        int next = (current + k) % n;
        int temp = nums[next];
        nums[next] = prev;
        prev = temp;
        current = next;
      } while (start != current);
    }
  }

  public static int gcd(int x, int y) {
    return y > 0 ? gcd(y, x % y) : x;
  }

  public static int search(int[] nums, int target) {
    int pivot, left = 0;
    int right = nums.length - 1;
    while (left <= right) {
      pivot = (left + right) / 2;
      if (nums[pivot] < target) {
        left = pivot+1;
      }
      if (nums[pivot] > target) {
        right = pivot-1;
      }
      if (nums[pivot] == target) {
        return pivot;
      }
    }
    return -1;
  }

  public static int firstBadVersion(int n) {
    int left = 0;
    int pivot = 0;
    int right = n;

    while (left < right) {
      pivot = left + (right - left) / 2;

      if (isBadVersion(pivot)) {
        right = pivot;
      }
      else {
        left = pivot + 1;
      }
    }
    return left;
  }

  private static boolean isBadVersion(int n) {
    return n >= 4;
  }

  public static int searchInsert(int[] nums, int target) {
    int left = 0;
    int mid = 0;
    int right = nums.length - 1;

    while (left <= right) {
      mid = left + (right - left) / 2;

      if (nums[mid] < target) {
        left = mid + 1;
      }
      if (nums[mid] > target) {
        right = mid - 1;
      }
      if (nums[mid] == target) {
        return mid;
      }
    }
    return left;
  }
}
