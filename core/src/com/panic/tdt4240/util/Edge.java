package com.panic.tdt4240.util;

/**
 * Created by magnus on 05.03.2018.
 */

public class Edge {

    private Vertex source, destination;
    private int weight;

    public Edge(Vertex source, Vertex destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public Vertex getDestination() {
        return destination;
    }

    public Vertex getSource() {
        return source;
    }
}
