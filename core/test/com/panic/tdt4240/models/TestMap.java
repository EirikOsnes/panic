package com.panic.tdt4240.models;

/**
 * Created by magnus on 06.03.2018.
 */

import com.panic.tdt4240.util.Dijkstra;
import com.panic.tdt4240.util.Edge;
import com.panic.tdt4240.util.Graph;
import com.panic.tdt4240.util.Vertex;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestMap {

    private Map map;
    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;
    private Graph graph;
    private ArrayList<Asteroid> asteroids;
    private Dijkstra dijkstra;


    @Before
    public void init(){
        asteroids = new ArrayList<Asteroid>();
        asteroids.add(new Asteroid(null));
        asteroids.add(new Asteroid(null));
        asteroids.add(new Asteroid(null));
        asteroids.add(new Asteroid(null));
        asteroids.add(new Asteroid(null));
        map = new Map(asteroids);
        map.connectAsteroids(map.getAsteroids().get(0),map.getAsteroids().get(1));
        map.connectAsteroids(map.getAsteroids().get(0),map.getAsteroids().get(3));
        map.connectAsteroids(map.getAsteroids().get(1),map.getAsteroids().get(2));
        map.connectAsteroids(map.getAsteroids().get(2),map.getAsteroids().get(3));
        map.connectAsteroids(map.getAsteroids().get(3),map.getAsteroids().get(4));
    }

    @Test
    public void shouldAddConnection(){
        assertTrue(map.getAsteroids().get(0).isConnected(map.getAsteroids().get(1)));
        assertTrue(map.getAsteroids().get(0).isConnected(map.getAsteroids().get(3)));
        assertTrue(map.getAsteroids().get(1).isConnected(map.getAsteroids().get(2)));
        assertTrue(map.getAsteroids().get(2).isConnected(map.getAsteroids().get(3)));
    }

    @Test
    public void shouldCreateGraph(){
        graph = map.generateVertexAndEdge();
        edges = graph.getEdges();
        vertices = graph.getVertices();

        assertEquals(edges.size(),5);
        assertEquals(vertices.size(),5);

        assertEquals(edges.get(0).getSource(), vertices.get(0));
        assertEquals(edges.get(0).getDestination(), vertices.get(1));

        assertEquals(edges.get(1).getSource(), vertices.get(0));
        assertEquals(edges.get(1).getDestination(), vertices.get(3));
    }

    @Test
    public void shouldGetDistances(){
        graph = map.generateVertexAndEdge();
        dijkstra = new Dijkstra(graph);

        dijkstra.execute(dijkstra.getNodes().get(0));
        assertNotNull(dijkstra.getDistances());

        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(1)), (Integer) 1);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(2)), (Integer) 2);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(3)), (Integer) 1);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(4)), (Integer) 2);

        dijkstra.execute(dijkstra.getNodes().get(1));

        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(0)), (Integer) 1);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(2)), (Integer) 1);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(3)), (Integer) 2);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(4)), (Integer) 3);

        dijkstra.execute(dijkstra.getNodes().get(2));

        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(0)), (Integer) 2);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(1)), (Integer) 1);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(3)), (Integer) 1);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(4)), (Integer) 2);

        dijkstra.execute(dijkstra.getNodes().get(3));

        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(0)), (Integer) 1);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(1)), (Integer) 2);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(2)), (Integer) 1);
        assertEquals(dijkstra.getDistances().get(dijkstra.getNodes().get(4)), (Integer) 1);
    }

    @Test
    public void shouldSetMatrix(){
        int[][] testMatrix = map.generateAdjacencyMatrix();

        assertEquals((Integer) testMatrix[0][0], (Integer) 0);
        assertEquals((Integer) testMatrix[0][1],(Integer) 1);
        assertEquals((Integer) testMatrix[0][2], (Integer) 2);
        assertEquals((Integer) testMatrix[0][3], (Integer) 1);
        assertEquals((Integer) testMatrix[0][4], (Integer) 2);

        assertEquals((Integer) testMatrix[1][0], (Integer) 1);
        assertEquals((Integer) testMatrix[1][1],(Integer) 0);
        assertEquals((Integer) testMatrix[1][2], (Integer) 1);
        assertEquals((Integer) testMatrix[1][3], (Integer) 2);
        assertEquals((Integer) testMatrix[1][4], (Integer) 3);

        assertEquals((Integer) testMatrix[2][0], (Integer) 2);
        assertEquals((Integer) testMatrix[2][1],(Integer) 1);
        assertEquals((Integer) testMatrix[2][2], (Integer) 0);
        assertEquals((Integer) testMatrix[2][3], (Integer) 1);
        assertEquals((Integer) testMatrix[2][4], (Integer) 2);

        assertEquals((Integer) testMatrix[3][0], (Integer) 1);
        assertEquals((Integer) testMatrix[3][1],(Integer) 2);
        assertEquals((Integer) testMatrix[3][2], (Integer) 1);
        assertEquals((Integer) testMatrix[3][3], (Integer) 0);
        assertEquals((Integer) testMatrix[3][4], (Integer) 1);
    }
}
