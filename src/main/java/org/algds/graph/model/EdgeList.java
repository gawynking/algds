package org.algds.graph.model;


import graph.model.Edge;

import java.util.HashMap;
import java.util.Map;

public class EdgeList {

    private Map<Integer, Edge> edgeList; // map结构用来保证ID不重复

    public EdgeList(){
        this.edgeList = new HashMap<>();
    }

    public Map<Integer, Edge> getEdgeList() {
        return edgeList;
    }

    public void addEdge(Edge edge){
        this.edgeList.put(edge.getId(),edge);
    }

}
