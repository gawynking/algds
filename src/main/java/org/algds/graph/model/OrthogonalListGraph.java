package org.algds.graph.model;

/**
 * 有向图的十字链表存储表示
 */
public class OrthogonalListGraph<V, E> {
    public static final int MAX_VERTEX_NUM = 20;
    public VexNode<V,E>[] xlist;        // 表头向量（顶点数组）
    public int vexnum;             // 有向图的当前顶点数
    public int arcnum;             // 有向图的当前弧数

    @SuppressWarnings("unchecked")
    public OrthogonalListGraph() {
        xlist = (VexNode<V,E>[]) new VexNode<?,?>[MAX_VERTEX_NUM];
        vexnum = 0;
        arcnum = 0;
    }

    public static class ArcBox<E> {
        public int tailvex;        // 该弧的尾顶点的位置
        public int headvex;        // 该弧的头顶点的位置
        public ArcBox<E> hlink;    // 弧头相同的弧的链域
        public ArcBox<E> tlink;    // 弧尾相同的弧的链域
        public E info;             // 该弧相关信息的指针
    }

    public static class VexNode<V,E> {
        public V data;             // 顶点数据
        public ArcBox<E> firstin;  // 指向该顶点第一条入弧
        public ArcBox<E> firstout; // 指向该顶点第一条出弧
    }
}