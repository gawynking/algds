package org.algds.graph;

import java.util.*;

public class TaskSchedulerWithPriority {
    public static class Task {
        private int id;
        private int priority;
        private List<Task> edges = new ArrayList<>(); // 边列表
        private int inDegree = 0; // 入度
        public Task(int id, int priority) {
            this.id = id;
            this.priority = priority;
        }
        public int getId() {
            return id;
        }
        public int getPriority() {
            return priority;
        }
        public void setPriority(int priority) {
            this.priority = priority;
        }
        public List<Task> getEdges() {
            return edges;
        }
        public int getInDegree() {
            return inDegree;
        }
        public void setInDegree(int inDegree) {
            this.inDegree = inDegree;
        }
        @Override
        public String toString() {
            return "Task{id=" + id + ", priority=" + priority + "}";
        }
    }


    /**
     * 按优先级和依赖顺序进行拓扑排序
     */
    public static List<Task> scheduleTasks(List<Task> tasks) {
        PriorityQueue<Task> queue = new PriorityQueue<>(
                (a, b) -> b.getPriority() - a.getPriority()
        );

        // 初始化：将所有入度为 0 的任务放入队列
        for (Task task : tasks) {
            if (task.getInDegree() == 0) {
                queue.offer(task);
            }
        }

        List<Task> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            Task current = queue.poll();
            result.add(current);

            // 遍历后继节点
            for (Task neighbor : current.getEdges()) {
                neighbor.setInDegree(neighbor.getInDegree()-1);
                if (neighbor.getInDegree() == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // 检查是否有环
        if (result.size() != tasks.size()) {
            throw new RuntimeException("存在循环依赖，无法完成调度");
        }

        return result;
    }


    public static void main(String[] args) {
        // 创建任务
        Task t0 = new Task(0, 10);
        Task t1 = new Task(1, 20);
        Task t2 = new Task(2, 10);
        Task t3 = new Task(3, 20);

        // 构建依赖关系 (邻接表 + 入度)
        t0.getEdges().add(t2);
        t1.getEdges().add(t2);
        t2.getEdges().add(t3);

        t2.setInDegree(2); // 来自 t0 和 t1
        t3.setInDegree(1); // 来自 t2

        List<Task> tasks = Arrays.asList(t0, t1, t2, t3);

        // 调度
        List<Task> schedule = scheduleTasks(tasks);
        System.out.print("调度任务执行顺序: ");
        for (Task task : schedule) {
            System.out.print(task.id + " ");
        }
    }
}