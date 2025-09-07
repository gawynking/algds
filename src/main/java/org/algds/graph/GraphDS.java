package org.algds.graph;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 顶点定义
 */
class Node {
    private static AtomicInteger nodeIdCounter = new AtomicInteger(0);
    public int id = 0;
    public String name;          // 顶点名称
    public List<Edge> adj;       // 邻接顶点列表
    boolean processed;           // 是否已处理
    public int dist;             // 初始距离设为无穷大
    public Node preNode;         // 最短路径上的前驱顶点

    public Node(String name) {
        this.id = genId();
        this.name = name;
        this.adj = new ArrayList<>();
        this.processed = false;
        this.dist = Integer.MAX_VALUE;
        this.preNode = null;
    }

    synchronized private int genId() {
        return nodeIdCounter.getAndIncrement();
    }

    // 添加邻接边
    public void addEdge(Node to, int weight) {
        this.adj.add(new Edge(this, to, weight));
    }
}


/**
 * 边类定义
 */
class Edge {
    private static AtomicInteger nodeIdCounter = new AtomicInteger(0);

    int id;
    Node from;
    Node to;   // 目标节点
    int weight;    // 边权重

    public Edge(Node from, Node to, int weight) {
        this.id = genId();
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    synchronized private int genId() {
        return nodeIdCounter.getAndIncrement();
    }
}


/**
 * 图数据结构
 */
public class GraphDS {

    /**
     * 一 邻接矩阵定义
     */
    public static class AdjacencyMatrix {
        private Edge[][] matrix;
        private int vertexCount;

        // 通过强制类型转换创建泛型数组
        public AdjacencyMatrix(int vertexCount) {
            this.vertexCount = vertexCount;
            this.matrix = new Edge[vertexCount][vertexCount];
        }

        public void addEdge(Node from, Node to) {
            // 无向图需要对称设置
            matrix[from.id][to.id] = new Edge(from, to, 1);
        }
    }

    /**
     * 二 邻接表定义
     */
    public static class AdjacencyListByVertex1 {
        static class Vertex {
            public int id;
            public String name;          // 顶点名称
            public List<Vertex> adj;       // 邻接顶点列表
        }
    }

    public static class AdjacencyListByVertex2 {
        static class Vertex {
            public int id;
            public String name;          // 顶点名称
            public List<Edge> adj;       // 邻接顶点边列表
        }

        static class Edge {
            int id;
            Node from;
            Node to;   // 目标节点
            int weight;    // 边权重
        }
    }

    public static class AdjacencyListByVertex { // 缺点不容易描述边信息
        private Map<Integer, List<Node>> adjList;
        private int vertexCount;

        public AdjacencyListByVertex() {
            adjList = new HashMap<>();
        }

        public void addEdge(Node from, Node to) {
            adjList.getOrDefault(from.id, new LinkedList<>()).add(to);
        }

    }

    public static class AdjacencyListByEdge {
        private Map<Integer, List<Edge>> adjList;
        private int vertexCount;

        public AdjacencyListByEdge(int vertexCount) {
            this.vertexCount = vertexCount;
            adjList = new HashMap<>();
        }

        public void addEdge(Node node, Edge edge) {
            adjList.getOrDefault(node.id, new LinkedList<>()).add(edge);
        }
    }


    /**
     * 三 关联矩阵
     */
    public static class IncidenceMatrix {
        private int[][] matrix;  // 矩阵：行=顶点，列=边
        private int vertexCount;
        private int edgeCount;

        public IncidenceMatrix(int vertexCount, int edgeCount) {
            this.vertexCount = vertexCount;
            this.edgeCount = edgeCount;
            matrix = new int[vertexCount][edgeCount];
        }

        public void addEdge(Edge edge) {
            matrix[edge.from.id][edge.id] = 1;
            matrix[edge.to.id][edge.id] = 1;
        }


        public void addEdge(Node node, Edge edge) {
            matrix[node.id][edge.id] = 1;
        }
    }

    /**
     * 四 边列表
     */
    public static class EdgeList {

        private List<Edge> edges = new ArrayList<>();

        public void addEdge(Edge edge) {
            edges.add(edge);
        }

        public void addEdge(Node from, Node to) {
            edges.add(new Edge(from, to, 1));
        }

    }
}