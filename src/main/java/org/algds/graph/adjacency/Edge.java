package org.algds.graph.adjacency;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Edge {
    private static final AtomicInteger edgeIdCounter = new AtomicInteger(1);

    private Integer id; // 订单ID,隐式ID,自增ID
    private Vertex from; // 起始节点
    private Vertex to;   // 目标节点
    private Label label; // 边标签
    private Integer weight;    // 边权重
    private Map<String, Object> properties; // 边属性

    public Edge(Vertex from, Vertex to, int weight, Label label) {
        this.id = genId();
        this.from = from;
        this.to = to;
        this.label = label;
        this.weight = weight;
        this.properties = new HashMap<>();
    }


    public Integer getId() {
        return id;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object getProperty(String key){
        return this.getProperties().get(key);
    }

    public void setProperty(String key,Object value) {
        this.getProperties().put(key,value);
    }

    private int genId() {
        return edgeIdCounter.getAndIncrement();
    }

}
