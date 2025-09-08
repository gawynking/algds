package org.algds.graph.model;

/**
 * 图的边列表表示
 */
public class EdgeListGraph<V,E> {
    public static final int MAX_EDGE_NUM = 20;
    public Edge<V,E>[] edges;
    public int edgenum;

    public EdgeListGraph(){
        this.edges = (Edge<V,E>[])new Edge<?,?>[MAX_EDGE_NUM];
        this.edgenum = 0;
    }

    // 边类包含顶点数据，不包含顶点对象引用
    public static class Edge<V,E>{
        V fromData;  // 直接存储顶点数据
        V toData;
        E data;
        public Edge(V fromData, V toData, E data){
            this.fromData = fromData;
            this.toData = toData;
            this.data = data;
        }
    }
}
