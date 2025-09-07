package org.algds.graph;

import java.util.*;

public class GraphShortPath {

    /**
     * 计算单源无权最短路径
     *
     * @param source 源顶点
     */
    public static void unweighted(Node source) {

        Queue<Node> queue = new LinkedList<>();
        source.dist = 0;             // 源点距离设为0
        queue.add(source);               // 源点入队

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            current.processed = true; // 列1 是否被处理过

            // 遍历所有邻接顶点
            for (Edge edge : current.adj) {
                Node neighbor = edge.to;
                // 如果顶点未被访问过
                if (!neighbor.processed) {
                    neighbor.dist = current.dist + 1;    // 列2 更新距离
                    neighbor.preNode = current;          // 列3 记录路径
                    queue.add(neighbor);                 // 邻接点入队
                }
            }
        }
    }

    /**
     * Dijkstra(迪杰斯特拉) 算法 计算 单源赋权最短路径
     *
     * @param source
     */
    public static void dijkstra(Node source) {

        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.dist));
        source.dist = 0;
        queue.add(source);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            current.processed = true; // 标识作用

            for (Edge edge : current.adj) {
                Node neighbor = edge.to;
                long newDist = (long) current.dist + edge.weight;

                if (newDist < neighbor.dist) { // 发现更短路径则更新
                    neighbor.dist = (int) newDist; // 列1 距离
                    neighbor.preNode = current;    // 列2 前一个节点
                    queue.add(neighbor);           // 邻接顶点变化过则下游节点都会跟着变化，因此加入优先队列
                }
            }
        }
    }


    /**
     * 重构从源点到目标顶点的最短路径
     *
     * @param to 目标顶点
     * @return 路径顶点列表
     */
    public static List<Node> reconstructPath(Node to) {
        List<Node> path = new ArrayList<>();
        // 如果目标顶点不可达
        if (null == to.preNode) {
            return path;
        }

        // 从目标顶点回溯到源顶点
        for (Node v = to; v != null; v = v.preNode) {
            path.add(v);
        }

        // 反转路径：从源顶点到目标顶点
        Collections.reverse(path);
        return path;
    }

    /**
     * 打印最短路径结果
     *
     * @param vertices 顶点列表
     */
    public static void printResults(List<Node> vertices) {

        List<String> shortTable = new ArrayList<>();
        for (Node node : vertices) {

            String targetNode = String.valueOf(node.name);
            String processed = String.valueOf(node.processed);
            String dist = String.valueOf(node.dist);
            String preNode = String.valueOf(Optional.ofNullable(node.preNode).orElse(new Node("")).name);

            List<Node> pathList = reconstructPath(node);
            Collections.reverse(pathList);

            String path = "";
            for (Node n : pathList) {
                path = n.name + "->" + path;
            }

            String line = String.format("%s  |  %s  |  %s  |  %s  |  %s",targetNode,processed,dist,preNode,path);

            shortTable.add(Arrays.asList(line).toString());
        }

        System.out.println(String.format("%s  |  %s  |  %s  |  %s  |  %s","目标节点","是否处理","路长","上一个节点","路径"));
        for (String l : shortTable) {
            System.out.println(l);
        }

    }

    // 测试用例
    public static void main(String[] args) {
        // 创建顶点
        Node v1 = new Node("v1");
        Node v2 = new Node("v2");
        Node v3 = new Node("v3");
        Node v4 = new Node("v4");
        Node v5 = new Node("v5");
        Node v6 = new Node("v6");
        Node v7 = new Node("v7");

        v1.addEdge(v2,2);
        v1.addEdge(v3,4);
        v1.addEdge(v4,1);

        v2.addEdge(v1,2);
        v2.addEdge(v5,10);
        v2.addEdge(v4,3);

        v3.addEdge(v1,4);
        v3.addEdge(v4,2);
        v3.addEdge(v6,5);

        v4.addEdge(v1,1);
        v4.addEdge(v2,3);
        v4.addEdge(v3,2);
        v4.addEdge(v5,7);
        v4.addEdge(v6,8);
        v4.addEdge(v7,4);

        v5.addEdge(v2,10);
        v5.addEdge(v4,7);
        v5.addEdge(v7,6);

        v6.addEdge(v3,5);
        v6.addEdge(v4,8);
        v6.addEdge(v7,1);

        v7.addEdge(v4,4);
        v7.addEdge(v5,6);
        v7.addEdge(v6,1);

        List<Node> nodes = Arrays.asList(v1, v2, v3, v4, v5, v6, v7);

        unweighted(v1);

//        dijkstra(v1);

        // 打印结果
        printResults(nodes);

        // 额外测试
        System.out.println("\n从v1到v7的最短路径:");
        List<Node> pathToGate = reconstructPath(v7);
        for (Node v : pathToGate) {
            System.out.print(v.name + (v != v7 ? " → " : ""));
        }
    }
}
