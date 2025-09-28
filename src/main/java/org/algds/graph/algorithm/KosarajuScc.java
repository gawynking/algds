package org.algds.graph.algorithm;

import org.algds.graph.adjacency.Graph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.*;

/**
 * Kosaraju算法求解有向图强连通分量
 */
public class KosarajuScc {

    /**
     * kosaraju算法求有向图强连通分量
     *
     * 强连通分量在反转后不变：如果将图中所有边的方向反转（称为转置图），那么原图的强连通分量结构在转置图中保持不变。
     *
     */
    public static Result kosaraju(Graph graph) {

        List<List<Vertex>> kosaraju = new ArrayList<>();

        // 1 第一次深度优先遍历获取顶点的DFS逆后续列表
        Stack<Integer> dfsStack = new Stack<>();
        Set<Integer> visited = new HashSet<>();
        for (Vertex vertex : graph.getVertices().values()) {
            if (!visited.contains(vertex.getId())) {
                dfs(vertex, visited, dfsStack); // 图DFS
            }
        }

        // 2 获取原图G的反向图Gr
        Graph graphR = new Graph();
        for (Vertex vertex : graph.getVertices().values()) {
            Vertex newFrom = new Vertex(vertex.getId(), vertex.getName(), vertex.getLabel());
            if (!graphR.getVertices().containsKey(newFrom.getId())) {
                graphR.addVertex(newFrom); // 添加顶点
            }
            for (Edge edge : vertex.getEdgeList()) {
                Vertex newTo = new Vertex(edge.getTo().getId(), edge.getTo().getName(), edge.getTo().getLabel());
                if (!graphR.getVertices().containsKey(newTo.getId())) {
                    graphR.addVertex(newTo); // 添加顶点
                }
                graphR.addEdge(edge.getId(), newTo, newFrom, edge.getWeight(), edge.getLabel()); // 添加反向边
            }
        }

        // 3 第二次DFS遍历针对第一次获取逆后续顺序进行遍历,每次返回的DFS顶点集合即为有向图G的强连通分量
        Integer counter = 0;
        Map<Integer, Integer> sccMap = new HashMap<>();
        visited = new HashSet<>();
        while (!dfsStack.isEmpty()) {
            Integer vertexId = dfsStack.pop();
            if (!visited.contains(vertexId)) {
                counter++;
                List<Vertex> scc = new ArrayList<>();
                rdfs(graphR, vertexId, visited, scc, sccMap, counter); // 反向图DFS
                kosaraju.add(scc);
            }
        }

        // 组装返回结果
        Result result = new Result();
        result.kosaraju = kosaraju;
        result.sccMap = sccMap;
        result.counter = counter;

        return result;
    }

    /**
     * 首次DFS遍历获取有向图你后续序列
     *
     * @param curVertex
     * @param visited
     * @param stack
     * @return
     */
    public static Stack<Integer> dfs(Vertex curVertex, Set<Integer> visited, Stack<Integer> stack) {
        if (curVertex == null) {
            return stack;
        }
        visited.add(curVertex.getId());
        for (Edge edge : curVertex.getEdgeList()) {
            Vertex neighbor = edge.getTo();
            if (!visited.contains(neighbor.getId())) {
                dfs(neighbor, visited, stack);
            }
        }
        stack.push(curVertex.getId());
        return stack;
    }

    /**
     * 二次DFS遍历获取有向图强连通分量集合
     *
     * @param graphR
     * @param curVertexId
     * @param visited
     * @param scc
     * @param sccMap
     * @param counter
     * @return
     */
    public static List<Vertex> rdfs(Graph graphR, Integer curVertexId, Set<Integer> visited, List<Vertex> scc, Map<Integer, Integer> sccMap, Integer counter) {
        if (null == curVertexId) {
            return scc;
        }
        Vertex vertex = graphR.getVertices().get(curVertexId);
        scc.add(vertex);
        sccMap.put(vertex.getId(), counter);
        visited.add(vertex.getId());
        for (Edge edge : vertex.getEdgeList()) {
            Vertex to = edge.getTo();
            if (!visited.contains(to.getId())) {
                rdfs(graphR, to.getId(), visited, scc, sccMap, counter);
            }
        }
        return scc;
    }


    public static class Result {
        List<List<Vertex>> kosaraju;
        Map<Integer, Integer> sccMap;
        Integer counter;
    }

    /**
     * 测试用例
     * A -> B -> C -> D -> E
     *     /|   /    /|    |
     *      ||/       |   \|
     *      F         G <- H
     */
    public static void main(String[] args) {

        // 构建图
        Graph graph = new Graph();

        // 创建标签
        Label vertexLabel = new Label("V", 1);
        Label edgeLabel = new Label("E", 2);

        // 构造顶点
        Vertex A = new Vertex("A", vertexLabel);
        Vertex B = new Vertex("B", vertexLabel);
        Vertex C = new Vertex("C", vertexLabel);
        Vertex D = new Vertex("D", vertexLabel);
        Vertex E = new Vertex("E", vertexLabel);
        Vertex F = new Vertex("F", vertexLabel);
        Vertex G = new Vertex("G", vertexLabel);
        Vertex H = new Vertex("H", vertexLabel);

        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);
        graph.addVertex(D);
        graph.addVertex(E);
        graph.addVertex(F);
        graph.addVertex(G);
        graph.addVertex(H);

        // 构造边
        graph.addEdge(A, B, 1, edgeLabel);

        graph.addEdge(B, C, 1, edgeLabel);

        graph.addEdge(C, D, 1, edgeLabel);
        graph.addEdge(C, F, 1, edgeLabel);

        graph.addEdge(D, E, 1, edgeLabel);

        graph.addEdge(E, H, 1, edgeLabel);

        graph.addEdge(F, B, 1, edgeLabel);

        graph.addEdge(G, D, 1, edgeLabel);

        graph.addEdge(H, G, 1, edgeLabel);

        // 求有向图强连通分量
        Result kosarajuResult = kosaraju(graph);
        System.out.println(String.format("本次执行有向图强连通算法共识别%s个强连通分量: ",kosarajuResult.counter));
        for (List<Vertex> scc : kosarajuResult.kosaraju) {
            System.out.println(scc);
        }
        System.out.println(kosarajuResult.sccMap);
    }

}
