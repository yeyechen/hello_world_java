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
}
