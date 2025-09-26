package org.algds.graph.algorithm;

import org.algds.graph.adjacency.AdjacencyGraph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 弗洛伊德算法,求多源最短路径
 *  使用邻接表数据结构构造图，在执行floyd算法时首先将邻接表转换成索引实现邻接矩阵
 */
public class FloydAlgorithm {

    // 使用INF表示两个顶点之间没有直接连接
    final static int INF = Integer.MAX_VALUE;

    /**
     * 使用Floyd算法计算所有顶点对之间的最短路径
     */
    public static Map<String, int[][]> floyd(AdjacencyGraph graph) {

        // 通过邻接表构造邻接矩阵
//        int[][] metrix = {
//                {0, 3, INF, 7},
//                {8, 0, 2, INF},
//                {5, INF, 0, 1},
//                {2, INF, INF, 0}
//        };
        int[][] metrix = new int[graph.getVertexNum()][graph.getVertexNum()];
        for (int i = 0; i < metrix.length; i++) { // 初始化数组
            for (int j = 0; j < metrix.length; j++) {
                if (i == j) {
                    metrix[i][j] = 0;
                } else {
                    metrix[i][j] = INF;
                }
            }
        }
        Map<Integer, Vertex> vertices = graph.getVertices();
        for (Map.Entry<Integer, Vertex> entry : vertices.entrySet()) {
            Integer i = entry.getKey();
            List<Edge> edgeList = entry.getValue().getEdgeList();
            for (Edge edge : edgeList) {
                Integer j = edge.getTo().getId();
                metrix[i - 1][j - 1] = edge.getWeight();
            }
        }


        // 距离矩阵变化趋势追踪
        System.out.println("距离矩阵变化趋势追踪: ");

        // 打印原始矩阵
        printSolution(metrix, -1);

        int n = metrix.length;
        int[][] dist = new int[n][n];
        int[][] next = new int[n][n]; // next[i][j]表示在顶点i到顶点j的最短路径上，顶点i的下一个顶点索引

        // 初始化矩阵
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = metrix[i][j];
                if (dist[i][j] != Integer.MAX_VALUE) {
                    next[i][j] = j; // 初始化,顶点i到顶点j可达,初始可以顶点i的下一个顶点信息为j
                } else {
                    next[i][j] = -1; // 初始化,没有下一个顶点信息,表示i->j不可达
                }
            }
        }

        // 应用Floyd算法
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    /**
                     * 如果通过顶点k的路径更短，则更新距离
                     */
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                        // 每次更新矩阵都将大新最新距离矩阵,下一次更新要在上一次距离矩阵基础上迭代
                        printSolution(dist, k);
                    }
                }
            }
        }


        // 返回处理结果
        Map<String, int[][]> result = new HashMap<>();
        result.put("dist", dist);
        result.put("next", next);
        return result;
    }


    /**
     * 示例图
     *     3
     * 0 <---> 1
     * /|| \ 8  |
     * 2||7 \ 5 |2
     * ||/   \| |/
     * 3 <---> 3
     *     1
     *
     * @param args
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

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);


        // 构造边
        graph.addEdge(v1, v2, 3, edgeLabel);
        graph.addEdge(v1, v4, 7, edgeLabel);

        graph.addEdge(v2, v3, 2, edgeLabel);
        graph.addEdge(v2, v1, 8, edgeLabel);

        graph.addEdge(v3, v1, 5, edgeLabel);
        graph.addEdge(v3, v4, 1, edgeLabel);

        graph.addEdge(v4, v1, 2, edgeLabel);



        // 应用Floyd算法
        Map<String, int[][]> floyd = floyd(graph);


        // 打印多源最短路径
        System.out.println("============================================================================");
        int[][] nexts = floyd.get("next");
        printShortestPath(graph, nexts);

    }

    // 打印多源最短路径
    private static void printShortestPath(AdjacencyGraph graph, int[][] next) {
        int n = next.length;
        for (int i = 0; i < n; i++) {
            System.out.println(String.format("源点: %s", graph.getVertexName(i + 1)));
            for (int j = 0; j < n; j++) {
                System.out.print(String.format("    %s -> %s 最短路径 : %s -> ", graph.getVertexName(i + 1), graph.getVertexName(j + 1), graph.getVertexName(i + 1)));
                int index = next[i][j];
                while (index != -1 && index != i && index != j) {
                    System.out.print(String.format("%s -> ", graph.getVertexName(index + 1)));
                    index = next[index][j];
                }
                if (index != i) {
                    System.out.println(String.format("%s -> ", graph.getVertexName(index + 1)));
                } else {
                    System.out.println();
                }
            }
        }
    }

    // 打印最短距离矩阵
    private static void printSolution(int[][] dist, int k) {
        int n = dist.length;

        if (k == -1) {
            System.out.println("原始图的邻接矩阵:");
        } else if (k == -2) {
            System.out.println("所有顶点对之间的最短距离矩阵:");
        } else {
            System.out.println("所有顶点对之间的最短距离矩阵: k = " + k);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (dist[i][j] == Integer.MAX_VALUE) {
                    System.out.print("INF\t");
                } else {
                    System.out.print(dist[i][j] + "\t");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}