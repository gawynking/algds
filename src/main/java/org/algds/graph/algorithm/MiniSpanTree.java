package org.algds.graph.algorithm;

import org.algds.graph.adjacency.AdjacencyGraph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.*;

/**
 * 最小生成树（MST）的性质：
 *  假设 N = (V, E) 是一个连通网，U 是顶点集 V 的一个非空子集。若 (u, v) 是一条具有最小权值的边，其中 u 属于 U，v 属于 V 减去 U，则必存在一棵包含边 (u, v) 的最小生成树。
 *
 */
public class MiniSpanTree {


    /**
     * prim算法求解最小生成树 （广度优先）
     *
     */
    public static List<Edge> primMST(AdjacencyGraph graph) {

        if (graph.getEdgeNum()==0) return Collections.emptyList();

        List<Edge> mst = new ArrayList<>();
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.getWeight())); // 存储访问过的节点集合对应的边集合，并且按照权重排序
        Set<Vertex> visited = new HashSet<>(); // 存储访问过的节点集合

        // 从第一个节点开始
        Vertex startNode = graph.getVertices().get(3);
        visited.add(startNode);
        edgeQueue.addAll(startNode.getEdgeList());

        while (!edgeQueue.isEmpty() && visited.size() < graph.getVertexNum()) {
            Edge minEdge = edgeQueue.poll();
            Vertex nextNode = minEdge.getTo();

            if (!visited.contains(nextNode)) {
                visited.add(nextNode);
                mst.add(minEdge);

                // 添加新节点的所有边到优先队列
                for (Edge edge : nextNode.getEdgeList()) {
                    if (!visited.contains(edge.getTo())) {
                        edgeQueue.add(edge);
                    }
                }
            }
        }

        return mst;
    }


    /**
     * Kruskal算法求解最小生成树 （边遍历）
     *
     */
    public static List<Edge> kruskalMST(AdjacencyGraph graph) {

        if (graph.getEdgeNum()==0) return Collections.emptyList();

        List<Edge> mst = new ArrayList<>();

        // 收集所有边,按权重从小到大排序
        List<Edge> allEdges = new ArrayList<>();
        for (Vertex vertex : graph.getVertices().values()) {
            allEdges.addAll(vertex.getEdgeList());
        }
        allEdges.sort(Comparator.comparingInt(e -> e.getWeight()));


        // 使用【并查集】来检测环
        UnionFind uf = new UnionFind(graph);

        for (Edge edge : allEdges) {
            Vertex from = edge.getFrom();
            Vertex to = edge.getTo();

            if (!uf.isConnected(from, to)) { // 判断两个集合能否联通
                mst.add(edge);
                uf.union(from, to);
            }

            // 如果已经连接了所有节点，提前退出
            if (mst.size() == graph.getVertexNum() - 1) {
                break;
            }
        }

        return mst;
    }


    /**
     * 测试用例
     *          2
     *     V1 ---- V2
     *    /  \     / \
     *  4/   1\  3/ 10\
     *  /   2  \ /  7  \
     * V3 ---- V4 ---- V5
     *  \      / \     /
     *   \5   /8  \4  /6
     *    \  /     \ /
     *     V6 ---- V7
     *          1
     * @param args
     */
    public static void main(String[] args) {

        // 构建图
        AdjacencyGraph graph = new AdjacencyGraph();

        // 创建标签
        Label vertexLabel = new Label("V", 1);
        Label edgeLabel = new Label("E", 2);

        // 创建顶点
        Vertex v1 = new Vertex("v1",vertexLabel);
        Vertex v2 = new Vertex("v2",vertexLabel);
        Vertex v3 = new Vertex("v3",vertexLabel);
        Vertex v4 = new Vertex("v4",vertexLabel);
        Vertex v5 = new Vertex("v5",vertexLabel);
        Vertex v6 = new Vertex("v6",vertexLabel);
        Vertex v7 = new Vertex("v7",vertexLabel);

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);

        // 构造边
        graph.addEdge(v1,v2,2,edgeLabel);
        graph.addEdge(v1,v3,4,edgeLabel);
        graph.addEdge(v1,v4,1,edgeLabel);

        graph.addEdge(v2,v1,2,edgeLabel);
        graph.addEdge(v2,v5,10,edgeLabel);
        graph.addEdge(v2,v4,3,edgeLabel);

        graph.addEdge(v3,v1,4,edgeLabel);
        graph.addEdge(v3,v4,2,edgeLabel);
        graph.addEdge(v3,v6,5,edgeLabel);

        graph.addEdge(v4,v1,1,edgeLabel);
        graph.addEdge(v4,v2,3,edgeLabel);
        graph.addEdge(v4,v3,2,edgeLabel);
        graph.addEdge(v4,v5,7,edgeLabel);
        graph.addEdge(v4,v6,8,edgeLabel);
        graph.addEdge(v4,v7,4,edgeLabel);

        graph.addEdge(v5,v2,10,edgeLabel);
        graph.addEdge(v5,v4,7,edgeLabel);
        graph.addEdge(v5,v7,6,edgeLabel);

        graph.addEdge(v6,v3,5,edgeLabel);
        graph.addEdge(v6,v4,8,edgeLabel);
        graph.addEdge(v6,v7,1,edgeLabel);

        graph.addEdge(v7,v4,4,edgeLabel);
        graph.addEdge(v7,v5,6,edgeLabel);
        graph.addEdge(v7,v6,1,edgeLabel);


        // Prim算法求最小生成树
        System.out.println("Prim算法: ");
        List<Edge> edges = primMST(graph);
        System.out.println("Total weight: " + edges.stream().map(item -> item.getWeight()).reduce(0, Integer::sum));
        for(Edge edge:edges){
            System.out.println(edge.getFrom().getName() + "->" + edge.getTo().getName() + ":" + edge.getWeight());
        }

        System.out.println("----------------------------------------");

        // Kruskal算法求最小生成树
        System.out.println("Kruskal算法: ");
        edges = kruskalMST(graph);
        System.out.println("Total weight: " + edges.stream().map(item -> item.getWeight()).reduce(0, Integer::sum));
        for(Edge edge:edges){
            System.out.println(edge.getFrom().getName() + "->" + edge.getTo().getName() + ":" + edge.getWeight());
        }

    }



    /**
     * 并查集
     */
    public static class UnionFind {

        private Map<Vertex, Vertex> parent; // 构造森林
        private Map<Vertex, Integer> rank;

        public UnionFind(AdjacencyGraph graph) {
            parent = new HashMap<>();
            rank = new HashMap<>();

            for (Vertex node : graph.getVertices().values()) {
                parent.put(node, node);
                rank.put(node, 0);
            }
        }

        public Vertex find(Vertex vertex) {
            if (parent.get(vertex) != vertex) {
                parent.put(vertex, find(parent.get(vertex))); // 递归路径压缩
            }
            return parent.get(vertex);
        }

        public boolean isConnected(Vertex x, Vertex y) {
            return find(x) == find(y);
        }

        public void union(Vertex x, Vertex y) {
            Vertex rootX = find(x);
            Vertex rootY = find(y);

            if (rootX == rootY) return;

            // 按秩合并
            if (rank.get(rootX) > rank.get(rootY)) {
                parent.put(rootY, rootX);
            } else if (rank.get(rootX) < rank.get(rootY)) {
                parent.put(rootX, rootY);
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rank.get(rootX) + 1);
            }
        }
    }

}