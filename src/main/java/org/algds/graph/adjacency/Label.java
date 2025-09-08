package org.algds.graph.adjacency;

import java.util.concurrent.atomic.AtomicInteger;

public class Label {
    private static final AtomicInteger labelIdCounter = new AtomicInteger(0);

    private Integer id; // 订单ID,隐式ID,自增ID
    private String label; // 标签类型
    private Integer category; // 1-顶点 2-边

    public Label(String label, Integer category) {
        this.id = genId();
        this.label = label;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }


    private int genId() {
        return labelIdCounter.getAndIncrement();
    }

    enum Category {
        VERTEX(1), EDGE(2);
        Integer id;

        Category(int id) {
            this.id = id;
        }
    }

}
