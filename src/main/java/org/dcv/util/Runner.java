package org.dcv.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Runner {
    private Map<Integer, MyResource> resources = new HashMap<Integer, MyResource>();

    public Iterable<MyResource> getResources() {
        return this.resources.values();
    }

    public MyResource acquireResource(int id) {
        MyResource w = this.resources.getOrDefault(id, null);
        if (w == null) {
            w = new MyResource(id);
            this.resources.put(id, w);
        }

        return w;
    }

    public void releaseResource(int id) {
        MyResource w = this.resources.getOrDefault(id, null);
        if (w == null)
            throw new IllegalArgumentException();

        w.close();
        this.resources.remove(id);
    }

    public class MyResource implements AutoCloseable {
        private List<String> tasks = new ArrayList<String>();

        private int id;

        public int getId() {
            return this.id;
        }

        public Iterable<String> getTasks() {
            return this.tasks;
        }

        public MyResource(int id) {
            this.id = id;
        }

        public void performTask(String task) {
            if (this.tasks == null)
                throw new IllegalStateException(this.getClass().getName());

            this.tasks.add(task);
        }

        @Override
        public void close() {
            this.tasks = null;
        }
    }

    public static void main(String[] args) {
        Runner d = new Runner();

        d.acquireResource(1).performTask("Task11");
        d.acquireResource(2).performTask("Task21");
        System.out.println(String.join(", ", d.acquireResource(2).getTasks()));
        d.releaseResource(2);
        d.acquireResource(1).performTask("Task12");
        System.out.println(String.join(", ", d.acquireResource(1).getTasks()));
        d.releaseResource(1);
    }}
