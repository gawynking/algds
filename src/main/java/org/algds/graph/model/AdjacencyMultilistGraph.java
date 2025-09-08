package org.algds.graph.model;

/**
 * 无向图的邻接多重表存储表示
 */
public class AdjacencyMultilistGraph<V,E> {
    public static final int MAX_VERTEX_NUM = 20;

    public VexBox<V,E>[] adjmulist; // 邻接多重表
    public int vexnum;          // 当前顶点数
    public int edgenum;         // 当前边数

    @SuppressWarnings("unchecked")
    public AdjacencyMultilistGraph() {
        this.adjmulist = (VexBox<V, E>[]) new VexBox<?,?>[MAX_VERTEX_NUM];
        this.vexnum = 0;
        this.edgenum = 0;
    }

    public static class EBox<E> {
        public int iVex;        // 该边依附的两个顶点的位置
        public int jVex;        // 该边依附的两个顶点的位置
        public EBox<E> iLink;   // 指向依附顶点iVex的下一条边
        public EBox<E> jLink;   // 指向依附顶点jVex的下一条边
        public E info;          // 该边信息指针
    }

    public static class VexBox<V,E> {
        public V data;              // 顶点数据
        public EBox<E> firstedge;   // 指向第一条依附该顶点的边
    }

}