package org.algds.graph.traversal;

import org.algds.graph.adjacency.Graph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.*;

public class DepthFirstSearch {

    /**
     * 递归DFS（隐式栈）
     *
     * @param graph
     */
    public static List<List<Vertex>> dfsRecursive(Graph graph) {
        List<List<Vertex>> dfsList = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        for (Vertex vertex : graph.getVertices().values()) {
            if (!visited.contains(vertex.getId())) {
                List<Vertex> dfs = new ArrayList<>();
                dfs(vertex, visited, dfs);
                dfsList.add(dfs);
            }
        }
        return dfsList;
    }

    public static List<Vertex> dfs(Vertex start, Set<Integer> visited, List<Vertex> dfs) {
        if (start == null) {
            return dfs;
        }
        visited.add(start.getId());
        dfs.add(start);
        for (Edge edge : start.getEdgeList()) {
            Vertex neighbor = edge.getTo();
            if (!visited.contains(neighbor.getId())) {
                dfs(neighbor, visited, dfs);
            }
        }
        return dfs;
    }


    /**
     * 非递归DFS（显式栈）
     *
     * @param graph
     */
    public static List<List<Vertex>> dfsIterative(Graph graph) {

        Set<Integer> visited = new HashSet<>();
        Deque<Vertex> stack = new ArrayDeque<>();
        List<List<Vertex>> dfsList = new ArrayList<>();
        for (Vertex vertex : graph.getVertices().values()) {
            if (!visited.contains(vertex.getId())) {
                stack.push(vertex);
                List<Vertex> dfs = new ArrayList<>();
                while (!stack.isEmpty()) {
                    Vertex v = stack.pop();
                    if (!visited.contains(v.getId())) {
                        visited.add(v.getId());
                        dfs.add(v);
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
                dfsList.add(dfs);
            }
        }
        return dfsList;
    }


    /**
     * 主函数测试
     *    A             F
     *    / \          / \
     *   B   C        /   \
     *   |    \      G     H
     *   D     E
     */
    public static void main(String[] args) {

        // 构建图
        Graph graph = new Graph();

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

        // 构造多个树的图
        Vertex F = new Vertex("F", vertexLabel);
        Vertex G = new Vertex("G", vertexLabel);
        Vertex H = new Vertex("H", vertexLabel);
        graph.addVertex(F);
        graph.addVertex(G);
        graph.addVertex(H);
        graph.addEdge(F, G, 1, edgeLabel);
        graph.addEdge(F, H, 1, edgeLabel);

        // 打印顶点和边的数量
        System.out.println(String.format("顶点数量 %s", graph.getVertexNum()));
        System.out.println(String.format("边的数量 %s", graph.getEdgeNum()));


        // 执行DFS遍历
        List<List<Vertex>> dfsList = new ArrayList<>();
        dfsList = dfsRecursive(graph);// 递归DFS
        System.out.println(String.format("DFS递归(隐式栈)遍历顺序(共%s个子图)：", dfsList.size()));
        for (int i = 0; i < dfsList.size(); i++) {
            List<Vertex> bfs = dfsList.get(i);
            System.out.println(String.format("--------- 第%s个子图 --------", (i + 1)));
            System.out.print("    ");
            for (Vertex vertex : bfs) {
                System.out.print(vertex.getName() + " -> ");
            }
            System.out.println("end.");
        }

        System.out.println("======================================================");

        dfsList = dfsIterative(graph);     // 显式栈DFS
        System.out.println(String.format("DFS迭代(显式栈)遍历顺序(共%s个子图)：", dfsList.size()));
        for (int i = 0; i < dfsList.size(); i++) {
            List<Vertex> bfs = dfsList.get(i);
            System.out.println(String.format("--------- 第%s个子图 --------", (i + 1)));
            System.out.print("    ");
            for (Vertex vertex : bfs) {
                System.out.print(vertex.getName() + " -> ");
            }
            System.out.println("end.");
        }
    }

}
