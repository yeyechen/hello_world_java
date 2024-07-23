package LeetCode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Code0020 {
  //  valid brackets
  public static boolean isValid(String s) {

    // filter out odd length string
    if (s.length() % 2 == 1) {
      return false;
    }

    // define matching rules
    Map<Character, Character> map = new HashMap<>();
    map.put(')', '(');
    map.put(']', '[');
    map.put('}', '{');

    LinkedList<Character> stack = new LinkedList<>();
    for (int i = 0; i < s.length(); i++) {
      char currChar = s.charAt(i);
      if (!map.containsKey(currChar)) {
        stack.push(currChar);
      } else {
        char prevChar = stack.isEmpty() ? Character.MIN_VALUE : stack.pop();
        char matchChar = map.get(currChar);
        if (prevChar != matchChar) {
          return false;
        }
      }
    }

    return stack.isEmpty();
  }

  public static void main(String[] args) {
    System.out.println(isValid("()[]{}"));
  }
}
