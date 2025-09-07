package org.algds.graph;

import java.util.*;

/**
 * 无向图 无环 连通
 */
public class MinimumSpanningTrees {


    /**
     * prim算法求解最小生成树 （广度优先）
     *
     * @param nodes
     */
    public static List<Edge> primMST(List<Node> nodes) {
        if (nodes.isEmpty()) return Collections.emptyList();

        List<Edge> mst = new ArrayList<>();
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight)); // 存储访问过的节点集合对应的边集合，并且按照权重排序
        Set<Node> visited = new HashSet<>(); // 存储访问过的节点集合

        // 从第一个节点开始
        Node startNode = nodes.get(3);
        visited.add(startNode);
        edgeQueue.addAll(startNode.adj);

        while (!edgeQueue.isEmpty() && visited.size() < nodes.size()) {
            Edge minEdge = edgeQueue.poll();
            Node nextNode = minEdge.to;

            if (!visited.contains(nextNode)) {
                visited.add(nextNode);
                mst.add(minEdge);

                // 添加新节点的所有边到优先队列
                for (Edge edge : nextNode.adj) {
                    if (!visited.contains(edge.to)) {
                        edgeQueue.add(edge);
                    }
                }
            }
        }

        return mst;
    }


    /**
     * Kruskal算法求解最小生成树 （边遍历）
     *
     * @param nodes 图中的所有节点
     * @return 最小生成树的边集合
     */
    public static List<Edge> kruskalMST(List<Node> nodes) {
        if (nodes.isEmpty()) return Collections.emptyList();

        List<Edge> mst = new ArrayList<>();

        // 收集所有边,按权重从小到大排序
        List<Edge> allEdges = new ArrayList<>();
        for (Node node : nodes) {
            allEdges.addAll(node.adj);
        }
        allEdges.sort(Comparator.comparingInt(e -> e.weight));


        // 使用【并查集】来检测环
        UnionFind uf = new UnionFind(nodes);

        for (Edge edge : allEdges) {
            Node from = edge.from;
            Node to = edge.to;

            if (!uf.isConnected(from, to)) { // 判断两个集合能否联通
                mst.add(edge);
                uf.union(from, to);
            }

            // 如果已经连接了所有节点，提前退出
            if (mst.size() == nodes.size() - 1) {
                break;
            }
        }

        return mst;
    }


    public static void main(String[] args) {

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

//        List<Edge> edges = primMST(nodes);
        List<Edge> edges = kruskalMST(nodes);
        System.out.println("Total weight: " + edges.stream().map(item -> item.weight).reduce(0, Integer::sum));
        for(Edge edge:edges){
            System.out.println(edge.from.name + "->" + edge.to.name + ":" + edge.weight);
        }

    }

}

/**
 * 并查集
 */
class UnionFind {

    private Map<Node, Node> parent; // 构造森林
    private Map<Node, Integer> rank;

    public UnionFind(List<Node> nodes) {
        parent = new HashMap<>();
        rank = new HashMap<>();

        for (Node node : nodes) {
            parent.put(node, node);
            rank.put(node, 0);
        }
    }

    public Node find(Node node) {
        if (parent.get(node) != node) {
            parent.put(node, find(parent.get(node))); // 递归路径压缩
        }
        return parent.get(node);
    }

    public boolean isConnected(Node x, Node y) {
        return find(x) == find(y);
    }

    public void union(Node x, Node y) {
        Node rootX = find(x);
        Node rootY = find(y);

        if (rootX == rootY) return;

        // 按秩合并
        if (rank.get(rootX) > rank.get(rootY)) {
            parent.put(rootY, rootX);
        } else if (rank.get(rootX) < rank.get(rootY)) {
            parent.put(rootX, rootY);
        } else {
            parent.put(rootY, rootX);
            rank.put(rootX, rank.get(rootX) + 1);
        }
    }
}
