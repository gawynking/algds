package org.algds.graph;

import java.util.Arrays;
import java.util.List;

/**
 * 最大流算法 有权图
 * Flow = Capacity - Residual
 * 依赖最短路径算法
 */
public class MaximumFlow {


    /**
     * 朴素算法 解不一定是最大流路径
     */
    public static void naiveMF(List<Node> nodes){

    }





    public static void main(String[] args) {

        // 构造节点
        Node s = new Node("s");
        Node v1 = new Node("v1");
        Node v2 = new Node("v2");
        Node v3 = new Node("v3");
        Node v4 = new Node("v4");
        Node t = new Node("t");

        // 构造边
        s.addEdge(v1,4);
        s.addEdge(v2,2);

        v1.addEdge(v2,1);
        v1.addEdge(v3,2);
        v1.addEdge(v4,4);

        v2.addEdge(v4,2);

        v3.addEdge(t,3);
        v4.addEdge(t,3);

        // 构造邻接表
        List<Node> nodes = Arrays.asList(s, v1, v2, v3, v4, t);




    }

}
