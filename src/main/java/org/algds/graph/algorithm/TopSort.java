package org.algds.graph.algorithm;

import org.algds.graph.adjacency.AdjacencyGraph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.*;


public class TopSort {

    /**
     * 广度优先搜索 卡恩算法
     * 时间复杂度 O(V+E)
     *
     * @param graph 顶点集合 + 邻接表
     * @return
     */
    public static List<Vertex> topSortKahn(AdjacencyGraph graph) {

        // 1. 计算每个节点的入度 时间复杂度 V+E
        Map<Integer, Integer> indegree = new HashMap<>();
        for (Integer id : graph.getVertices().keySet()) {
            indegree.putIfAbsent(id, 0);
            for (Edge edge : graph.getVertices().get(id).getEdgeList()) {
                indegree.put(edge.getTo().getId(), indegree.getOrDefault(edge.getTo().getId(), 0) + 1);
            }
        }

        // 2. 所有入度为 0 的节点入队 时间复杂度 V
        Queue<Integer> queue = new LinkedList<>();
        for (Map.Entry<Integer, Integer> entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }


        // 3. 进行拓扑排序 时间复杂度 V+E
        List<Vertex> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            Integer vertexId = queue.poll();
            Vertex vertex = graph.getVertices().get(vertexId);
            result.add(vertex);

            List<Edge> neighbors = vertex.getEdgeList();
            for (Edge neighbor : neighbors) {
                Vertex to = neighbor.getTo();
                indegree.put(to.getId(), indegree.get(to.getId()) - 1);
                if (indegree.get(to.getId()) == 0) {
                    queue.add(to.getId());
                }
            }
        }

        // 4. 如果排序结果数量 < 节点总数，说明有环
        if (result.size() != indegree.size()) {
            return null;
        }

        return result;
    }


    /**
     * 打印拓扑排序结果
     *
     * @param vertexList
     */
    public static void printTopSort(List<Vertex> vertexList) {
        if (null == vertexList) {
            System.out.println("存在环.");
            return;
        }
        System.out.print("Begin -> ");
        for (Vertex vertex : vertexList) {
            System.out.print(String.format("%s -> ", vertex.getName()));
        }
        System.out.println("End.");
    }

    /**
     * 测试有向无环图结构
     *     v1 ----> v2
     *     / \      / \
     *    /   \    /   \
     *  |/     \||/     \|
     * v3 <---- v4 <---- v5
     *  \      /  \      /
     *   \    /    \    /
     *    \||/      \||/
     *     v6 <---- v7
     */
    public static void main(String[] args) {

        // 构建图
        AdjacencyGraph graph = new AdjacencyGraph();

        // 创建标签
        Label vertexLabel = new Label("V", 1);
        Label edgeLabel = new Label("E", 2);

        // 构造顶点
        Vertex v1 = new Vertex("v1", vertexLabel);
        Vertex v2 = new Vertex("v2", vertexLabel);
        Vertex v3 = new Vertex("v3", vertexLabel);
        Vertex v4 = new Vertex("v4", vertexLabel);
        Vertex v5 = new Vertex("v5", vertexLabel);
        Vertex v6 = new Vertex("v6", vertexLabel);
        Vertex v7 = new Vertex("v7", vertexLabel);

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);

        // 构造边
        graph.addEdge(v1, v2, 1, edgeLabel);
        graph.addEdge(v1, v3, 1, edgeLabel);
        graph.addEdge(v1, v4, 1, edgeLabel);

        graph.addEdge(v2, v4, 1, edgeLabel);
        graph.addEdge(v2, v5, 1, edgeLabel);

        graph.addEdge(v3, v6, 1, edgeLabel);

        graph.addEdge(v4, v3, 1, edgeLabel);
        graph.addEdge(v4, v6, 1, edgeLabel);
        graph.addEdge(v4, v7, 1, edgeLabel);

        graph.addEdge(v5, v4, 1, edgeLabel);
        graph.addEdge(v5, v7, 1, edgeLabel);

        graph.addEdge(v7, v6, 1, edgeLabel);

        // 构造环
        graph.addEdge(v4,v2,1,edgeLabel);

        // kahn算法实现拓扑排序
        List<Vertex> vertices = topSortKahn(graph);
        printTopSort(vertices);

    }
}