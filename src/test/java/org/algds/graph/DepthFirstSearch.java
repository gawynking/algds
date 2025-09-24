package org.algds.graph;

import org.algds.graph.adjacency.AdjacencyGraph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.*;

public class DepthFirstSearch {

    /**
     * 递归DFS（隐式栈）
     *
     * @param graph
     * @param start
     */
    public static void dfsRecursive(AdjacencyGraph graph, Vertex start) {
        System.out.print("递归DFS遍历顺序：");
        dfs(graph, start, new HashSet<>());
        System.out.println();
    }

    public static void dfs(AdjacencyGraph graph, Vertex start,Set<Integer> visited) {
        if (start == null) {
            return;
        }
        visited.add(start.getId());
        System.out.print(start.getName() + " ");
        for (Edge edge : start.getEdgeList()) {
            Vertex neighbor = edge.getTo();
            if (!visited.contains(neighbor.getId())) {
                dfs(graph,neighbor,visited);
            }
        }
    }


    /**
     * 非递归DFS（显式栈）
     *
     * @param graph
     * @param start
     */
    public static void dfsIterative(AdjacencyGraph graph, Vertex start) {
        if (start == null) {
            return;
        }
        Set<Integer> visited = new HashSet<>();
        Deque<Vertex> stack = new ArrayDeque<>();
        stack.push(start);

        System.out.print("显式栈DFS遍历顺序：");
        while (!stack.isEmpty()) {
            Vertex v = stack.pop();
            if (!visited.contains(v.getId())) {
                visited.add(v.getId());
                System.out.print(v.getName() + " ");
                // 倒序入栈以保持与递归顺序一致
                List<Edge> edges = v.getEdgeList();
                for (int i = edges.size() - 1; i >= 0; i--) {
                    Vertex neighbor = edges.get(i).getTo();
                    if (!visited.contains(neighbor.getId())) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        System.out.println();
    }


    /**
     * 主函数测试
     *
     *     A
     *    / \
     *   B   C
     *   |    \
     *   D     E
     *
     * @param args
     */
    public static void main(String[] args) {

        // 构建图
        AdjacencyGraph graph = new AdjacencyGraph();

        // 创建标签
        Label vertexLabel = new Label("V", 1);
        Label edgeLabel = new Label("E", 2);

        // 创建顶点
        Vertex A = new Vertex("A", vertexLabel);
        Vertex B = new Vertex("B", vertexLabel);
        Vertex C = new Vertex("C", vertexLabel);
        Vertex D = new Vertex("D", vertexLabel);
        Vertex E = new Vertex("E", vertexLabel);

        // 添加顶点
        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);
        graph.addVertex(D);
        graph.addVertex(E);


        // 添加无向边（使用双向边模拟）
        graph.addEdge(new Edge(A, B, 1, edgeLabel));
        graph.addEdge(new Edge(B, A, 1, edgeLabel));

        graph.addEdge(new Edge(A, C, 1, edgeLabel));
        graph.addEdge(new Edge(C, A, 1, edgeLabel));

        graph.addEdge(new Edge(B, D, 1, edgeLabel));
        graph.addEdge(new Edge(D, B, 1, edgeLabel));

        graph.addEdge(new Edge(C, E, 1, edgeLabel));
        graph.addEdge(new Edge(E, C, 1, edgeLabel));

        // 打印顶点和边的数量
        System.out.println(String.format("顶点数量 %s",graph.getVertexNum()));
        System.out.println(String.format("边的数量 %s",graph.getEdgeNum()));

        // 执行DFS遍历
        dfsRecursive(graph, A);     // 递归DFS
        dfsIterative(graph, A);     // 显式栈DFS
    }

}
