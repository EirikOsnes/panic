package com.panic.tdt4240.UnitTests;

/**
 * Created by magnus on 06.03.2018.
 */

import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.util.Edge;
import com.panic.tdt4240.util.Graph;
import com.panic.tdt4240.util.Vertex;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestMap {

    Map map;
    ArrayList<Vertex> vertices;
    ArrayList<Edge> edges;
    Graph graph;
    ArrayList<Asteroid> asteroids;


    @Before
    public void init(){
        asteroids = new ArrayList<Asteroid>();
        asteroids.add(new Asteroid(null));
        asteroids.add(new Asteroid(null));
        asteroids.add(new Asteroid(null));
        asteroids.add(new Asteroid(null));
        map = new Map(asteroids);
        map.connectAsteroids(map.getAsteroids().get(0),map.getAsteroids().get(1));
        map.connectAsteroids(map.getAsteroids().get(0),map.getAsteroids().get(3));
        map.connectAsteroids(map.getAsteroids().get(1),map.getAsteroids().get(2));
        map.connectAsteroids(map.getAsteroids().get(2),map.getAsteroids().get(3));
    }

    @Test
    public void shouldAddConnection(){
        assertEquals(map.getAsteroids().get(0).isConnected(map.getAsteroids().get(1)),true);
        assertEquals(map.getAsteroids().get(0).isConnected(map.getAsteroids().get(3)),true);
        assertEquals(map.getAsteroids().get(1).isConnected(map.getAsteroids().get(2)),true);
        assertEquals(map.getAsteroids().get(2).isConnected(map.getAsteroids().get(3)),true);
    }

    @Test
    public void shouldCreateGraph(){
        Graph graph = map.generateVertexAndEdge();
        ArrayList<Edge> edges = graph.getEdges();
        ArrayList<Vertex> vertices = graph.getVertices();

        assertEquals(edges.size(),4);
        assertEquals(vertices.size(),4);

        assertEquals(edges.get(0).getSource(), vertices.get(0));
        assertEquals(edges.get(0).getDestination(), vertices.get(1));

        assertEquals(edges.get(1).getSource(), vertices.get(0));
        assertEquals(edges.get(1).getDestination(), vertices.get(3));
    }
}
