package Util;

public class Utility {

  public static int[] randomArrayGenerator(int maxLen, int maxValue, boolean elementsAllPositive) {
    int len = (int) (Math.random() * maxLen) + 1;
    int[] result = new int[len];
    for (int i = 0; i < len; i++) {
      int randomNum = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * maxValue);
      result[i] = elementsAllPositive ? Math.abs(randomNum) : randomNum;
    }
    return result;
  }

  // generates two random arrays with same length
  public static int[][] randomArrayPairGenerator(int maxLen,  int maxValue, boolean elementsAllPositive) {
    int len = (int) (Math.random() * maxLen) + 1;
    int[][] result = new int[2][len];
    for (int i = 0; i < len; i++) {
      int num1 = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * maxValue);
      int num2 = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * maxValue);
      result[0][i] = elementsAllPositive ? Math.abs(num1) : num1;
      result[1][i] = elementsAllPositive ? Math.abs(num2) : num2;
    }
    return result;
  }

  // Adjusted pre-sum, with additional 0 at the first index, for convenience to get the range sum.
  public static int[] calcPreSum(int[] arr) {
    int N = arr.length;
    int[] s = new int[N + 1];
    s[0] = 0;
    for (int i = 0; i < N; i++) {
      s[i + 1] = s[i] + arr[i];
    }
    return s;
  }

  public static int getSum(int[] preSum, int L, int R) {
    return preSum[R + 1] - preSum[L];
  }
}
