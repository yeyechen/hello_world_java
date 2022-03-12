package LeetCodeHot100;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class Code084 {

  // brute-force
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

  // using stack
  public static int largestRectangleAreaOpt(int[] heights) {
    if (heights.length == 0) {
      return 0;
    }
    if (heights.length == 1) {
      return heights[0];
    }
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(0);
    int maxArea = 0;
    for (int i = 1; i < heights.length; i++) {
      // using while, we know that if heights[stack.peek()] > heights[i] holds, anything in between
      // index stack.peek() and i are higher than at index stack.peek(), which means we can directly
      // calculate the current area, knowing at index stack.peek() is the lower bound.
      while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
        int currHeight = heights[stack.pop()];
        int width = stack.isEmpty() ? i : i - stack.peek() - 1;
        maxArea = Math.max(maxArea, currHeight * width);
      }
      stack.push(i);
    }
    // calculate the rest of heights in the stack, and we know that heights is ascending.
    while (!stack.isEmpty()) {
      int height = heights[stack.pop()];
      int width = stack.isEmpty() ? heights.length : heights.length - stack.peek() - 1;
      maxArea = Math.max(maxArea, height * width);
    }
    return maxArea;
  }

  public static void main(String[] args) {
    System.out.println(largestRectangleAreaOpt(new int[]{2,1,5,6,2,3}));
  }
}
