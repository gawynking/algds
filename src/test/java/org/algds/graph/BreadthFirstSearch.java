package org.algds.graph;

import org.algds.graph.adjacency.Graph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BreadthFirstSearch {

    /**
     * BFS实现：使用队列
     */
    public static void bfs(Graph graph, Vertex start) {
        if (start == null) {
            return;
        }

        Set<Integer> visited = new HashSet<>(); // 已访问集合
        Queue<Vertex> queue = new LinkedList<>(); // 队列实现BFS
        queue.offer(start);
        visited.add(start.getId());

        System.out.print("BFS遍历顺序：");
        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            System.out.print(v.getName() + " ");
            for (Edge edge : v.getEdgeList()) {
                Vertex neighbor = edge.getTo();
                if (!visited.contains(neighbor.getId())) {
                    visited.add(neighbor.getId());
                    queue.offer(neighbor);
                }
            }
        }
        System.out.println();
    }


    /**
     * 主函数测试
     *    A
     *    / \
     *   B   C
     *   |    \
     *   D     E
     * @param args
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

        // 打印顶点和边的数量
        System.out.println(String.format("顶点数量 %s",graph.getVertexNum()));
        System.out.println(String.format("边的数量 %s",graph.getEdgeNum()));

        // 执行BFS
        bfs(graph, A);
    }

}
