package org.algds.graph.algorithm;

import org.algds.graph.adjacency.Graph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.*;

/**
 * 求连通图的割点
 */
public class FindArticulAlgorithm {

    private static Integer counter = 1;


    /**
     * 求割点
     *
     * @param graph
     */
    public static Set<Integer> findArticul(Graph graph) {

        Set<Integer> articulSet = new HashSet<>();
        Map<Integer, Integer> visited = new HashMap<>();

        // 获取第一个节点信息,顶点编号从1开始
        Vertex start = graph.getVertex(1);
        Integer treeNumber = 0;
        visited.put(1, counter);

        // 遍历首节点的邻接表
        List<Edge> edgeList = start.getEdgeList();
        for (Edge edge : edgeList) {
            Vertex to = edge.getTo();
            if (!visited.containsKey(to.getId())) {
                dfsArticul(to, visited, articulSet);
                treeNumber++;
            }
        }
        // 1 如果根节点存在多棵不相关子树则说明根节点是割点
        if (treeNumber > 1) {
            articulSet.add(start.getId());
        }

        return articulSet;
    }

    /**
     * 查找分支割点
     */
    public static Integer dfsArticul(Vertex start, Map<Integer, Integer> visited, Set<Integer> articulSet) {
        Integer min = ++counter;
        visited.put(start.getId(), counter);

        for (Edge edge : start.getEdgeList()) {
            Vertex to = edge.getTo();
            if (!visited.containsKey(to.getId())) { // 子节点
                Integer subMin = dfsArticul(to, visited, articulSet);
                if (subMin >= min) { // 2 如果子节点最小值>=当前节点处理序号, 那么当前节点必为割点
                    articulSet.add(start.getId());
                } else {
                    min = subMin;
                }
            } else { // 回边节点求最小值
                if(visited.get(to.getId())<min){
                    min = visited.get(to.getId());
                }
            }
        }
        return min;
    }


    /**
     * 测试示例
     * A ------------------ B
     * |       _________/ | |
     * |\     /|  ______| | |
     * | \   / | |        | |
     * | |\ /  | D --- E  | |
     * | | |   \         /  |
     * | | C     G --- H    |
     * | F      / \   /     |
     * |       /   \ /      |
     * |       I    K       |
     * |  _____ J _______   |
     * |/                 \ |
     * L ------------------ M
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
        graph.addEdge(B, C, 1, edgeLabel);
        graph.addEdge(B, D, 1, edgeLabel);
        graph.addEdge(B, G, 1, edgeLabel);
        graph.addEdge(B, H, 1, edgeLabel);
        graph.addEdge(B, M, 1, edgeLabel);

        graph.addEdge(C, A, 1, edgeLabel);
        graph.addEdge(C, B, 1, edgeLabel);

        graph.addEdge(D, B, 1, edgeLabel);
        graph.addEdge(D, E, 1, edgeLabel);

        graph.addEdge(E, D, 1, edgeLabel);

        graph.addEdge(F, A, 1, edgeLabel);

        graph.addEdge(G, B, 1, edgeLabel);
        graph.addEdge(G, I, 1, edgeLabel);
        graph.addEdge(G, K, 1, edgeLabel);
        graph.addEdge(G, H, 1, edgeLabel);

        graph.addEdge(H, B, 1, edgeLabel);
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

        // 求割点
        Set<Integer> articulSet = findArticul(graph);
        System.out.println("打印所有割点: ");
        for (Integer vertexId : articulSet) {
            System.out.println(String.format("    顶点 %s 为割点.", graph.getVertexName(vertexId)));
        }

    }
}
