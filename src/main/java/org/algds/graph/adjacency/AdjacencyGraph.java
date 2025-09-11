package org.algds.graph.adjacency;


import java.util.HashMap;
import java.util.Map;

public class AdjacencyGraph {

    private Map<Integer, Vertex> vertices;

    private Integer vertexNum;
    private Integer edgeNum;


    public AdjacencyGraph(){
        vertices = new HashMap<>();
        vertexNum = 0;
        edgeNum = 0;
    }

    public Map<Integer, Vertex> getVertices() {
        return vertices;
    }

    public Integer getVertexNum() {
        return vertexNum;
    }

    public Integer getEdgeNum() {
        return edgeNum;
    }

    public void addVertex(Vertex vertex){
        this.getVertices().put(vertex.getId(),vertex);
        this.vertexNum++;
    }

    public void addEdge(Edge edge){
        this.getVertices().get(edge.getFrom().getId()).addEdge(edge);
        this.edgeNum++;
    }

    public void addEdge(Vertex from, Vertex to, int weight, Label label){
        Edge edge = new Edge(from, to, weight, label);
        this.getVertices().get(edge.getFrom().getId()).addEdge(edge);
        this.edgeNum++;
    }
}
