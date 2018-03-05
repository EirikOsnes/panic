package com.panic.tdt4240.util;

import java.util.List;

/**
 * Created by magnus on 05.03.2018.
 */

public class Graph {

    private List<Vertex> vertices;
    private List<Edge> edges;

    public Graph(List<Vertex> vertices, List<Edge> edges){
        this.vertices=vertices;
        this.edges=edges;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}
