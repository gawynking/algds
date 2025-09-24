package org.algds.graph.traversal;

import org.algds.graph.adjacency.AdjacencyGraph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.*;

public class BreadthFirstSearch {

    /**
     * BFS实现：使用队列
     */
    public static List<List<Vertex>> bfs(AdjacencyGraph graph) {

        Set<Integer> visited = new HashSet<>(); // 已访问集合
        Queue<Vertex> queue = new LinkedList<>(); // 队列实现BFS

        List<List<Vertex>> bfsList = new ArrayList<>();
        for(Vertex vertex :graph.getVertices().values()) {
            if (!visited.contains(vertex.getId())) {
                queue.offer(vertex);
                visited.add(vertex.getId());
                List<Vertex> bfs = new ArrayList<>();
                while (!queue.isEmpty()) {
                    Vertex v = queue.poll();
                    bfs.add(v);
                    for (Edge edge : v.getEdgeList()) {
                        Vertex neighbor = edge.getTo();
                        if (!visited.contains(neighbor.getId())) {
                            visited.add(neighbor.getId());
                            queue.offer(neighbor);
                        }
                    }
                }
                bfsList.add(bfs);
            }
        }
        return bfsList;
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
        AdjacencyGraph graph = new AdjacencyGraph();

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
        System.out.println(String.format("顶点数量 %s",graph.getVertexNum()));
        System.out.println(String.format("边的数量 %s",graph.getEdgeNum()));

        // 执行BFS
        List<List<Vertex>> bfsList = bfs(graph);


        System.out.println(String.format("BFS遍历顺序(共%s个子图)：", bfsList.size()));
        for(int i=0; i<bfsList.size(); i++){
            List<Vertex> bfs = bfsList.get(i);
            System.out.println(String.format("--------- 第%s个子图 --------",(i+1)));
            System.out.print("    ");
            for(Vertex vertex:bfs){
                System.out.print(vertex.getName()+" -> ");
            }
            System.out.println("end.");
        }
    }

}
