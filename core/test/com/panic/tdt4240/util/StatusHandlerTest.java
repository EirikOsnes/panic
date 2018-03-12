package com.panic.tdt4240.util;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Eirik on 12-Mar-18.
 */
public class StatusHandlerTest {

    StatusHandler statusHandler;

    @Before
    public void init(){
        statusHandler = new StatusHandler(null);
        statusHandler.addStatus("health", 100);
        statusHandler.addStatus("damage_modifier", 1);
        statusHandler.addStatus("invulnerability", 0);
    }

    @Test
    public void getStatusResultant() throws Exception {
        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(1,statusHandler.getStatusResultant("damage_modifier"),0.01);
        assertEquals(0,statusHandler.getStatusResultant("invulnerability"),0.01);

        statusHandler.addStatusMultiplier("health", (float) -0.3, -1);
        assertEquals(70, statusHandler.getStatusResultant("health"), 0.01);

        statusHandler.addStatusAddition("health", 100, 1);
        assertEquals(140, statusHandler.getStatusResultant("health"),0.01);

        assertEquals(0,statusHandler.getStatusResultant("not_a_status"),0.01);
    }

    @Test
    public void addStatus() throws Exception {

        int currentSize = statusHandler.getAllResultants().size();

        statusHandler.addStatus("test_status", 1);
        statusHandler.addStatus("test_status2", 100);

        HashMap<String, Float> resultants = statusHandler.getAllResultants();

        assertEquals(currentSize+2,resultants.size());
        assertTrue(resultants.containsKey("test_status"));
        assertTrue(resultants.containsKey("test_status2"));
        assertTrue(resultants.get("test_status")==1);
        assertTrue(resultants.get("test_status2")==100);
    }

    @Test
    public void addStatusMultiplier() throws Exception {

        statusHandler.addStatusMultiplier("health", (float) -0.3, -1);
        assertEquals(70, statusHandler.getStatusResultant("health"), 0.01);

        statusHandler.addStatusMultiplier("health", (float) 0.1, 10);
        assertEquals(80, statusHandler.getStatusResultant("health"), 0.01);

        statusHandler.addStatusMultiplier("invulnerability", (float) 0.1, 10);
        assertEquals(0, statusHandler.getStatusResultant("invulnerability"), 0.01);

    }

    @Test
    public void addStatusAddition() throws Exception {

        statusHandler.addStatusAddition("health", (float) -50, -1);
        assertEquals(50, statusHandler.getStatusResultant("health"), 0.01);

        statusHandler.addStatusAddition("health", (float) 30, 10);
        assertEquals(80, statusHandler.getStatusResultant("health"), 0.01);

        statusHandler.addStatusAddition("invulnerability", (float) 1, 10);
        assertEquals(1, statusHandler.getStatusResultant("invulnerability"), 0.01);

    }

    @Test
    public void getAllResultants() throws Exception {

        HashMap<String, Float> checkMap = new HashMap<>();
        checkMap.put("health", (float) 100);
        checkMap.put("damage_modifier", (float) 1);
        checkMap.put("invulnerability", (float) 0);

        assertEquals(checkMap,statusHandler.getAllResultants());
    }

    @Test
    public void nextTurn() throws Exception {

        statusHandler.addStatusMultiplier("health", (float) 0.5, 1);
        statusHandler.addStatusAddition("damage_modifier", (float) 1,2);
        statusHandler.addStatusAddition("damage_modifier", (float) 1, 1);
        statusHandler.addStatusAddition("damage_modifier", (float) 1, 0);

        assertEquals(150,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(4, statusHandler.getStatusResultant("damage_modifier"),0.01);

        statusHandler.nextTurn();

        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(3, statusHandler.getStatusResultant("damage_modifier"),0.01);

        statusHandler.nextTurn();

        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(2, statusHandler.getStatusResultant("damage_modifier"),0.01);

        statusHandler.nextTurn();

        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(2, statusHandler.getStatusResultant("damage_modifier"),0.01);

    }

}