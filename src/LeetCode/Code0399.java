package LeetCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Code0399 {

  // just create a graph (two ways), and use breadth first search, record weight along the path

  public static double[] calcEquation(List<List<String>> equations, double[] values,
      List<List<String>> queries) {

    // construct graph
    Graph graph = new Graph(equations, values);

    double[] ans = new double[queries.size()];
    for (int i = 0; i < queries.size(); i++) {

      String startStr = queries.get(i).get(0);
      String endStr = queries.get(i).get(1);

      if (!graph.nodes.containsKey(startStr) || !graph.nodes.containsKey(endStr)) {
        ans[i] = -1.0;
        continue;
      }
      GraphNode startNode = graph.nodes.get(startStr);
      GraphNode endNode = graph.nodes.get(endStr);
      if (startNode == endNode) {
        ans[i] = 1.0;
        continue;
      }

      // breadth first search
      Queue<GraphNode> nodes = new LinkedList<>();
      nodes.add(startNode);

      // pathRatio contains the value of ratio from startNode to key
      // can also check if nodes are visited
      HashMap<GraphNode, Double> pathRatios = new HashMap<>();
      pathRatios.put(startNode, 1.0);

      while (!nodes.isEmpty() && !pathRatios.containsKey(endNode)) {
        GraphNode curr = nodes.poll();

        for (Edge edge : curr.edges) {
          GraphNode next = edge.to;
          if (!pathRatios.containsKey(next)) {
            double nextRatio = pathRatios.get(curr) * edge.weight;
            pathRatios.put(next, nextRatio);
            nodes.add(next);
          }
        }
      }
      ans[i] = pathRatios.getOrDefault(endNode, -1.0);
    }

    return ans;
  }

  public static class Graph {

    HashMap<String, GraphNode> nodes;
    HashSet<Edge> edges;

    public Graph() {
      nodes = new HashMap<>();
      edges = new HashSet<>();
    }

    // constructor
    public Graph(List<List<String>> fromTo, double[] weights) {
      nodes = new HashMap<>();
      edges = new HashSet<>();
      for (int i = 0; i < fromTo.size(); i++) {
        String fromValue = fromTo.get(i).get(0);
        String toValue = fromTo.get(i).get(1);
        double weight = weights[i];
        if (!nodes.containsKey(fromValue)) {
          nodes.put(fromValue, new GraphNode(fromValue));
        }
        if (!nodes.containsKey(toValue)) {
          nodes.put(toValue, new GraphNode(toValue));
        }
        GraphNode fromNode = nodes.get(fromValue);
        GraphNode toNode = nodes.get(toValue);

        Edge forwardEdge = new Edge(weight, fromNode, toNode);
        fromNode.nexts.add(toNode);
        fromNode.edges.add(forwardEdge);
        edges.add(forwardEdge);

        Edge backwardEdge = new Edge(1 / weight, toNode, fromNode);
        toNode.nexts.add(fromNode);
        toNode.edges.add(backwardEdge);
        edges.add(backwardEdge);
      }
    }
  }

  private static class GraphNode {

    String value;
    List<GraphNode> nexts;
    List<Edge> edges;

    public GraphNode(String value) {
      this.value = value;
      this.nexts = new ArrayList<>();
      this.edges = new ArrayList<>();
    }
  }

  private static class Edge {

    double weight;
    GraphNode from;
    GraphNode to;

    public Edge(double weight, GraphNode from, GraphNode to) {
      this.weight = weight;
      this.from = from;
      this.to = to;
    }
  }

  public static void main(String[] args) {
    List<List<String>> equations = new ArrayList<>();
    equations.add(Arrays.asList("a", "b"));
    equations.add(Arrays.asList("b", "c"));
    equations.add(Arrays.asList("bc", "cd"));
    double[] values = {2, 3, 5};
    List<List<String>> queries = new ArrayList<>();
    queries.add(Arrays.asList("a", "c"));
    queries.add(Arrays.asList("b", "a"));
    queries.add(Arrays.asList("bc", "cd"));
    queries.add(Arrays.asList("cd", "bc"));

    double[] ans = calcEquation(equations, values, queries);
    System.out.println(Arrays.toString(ans));
  }
}
