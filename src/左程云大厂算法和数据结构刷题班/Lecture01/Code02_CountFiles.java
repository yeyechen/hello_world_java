package 左程云大厂算法和数据结构刷题班.Lecture01;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Code02_CountFiles {

  /*
   * 题目：给定一个文件目录的路径，写一个函数统计这个目录下所有的文件数量并返回，隐藏文件也算，但是文件夹不算
   *
   * 可以深度优先也可以宽度优先遍历，这道题没区别。深度用stack，也可以递归；宽度用queue
   * */

  // In this func breadth first search is used
  public static int fileCount(String folderPath) {
    // how to read in a file is also important when it comes to the interview.
    File root = new File(folderPath);
    if (!root.isFile() && !root.isDirectory()) {
      return 0;
    }
    if (root.isFile()) {
      return 1;
    }
    Queue<File> queue = new LinkedList<>();
    queue.add(root);
    int counter = 0;
    while (!queue.isEmpty()) {
      File currFolder = queue.poll();
      for (File file : currFolder.listFiles()) {
        if (file.isFile()) {
          counter++;
        }
        if (file.isDirectory()) {
          queue.add(file);
        }
      }
    }
    return counter;
  }

  // depth first seach
  public static int fileCount1(String folderPath) {
    File root = new File(folderPath);
    if (!root.isDirectory() && !root.isFile()) {
      return 0;
    }
    if (root.isFile()) {
      return 1;
    }
    Stack<File> stack = new Stack<>();
    stack.add(root);
    int files = 0;
    while (!stack.isEmpty()) {
      File folder = stack.pop();
      for (File next : folder.listFiles()) {
        if (next.isFile()) {
          files++;
        }
        if (next.isDirectory()) {
          stack.push(next);
        }
      }
    }
    return files;
  }

  public static void main(String[] args) {
    // 你可以自己更改目录
    String path = "/Users/Mike_Home/Desktop/";
    System.out.println(fileCount(path));
    System.out.println(fileCount1(path));
  }
}
