package org.algds.graph.algorithm;

import org.algds.graph.adjacency.AdjacencyGraph;
import org.algds.graph.adjacency.Edge;
import org.algds.graph.adjacency.Label;
import org.algds.graph.adjacency.Vertex;

import java.util.*;

public class ShortestPath {

    /**
     * 计算单源无权最短路径
     *
     */
    public static Map<String,Map<Integer,Integer>> unweighted(AdjacencyGraph graph, Vertex start) {

        Queue<Vertex> queue = new LinkedList<>();

        Map<Integer, Integer> processed = new HashMap<>(); // 保存处理过顶点
        Map<Integer, Integer> dist = new HashMap<>();      // 保存距离
        Map<Integer, Integer> preVertex = new HashMap<>(); // 保存前一个顶点ID

        dist.put(start.getId(),0);                         // 源点距离设为0
        queue.add(start);                                  // 源点入队
        preVertex.put(start.getId(),null);                 // 设置源点路径为null

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            processed.put(current.getId(),1);              // 列1 是否被处理过

            // 遍历所有邻接顶点
            for (Edge edge : current.getEdgeList()) {
                Vertex neighbor = edge.getTo();
                // 如果顶点未被访问过
                if (!processed.containsKey(neighbor.getId())) {
                    dist.put(neighbor.getId(),dist.getOrDefault(current.getId(),0)+1);    // 列2 更新距离
                    preVertex.put(neighbor.getId(),current.getId());                                  // 列3 记录路径
                    queue.add(neighbor);                                                              // 邻接点入队
                }
            }
        }

        Map<String,Map<Integer,Integer>> result = new HashMap<>();
        result.put("processed",processed);
        result.put("dist",dist);
        result.put("preVertex",preVertex);
        return result;
    }


    /**
     * Dijkstra(迪杰斯特拉) 算法 计算 单源赋权最短路径
     *
     */
    public static Map<String,Map<Integer,Integer>> dijkstra(AdjacencyGraph graph, Vertex start) {

        PriorityQueue<Dist> queue = new PriorityQueue<>(Comparator.comparingInt(item -> item.getDist()));

        Map<Integer, Integer> processed = new HashMap<>();        // 保存处理过顶点
        Map<Integer, Dist> dist = new HashMap<>();                // 保存距离,为了便于使用优先队列,这里定义了本地类Dist辅助算法实现
        Map<Integer, Integer> preVertex = new HashMap<>();        // 保存前一个顶点ID


        dist.put(start.getId(),new Dist(start.getId(), 0));  // 源点距离设为0
        queue.add(dist.get(start.getId()));                       // 源点入队
        preVertex.put(start.getId(),null);                        // 设置源点路径为null


        while (!queue.isEmpty()) {
            Dist current = queue.poll();
            processed.put(current.getId(), 1); // 标识作用

            for (Edge edge : graph.getVertices().get(current.getId()).getEdgeList()) {
                Vertex neighbor = edge.getTo();
                if(!dist.containsKey(neighbor.getId())) dist.put(neighbor.getId(),new Dist(neighbor.getId(),Integer.MAX_VALUE));
                Integer newDist = dist.getOrDefault(current.getId(),new Dist(current.getId(), Integer.MAX_VALUE)).getDist() + edge.getWeight();

                if (newDist < dist.get(neighbor.getId()).getDist()) { // 发现更短路径则更新

                    dist.getOrDefault(neighbor.getId(),new Dist(neighbor.getId(), Integer.MAX_VALUE)).setDist(newDist);      // 更新距离
                    preVertex.put(neighbor.getId(),current.getId());                                                         // 列2 前一个节点
                    queue.add(dist.getOrDefault(neighbor.getId(),new Dist(neighbor.getId(), Integer.MAX_VALUE)));            // 邻接顶点变化过则下游节点都会跟着变化，因此加入优先队列
                }
            }
        }

        Map<String,Map<Integer,Integer>> result = new HashMap<>();
        result.put("processed",processed);
        result.put("preVertex",preVertex);
        Map<Integer, Integer> newDist = new HashMap<>();
        for(Map.Entry<Integer,Dist> item :dist.entrySet()){
            newDist.put(item.getKey(),item.getValue().getDist());
        }
        result.put("dist",newDist);
        return result;
    }


    /**
     * 重构从源点到目标顶点的最短路径
     *
     */
    public static Stack<Integer> reconstructPath(Map<Integer,Integer> preVertex,Integer target) {
        Stack<Integer> path = new Stack<>();
        // 如果目标顶点不可达
        if (null == preVertex.get(target)) {
            return path;
        }

        // 从目标顶点回溯到源顶点
        path.push(target);
        Integer pre = preVertex.get(target);
        while (pre != null){
            path.push(pre);
            pre = preVertex.get(pre);
        }

        return path;
    }

    /**
     * 打印最短路径结果
     *
     */
    public static void printResults(AdjacencyGraph graph, Map<String, Map<Integer, Integer>> result) {

        Map<Integer, Integer> preVertex = result.get("preVertex");
        Map<Integer, Integer> dist = result.get("dist");

        System.out.println(String.format("%s  |  %s  |  %s","目标节点","路长","路径"));
        for(Integer vertex:preVertex.keySet()){
            System.out.print(String.format("  %s         %s      ",graph.getVertexName(vertex),dist.get(vertex)));
            Stack<Integer> path = reconstructPath(preVertex, vertex);
            while (!path.isEmpty()){
                Integer tmp = path.pop();
                System.out.print(String.format("%s ->",graph.getVertexName(tmp)));
            }
            System.out.println();
        }
    }

    public static class Dist{
        Integer id;
        Integer dist;

        public Dist(Integer id, Integer dist) {
            this.id = id;
            this.dist = dist;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getDist() {
            return dist;
        }

        public void setDist(Integer dist) {
            this.dist = dist;
        }
    }

    /**
     * 测试图例
     *         v1 ----> v2
     *        /| \      / \
     *       /    \    /   \
     *      /      \||/     \|
     *     v3 <---- v4 ----> v5
     *      \      / \      /
     *       \    /   \    /
     *        \||/     \||/
     *         v6 <---- v7
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
        graph.addEdge(v1,v4,1,edgeLabel);

        graph.addEdge(v2,v5,10,edgeLabel);
        graph.addEdge(v2,v4,3,edgeLabel);

        graph.addEdge(v3,v1,4,edgeLabel);
        graph.addEdge(v3,v6,5,edgeLabel);


        graph.addEdge(v4,v3,2,edgeLabel);
        graph.addEdge(v4,v5,2,edgeLabel);
        graph.addEdge(v4,v6,8,edgeLabel);
        graph.addEdge(v4,v7,4,edgeLabel);

        graph.addEdge(v5,v7,6,edgeLabel);

        graph.addEdge(v7,v6,1,edgeLabel);


        // 无权最短路径
        Map<String, Map<Integer, Integer>> unweighted = unweighted(graph, v1);
        System.out.println("单源无权最短路径算法: ");
        printResults(graph,unweighted);

        System.out.println("---------------------------------------------");

        // 赋权最短路径
        Map<String, Map<Integer, Integer>> dijkstra = dijkstra(graph, v1);
        System.out.println("单源赋权最短路径算法: ");
        printResults(graph,dijkstra);

    }
}
