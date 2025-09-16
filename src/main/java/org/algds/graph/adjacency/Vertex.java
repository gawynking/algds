package org.algds.graph.adjacency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Vertex {
    private static final AtomicInteger nodeIdCounter = new AtomicInteger(1);

    private Integer id; // 订单ID,隐式ID,自增ID
    private String name; // 顶点名称,显示ID

    private Label label; // 标签
    private Map<String, Object> properties; // 属性

    private Map<Integer,Edge> edgeList; // 邻接顶点列表


    public Vertex(String name, Label label) {
        this.id = genId();
        this.name = name;
        this.label = label;
        this.properties = new HashMap<>();
        this.edgeList = new HashMap<>();
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Label getLabel() {
        return label;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public List<Edge> getEdgeList() {
        return new ArrayList<>(this.edgeList.values());
    }

    public Object getProperty(String key) {
        return this.getProperties().get(key);
    }
    public void setProperty(String key,Object value) {
        this.getProperties().put(key,value);
    }

    private int genId() {
        return nodeIdCounter.getAndIncrement();
    }

    // 添加邻接边
    public void addEdge(Edge edge) {
        this.edgeList.put(edge.getId(),edge);
    }

    public void addEdge(Vertex to, int weight, Label label) {
        Edge edge = new Edge(this, to, weight, label);
        this.edgeList.put(edge.getId(),edge);
    }
}