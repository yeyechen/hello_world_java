package 左程云体系学习班;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import 左程云体系学习班.Lecture14.UnionFind;

public class Lecture16 {

  /*
   * 图 (Graph)：由点的集合和边的集合组成，虽然存在有向图和无向图，但都能用有向图表达，边上可能带有权值(weight)。
   *
   * 1. 图的表达方式：hashmap；二维矩阵一一对应关系；给定每一条连接的信息
   *
   * 2. 遍历：
   *    2.1 宽度优先遍历：利用队列实现；从源节点开始依次按照宽度进队列，然后弹出；每弹出一个点，把该节点所有没有进过队列的
   *    邻节点放入队列 (用set判断)，直到队列变空。
   *
   *    2.2 深度优先遍历：利用栈实现；从源节点开始节点按照深度放入栈，然后弹出；每弹出一个节点，把该节点的下一个没有进过栈
   *    的邻节点放入栈，直到栈变空。
   *
   * 3. 拓扑排序：在图中找到所有入度为0的点输出; 把所有入度为0的点在途中删掉, 继续找入度为0的点输出, 周而复始; 图的所有点都
   * 被删除后, 依次输出的顺序就是拓扑排序。
   * 要求: 有向图且无环
   *
   * 4. 最小生成树 (minimum spanning tree)：最小生成树是一副连通加权无向图中一棵权值最小的生成树。
   * 最小生成树一定是无向图，因为有向图必须给定起始节点，那么生成树就是相对于节点了而不是整个图。
   *
   *    4.1 最小生成树算法Kruskal：总是从权值最小的边开始考虑，依次考察权值从小到大的边；
   *                            如果当前的边进入最小生成树的集合不会形成环，就要当前边；
   *                            如果当前的边进入最小生成树的集合会形成环，就不要当前边；
   *                            考察完所有边之后，最小生成树的集合就得到了。
   *    4.2. 最小生成树算法Prim：可以从任意节点出发来寻找最小生成树；
   *                         某个点加入到被选取的点中后，解锁这个点出发的所有新的边；
   *                         在所有解锁的边中选最小的边，然后看看这个边会不会形成环；
   *                         如果会，不要当前边，继续考察剩下解锁的边中最小的边，重复3；
   *                         如果不会，要当前边，将该边的指向点加入到被选取的点中，重复2；
   *                         当所有点都被选取，最小生成数就得到了。
   * 当一个图的边极多，P算法优与K算法，反之。因为K算法是要遍历所有的边，而P算法是按点解锁。
   * */

  // 1. 图的内部表达方式，可以由外部给定的不同定义转化成自己熟悉的表示形式
  //    例子：用每一条连接信息转化
  public static class Graph {

    HashMap<Integer, GraphNode> nodes;
    HashSet<Edge> edges;

    public Graph() {
      nodes = new HashMap<>();
      edges = new HashSet<>();
    }

    // information 是一个N*3的矩阵
    // 里面"tuple"中的值分别表示：weight；from节点的值；to节点的值
    // [5, 0, 7]      weight 5
    // [3, 0, 1]    0  ------>  7
    //               \weight 3
    //                1
    public Graph(int[][] information) {
      for (int[] ints : information) {
        // 每次拿到一条边的信息
        int weight = ints[0];
        int fromValue = ints[1];
        int toValue = ints[2];
        // 如果是新节点，就创建并加入hashmap
        if (!nodes.containsKey(fromValue)) {
          nodes.put(fromValue, new GraphNode(fromValue));
        }
        if (!nodes.containsKey(toValue)) {
          nodes.put(toValue, new GraphNode(toValue));
        }
        // 从hashmap中获取节点
        GraphNode fromNode = nodes.get(fromValue);
        GraphNode toNode = nodes.get(fromValue);
        // 创建新边
        Edge edge = new Edge(weight, fromNode, toNode);
        fromNode.nexts.add(toNode);
        fromNode.out++;
        toNode.in++;
        fromNode.edges.add(edge);
        edges.add(edge);
      }
    }
  }

  public static class GraphNode {

    private final int value;
    private int in; // 入度：多少个节点是指向此节点的
    private int out; // 出度：有多少个节点被此节点指向
    private final List<GraphNode> nexts; // 被指向的节点
    private final List<Edge> edges; // 只记录往外出的边

    public GraphNode(int value) {
      this.value = value;
      in = 0;
      out = 0;
      nexts = new ArrayList<>();
      edges = new ArrayList<>();
    }
  }

  public static class Edge {

    private final int weight;
    private final GraphNode from;
    private final GraphNode to;

    public Edge(int weight, GraphNode from, GraphNode to) {
      this.weight = weight;
      this.from = from;
      this.to = to;
    }
  }

  // 2.1 宽度优先遍历
  public static void bfs(GraphNode start) {
    if (start == null) {
      return;
    }
    Queue<GraphNode> nodes = new LinkedList<>();
    // 必须有一个set检查是否遍历过某些节点，防止infinite loop
    HashSet<GraphNode> visited = new HashSet<>();
    nodes.add(start);
    visited.add(start);
    while (!nodes.isEmpty()) {
      GraphNode curr = nodes.poll();
      System.out.println(curr.value);
      for (GraphNode next : curr.nexts) {
        if (!visited.contains(next)) {
          nodes.add(next);
          visited.add(next);
        }
      }
    }
  }

  // 2.2 深度优先遍历 (不使用递归，自己压栈)
  public static void dfs(GraphNode node) {
    if (node == null) {
      return;
    }
    Stack<GraphNode> stack = new Stack<>();
    // 同样的，set用来检查是否遍历过某些节点
    HashSet<GraphNode> visited = new HashSet<>();
    stack.add(node);
    visited.add(node);
    System.out.println(node.value);
    while (!stack.isEmpty()) {
      GraphNode cur = stack.pop();
      for (GraphNode next : cur.nexts) {
        if (!visited.contains(next)) {
          stack.push(cur);
          stack.push(next);
          visited.add(next);
          System.out.println(next.value);
          break;
        }
      }
    }
  }

  // 3. 拓扑排序
  public static List<GraphNode> sortedTopology(Graph graph) {
    // key 某个节点   value 剩余的入度
    HashMap<GraphNode, Integer> inMap = new HashMap<>();
    // 只有剩余入度为0的点，才进入这个队列
    Queue<GraphNode> zeroInQueue = new LinkedList<>();
    for (GraphNode node : graph.nodes.values()) {
      inMap.put(node, node.in);
      if (node.in == 0) {
        zeroInQueue.add(node);
      }
    }
    List<GraphNode> result = new ArrayList<>();
    while (!zeroInQueue.isEmpty()) {
      GraphNode cur = zeroInQueue.poll();
      result.add(cur);
      for (GraphNode next : cur.nexts) {
        inMap.put(next, inMap.get(next) - 1);
        if (inMap.get(next) == 0) {
          zeroInQueue.add(next);
        }
      }
    }
    return result;
  }

  public static class EdgeWeightAscending implements Comparator<Edge> {

    @Override
    public int compare(Edge o1, Edge o2) {
      return o1.weight - o2.weight;
    }
  }

  // 4.2. 最小生成树算法Prim
}
