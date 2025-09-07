package org.algds.graph.model;

import graph.model.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图数据结构 顶点集合
 */
public class VertexList {

    private Map<Integer,Vertex> vertexList;

    public VertexList(){
        this.vertexList = new HashMap<>();
    }

    public Map<Integer,Vertex> getVertexList() {
        return this.vertexList;
    }

    public Vertex getVertexById(Integer id){
        return this.vertexList.get(id);
    }

    public void addVertex(Vertex vertex){
        this.vertexList.put(vertex.getId(),vertex);
    }

    public void addVertexs(List<Vertex> vertexList){
        for (Vertex vertex:vertexList){
            this.vertexList.put(vertex.getId(),vertex);
        }
    }

}
