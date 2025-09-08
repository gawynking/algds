package org.algds.graph.model;

/**
 * 图的关联矩阵表示
 */
public class IncidenceMatrixGraph<V,E> {
    public static final int MAX_VERTEX_NUM = 20;
    public static final int MAX_EDGE_NUM = 100;

    // 顶点数组
    public Vertex<V>[] vertices;

    // 边数组（只存储边数据，不存储端点信息）
    public Edge<E>[] edges;

    // 关联矩阵：表达顶点与边的关联关系
    public int[][] matrix;

    // 当前顶点数和边数
    public int vernum;
    public int edgenum;

    // 图类型：0-无向图，1-有向图
    public int graphType;

    public IncidenceMatrixGraph(int graphType) {
        this.vertices = (Vertex<V>[])new Vertex<?>[MAX_VERTEX_NUM];
        this.edges = (Edge<E>[])new Edge<?>[MAX_EDGE_NUM];
        this.matrix = new int[MAX_VERTEX_NUM][MAX_EDGE_NUM];
        this.vernum = 0;
        this.edgenum = 0;
        this.graphType = graphType;
    }

    public static class Vertex<V> {
        V data;
        public Vertex(V data) {
            this.data = data;
        }
    }

    // 边只存储自身数据，端点信息由矩阵表达
    public static class Edge<E> {
        E data;
        public Edge(E data) {
            this.data = data;
        }
    }
}