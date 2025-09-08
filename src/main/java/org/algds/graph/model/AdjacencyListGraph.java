package org.algds.graph.model;

/**
 * 图的邻接表数据结构表示
 */
public class AdjacencyListGraph<V, E> {
    public static final int MAX_VERTEX_NUM = 20;
    public VNode<V, E>[] vertices;         // 邻接表
    public int vexnum;                     // 图的当前顶点数
    public int arcnum;                     // 图的当前弧数
    public GraphKind kind = GraphKind.UDG; // 图的种类标志

    @SuppressWarnings("unchecked")
    public AdjacencyListGraph() {
        vertices = (VNode<V, E>[]) new VNode<?,?>[MAX_VERTEX_NUM];
        vexnum = 0;
        arcnum = 0;
    }

    // 图的种类 {有向图,有向网,无向图,无向网}
    public enum GraphKind { DG, DN, UDG, UDN }

    // 弧节点类
    public static class ArcNode<E> {
        public int adjvex;            // 该弧所指向的顶点的位置
        public ArcNode<E> nextarc;    // 指向下一条弧的指针
        public E info;                // 该弧相关信息的指针（泛型类型）

        public ArcNode(int adjvex, E info) {
            this.adjvex = adjvex;
            this.info = info;
            this.nextarc = null;
        }
    }

    // 顶点节点类
    public static class VNode<V, E> {
        public V data;                // 顶点信息（泛型类型）
        public ArcNode<E> firstarc;   // 指向第一条依附该顶点的弧的指针

        public VNode(V data) {
            this.data = data;
            this.firstarc = null;
        }
    }

    // 示例使用方法
    public void addVertex(int index, V data) {
        if (index >= 0 && index < MAX_VERTEX_NUM) {
            vertices[index] = new VNode<>(data);
            vexnum++;
        }
    }

    public void addArc(int fromIndex, int toIndex, E info) {
        if (fromIndex >= 0 && fromIndex < MAX_VERTEX_NUM && toIndex >= 0 && toIndex < MAX_VERTEX_NUM) {
            ArcNode<E> newArc = new ArcNode<>(toIndex, info);
            newArc.nextarc = vertices[fromIndex].firstarc;
            vertices[fromIndex].firstarc = newArc;
            arcnum++;
        }
    }
}