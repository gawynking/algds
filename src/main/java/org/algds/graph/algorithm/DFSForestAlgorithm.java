package org.algds.graph.algorithm;

import org.algds.graph.adjacency.Graph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DFSForestAlgorithm {


    /**
     * 深度优先生成森林
     *
     * @param graph
     * @return
     */
    public static List<CSNode<Vertex>> DFSForest(Graph graph) {

        List<CSNode<Vertex>> dfsForest = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        for (Vertex vertex : graph.getVertices().values()) {
            if (!visited.contains(vertex.getId())) {
                // 创建新的树节点
                CSNode<Vertex> p = new CSNode<>(vertex);
                // 深度优先遍历构建生成树
                DFSTree(vertex, visited, p);
                dfsForest.add(p);
            }
        }
        return dfsForest;
    }


    private static void DFSTree(Vertex vertex, Set<Integer> visited, CSNode<Vertex> T) {
        visited.add(vertex.getId());

        CSNode<Vertex> firstChild = null;  // 第一个孩子节点
        CSNode<Vertex> lastSibling = null; // 上一个兄弟节点

        for (Edge edge : vertex.getEdgeList()) {
            Vertex to = edge.getTo();
            if (!visited.contains(to.getId())) {
                CSNode<Vertex> p = new CSNode<>(to);
                if (firstChild == null) {
                    // 第一个孩子作为左孩子
                    T.firstChild = p;
                    firstChild = p;
                } else {
                    // 后续孩子作为上一个孩子的右兄弟
                    lastSibling.nextSibling = p;
                }
                lastSibling = p;

                DFSTree(to, visited, p);
            }
        }
    }


    /**
     * 测试用例
     *
     * A --------------- B
     * | \               |
     * | |\     D --- E  |
     * | | C    G --- H  |
     * | F     / \   /   |
     * |      /   \ /    |
     * |      I    K     |
     * |  ———— J ————    |
     * |/             \  |
     * L --------------- M
     *
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
        Vertex F = new Vertex("F", vertexLabel);
        Vertex G = new Vertex("G", vertexLabel);
        Vertex H = new Vertex("H", vertexLabel);
        Vertex I = new Vertex("I", vertexLabel);
        Vertex J = new Vertex("J", vertexLabel);
        Vertex K = new Vertex("K", vertexLabel);
        Vertex L = new Vertex("L", vertexLabel);
        Vertex M = new Vertex("M", vertexLabel);

        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);
        graph.addVertex(D);
        graph.addVertex(E);
        graph.addVertex(F);
        graph.addVertex(G);
        graph.addVertex(H);
        graph.addVertex(I);
        graph.addVertex(J);
        graph.addVertex(K);
        graph.addVertex(L);
        graph.addVertex(M);

        // 构造边
        graph.addEdge(A, B, 1, edgeLabel);
        graph.addEdge(A, C, 1, edgeLabel);
        graph.addEdge(A, F, 1, edgeLabel);
        graph.addEdge(A, L, 1, edgeLabel);

        graph.addEdge(B, A, 1, edgeLabel);
        graph.addEdge(B, M, 1, edgeLabel);

        graph.addEdge(C, A, 1, edgeLabel);

        graph.addEdge(D, E, 1, edgeLabel);

        graph.addEdge(E, D, 1, edgeLabel);

        graph.addEdge(F, A, 1, edgeLabel);

        graph.addEdge(G, I, 1, edgeLabel);
        graph.addEdge(G, K, 1, edgeLabel);
        graph.addEdge(G, H, 1, edgeLabel);

        graph.addEdge(H, G, 1, edgeLabel);
        graph.addEdge(H, K, 1, edgeLabel);

        graph.addEdge(I, G, 1, edgeLabel);

        graph.addEdge(J, L, 1, edgeLabel);
        graph.addEdge(J, M, 1, edgeLabel);

        graph.addEdge(K, G, 1, edgeLabel);
        graph.addEdge(K, H, 1, edgeLabel);

        graph.addEdge(L, A, 1, edgeLabel);
        graph.addEdge(L, J, 1, edgeLabel);
        graph.addEdge(L, M, 1, edgeLabel);

        graph.addEdge(M, B, 1, edgeLabel);
        graph.addEdge(M, J, 1, edgeLabel);
        graph.addEdge(M, L, 1, edgeLabel);


        List<CSNode<Vertex>> dfsForest = DFSForest(graph);
        for (CSNode<Vertex> csNode : dfsForest) {
            System.out.println(csNode);
        }

    }


    /**
     * 孩子兄弟树-数据结构
     */
    public static class CSNode<T> {
        T data;             // 泛化数据
        CSNode firstChild;  // 左孩子
        CSNode nextSibling; // 右兄弟

        public CSNode(T data) {
            this.data = data;
            this.firstChild = null;
            this.nextSibling = null;
        }

        @Override
        public String toString() {
            return "CSNode{" +
                    "data=" + data +
                    ", firstChild=" + firstChild +
                    ", nextSibling=" + nextSibling +
                    '}';
        }
    }
}
