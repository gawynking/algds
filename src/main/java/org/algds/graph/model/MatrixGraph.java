package org.algds.graph.model;

/**
 * 图的邻接矩阵表示
 */
public class MatrixGraph<V,E,ED> {

    // 最大顶点个数
    public static final int MAX_VERTEX_NUM = 20;
    // 顶点向量集合,用来存储顶点信息,数组索引作为数据索引,用来构建邻接矩阵
    private Vertex<V>[] vexs;
    // 邻接矩阵
    private AdjMatrix<E,ED> arcs = new AdjMatrix<>();
    // 图的当前顶点数和弧数
    public int vexnum;
    public int arcnum;
    // 图的种类标志
    public GraphKind kind;

    @SuppressWarnings("unchecked")
    public MatrixGraph() {
        vexs = (Vertex<V>[])new Vertex<?>[MAX_VERTEX_NUM];
        // 初始化邻接矩阵的每个单元格
        for (int i = 0; i < MAX_VERTEX_NUM; i++) {
            for (int j = 0; j < MAX_VERTEX_NUM; j++) {
                arcs.cells[i][j] = new ArcCell<>();
            }
        }
    }

    // 图的种类 {有向图,有向网,无向图,无向网}
    public enum GraphKind { DG, DN, UDG, UDN }

    public static class Vertex<V>{
        public V data;
    }

    // 弧单元定义
    public static class ArcCell<E,ED> {
        // 顶点关系类型。对无权图,用 1 或 0 表示相邻否;对带权图,则为权值类型
        public E adj;

        // 该弧相关信息
        public ED info;
    }

    // 邻接矩阵类型
    public static class AdjMatrix<E,ED> {
        public ArcCell<E,ED>[][] cells = new ArcCell[MAX_VERTEX_NUM][MAX_VERTEX_NUM];
    }

    // 提供访问vexs和arcs的公共方法
    public Vertex<V>[] getVexs() {
        return vexs;
    }

    public AdjMatrix<E,ED> getArcs() {
        return arcs;
    }

}