package org.algds.graph.model.matrix;

import java.lang.reflect.Array;

/**
 * 图的邻接矩阵表示
 */
public class MatrixGraph<T> {

    // 最大顶点个数
    public static final int MAX_VERTEX_NUM = 20;
    // 顶点向量集合,用来存储顶点信息,数组索引作为数据索引,用来构建邻接矩阵
    private T[] vexs;
    // 邻接矩阵
    private AdjMatrix arcs = new AdjMatrix();
    // 图的当前顶点数和弧数
    public int vexnum;
    public int arcnum;
    // 图的种类标志
    public GraphKind kind;

    @SuppressWarnings("unchecked")
    public MatrixGraph(Class<?> clazz) {
        vexs = (T[]) Array.newInstance(clazz, MAX_VERTEX_NUM);
        // 初始化邻接矩阵的每个单元格
        for (int i = 0; i < MAX_VERTEX_NUM; i++) {
            for (int j = 0; j < MAX_VERTEX_NUM; j++) {
                arcs.cells[i][j] = new ArcCell<>();
            }
        }
    }

    // 图的种类 {有向图,有向网,无向图,无向网}
    public enum GraphKind { DG, DN, UDG, UDN }

    // 弧单元定义
    public static class ArcCell<U> {
        // 顶点关系类型。对无权图,用 1 或 0 表示相邻否;对带权图,则为权值类型
        public U adj;

        // 该弧相关信息
        public Object info;
    }

    // 邻接矩阵类型
    public static class AdjMatrix {
        public ArcCell<?>[][] cells = new ArcCell[MAX_VERTEX_NUM][MAX_VERTEX_NUM];
    }

    // 提供访问vexs和arcs的公共方法
    public T[] getVexs() {
        return vexs;
    }

    public AdjMatrix getArcs() {
        return arcs;
    }

    public static void main(String[] args) {
        MatrixGraph<Object> matrixGraph = new MatrixGraph<>(Integer.class);
        AdjMatrix arcs = matrixGraph.getArcs();
        System.out.println(arcs);
    }
}