package org.algds.graph.algorithm;

import org.algds.graph.adjacency.Graph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.*;

/**
 * 求AOE-网关键路径算法实现
 * 输入数据结构: adjacency.AdjacencyGraph
 */
public class CriticalPath {

    /**
     * 求AOE-网关键路径
     * 算法介绍：
     * 1 对输入图执行拓扑排序，在拓扑排序过程中计算每个事件节点最早开始时间ve
     * 2 针对执行完成拓扑排序的有序图执行逆拓扑排序顺序遍历，在遍历过程中计算每个事件节点最晚开始事件vl
     * 3 执行完成拓扑排序遍历和逆拓扑排序遍历后，针对ve=vl的节点即为关键节点
     *
     * @param graph
     */
    public static List<Vertex> criticalPath(Graph graph) {

        TopSortResult topSort = topSortKahn(graph);
        if (null == topSort) {
            return null;
        }

        Stack<Vertex> topSortStack = topSort.getTopSort();
        Map<Integer, Integer> ve = topSort.getVe(); // 存储事件节点最早开始时间ve
        Map<Integer, Integer> vl = new HashMap<>(); // 存储事件节点最晚开始事件vl

        Integer maxCost = topSort.getMaxCost();

        List<Vertex> creticalPathList = new ArrayList<>();
        // 执行逆拓扑排序遍历
        while (!topSortStack.isEmpty()) {
            Vertex vertex = topSortStack.pop();
            vl.put(vertex.getId(), vl.getOrDefault(vertex.getId(), ve.get(vertex.getId()))); // 初始化用最早完成时间
            Integer lCost = maxCost;
            for (Edge adj : vertex.getEdgeList()) {
                Vertex to = adj.getTo();
                Integer newLCost = vl.getOrDefault(to.getId(), ve.get(to.getId())) - adj.getWeight(); // 后继节点最晚开始时间 - 弧成本
                lCost = Math.min(lCost, newLCost);
                vl.put(vertex.getId(), lCost);
            }
        }

        // 计算关键路径,ve=vl的事件节点即为关键路径节点
        for (int j = 1; j <= graph.getVertexNum(); j++) {
            if (ve.get(j) == vl.get(j)) {
                creticalPathList.add(graph.getVertices().get(j));
            }
        }

        return creticalPathList;
    }


    public static TopSortResult topSortKahn(Graph graph) {

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
        Stack<Vertex> topSortStack = new Stack<>();
        Map<Integer, Integer> ve = new HashMap<>(); // 关键路径扩展结构，存储每个节点最早开始时间
        Integer maxCost = 0;
        while (!queue.isEmpty()) {
            Integer vertexId = queue.poll();
            Vertex vertex = graph.getVertices().get(vertexId);
            topSortStack.push(vertex);
            ve.put(vertexId, ve.getOrDefault(vertexId, 0));

            List<Edge> neighbors = vertex.getEdgeList();
            for (Edge neighbor : neighbors) {
                Vertex to = neighbor.getTo();
                indegree.put(to.getId(), indegree.get(to.getId()) - 1);
                if (indegree.get(to.getId()) == 0) {
                    queue.add(to.getId());
                }
                // 关键路径扩展-计算节点最早开始时间
                if (ve.getOrDefault(vertex.getId(), 0) + neighbor.getWeight() > ve.getOrDefault(to.getId(), 0)) {
                    ve.put(to.getId(), ve.getOrDefault(vertex.getId(), 0) + neighbor.getWeight());
                    maxCost = ve.get(to.getId());
                }
            }
        }

        // 4. 如果排序结果数量 < 节点总数，说明有环
        if (topSortStack.size() != indegree.size()) {
            return null;
        }

        // 5. 封装返回结构
        return new TopSortResult(topSortStack, ve, maxCost);
    }


    /**
     * 单元测试
     *          v2        v7
     *        /| \      /| \
     *      6/    \1  9/   2\
     *      /      \| /      \|
     *     v1        v5        v9
     *     | \      /| \      /|
     *    5| 4\   1/   7\   4/
     *     |   \| /      \| /
     *     |    v3        v8
     *     |             /|
     *      \|    2    4/
     *        v4 ----> v6
     * @param args
     */
    public static void main(String[] args) {

        // 构建图
        Graph graph = new Graph();

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
        Vertex v8 = new Vertex("v8", vertexLabel);
        Vertex v9 = new Vertex("v9", vertexLabel);

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);
        graph.addVertex(v8);
        graph.addVertex(v9);

        // 构造边
        graph.addEdge(v1, v2, 6, edgeLabel);
        graph.addEdge(v1, v3, 4, edgeLabel);
        graph.addEdge(v1, v4, 5, edgeLabel);

        graph.addEdge(v2, v5, 1, edgeLabel);

        graph.addEdge(v3, v5, 1, edgeLabel);

        graph.addEdge(v4, v6, 2, edgeLabel);

        graph.addEdge(v5, v7, 9, edgeLabel);
        graph.addEdge(v5, v8, 7, edgeLabel);

        graph.addEdge(v6, v8, 4, edgeLabel);

        graph.addEdge(v7, v9, 2, edgeLabel);
        graph.addEdge(v8, v9, 4, edgeLabel);

        // 构造环
//        graph.addEdge(v7,v5,1,edgeLabel);

        // 求关键路径
        List<Vertex> criticalPath = criticalPath(graph);
        if (null == criticalPath) {
            System.out.println("输入图存在环,无法计算关键路径.");
        } else {
            System.out.print("关键路径: ");
            for (Vertex vertex : criticalPath) {
                System.out.print(vertex.getName() + " -> ");
            }
            System.out.println("end.");
        }

    }


    /**
     * 封装拓扑排序返回结果,为关键路径求解输入
     */
    public static class TopSortResult {
        Stack<Vertex> topSort; // 拓扑排序栈
        Map<Integer, Integer> ve; // 节点最早开始时间
        Integer maxCost; // 最晚完成时间 = 最后节点最早开始时间

        public TopSortResult(Stack<Vertex> result, Map<Integer, Integer> ve, Integer maxCost) {
            this.topSort = result;
            this.ve = ve;
            this.maxCost = maxCost;
        }

        public Stack<Vertex> getTopSort() {
            return topSort;
        }

        public void setTopSort(Stack<Vertex> topSort) {
            this.topSort = topSort;
        }

        public Map<Integer, Integer> getVe() {
            return ve;
        }

        public void setVe(Map<Integer, Integer> ve) {
            this.ve = ve;
        }

        public Integer getMaxCost() {
            return maxCost;
        }

        public void setMaxCost(Integer maxCost) {
            this.maxCost = maxCost;
        }
    }
}