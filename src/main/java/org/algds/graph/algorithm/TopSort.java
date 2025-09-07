package org.algds.graph.algorithm;

import graph.model.Edge;
import graph.model.Label;
import graph.model.Vertex;
import graph.model.VertexList;

import java.util.*;

public class TopSort {

    /**
     * 广度优先搜索 卡恩算法
     *  时间复杂度 O(V+E)
     *
     * @param graph 顶点集合 + 邻接表
     * @return
     */
    public static List<Vertex> topSortKahn(VertexList graph) {

        // 1. 计算每个节点的入度 时间复杂度 V+E
        Map<Integer, Integer> indegree = new HashMap<>();
        for (Integer id : graph.getVertexList().keySet()) {
            indegree.putIfAbsent(id, 0);
            for (Edge edge : graph.getVertexById(id).getEdgeList()) {
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
            Vertex vertex = graph.getVertexById(vertexId);
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
    public static void printTopSort(List<Vertex> vertexList){
        if(null == vertexList){
            System.out.println("存在环.");
            return;
        }
        System.out.print("Begin -> ");
        for (Vertex vertex :vertexList){
            System.out.print(String.format("%s -> ",vertex.getName()));
        }
        System.out.println("End.");
    }

    // 测试用例
    public static void main(String[] args) {

        // 构造图
        VertexList graph = new VertexList();

        Label vertexLabel = new Label("top-sort", 1);
        Vertex v1 = new Vertex("v1", vertexLabel);
        Vertex v2 = new Vertex("v2", vertexLabel);
        Vertex v3 = new Vertex("v3", vertexLabel);
        Vertex v4 = new Vertex("v4", vertexLabel);
        Vertex v5 = new Vertex("v5", vertexLabel);
        Vertex v6 = new Vertex("v6", vertexLabel);
        Vertex v7 = new Vertex("v7", vertexLabel);

        Label edgeLabel = new Label("top-sort", 2);
        v1.addEdge(v2,1,edgeLabel);
        v1.addEdge(v3,1,edgeLabel);
        v1.addEdge(v4,1,edgeLabel);

        v2.addEdge(v4,1,edgeLabel);
        v2.addEdge(v5,1,edgeLabel);

        v3.addEdge(v6,1,edgeLabel);

        v4.addEdge(v3,1,edgeLabel);
        v4.addEdge(v6,1,edgeLabel);
        v4.addEdge(v7,1,edgeLabel);

        v5.addEdge(v4,1,edgeLabel);
        v5.addEdge(v7,1,edgeLabel);

        v7.addEdge(v6,1,edgeLabel);

        // 构造环
        v4.addEdge(v2,1,edgeLabel);

        graph.addVertexs(Arrays.asList(v1,v2,v3,v4,v5,v6,v7));

        // kahn算法实现拓扑排序
        List<Vertex> vertices = topSortKahn(graph);
        printTopSort(vertices);

    }
}